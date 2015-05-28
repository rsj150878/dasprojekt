package com.app.Components;

import org.tepi.filtertable.FilterGenerator;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

public class MitgliederFilterGenerator implements FilterGenerator {

	@Override
	public Filter generateFilter(Object propertyId, Object value) {
		System.out.println("propertyId" + propertyId);
		System.out.println("value" + value);
		if ("Name".equals(propertyId)) {
			// /* Create an 'equals' filter for the ID field */
			if (value != null && value instanceof String) {
				// try {

				return new SimpleStringFilter("FilterField",
						(String) value, true, false);
				// Integer.parseInt((String) value));
				// } catch (NumberFormatException ignored) {
				// // If no integer was entered, just generate default
				// filter
				// }
			}
		}
		// For other properties, use the default filter
		return null;
	}

	@Override
	public AbstractField<?> getCustomFilterComponent(Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void filterRemoved(Object propertyId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterAdded(Object propertyId,
			Class<? extends Filter> filterType, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Filter filterGeneratorFailed(Exception reason,
			Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filter generateFilter(Object propertyId, Field<?> field) {
		// TODO Auto-generated method stub
		return null;
	}

}