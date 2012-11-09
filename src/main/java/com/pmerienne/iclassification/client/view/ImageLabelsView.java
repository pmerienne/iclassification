package com.pmerienne.iclassification.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public interface ImageLabelsView extends IsWidget {

	void setImagesLabels(List<ImageLabel> labels);

	void clear();

	void setPresenter(Presenter presenter);

	interface Presenter {

		void save(ImageLabel label);

		void delete(ImageLabel label);
	}
}
