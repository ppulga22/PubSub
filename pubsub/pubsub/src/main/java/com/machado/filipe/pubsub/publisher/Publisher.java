package com.machado.filipe.pubsub.publisher;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.service.PubSubService;

public interface Publisher {	
	//Publishes new message to PubSubService
	void publish(Message message, PubSubService pubSubService);
}
