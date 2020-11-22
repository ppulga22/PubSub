package com.machado.filipe.keystore.database;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataStore {
	
	 static DataStore singleton;

	    Map<String, Object> store = new HashMap<>();

	
	  public static DataStore instance() { 
		  if(singleton==null) { 
			  singleton = new DataStore(); 
			  } 
		  return singleton; 
		  }
	 
	    

	/*
	 * public static DataStore instance(){ if(singleton == null){ synchronized
	 * (DataStore.class) { if(singleton == null){ singleton = new DataStore(); } } }
	 * return singleton; }
	 */


	    protected DataStore() {
	    }
	    
	    public Map<String, Object> getData(){
	    	return store;
	    }
    
	    public void put(String key, Object obj) {
	        store.put(key, obj);
	    }

	    public Object get(String key) {
	        return store.get(key);
	    }
	    
	
	  public Object update(String key, Object obj) { 
		  if(store.get(key) == null) {
			  return null;
	  } store.put(key, obj); 
	  return store.get(key); 
	  }
	 
	    
		public void remove(String key) {
	    	store.remove(key);

	    }
	    
		
	/*
	 * public List<String> getObjects() { return new
	 * ArrayList<String>(store.values()); }
	 */
		 
		/*
		 * public Object addObj(String key, Object obj) { store.put(key, obj); return
		 * obj; }
		 */

}
