package com.pmerienne.iclassification.client.widget;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ImageLabelTable extends CellTable<ImageLabel> {

	private final static Integer DESCRIPTION_LIMIT = 40;

	public ImageLabelTable() {
		this.setEmptyTableWidget(new Label("No data."));
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		// Name
		TextColumn<ImageLabel> nameColumn = new TextColumn<ImageLabel>() {
			@Override
			public String getValue(ImageLabel label) {
				return label.getName();
			}
		};
		this.addColumn(nameColumn, "Name");

		// Short code
		TextColumn<ImageLabel> shortCodeColumn = new TextColumn<ImageLabel>() {
			@Override
			public String getValue(ImageLabel label) {
				return label.getShortCode();
			}
		};
		this.addColumn(shortCodeColumn, "Short code");

		// Description
		TextColumn<ImageLabel> descriptionColumn = new TextColumn<ImageLabel>() {

			@Override
			public String getValue(ImageLabel label) {
				String description = label.getDescription() == null ? "" : label.getDescription();
				if (description.length() > DESCRIPTION_LIMIT) {
					description = description.substring(0, DESCRIPTION_LIMIT);
				}
				return description;
			}
		};
		this.addColumn(descriptionColumn, "Description");

		// Set fixed size
		this.setTableLayoutFixed(true);
		this.setColumnWidth(nameColumn, "20%");
		this.setColumnWidth(shortCodeColumn, "20%");
		this.setColumnWidth(descriptionColumn, "60%");
	}

}
