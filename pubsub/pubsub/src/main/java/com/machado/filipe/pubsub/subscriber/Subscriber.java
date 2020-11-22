package com.machado.filipe.pubsub.subscriber;
import com.machado.filipe.pubsub.server.ClientThread;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.service.PubSubService;

public abstract class Subscriber {	
	//store all messages received by the subscriber
	private List<Message> subscriberMessages = new ArrayList<>();
		
	public List<Message> getSubscriberMessages() {
		return subscriberMessages;
	}
	public void setSubscriberMessages(List<Message> subscriberMessages) {
		this.subscriberMessages = subscriberMessages;
	}
	public void setSubscriber (InetAddress clientIP, int clientport) {
	}
	//Add subscriber with PubSubService for a topic
	public abstract void addSubscriber(String topic, PubSubService pubSubService);
	
	//Unsubscribe subscriber with PubSubService for a topic
	public abstract void unSubscribe(String topic);
	
	//Request specifically for messages related to topic from PubSubService
	public abstract void getMessagesForSubscriberOfTopic(String topic);
	
	//public int getClientport(int clientport) {
	//	return clientport;}
	
	//public InetAddress getClientIP(InetAddress clientIP) {
	//	return clientIP;}
	//Print all messages received by the subscriber 
	
	  public void printMessages(){ 
		  for(Message message : subscriberMessages){
	  System.out.println("Message Topic -> "+ message.getTopic() + " : " + message.getPayload().toString()); 
	  } 
	}
	  public List<String> getMessages() {
		  String response = "";
		  List<String> respon = new ArrayList<String>();
			  for(Message message : subscriberMessages) {
				  
				   response = "Topic -> " + message.getTopic() + " : " + message.getPayload();
				   respon.add(response);
		};
		  
			  //System.out.println("Message Topic -> "+ message.getTopic() + " : " + message.getPayload().toString()); 
		return respon;
		  
		  
	  }
	public abstract int getClientport();
	public abstract InetAddress getClientIP();
	public abstract ClientThread getClientThread();
	public abstract SocketAddress getClientAddress();
}
