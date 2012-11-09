package com.pmerienne.iclassification.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;

public class BuildDetail extends Composite {

	private static BuildDetailUiBinder uiBinder = GWT.create(BuildDetailUiBinder.class);

	interface BuildDetailUiBinder extends UiBinder<Widget, BuildDetail> {
	}

//	private final static DateTimeFormat SHORT_TIME_FORMAT = DateTimeFormat.getFormat(PredefinedFormat.YEAR_MONTH_WEEKDAY_DAY);
	private final static DateTimeFormat LONG_TIME_FORMAT = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL);

	@UiField
	Label date;

	@UiField
	Label state;

	@UiField
	HTMLPanel featureConfigurationsContainer;

	@UiField
	HTMLPanel evaluationPanel;

	@UiField
	Label successRate;

	@UiField
	Label errorCount;

	@UiField
	Label successCount;

	public BuildDetail() {
		initWidget(uiBinder.createAndBindUi(this));
		this.setVisible(false);
	}

	public void setBuild(Build build) {
		this.setVisible(true);

		// Set build date and state
		this.date.setText(LONG_TIME_FORMAT.format(build.getDate()));
		this.state.setText(build.getState().name());

		// Fill feature configurations
		this.featureConfigurationsContainer.clear();
		for (FeatureConfiguration fc : build.getFeatureConfigurations()) {
			String fcDetail = "type : " + fc.getType().name() + ", crop : " + fc.isUseCropZone() + ", dictionary size : " + fc.getDictionarySize();
			Label fcLabel = new Label(fcDetail);
			this.featureConfigurationsContainer.add(fcLabel);
		}

		// Set evaluation
		ClassificationEvaluation eval = build.getClassificationEvaluation();
		this.evaluationPanel.clear();
		if (eval != null) {
			this.successRate.setText(Double.toString(eval.successRate()));
			this.errorCount.setText(Integer.toString(eval.getErrorCount()));
			this.successCount.setText(Integer.toString(eval.getSucessCount()));
			this.evaluationPanel.setVisible(true);
		} else {
			this.evaluationPanel.setVisible(false);
		}
	}

	public void clear() {
		this.setVisible(false);
	}
}
