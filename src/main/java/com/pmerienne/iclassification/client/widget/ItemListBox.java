package com.pmerienne.iclassification.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.ListBox;

public abstract class ItemListBox<T> extends ListBox {

	protected List<T> items = new ArrayList<T>();

	public ItemListBox() {
		super();
	}

	public T getSelected() {
		T item = null;

		// Get selected index
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex >= 0 && selectedIndex < this.items.size()) {
			item = this.items.get(selectedIndex);
		}

		return item;
	}

	public void setSelected(T item) {
		Integer index = this.items.indexOf(item);
		if (index < 0) {
			index = 0;
		}
		this.setSelectedIndex(index);
	}

	public void setItems(List<T> items) {
		this.items.clear();
		this.clear();

		for (T item : items) {
			String label = this.getLabel(item);
			this.addItem(label);
			this.items.add(item);
		}
	}

	protected abstract String getLabel(T item);

}