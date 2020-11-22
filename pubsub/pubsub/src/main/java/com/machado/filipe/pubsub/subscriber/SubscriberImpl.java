package com.machado.filipe.pubsub.subscriber;

import java.net.InetAddress;
import java.net.SocketAddress;

import com.machado.filipe.pubsub.service.PubSubService;
import com.machado.filipe.pubsub.server.ClientThread;

public class SubscriberImpl extends Subscriber{
	 InetAddress clientIP;
	 int clientport;
	 ClientThread clientThread;
	private SocketAddress clientAddress;

	public SubscriberImpl (InetAddress clientIP, int clientport) {
		this.clientIP = clientIP;
		this.clientport = clientport;
	}
	public SubscriberImpl() {
		// TODO Auto-generated constructor stub
	}
	public SubscriberImpl (ClientThread thread) {
		this.clientIP = thread.getSocket().getInetAddress();
		this.clientport = thread.getSocket().getPort();
		this.clientThread = thread;
	}

	
	  public SubscriberImpl(SocketAddress clientAddress) { 
		  this.clientAddress = clientAddress;
	  }
	   // TODO Auto-generated constructor stub }
	 
	public ClientThread getClientThread() {
		return clientThread;
	}
	//Add subscriber with PubSubService for a topic
	public void addSubscriber( String topic, PubSubService pubSubService){
		PubSubService.instance().addSubscriber(topic, this);
	}
	/*
	 * public void setSubscriber(InetAddress clientIP, int clientport, PubSubService
	 * pubsubService) { PubSubService.instance().setSubscriber(clientIP, clientport,
	 * this); }
	 */
	
	//Unsubscribe subscriber with PubSubService for a topic
	public void unSubscribe(String topic){
		PubSubService.instance().removeSubscriber(topic, this);
	}
 
	//Request specifically for messages related to topic from PubSubService
	public void getMessagesForSubscriberOfTopic(String topic) {
		PubSubService.instance().getMessagesForSubscriberOfTopic(topic);
		
	}
	public InetAddress getClientIP() {
		return clientIP;
	}
	public void setClientIP(InetAddress clientIP) {
		this.clientIP = clientIP;
	}
	public int getClientport() {
		return clientport;
	}
	public void setClientport(int clientport) {
		this.clientport = clientport;
	}
	
	  public SocketAddress getClientAddress() { 
		  return clientAddress; 
		  } 
	  public void setClientAddress(SocketAddress clientAddress) { 
		this.clientAddress = clientAddress; 
		}
	 
	
}
