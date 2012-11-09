package com.pmerienne.iclassification.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pmerienne.iclassification.client.widget.BuildDetail;
import com.pmerienne.iclassification.client.widget.BuildList;
import com.pmerienne.iclassification.client.widget.CreateBuildModal;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;

public class WorkspaceConfigurationViewImpl extends Composite implements WorkspaceConfigurationView {

	private static WorkspaceConfigurationViewImplUiBinder uiBinder = GWT.create(WorkspaceConfigurationViewImplUiBinder.class);

	interface WorkspaceConfigurationViewImplUiBinder extends UiBinder<Widget, WorkspaceConfigurationViewImpl> {
	}

	@UiField
	Button createBuildButton;

	@UiField
	BuildList buildList;

	@UiField
	BuildDetail buildDetail;

	@UiField
	CreateBuildModal createBuildModal;

	private Presenter presenter;

	private SingleSelectionModel<Build> buildSelectionModel;

	private BuildComparator comparator = new BuildComparator();

	public WorkspaceConfigurationViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		this.buildSelectionModel = new SingleSelectionModel<Build>();
		this.buildSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Build build = buildSelectionModel.getSelectedObject();
				buildDetail.setBuild(build);
			}
		});
		this.buildList.setSelectionModel(this.buildSelectionModel);

		this.createBuildModal.addSaveHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				BuildConfiguration buildConfiguration = createBuildModal.getBuildConfiguration();
				presenter.createBuild(buildConfiguration);
				createBuildModal.hide();
			}
		});
	}

	@Override
	public void setBuilds(List<Build> builds) {
		if (builds == null) {
			builds = new ArrayList<Build>();
		}
		Collections.sort(builds, this.comparator);
		this.buildList.setRowData(builds);

		if (!builds.isEmpty()) {
			this.buildSelectionModel.setSelected(builds.get(0), true);
		}
	}

	@UiHandler("createBuildButton")
	protected void onCreateBuildClicked(ClickEvent event) {
		this.createBuildModal.clear();
		this.createBuildModal.show();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private static class BuildComparator implements Comparator<Build> {
		@Override
		public int compare(Build o1, Build o2) {
			if (o1.getDate().equals(o2.getDate())) {
				return 0;
			} else {
				return o1.getDate().after(o2.getDate()) ? -1 : 1;
			}
		}

	}
}
