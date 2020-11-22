package com.machado.filipe.keystore.resource;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.machado.filipe.keystore.database.DataStore;
import com.machado.filipe.keystore.resource.Greeting;

@Path("/hello")
public class HelloResource {
	/*
	 * DataStore store;
	 * 
	 * @GET
	 * 
	 * @Produces("application/json") public List<Object> hello(Object obj) { return
	 * store.getObjects(); }
	 */
    @GET
    @Path("/{param}")
    @Produces("application/json")
    public Greeting hello(@PathParam("param") String name) {
        return new Greeting(name);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String helloUsingJson(Greeting greeting) {
        return greeting.getMessage() + "\n";
    }
}
