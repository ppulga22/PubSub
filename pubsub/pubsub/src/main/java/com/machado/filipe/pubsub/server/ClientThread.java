package com.machado.filipe.pubsub.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.publisher.Publisher;
import com.machado.filipe.pubsub.publisher.PublisherImpl;
import com.machado.filipe.pubsub.service.PubSubService;
import com.machado.filipe.pubsub.subscriber.Subscriber;
import com.machado.filipe.pubsub.subscriber.SubscriberImpl;

public class ClientThread extends Thread {

	  private DataInputStream is = null;
	  private PrintStream os = null;
	  private Socket clientSocket = null;
	  private final ClientThread[] threads;
	  private int maxClientsCount;
	  
	  public Socket getSocket() {
		  return clientSocket;
	  }
	  
	  public ClientThread(Socket clientSocket, Boolean sub) {		  
		    this.clientSocket = clientSocket;
		    if(!sub) {
			    this.threads = PubSubService.instance().getClientThreads();
			    maxClientsCount = this.threads.length;
		    }
		    else {
		    	this.threads = null;
		    	maxClientsCount = 1;
		    }
		  }
	  
	  public void sendMessage(Message message){
		  try {
			  String response;
		      os = new PrintStream(clientSocket.getOutputStream());
		      response = "Topic -> " + message.getTopic() + " : " + message.getPayload();
		    	os.println(response);			  
		  }
		  catch(Exception e){
			  
		  }
	  }

	  public void run() {
	    int maxClientsCount = this.maxClientsCount;
	    ClientThread[] threads = this.threads;

	    try {

	      is = new DataInputStream(clientSocket.getInputStream());
	      os = new PrintStream(clientSocket.getOutputStream());
	      String clientMessage = is.readLine();
	      decodeCommand(new String(clientMessage));
	      JSONObject jsonObj = new JSONObject(clientMessage);
	      String type = jsonObj.getString("type");
		  PubSubService pubSubService = new PubSubService();

		  
	  	  if(type.equalsIgnoreCase("pub")) {
		  		String topic = jsonObj.getString("topic");
		    	JSONObject payload = jsonObj.getJSONObject("payload");
		  		Message message = new Message(topic, payload);
			    Publisher publisher = new PublisherImpl();
		    	publisher.publish(message, pubSubService);
		    	if(!PubSubService.instance().getSubscribers(topic).isEmpty()) {
		    		for(Subscriber sub : PubSubService.instance().getSubscribers(topic)) {
				    	
				    	sub.getMessagesForSubscriberOfTopic(topic);
				    	String response = sub.getMessages().toString();
				    	os.println(response);
				    	
						
						  if(sub.getClientThread() != null) { 
						  sub.getClientThread().sendMessage(message); } 
			      }
		    	}
		      
	  		
	  	  }else if(type.equalsIgnoreCase("sub")) {
	  		String topic = jsonObj.getString("topic");
		  	  Subscriber subscriber = new SubscriberImpl(new ClientThread(clientSocket, true));
		  		subscriber.addSubscriber(topic, pubSubService);
		  		subscriber.getMessagesForSubscriberOfTopic(topic);
		  
		    	String response = subscriber.getMessages().toString();
		    	os.println(response);
	    	}else if(type.equalsIgnoreCase("unsubscribe")) {
	    		String topic = jsonObj.getString("topic");
	        	for (Subscriber sub : PubSubService.instance().getSubscribers(topic)) {
	        		if(sub.getClientport() == clientSocket.getPort()) {
						  sub.unSubscribe(topic);
					  }
	        	}
	  	  }
	     
	      while (true) {
	        String line = is.readLine();
	        if (line.startsWith("/quit")) {
	          break;
	        }
	       
	      synchronized (this) {
	        for (int i = 0; i < maxClientsCount; i++) {
	          if (threads[i] == this) {
	            threads[i] = null;
	          }
	        }
	      }
	     
	      }
	    } catch (IOException e) {
	    } catch (JSONException e) {
			e.printStackTrace();
		}
	  }
	  private static void decodeCommand(String jsonString) throws JSONException {
		  JSONObject jsonObject = new JSONObject(jsonString);
		  System.out.println(jsonObject);  
		  }

}

