package com.machado.filipe.pubsub.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient implements Runnable {
	  private static Socket clientSocket = null;
	  private static PrintStream os = null;
	  private static DataInputStream is = null;

	  private static BufferedReader inputLine = null;
	  private static boolean closed = false;
	  
	  public static void main(String[] args) {

	    int portNumber = 2222;
	    String host = "localhost";
	   
	    try {
	      clientSocket = new Socket(host, portNumber);
	      System.out.println("Usage: TCPClient " + "Now using host = " + host + ", Port# = " + portNumber);
	      inputLine = new BufferedReader(new InputStreamReader(System.in));
	      os = new PrintStream(clientSocket.getOutputStream());
	      is = new DataInputStream(clientSocket.getInputStream());
	    } catch (UnknownHostException e) {
	      System.err.println("Don't know about host " + host);
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O for the connection to the host "
	          + host);
	    }

	    if (clientSocket != null && os != null && is != null) {
	      try {

	        new Thread(new TcpClient()).start();
	        while (!closed) {
	          os.println(inputLine.readLine().trim());
	        }
	        clientSocket.close();
	      } catch (IOException e) {
	        System.err.println("IOException:  " + e);
	      }
	    }
	  }

	  public void run() {
	    String responseLine;
	    try {
	      while ((responseLine = is.readLine()) != null) {
	        System.out.println("Response: " + responseLine);
	        if (responseLine.indexOf("*** Bye") != -1)
	          break;
	      }
	      closed = true;
	    } catch (IOException e) {
	      System.err.println("IOException:  " + e);
	    }
	  }

}
