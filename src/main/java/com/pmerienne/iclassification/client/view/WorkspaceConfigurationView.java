package com.pmerienne.iclassification.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;

public interface WorkspaceConfigurationView extends IsWidget {

	void setBuilds(List<Build> builds);

	void setPresenter(Presenter presenter);

	interface Presenter {
		
		void createBuild(BuildConfiguration buildConfiguration);

	}
}
