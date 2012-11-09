package com.pmerienne.iclassification.client.widget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.Build.State;

public class BuildList extends CellList<Build> {

	private final static DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("yyyy/mm/dd");

	public BuildList() {
		super(new BuildCell());
	}

	private static class BuildCell extends AbstractCell<Build> {

		@Override
		public void render(Context context, Build build, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<div>");
			sb.appendHtmlConstant("<p style='font-weight:bold'>");
			sb.appendEscaped(TIME_FORMAT.format(build.getDate()));
			sb.appendHtmlConstant("</p>");
			sb.appendHtmlConstant("<p>");
			if (State.FINISH.equals(build.getState())) {
				sb.appendEscaped("Success rate : ");
				sb.appendEscaped(Double.toString(build.getClassificationEvaluation().successRate()));
				sb.appendHtmlConstant("%");
			} else {
				sb.appendEscaped(build.getState().name());
			}
			sb.appendHtmlConstant("</p>");
			sb.appendHtmlConstant("</div>");
		}
	}
}
