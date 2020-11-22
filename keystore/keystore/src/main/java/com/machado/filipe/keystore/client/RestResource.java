package com.machado.filipe.keystore.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.machado.filipe.keystore.database.DataStore;
import com.machado.filipe.keystore.model.Message;


@Path("/store")
@Produces(MediaType.APPLICATION_JSON)
public class RestResource {
	
		DataStore store;

	  @GET
	  @Path("/messages")
	  public Map<String, String> getData() /*throws JsonProcessingException*/{
		  System.out.println("testes");
		  HashMap<String, String> returnMap = new HashMap<String, String>();

		  try {
			  HashMap<String, Object> map = new HashMap<String, Object>(DataStore.instance().getData());
			  
			  Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
			  
			 // ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			  
			  while (it.hasNext()) {
			        
				  Map.Entry<String, Object> pair = (Map.Entry<String, Object>)it.next();
				
				  //String objectJsonString = ow.writeValueAsString(pair.getValue());
				  
				  //returnMap.put(pair.getKey(), objectJsonString);
				 
		        
		        returnMap.put(pair.getKey(), pair.getValue().toString());
		        
		        System.out.println(pair.getKey() + " = " + pair.getValue().toString());

		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		  
		  }
		  catch (Exception e) {
			returnMap = new HashMap<String, String>();
			returnMap.put("ErrorMessage", e.getMessage());
		}
		  
			return returnMap;
		}
	  
	  @GET
	  @Path("/messages/{key}")
	  public String get(@PathParam("key") String key) {
		  
		  try {
			  if(DataStore.instance().get(key) == null) throw new Exception("key " + key + " does not exist");
			  return DataStore.instance().get(key).toString();
		  
	  }catch (Exception e) {
		  return e.getMessage();
	  }
	  }
	 
	  @DELETE
	  @Path("/messages/{key}")
	  public String remove(@PathParam("key") String key) {
		  try {
			  if(DataStore.instance().get(key) == null) throw new Exception("key " + key + " does not exist");
		  DataStore.instance().remove(key);
		  
		  return "Success";
	  }catch (Exception e) {
		  return e.getMessage();
	  }
		   }
	  
	  @POST
	  @Path("/messages/add")
	  @Consumes(MediaType.APPLICATION_JSON)
	  public String addMessage(Message message) {
		  
		  String returnString = "";
		  
		  try {
			  if(DataStore.instance().get(message.getKey()) != null)
				  throw new Exception("Message with key " + message.getKey() + " alraedy exists.");
				  
			  DataStore.instance().put(message.getKey(), message.getPayload());
			  
			  returnString = "Success";
		  }
		  catch (Exception e) {
			returnString = "ErrorMessage - " + e.getMessage();
		}
		  
		  return returnString;
	  }
	
	  @PUT
	  @Path("/messages/update")
	  @Consumes(MediaType.APPLICATION_JSON)
	  public String update(Message message) {
		  
		  try {
			  if(DataStore.instance().get(message.getKey()) == null) throw new Exception("key " + message.getKey() + " does not exist");
			  return DataStore.instance().update(message.getKey(), message.getPayload()).toString();
		  
	  }catch (Exception e) {
		  return e.getMessage();
	  }
		  
	  }
	
}
