package com.machado.filipe.pubsub.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.server.ClientThread;
import com.machado.filipe.pubsub.server.TcpServer;
import com.machado.filipe.pubsub.server.UDPServer;
import com.machado.filipe.pubsub.subscriber.Subscriber;
import com.machado.filipe.pubsub.subscriber.SubscriberImpl;

public class PubSubService {
	
	static PubSubService singleton;
	static ClientThread[] clientThreads;
	
	
	
	public static PubSubService instance()
	{
		if (singleton == null)
		{
			synchronized (PubSubService.class)
			{
				if (singleton == null)
					singleton = new PubSubService();
			}
		}
		return singleton;
	}
	
	public ClientThread[] getClientThreads() {
		return clientThreads;
	}
	public void SetClientThreads(ClientThread[] clientThreads) {
		PubSubService.clientThreads = clientThreads;
	}
	
	//Keeps set of subscriber topic wise, using set to prevent duplicates 
	Map<String, Set<Subscriber>> subscribersTopicMap = new HashMap<String, Set<Subscriber>>();
 
	//Holds messages published by publishers
	Queue<Message> messagesQueue = new LinkedList<Message>();
 
	//Adds message sent by publisher to queue
	public void addMessageToQueue(Message message){
		messagesQueue.add(message);
	}
 
	public Map<String, Set<Subscriber>> subscribersMap(){
		return subscribersTopicMap;
	}
	
	public Set<Subscriber> getSubscribers(String topic){
		if(subscribersMap().containsKey(topic)) {
			Set<Subscriber> subscribers = subscribersTopicMap.get(topic);
			return subscribers;
		}
		return new HashSet<Subscriber>();
	}
	//Add a new Subscriber for a topic
	public void addSubscriber(String topic, Subscriber subscriber){
 
		if(subscribersTopicMap.containsKey(topic)){
			Set<Subscriber> subscribers = subscribersTopicMap.get(topic);
			subscribers.add(subscriber);
			subscribersTopicMap.put(topic, subscribers);
		}else{
			Set<Subscriber> subscribers = new HashSet<Subscriber>();
			subscribers.add(subscriber);
			subscribersTopicMap.put(topic, subscribers);
		}		
	}
	/*
	 * public void setSubscriber(InetAddress clientIP, int clientport, Subscriber
	 * subscriber) { subscriber = new SubscriberImpl(clientIP, clientport);
	 * clientport = subscriber.getClientport(); clientIP =
	 * subscriber.getClientIP(clientIP); Set<Subscriber> subscribers = new
	 * HashSet<Subscriber>(); subscribers.add(subscriber); }
	 */
 
	//Remove an existing subscriber for a topic
	public void removeSubscriber(String topic, Subscriber subscriber){
 
		if(subscribersTopicMap.containsKey(topic)){
			Set<Subscriber> subscribers = subscribersTopicMap.get(topic);
			subscribers.remove(subscriber);
			subscribersTopicMap.put(topic, subscribers);
		}
	}

 
	//Broadcast new messages added in queue to All subscribers of the topic. messagesQueue will be empty after broadcasting
	public void broadcast(){
		if(messagesQueue.isEmpty()){
			System.out.println("No messages from publishers to display");
		}else{
			while(!messagesQueue.isEmpty()){
				Message message = messagesQueue.remove();
				String topic = message.getTopic();
 
				Set<Subscriber> subscribersOfTopic = subscribersTopicMap.get(topic);
 
				for(Subscriber subscriber : subscribersOfTopic){
					//add broadcasted message to subscribers message queue
					List<Message> subscriberMessages = subscriber.getSubscriberMessages();
					subscriberMessages.add(message);
					subscriber.setSubscriberMessages(subscriberMessages);
				}			
			}
		}
	}
 
	//Sends messages about a topic for subscriber at any point
	public void getMessagesForSubscriberOfTopic(String topic/*, Subscriber subscriber*/) {
		if(messagesQueue.isEmpty()){
			System.out.println("No messages from publishers to display");
		}else{
			while(!messagesQueue.isEmpty()){
				Message message = messagesQueue.remove();
 
				if(message.getTopic().equalsIgnoreCase(topic)){
 
					Set<Subscriber> subscribersOfTopic = subscribersTopicMap.get(topic);
 
					for(Subscriber _subscriber : subscribersOfTopic){
						//if(_subscriber.equals(subscriber)){
							//add broadcasted message to subscriber message queue
						//List<Message> subscriberMessages = subscriber.getSubscriberMessages();
						List<Message> subscriberMessages = _subscriber.getSubscriberMessages();
							subscriberMessages.add(message);
							//subscriber.setSubscriberMessages(subscriberMessages);
							_subscriber.setSubscriberMessages(subscriberMessages);
						//}
					}
				}
			}
		}
	}
}