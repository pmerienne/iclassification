package com.pmerienne.iclassification.client.view;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Pagination;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.pmerienne.iclassification.client.widget.ImageLabelTable;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ImageLabelsViewImpl extends Composite implements Editor<ImageLabel>, ImageLabelsView {

	private static ImageLabelsViewImplUiBinder uiBinder = GWT.create(ImageLabelsViewImplUiBinder.class);

	interface ImageLabelsViewImplUiBinder extends UiBinder<Widget, ImageLabelsViewImpl> {
	}

	interface Driver extends SimpleBeanEditorDriver<ImageLabel, ImageLabelsViewImpl> {
	}

	private Driver driver = GWT.create(Driver.class);

	private Presenter presenter;

	@UiField
	ControlGroup nameControlGroup;
	@UiField
	TextBox name;
	@UiField
	@Editor.Ignore
	HelpInline nameHelpInline;

	@UiField
	ControlGroup shortCodeControlGroup;
	@UiField
	TextBox shortCode;
	@UiField
	@Editor.Ignore
	HelpInline shortCodeHelpInline;

	@UiField
	TextArea description;

	@UiField
	ImageLabelTable table;

	@UiField
	Pagination pagination;

	private SimplePager pager;
	private ListDataProvider<ImageLabel> dataProvider;

	private ImageLabel editedLabel;

	public ImageLabelsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		this.driver.initialize(this);
		this.setLabel(new ImageLabel());

		this.table.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				rebuildPager();
			}

		});

		final SingleSelectionModel<ImageLabel> selectionModel = new SingleSelectionModel<ImageLabel>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ImageLabel selectedLabel = selectionModel.getSelectedObject();
				ImageLabelsViewImpl.this.setLabel(selectedLabel);
			}
		});
		this.table.setSelectionModel(selectionModel);

		this.pager = new SimplePager();
		this.pager.setDisplay(this.table);
		this.pager.setPageSize(15);
		this.pagination.clear();

		this.dataProvider = new ListDataProvider<ImageLabel>();
		this.dataProvider.addDataDisplay(table);
	}

	@UiHandler("newLabelButton")
	protected void onNewLabelClicked(ClickEvent event) {
		this.setLabel(new ImageLabel());
	}

	@UiHandler("saveButton")
	protected void onSaveClicked(ClickEvent event) {
		ImageLabel label = this.driver.flush();
		this.save(label);
	}

	@UiHandler("cancelButton")
	protected void onCancelClicked(ClickEvent event) {
		this.setLabel(this.editedLabel);
	}

	@UiHandler("deleteButton")
	protected void onDeleteClicked(ClickEvent event) {
		this.deleteLabel(this.editedLabel);
	}

	@Override
	public void setImagesLabels(List<ImageLabel> labels) {
		this.dataProvider.setList(labels);
		this.dataProvider.flush();
		this.dataProvider.refresh();
		this.rebuildPager();
	}

	private void rebuildPager() {
		this.pagination.clear();

		if (this.pager.getPageCount() == 0) {
			return;
		}

		NavLink prev = new NavLink("<");
		prev.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ImageLabelsViewImpl.this.pager.previousPage();
			}
		});
		prev.setDisabled(!this.pager.hasPreviousPage());
		this.pagination.add(prev);

		int before = 2;
		int after = 2;
		while (!this.pager.hasPreviousPages(before) && before > 0) {
			before--;
			if (this.pager.hasNextPages(after + 1)) {
				after++;
			}
		}

		while (!this.pager.hasNextPages(after) && after > 0) {
			after--;
			if (this.pager.hasPreviousPages(before + 1)) {
				before++;
			}
		}

		for (int i = this.pager.getPage() - before; i <= this.pager.getPage() + after; i++) {
			final int index = i + 1;
			NavLink page = new NavLink(String.valueOf(index));
			page.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ImageLabelsViewImpl.this.pager.setPage(index - 1);
				}
			});
			if (i == this.pager.getPage()) {
				page.setActive(true);
			}
			this.pagination.add(page);
		}

		NavLink next = new NavLink(">");
		next.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ImageLabelsViewImpl.this.pager.nextPage();
			}
		});
		next.setDisabled(!this.pager.hasNextPage());
		this.pagination.add(next);
	}

	private void deleteLabel(ImageLabel label) {
		this.presenter.delete(label);
	}

	private void setLabel(ImageLabel imageLabel) {
		this.editedLabel = imageLabel;
		this.driver.edit(imageLabel);
	}

	private void save(ImageLabel label) {
		boolean hasError = false;

		if (label.getName() == null && label.getName().isEmpty()) {
			this.nameControlGroup.setType(ControlGroupType.ERROR);
			this.nameHelpInline.setText("Name is required");
			hasError = true;
		}

		if (label.getShortCode() == null && label.getShortCode().isEmpty()) {
			this.shortCodeControlGroup.setType(ControlGroupType.ERROR);
			this.shortCodeHelpInline.setText("Short code is required");
			hasError = true;
		}

		if (!hasError) {
			this.presenter.save(label);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void clear() {
		this.setLabel(new ImageLabel());
		this.setImagesLabels(new ArrayList<ImageLabel>());
	}
}
