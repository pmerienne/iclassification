package com.pmerienne.iclassification.client.activity;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.service.Services;
import com.pmerienne.iclassification.client.utils.Notifications;
import com.pmerienne.iclassification.client.view.ImageLabelsView;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ImageLabelsActivity extends AbstractActivity implements ImageLabelsView.Presenter {

	private ClientFactory clientFactory;

	public ImageLabelsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		ImageLabelsView view = this.clientFactory.getImageLabelsView();
		view.clear();

		view.setPresenter(this);
		panel.setWidget(view);

		this.loadImageLabels();
	}

	@Override
	public void save(ImageLabel label) {
		Services.getImageLabelService().save(label, new MethodCallback<ImageLabel>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to save label. Cause : " + arg1.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, ImageLabel label) {
				Notifications.info("Label " + label.getName() + " saved");
				ImageLabelsActivity.this.loadImageLabels();
			}
		});
	}

	@Override
	public void delete(ImageLabel label) {
		Services.getImageLabelService().delete(label, new MethodCallback<Void>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to delete label. Cause : " + arg1.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, Void arg1) {
				loadImageLabels();
			}
		});
	}

	private void loadImageLabels() {
		final ImageLabelsView view = this.clientFactory.getImageLabelsView();

		Services.getImageLabelService().findAll(new MethodCallback<List<ImageLabel>>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to load image labels : " + arg1.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, List<ImageLabel> labels) {
				view.setImagesLabels(labels);
			}
		});

	}

}
