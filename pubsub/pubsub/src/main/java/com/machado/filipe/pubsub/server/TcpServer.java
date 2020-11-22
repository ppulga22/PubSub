package com.machado.filipe.pubsub.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.machado.filipe.pubsub.Message;
import com.machado.filipe.pubsub.publisher.Publisher;
import com.machado.filipe.pubsub.publisher.PublisherImpl;
import com.machado.filipe.pubsub.service.PubSubService;
import com.machado.filipe.pubsub.subscriber.Subscriber;
import com.machado.filipe.pubsub.subscriber.SubscriberImpl;

public class TcpServer implements Runnable {
	  private static ServerSocket serverSocket = null;
	  private static Socket clientSocket = null;
	  

	  private static final int maxClientsCount = 10;
	  private static final ClientThread[] threads = new ClientThread[maxClientsCount];
	  int portNumber = 2222;
	  public TcpServer(int portNumber) {
		  this.portNumber = portNumber;
	}

	public void run() {
	  
	    try {
	      serverSocket = new ServerSocket(portNumber);
	    } catch (IOException e) {
	      System.out.println(e);
	    }

	    while (true) {
	      try {
	        clientSocket = serverSocket.accept();
	        int i = 0;
	        for (i = 0; i < maxClientsCount; i++) {
	          if (threads[i] == null) {
		            PubSubService.instance().SetClientThreads(threads);
		            (threads[i] = new ClientThread(clientSocket, false)).start();
	            break;
	          }
	        }
	        if (i == maxClientsCount) {
	          PrintStream os = new PrintStream(clientSocket.getOutputStream());
	          os.println("Server too busy. Try later.");
	          os.close();
	          clientSocket.close();
	        }
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }
	  }
	}


	