package com.machado.filipe.keystore.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

import javax.ws.rs.Path;

import org.json.JSONException;
import org.json.JSONObject;

import com.machado.filipe.keystore.database.DataStore;



public class UdpServer implements Runnable{
	
	DataStore store;
    DatagramSocket serverSocket;

    public UdpServer() {
        this.store = DataStore.instance();
    }
    
    public UdpServer (int port) throws SocketException {
    	this.serverSocket = new DatagramSocket(9876);
    }

	public UdpServer (UdpServer udpServer) {
		try {
			this.serverSocket = new DatagramSocket(9876);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  public void start() throws SocketException { this.serverSocket = new
	  DatagramSocket(9876);
	  
	  }
	 

   
    public void run() {
        byte[] receiveData = new byte[1024];
	    //byte[] sendData = new byte[1024];
	    byte[] sendResponse = new byte[1024];


        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                decodeCommand(new String(receiveData));
                String sentence = new String(receivePacket.getData());
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
				/*
				 * String capitalizedSentence = sentence.toUpperCase(); sendData =
				 * capitalizedSentence.getBytes(); DatagramPacket sendPacket = new
				 * DatagramPacket(sendData, sendData.length, IPAddress, port);
				 * serverSocket.send(sendPacket);
				 */
                
                JSONObject jsonObj = new JSONObject(sentence);
                String command = jsonObj.getString("command");
                
                
                
                if(command.equalsIgnoreCase("put")) {
                	String key = jsonObj.getString("key");
                    JSONObject payload = jsonObj.getJSONObject("payload");
                    try {
                    	if (DataStore.instance().get(key) != null) throw new Exception("Message with key " + DataStore.instance().get(key) + " alraedy exists.");
                	DataStore.instance().put(key, payload);
                	String response = DataStore.instance().get(key).toString() + "Added with success.";
                    sendResponse = response.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendResponse, sendResponse.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                	System.out.println("Success.");
                	}catch (Exception e) {
						System.out.println(e);
					}
                	System.out.println(DataStore.instance().get(key));
                	System.out.println(DataStore.instance().getData());
                	
                }else if(command.equalsIgnoreCase("get")) {
                	try {
                	String key = jsonObj.getString("key");
                	if(DataStore.instance().get(key) == null) throw new Exception("Key does not exist");
                	System.out.println(DataStore.instance().get(key));
                	String response = DataStore.instance().get(key).toString();
                    sendResponse = response.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendResponse, sendResponse.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                	}catch (Exception e) {
						System.out.println(e);
					}
                }else if(command.equalsIgnoreCase("remove")) {
                	try {
                    	String key = jsonObj.getString("key");
                    	if(DataStore.instance().get(key) == null) throw new Exception("Key does not exist");
                    	DataStore.instance().remove(key);
                    	String response = "Success";
                        sendResponse = response.getBytes();
                        DatagramPacket sendPacket =
                                new DatagramPacket(sendResponse, sendResponse.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                    	}catch (Exception e) {
    						System.out.println(e);
    					}
                }else if(command.equalsIgnoreCase("update")) {
                	try {
                	String key = jsonObj.getString("key");
                    JSONObject payload = jsonObj.getJSONObject("payload");
                	if(DataStore.instance().get(key) == null) throw new Exception("Key does not exist");

                	DataStore.instance().update(key, payload);
                	String response = DataStore.instance().get(key).toString();
                    sendResponse = response.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendResponse, sendResponse.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                	System.out.println(DataStore.instance().get(key));
                	}catch (Exception e) {
                		System.out.println(e);
                	}
                }
                else if(command.equalsIgnoreCase("getAll")) {
                	System.out.println(DataStore.instance().getData());
                	String response = DataStore.instance().getData().toString();
                    sendResponse = response.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendResponse, sendResponse.length, IPAddress, port);
                    serverSocket.send(sendPacket);
                	
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
     

               
        }

    }

	  private static void decodeCommand(String jsonString) throws JSONException {
	  JSONObject jsonObject = new JSONObject(jsonString);
	  System.out.println(jsonObject);  
	  }
	
}
