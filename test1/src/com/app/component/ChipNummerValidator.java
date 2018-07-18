package com.app.component;

import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.Validator.InvalidValueException;

public class ChipNummerValidator implements Validator {

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!(value == null)) {
			if (!(value instanceof String && ((String) value).length() == 15))
				throw new InvalidValueException(
						"die Chipnummer muss 15 Stellen haben");

			try {
				Double db = Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				throw new InvalidValueException(
						"die Chipnummer darf nur aus Ziffern bestehen");
			}
		}

	}
}
