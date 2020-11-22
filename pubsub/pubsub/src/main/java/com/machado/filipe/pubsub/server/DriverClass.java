package com.machado.filipe.pubsub.server;

import java.net.SocketException;

import com.machado.filipe.pubsub.service.PubSubService;



public class DriverClass {
	
	public DriverClass (int port) {
		
	}

	public static void main(String[] args) throws SocketException {
		  TcpServer tcpServer = new TcpServer(2222);
		  try {
			  new Thread(tcpServer).start();
		  }catch (Exception e) {
			  e.printStackTrace();
		}
		UDPServer udpServer = new UDPServer();
		
		  try { 
			  udpServer.run(); 
		  } catch (Exception e) {
			  e.printStackTrace(); 
			  }

	}

}
