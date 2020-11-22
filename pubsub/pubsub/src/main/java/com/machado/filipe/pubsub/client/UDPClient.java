package com.machado.filipe.pubsub.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
public class UDPClient {
	 public static void main(String args[]) throws Exception {  
		 
	        int clientport = 7777;
	        String host = "localhost";
	 
	        if (args.length < 1) {
	           System.out.println("Usage: UDPClient " + "Now using host = " + host + ", Port# = " + clientport);
	        } 
	        else {      
	           host = args[0];
	           clientport = Integer.valueOf(args[0]).intValue();
	           System.out.println("Usage: UDPClient " + "Now using host = " + host + ", Port# = " + clientport);
	        } 
	 
	        // Get the IP address of the local machine - we will use this as the address to send the data to
	        InetAddress ia = InetAddress.getByName(host);
	 
	        SenderThread sender = new SenderThread(ia, clientport);
	        sender.start();
	        ReceiverThread receiver = new ReceiverThread(sender.getSocket());
	        receiver.start();
	    }
	}      
	 
	class SenderThread extends Thread {
	 
	    private InetAddress serverIPAddress;
	    private DatagramSocket udpClientSocket;
	    private boolean stopped = false;
	    private int serverport;
	 
	    public SenderThread(InetAddress address, int serverport) throws SocketException {
	        this.serverIPAddress = address;
	        this.serverport = serverport;
	        this.udpClientSocket = new DatagramSocket();
	        this.udpClientSocket.connect(serverIPAddress, serverport);
	    }
	    public void halt() {
	        this.stopped = true;
	    }
	    public DatagramSocket getSocket() {
	        return this.udpClientSocket;
	    }
	 
	    public void run() {       
	        try {    
	        	byte[] data = new byte[1024];
	        	
	            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	            while (true) 
	            {
	                if (stopped)
	                    return;
	 
	                String clientMessage = inFromUser.readLine();
	 
	                if (clientMessage.equals("."))
	                    break;
	 
	                byte[] sendData = new byte[1024];
	 
	                sendData = clientMessage.getBytes();
	 
	                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverport);
	 
	                System.out.println("I just sent: "+clientMessage);
	                udpClientSocket.send(sendPacket);
	 
	                Thread.yield();
	            }
	        }
	        catch (IOException ex) {
	            System.err.println(ex);
	        }
	    }
	}   
	 
	class ReceiverThread extends Thread {
	 
	    private DatagramSocket udpClientSocket;
	    private boolean stopped = false;
	 
	    public ReceiverThread(DatagramSocket ds) throws SocketException {
	        this.udpClientSocket = ds;
	    }
	 
	    public void halt() {
	        this.stopped = true;
	    }
	 
	    public void run() {
	 
	        byte[] receiveData = new byte[1024];
	 
	        while (true) {            
	            if (stopped)
	            return;
	 
	            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	            System.out.println("I am in the reader!");
	            try {
	                udpClientSocket.receive(receivePacket);
	                System.out.println("Am i receiving?");
	                String serverReply =  new String(receivePacket.getData(), 0, receivePacket.getLength());
	 
	                System.out.println("UDPClient: Response from Server: \"" + serverReply + "\"\n");
	 
	                Thread.yield();
	            } 
	            catch (IOException ex) {
	            System.err.println(ex);
	            }
	        }
	    }
}
