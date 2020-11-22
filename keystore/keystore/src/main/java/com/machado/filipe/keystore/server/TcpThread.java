package com.machado.filipe.keystore.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.hamcrest.core.Is;
import org.json.JSONException;
import org.json.JSONObject;

import com.machado.filipe.keystore.database.DataStore;

public class TcpThread implements Runnable{

    protected Socket clientSocket = null;
    protected ServerSocket serverSocket   = null;

    public TcpThread(Socket clientSocket, ServerSocket server) {
        this.clientSocket = clientSocket;
        this.serverSocket   = server;
    }

    public void run() {
        try {
        	  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
       		//DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
    	      PrintStream os = new PrintStream(clientSocket.getOutputStream());

       		String clientString = in.readLine();
               decodeCommand(new String(clientString));

       		System.out.println("Received: " + clientString);
       		//String serverString = clientString.toUpperCase() + 'n';
       		JSONObject jsonObj = new JSONObject(clientString);
               String command = jsonObj.getString("command");
               
              
               if(command.equalsIgnoreCase("put")) {
               	String key = jsonObj.getString("key");
                   JSONObject payload = jsonObj.getJSONObject("payload");
                   try {
                   	if (DataStore.instance().get(key) != null) throw new Exception("Message with key " + DataStore.instance().get(key) + " alraedy exists.");
{}
               	DataStore.instance().put(key, payload);
               	
               	System.out.println("Success.");
               	os.println("Success");
               	os.println(DataStore.instance().get(key) + "Added with Success.");
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
               	os.println(DataStore.instance().get(key).toString());
               	}catch (Exception e) {
						System.out.println(e);
					}
               }else if(command.equalsIgnoreCase("remove")) {
               	try {
                   	String key = jsonObj.getString("key");
                   	if(DataStore.instance().get(key) == null) throw new Exception("Key does not exist");
                   	DataStore.instance().remove(key);
                   	os.println("Success");
                   	}catch (Exception e) {
   						System.out.println(e);
   					}
               }else if(command.equalsIgnoreCase("update")) {
               	try {
               	String key = jsonObj.getString("key");
                   JSONObject payload = jsonObj.getJSONObject("payload");
               	if(DataStore.instance().get(key) == null) throw new Exception("Key does not exist");

               	DataStore.instance().update(key, payload);
               	os.println(DataStore.instance().get(key).toString() + "updated");
               	System.out.println(DataStore.instance().get(key));
               	}catch (Exception e) {
               		System.out.println(e);
               	}
               }
               else if(command.equalsIgnoreCase("getAll")) {
            	   os.println(DataStore.instance().getData().toString());
               	System.out.println(DataStore.instance().getData());
               	
               }
               } catch (IOException | JSONException e) {
                   //report exception somewhere.
                   e.printStackTrace();
               }
        
               
               
    }
    private void decodeCommand(String jsonString) throws JSONException {
  	  JSONObject jsonObject = new JSONObject(jsonString);
  	  System.out.println(jsonObject);  
  	  }
}
