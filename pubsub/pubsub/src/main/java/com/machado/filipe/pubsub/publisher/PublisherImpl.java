package com.machado.filipe.pubsub.publisher;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.service.PubSubService;

public class PublisherImpl implements Publisher {
	//Publishes new message to PubSubService
	public void publish(Message message, PubSubService pubSubService) {		
		PubSubService.instance().addMessageToQueue(message);
	}
}