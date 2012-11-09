package com.pmerienne.iclassification.server.rest;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pmerienne.iclassification.server.service.BuildService;
import com.pmerienne.iclassification.server.service.ImageService;
import com.pmerienne.iclassification.server.service.WorkspaceService;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;
import com.pmerienne.iclassification.shared.model.Workspace;
import com.sun.jersey.api.core.InjectParam;

@Path("workspaces")
public class WorkspacesResource {

	@InjectParam
	private WorkspaceService workspaceService;

	@InjectParam
	private ImageService imageService;

	@InjectParam
	private BuildService buildService;

	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Workspace create(@QueryParam("name") String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name is empty");
		}

		Workspace workspace = new Workspace(name);
		return this.workspaceService.save(workspace);
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Workspace find(@PathParam("id") String id) {
		Workspace workspace = this.workspaceService.findById(id);
		return workspace;
	}

	@GET
	@Path("{workspaceId}/exports")
	@Produces("application/zip")
	public Response export(@PathParam("workspaceId") String workspaceId) {
		// Get workspace
		Workspace workspace = this.workspaceService.findById(workspaceId);
		if (workspace == null) {
			throw new IllegalArgumentException("Workspace " + workspaceId + " not found");
		}

		File zipFile = this.imageService.exportImages(workspace);
		return Response.ok(zipFile).header("Content-Disposition", "attachment; filename=\"" + workspace.getName() + ".zip\"").header("Content-Length", zipFile.length()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Workspace> findAll() {
		List<Workspace> workspaces = this.workspaceService.findAll();
		return workspaces;
	}

	@POST
	@Path("createBuild")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Build createBuild(BuildConfiguration buildConfiguration) {
		// Check params
		if (buildConfiguration.getWorkspaceId() == null) {
			throw new IllegalArgumentException("Cannot create build : workspace is null");
		}
		Workspace workspace = this.workspaceService.findById(buildConfiguration.getWorkspaceId());
		if (workspace == null) {
			throw new IllegalArgumentException("Workspace " + buildConfiguration.getWorkspaceId() + " not found");
		}
		if (buildConfiguration.getFeatureConfigurations() == null || buildConfiguration.getFeatureConfigurations().isEmpty()) {
			throw new IllegalArgumentException("Cannot create build : no feature configurations found");
		}

		Build build = this.buildService.createBuild(workspace, buildConfiguration.getFeatureConfigurations());
		return build;
	}

	@GET
	@Path("{workspaceId}/builds")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Build> getBuilds(@PathParam("workspaceId") String workspaceId) {
		// Get workspace
		Workspace workspace = this.workspaceService.findById(workspaceId);
		if (workspace == null) {
			throw new IllegalArgumentException("Workspace " + workspaceId + " not found");
		}

		List<Build> builds = workspace.getBuilds();
		return builds;
	}
}
