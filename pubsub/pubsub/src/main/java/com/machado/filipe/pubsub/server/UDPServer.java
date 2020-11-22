package com.machado.filipe.pubsub.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.publisher.Publisher;
import com.machado.filipe.pubsub.publisher.PublisherImpl;
import com.machado.filipe.pubsub.service.PubSubService;
import com.machado.filipe.pubsub.subscriber.Subscriber;
import com.machado.filipe.pubsub.subscriber.SubscriberImpl;
public class UDPServer {
		private static HashSet<Integer> portSet = new HashSet<Integer>();
		
		static PubSubService pubSubService;
	  
		public void run() throws Exception {
	 
	        int serverport = 7777;        
	 
		    DatagramSocket udpServerSocket = new DatagramSocket(serverport);        
	 
		    System.out.println("Server started...\n");
	 
		    while(true)
			{
				byte[] receiveData = new byte[1024];          
	 
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	 
				udpServerSocket.receive(receivePacket);           
	 
				String clientMessage = (new String(receivePacket.getData())).trim();
	 
				
				InetAddress clientIP = receivePacket.getAddress();           
	 
			
			  int clientport = receivePacket.getPort(); 
			 
				byte[] sendData  = new byte[1024];

				decodeCommand(new String(clientMessage));
		   		JSONObject jsonObj = new JSONObject(clientMessage);
		        String type = jsonObj.getString("type");

		        if(type.equalsIgnoreCase("pub")) {
		        	String topic = jsonObj.getString("topic");
		        	JSONObject payload = jsonObj.getJSONObject("payload");
		    		Publisher publisher = new PublisherImpl();

		        	Message message = new Message(topic, payload);
		        	publisher.publish(message, pubSubService);
		        	Set<Subscriber> _subscribers = PubSubService.instance().getSubscribers(topic);

		        	for(Subscriber sub : _subscribers) {
		        			sub.getMessagesForSubscriberOfTopic(topic);
			        		
			        		String response = sub.getMessages().toString();
				        	sendData = response.getBytes();
						
						  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						  clientIP, sub.getClientport()); 
						  udpServerSocket.send(sendPacket);
				        	
		        	}
		        	
		        }else if (type.equalsIgnoreCase("sub")) {
		        	String topic = jsonObj.getString("topic");
		        	Subscriber subscriber = new SubscriberImpl(clientIP, clientport);
		        	subscriber.addSubscriber(topic, pubSubService);
		        	subscriber.getMessagesForSubscriberOfTopic(topic);
		        	String response = subscriber.getMessages().toString();
		        	sendData = response.getBytes();
				
				  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
				  clientIP, clientport); 
				  udpServerSocket.send(sendPacket);
				  
		        	
		        }else if (type.equalsIgnoreCase("unsub")) {
		        	String topic = jsonObj.getString("topic");
		        	for (Subscriber sub : PubSubService.instance().getSubscribers(topic)) {
		        		if(sub.getClientport() == clientport && sub.getClientIP() == clientIP) {
							  sub.unSubscribe(topic);
						  }
		        	}
		        	
		        	
		        }
			
	        }
	    }
		private static void decodeCommand(String jsonString) throws JSONException {
			  JSONObject jsonObject = new JSONObject(jsonString);
			  System.out.println(jsonObject);  
			  }
}