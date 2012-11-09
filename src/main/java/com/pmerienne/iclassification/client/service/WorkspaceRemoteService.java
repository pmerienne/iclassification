package com.pmerienne.iclassification.client.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;
import com.pmerienne.iclassification.shared.model.Workspace;

@Path("workspaces/")
public interface WorkspaceRemoteService extends RestService {

	@POST
	@Path("create")
	void create(@QueryParam("name") String name, MethodCallback<Workspace> callback);

	@GET
	@Path("{id}")
	void find(@PathParam("id") String id, MethodCallback<Workspace> callback);

	@GET
	@Path("{workspaceId}/exports")
	@Produces("application/zip")
	void export(@PathParam("workspaceId") String workspaceId, MethodCallback<Void> callback);

	@GET
	void findAll(MethodCallback<List<Workspace>> callback);

	@POST
	@Path("createBuild")
	void createBuild(BuildConfiguration buildConfiguration, MethodCallback<Build> callback);

	@GET
	@Path("{workspaceId}/builds")
	void getBuilds(@PathParam("workspaceId") String workspaceId, MethodCallback<List<Build>> callback);
}
