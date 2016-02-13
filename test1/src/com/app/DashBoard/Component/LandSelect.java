package com.app.DashBoard.Component;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class LandSelect<T> extends CustomField<T> {

	private final ComboBox comboBox;
	private final HorizontalLayout content;
	private final Label label;

	@Override
	protected Component initContent() {
		return content;
	}

	public LandSelect() {
		content = new HorizontalLayout();
		content.setSpacing(true);
		content.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		content.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		label = new Label("Land");
		
		label.addStyleName(ValoTheme.LABEL_LARGE);
		label.setWidth(10.0f,Unit.EM);
		content.addComponent(label);
		
		comboBox = new ComboBox();
		comboBox.setTextInputAllowed(false);
		comboBox.setNullSelectionAllowed(false);
		comboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
		comboBox.setWidth(10.0f, Unit.EM);
		comboBox.setEnabled(true);
		comboBox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(
					final com.vaadin.data.Property.ValueChangeEvent event) {
				setValue((T) event.getProperty().getValue());
			}
		});
		content.addComponent(comboBox);

	}

	@Override
	protected void setInternalValue(final T newValue) {
		System.out.println("newValue");
		super.setInternalValue(newValue);
		comboBox.setValue(newValue);
	}

	public void addOption(final T itemId, final String caption) {
		comboBox.addItem(itemId);
		comboBox.setItemCaption(itemId, caption);
	}

	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) Object.class;
	}

}
