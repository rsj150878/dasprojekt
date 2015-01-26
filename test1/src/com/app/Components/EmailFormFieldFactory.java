package com.app.Components;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

class EmailFormFieldFactory extends DefaultFieldFactory {

	final private String inputFieldWidth = "100%";
	
	public EmailFormFieldFactory() {
	}

	public Field createField(Item item, Object propertyId,
			Component uiContext) {

		if ("to".equals(propertyId)) {
			TextField tf = new TextField();
			tf.setWidth(inputFieldWidth);
			return tf;
		} else if ("cc".equals(propertyId)) {
			TextField tf = new TextField();
			tf.setWidth(inputFieldWidth);
			return tf;
		} else if ("bcc".equals(propertyId)) {
			TextField tf = new TextField();
			tf.setWidth(inputFieldWidth);
			return tf;
		} else if ("subject".equals(propertyId)) {
			TextField tf = new TextField();
			tf.setWidth(inputFieldWidth);
			return tf;
		} else if ("body".equals(propertyId)) {
			return new RichTextArea();
		}

		return null;

	}
}
