package com.machado.filipe.keystore.model;


public class Message {
	String key;
	Object payload;
	
	Message(){
		
	}
	Message (String key, Object payload){
		this.key = key;
		this.payload = payload;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
