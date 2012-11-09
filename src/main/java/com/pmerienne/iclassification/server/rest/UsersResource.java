package com.pmerienne.iclassification.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.pmerienne.iclassification.shared.model.User;

@Path("users")
public class UsersResource {

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@QueryParam("login") String login, @QueryParam("password") String password) {
		return new User(login, password);
	}
}
