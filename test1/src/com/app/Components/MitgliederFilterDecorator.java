package com.app.Components;

import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.datefield.Resolution;

public class MitgliederFilterDecorator implements FilterDecorator {

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId,
			boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		// TODO Auto-generated method stub
		return 400;
	}

	@Override
	public String getFromCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSetCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClearCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resolution getDateFieldResolution(Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateFormatPattern(Object propertyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumberFilterPopupConfig getNumberFilterPopupConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		// TODO Auto-generated method stub
		return false;
	}

}
