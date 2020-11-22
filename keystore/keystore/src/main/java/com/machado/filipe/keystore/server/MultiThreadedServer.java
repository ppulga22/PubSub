package com.machado.filipe.keystore.server;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

import javax.jws.WebResult;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.client.ClientResponse;

import com.machado.filipe.keystore.client.RestResource;


public class MultiThreadedServer implements Runnable{

    protected int          serverPort   = 2222;
    protected int          udpPort   = 9876;
    protected int 		   port = 8081;
    protected Server server = null;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread, process, udpThread= null;
    protected DatagramSocket udpSocket = null;
    
    
    public void JerseyApplication (int port) {
    	this.port = port;
    }
    public MultiThreadedServer(int port){
        this.serverPort = port;
    }
    
    
    public void udp (DatagramSocket udp, int port) {
    	this.udpSocket = udp;
    	this.udpPort = port;
		
	}
    
    public void run(){
    	
        synchronized(this){
            this.runningThread = Thread.currentThread();  
            
            URL url;
			try {
				url = new URL("http://localhost:8080/api/store");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			 * try {server.start(); server.join();
			 * 
			 * }catch (Exception ex) {
			 * System.out.println("Error occurred while starting Jetty"); System.exit(1); }
			 * new Thread( new JerseyApplication() ).start();
			 */
			 
        }
        openServerSocket();
        openServer();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(
                new TcpThread(
                    clientSocket, serverSocket)
            ).start();
        }
        
        //System.out.println("Server Stopped.") ;
    }

    

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServer() {
        this.server = new Server(this.port);
    }
    
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 2222", e);
        }
    }

	/*
	 * private void openUdpServerSocket() { try { this.udpSocket = new
	 * DatagramSocket(this.udpPort); process = new Thread(this, "server_process");
	 * process.start();
	 * 
	 * } catch (IOException e) { throw new RuntimeException("Cannot open port 9876",
	 * e); } }
	 */
    public static void main(String[] args) throws Exception {
		
		
		  JerseyApplication server2 = new JerseyApplication(); new
		  Thread(server2).start();
		 
		 
		MultiThreadedServer server = new MultiThreadedServer(2222); 
		new Thread(server).start();

		UdpServer udpServer = new UdpServer(9876);
		udpServer.run(); 
		udpServer.start();
		 
			
			
		}

	
    	
		/*
		 * Server serverJ = new Server(8080); serverJ.start(); serverJ.join();
		 */
    	 
		/*
		 * MultiThreadedServer udp = new MultiThreadedServer(9876); new
		 * Thread(udp).start();
		 */

		/*
		 * try { Thread.sleep(20 * 1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } System.out.println("Stopping Server"); server.stop();
		 */
	}


