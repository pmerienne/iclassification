package com.pmerienne.iclassification.server.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.pmerienne.iclassification.server.service.DatasetService;
import com.pmerienne.iclassification.server.service.FeatureService;
import com.pmerienne.iclassification.server.service.ImageLabelService;
import com.pmerienne.iclassification.server.service.ImageService;
import com.pmerienne.iclassification.server.service.WorkspaceService;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("images")
public class ImagesResource {

	private final static Logger LOGGER = Logger.getLogger(ImagesResource.class);

	@InjectParam
	private ImageService imageService;

	@InjectParam
	private WorkspaceService workspaceService;

	@InjectParam
	private DatasetService datasetService;

	@InjectParam
	private ImageLabelService imageLabelService;

	@InjectParam
	private FeatureService featureService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageMetadata find(@PathParam("id") String id) {
		ImageMetadata metadata = this.imageService.findById(id);
		return metadata;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ImageMetadata> find(@QueryParam("label") String label, @QueryParam("workspaceId") String workspaceId) {
		// Check params
		ImageLabel imageLabel = null;
		try {
			imageLabel = this.imageLabelService.findByName(label);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Label " + label + " not found");
		}

		// Get workspace
		Workspace workspace = this.workspaceService.findById(workspaceId);
		if (workspace == null) {
			throw new IllegalArgumentException("Workspace " + workspaceId + " not found");
		}

		List<ImageMetadata> images = this.imageService.find(workspace, imageLabel);
		return images;
	}

	@GET
	@Path("{id}/file")
	@Produces("image/*")
	public Response getFile(@PathParam("id") String id) throws FileNotFoundException {
		ImageMetadata metadata = this.imageService.findById(id);
		if (metadata == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		File file = this.imageService.getFile(metadata);
		return Response.ok(new FileInputStream(file)).build();
	}

	@GET
	@Path("{id}/segmentedFile")
	@Produces("image/*")
	public Response getSegmentedFile(@PathParam("id") String id) throws FileNotFoundException {
		ImageMetadata metadata = this.imageService.findById(id);
		if (metadata == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			File file = this.imageService.getSegmentedFile(metadata);

			return Response.ok(new FileInputStream(file)).build();
		} catch (IOException ioe) {
			return Response.serverError().entity(ioe.getMessage()).build();
		}
	}

	@GET
	@Path("{id}/featureFile")
	@Produces("image/*")
	public Response getSegmentedFile(@PathParam("id") String id, @QueryParam("type") String type, @QueryParam("useCropZone") boolean useCropZone) throws FileNotFoundException {
		ImageMetadata metadata = this.imageService.findById(id);
		if (metadata == null) {
			return Response.status(Status.NOT_FOUND).entity("Image " + id + " not found").build();
		}

		FeatureType featureType = null;
		try {
			featureType = FeatureType.valueOf(type);
		} catch (IllegalArgumentException iae) {
			return Response.status(Status.NOT_FOUND).entity("Feature type " + type + " not found").build();
		}

		try {
			File file = this.featureService.getFeatureFile(metadata, featureType, useCropZone);

			return Response.ok(new FileInputStream(file)).build();
		} catch (IOException ioe) {
			return Response.serverError().entity(ioe.getMessage()).build();
		}
	}

	@POST
	@Path("remove")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@QueryParam("workspaceId") String workspaceId, @QueryParam("filename") String filename) {
		Workspace workspace = this.workspaceService.findById(workspaceId);
		if (workspace == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ImageMetadata metadata = this.imageService.findById(filename);
		if (metadata == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		this.imageService.delete(workspace, filename);
		return Response.ok().build();
	}

	@POST
	@Path("/importFromZip")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response importFromZip(@FormDataParam("workspaceId") String workspaceId, @FormDataParam("file") InputStream is,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		try {
			Workspace workspace = this.workspaceService.findById(workspaceId);
			this.imageService.importFromZip(workspace, is);
		} catch (Exception ex) {
			LOGGER.error("Error while importing from zip", ex);
			return Response.serverError().entity(ex.getMessage()).build();
		}

		return Response.status(200).build();

	}

	@POST
	@Path("setCropZone")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageMetadata setCropZone(@QueryParam("workspaceId") String workspaceId, @QueryParam("imageFilename") String imageFilename, CropZone cropZone) {

		Workspace workspace = this.workspaceService.findById(workspaceId);
		if (workspace == null) {
			throw new IllegalArgumentException("Cannot set image crop zone : Workspace " + workspaceId + " not found");
		}

		ImageMetadata imageMetadata = this.imageService.findById(imageFilename);
		if (imageMetadata == null) {
			throw new IllegalArgumentException("Cannot set image crop zone : Image " + imageFilename + " not found");
		}

		this.imageService.setCropZone(workspace, imageMetadata, cropZone);

		return imageMetadata;
	}
}
