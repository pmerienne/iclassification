package com.pmerienne.iclassification.client.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;

public class JavascriptInjector {

	private static HeadElement head;

	/**
	 * Injects the JavaScript code into a
	 * {@code <script type="text/javascript">...</script>} element in the
	 * document header.
	 * 
	 * @param javascript
	 *            the JavaScript code
	 */
	public static void inject(String javascript) {
		HeadElement head = getHead();
		ScriptElement element = createScriptElement();
		element.setText(javascript);
		head.appendChild(element);
	}

	private static ScriptElement createScriptElement() {
		ScriptElement script = Document.get().createScriptElement();
		script.setAttribute("type", "text/javascript");
		script.setAttribute("charset", "UTF-8");
		return script;
	}

	/**
	 * Gets the document header.
	 * 
	 * @return the document header
	 */
	protected static HeadElement getHead() {
		if (head == null) {
			Element element = Document.get().getElementsByTagName("head").getItem(0);
			assert element != null : "HTML Head element required";
			HeadElement head = HeadElement.as(element);
			JavascriptInjector.head = head;
		}
		return JavascriptInjector.head;
	}
}
