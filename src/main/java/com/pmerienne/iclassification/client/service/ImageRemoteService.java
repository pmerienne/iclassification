package com.pmerienne.iclassification.client.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Path("images")
public interface ImageRemoteService extends RestService {

	@GET
	@Path("{id}")
	void find(@PathParam("id") String id, MethodCallback<ImageMetadata> callback);

	@GET
	void find(@QueryParam("label") String label, @QueryParam("workspaceId") String workspaceId, MethodCallback<List<ImageMetadata>> callback);

	@POST
	@Path("remove")
	void remove(@QueryParam("workspaceId") String workspaceId, @QueryParam("filename") String filename, MethodCallback<Void> callback);

	@POST
	@Path("setCropZone")
	void setCropZone(@QueryParam("workspaceId") String workspaceId, @QueryParam("imageFilename") String imageFilename, CropZone cropZone,
			MethodCallback<ImageMetadata> callback);

}
