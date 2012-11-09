package com.pmerienne.iclassification.server.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pmerienne.iclassification.server.service.ImageLabelService;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.sun.jersey.api.core.InjectParam;

@Path("labels")
public class ImageLabelsResource {

	@InjectParam
	private ImageLabelService imageLabelService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ImageLabel> findAll() {
		List<ImageLabel> imageLabels = this.imageLabelService.findAll();
		return imageLabels;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ImageLabel save(ImageLabel imageLabel) {
		return this.imageLabelService.save(imageLabel);
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(ImageLabel imageLabel) {
		this.imageLabelService.delete(imageLabel);
	}

}
