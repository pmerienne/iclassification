package com.pmerienne.iclassification.client.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.pmerienne.iclassification.shared.model.User;

@Path("users")
public interface UserRemoteService extends RestService {

	@GET
	@Path("login")
	void login(@QueryParam("login") String login, @QueryParam("password") String password, MethodCallback<User> callback);
}
