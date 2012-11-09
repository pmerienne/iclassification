package com.pmerienne.iclassification.client.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.pmerienne.iclassification.shared.model.ImageLabel;

@Path("labels")
public interface ImageLabelRemoteService extends RestService {

	@GET
	void findAll(MethodCallback<List<ImageLabel>> callback);

	@POST
	void save(ImageLabel imageLabel, MethodCallback<ImageLabel> callback);

	@DELETE
	void delete(ImageLabel imageLabel, MethodCallback<Void> callback);
}
