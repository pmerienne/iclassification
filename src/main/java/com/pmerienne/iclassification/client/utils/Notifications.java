package com.pmerienne.iclassification.client.utils;

public class Notifications {


	public static native void info(String title, String message) /*-{
		$wnd.toastr.info(message, title);
	}-*/;

	public static native void warning(String title, String message) /*-{
		$wnd.toastr.warning(message, title);
	}-*/;

	public static native void sucess(String title, String message) /*-{
		$wnd.toastr.sucess(message, title);
	}-*/;

	public static native void error(String title, String message) /*-{
		$wnd.toastr.error(message, title);
	}-*/;

	public static native void info(String message) /*-{
		$wnd.toastr.info(message);
	}-*/;

	public static native void warning(String message) /*-{
		$wnd.toastr.warning(message);
	}-*/;

	public static native void sucess(String message) /*-{
		$wnd.toastr.sucess(message);
	}-*/;

	public static native void error( String message) /*-{
		$wnd.toastr.error(message);
	}-*/;

}
