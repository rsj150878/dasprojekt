package com.app.component;

import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;

public class ShowInfoForm extends Panel {
	
	private final DateField wurfDatumField;
	private final TextField hundeName;
	private final TextField zbNr;
	private final TextField chipNr;
	private final TextArea bewertung;
	private final OptionGroup formWertGroup;

	
	public ShowInfoForm(String forKlasse) {
		//setSizeFull();
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		
		FormLayout mainLayout = new FormLayout();
		//mainLayout.setWidth("100%");
		//mainLayout.setHeight("100%");
		 mainLayout.setSizeFull();
		//mainLayout.setSizeUndefined();
		mainLayout.setResponsive(true);
		mainLayout.setSpacing(true);

		hundeName = new TextField("Hundename");
		hundeName.setNullRepresentation("");
		hundeName.setWidth("100%");
		mainLayout.addComponent(hundeName);

		zbNr = new TextField("Zuchtbuchnummer");
		zbNr.setNullRepresentation("");

		mainLayout.addComponent(zbNr);

		chipNr = new TextField("Chipnummer");
		chipNr.setNullRepresentation("");

		mainLayout.addComponent(chipNr);

		wurfDatumField = new PopupDateField("Wurfdatum");
		wurfDatumField.setResolution(Resolution.DAY);
		wurfDatumField.setDateFormat("dd.MM.yyyy");

		mainLayout.addComponent(wurfDatumField);

		bewertung = new TextArea("Bewertung");
		bewertung.setNullRepresentation("");
		bewertung.addStyleName(ValoTheme.TEXTAREA_HUGE);
		//bewertung.setSizeFull();
		//bewertung.setWidth("100%");

		 mainLayout.addComponent(bewertung);

		formWertGroup = new OptionGroup("Formwert");

		if (forKlasse.equals("JÜ")) {
			formWertGroup.addItem("vv");
			formWertGroup.setItemCaption("vv", "vielversprechend");
			formWertGroup.addItem("v");
			formWertGroup.setItemCaption("v", "versprechend");
			formWertGroup.addItem("g");
			formWertGroup.setItemCaption("g", "gut");

		} else {
			formWertGroup.addItem("v");
			formWertGroup.setItemCaption("v", "vorzüglich");
			formWertGroup.addItem("sg");
			formWertGroup.setItemCaption("sg", "sehr gut");
			formWertGroup.addItem("g");
			formWertGroup.setItemCaption("g", "gut");
		}
		formWertGroup.setNullSelectionAllowed(true);

		mainLayout.addComponent(formWertGroup);
		
		setContent(mainLayout);
		
	}

	public void setDataSource(Item dataSource) {
		formWertGroup.setPropertyDataSource(dataSource.getItemProperty("formwert"));
		bewertung.setPropertyDataSource(dataSource.getItemProperty("bewertung"));
		wurfDatumField.setPropertyDataSource(dataSource.getItemProperty("wurftag"));
		chipNr.setPropertyDataSource(dataSource.getItemProperty("chipnummer"));
		zbNr.setPropertyDataSource(dataSource.getItemProperty("zuchtbuchnummer"));
		hundeName.setPropertyDataSource(dataSource.getItemProperty("name"));

	}


}
