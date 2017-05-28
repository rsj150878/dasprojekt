package com.app.Components;

import java.util.Arrays;
import java.util.List;

import com.app.dbIO.DBShowNeu;
import com.app.printClasses.ShowBewertungsBlatt;
import com.app.showData.Show;
import com.app.showData.ShowGeschlechtEnde;
import com.app.showData.ShowHund;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowGeschlechtAbschlussComponent extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919587212524657461L;

	private DBShowNeu db;
	private ShowGeschlechtEnde ende;
	private Show show;
	VerticalLayout panelContent;

	public ShowGeschlechtAbschlussComponent(DBShowNeu db, Show show, ShowGeschlechtEnde ende) {
		this.db = db;
		this.show = show;
		this.ende = ende;

		setWidth("100%");
		setCaption("Ende " + ende.getRasse().getRassenLangBezeichnung() + " - "
				+ ("R".equals(ende.getGeschlechtEnde()) ? "Rüden" : "Hündinnen"));
		panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponent(buildTab());
		panelContent.addComponent(buildPrintButton());

		setContent(panelContent);
	}

	private Component buildTab() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);

		layout.addComponent(buildKlubSieger("Klubsieger", "C"));
		
		layout.addComponent(buildBesterHund());

		return layout;

	}
	
	private Component buildPrintButton() {
		Button printButton = new Button ("Rasse des Rings drucken");
		printButton.addClickListener( event -> {
			ShowHund[] arry = ende.getRingGeschlechtEndeFor().flattened().filter(p -> (p instanceof ShowHund
					&& p.getRasse()
					.equals(ende.getRasse()) && p.getGeschlecht().equals(ende.getGeschlechtEnde())))
					.toArray(ShowHund[]::new);
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, arry);
			panelContent.addComponent(blatt);
			
		});
		
		return printButton;
	}

	private Component buildKlubSieger(String klubSiegerFor, String dataBaseValue) {
		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(klubSiegerFor);
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		System.out.println("aufbau feld");
		;
		ShowHund[] hund = { (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
				.filter(x -> (dataBaseValue.equals(x.getClubsieger()) && x.getRasse()
						.equals(ende.getRasse()) && x.getGeschlecht().equals(ende.getGeschlechtEnde())))
				.findFirst().orElse(null) };

		if (!(hund[0] == null)) {
			// hund = (ShowHund) zwData.get();
			textField.setValue(hund[0].getKatalognumer());
			hundeName.setValue(hund[0].getShowHundName());
		}

		textField.addValueChangeListener(event -> {

			if (!(hund[0] == null)) {
				hund[0].setClubsieger("");
				saveHund(hund[0]);

			}
			hund[0] = (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
					.filter(x -> event.getValue().trim().equals(x.getKatalognumer().trim())).findFirst().orElse(null);

			if (!(hund[0] == null)) {
				// textField.setValue(hund[0].getKatalognumer());
				hundeName.setValue(hund[0].getShowHundName());
				hund[0].setClubsieger(dataBaseValue);
				saveHund(hund[0]);
			} else {
				hundeName.setValue("");
			}

		});
		platzLayout.addComponent(textField);

		platzLayout.addComponent(hundeName);

		return platzLayout;

	}

	private Component buildBesterHund() {
		
		CheckBoxGroup<String> groupBOB = new CheckBoxGroup<>();
		List<String> dataBOB = Arrays.asList("B","O");
		
		
		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(ende.getGeschlechtEnde().equals("R") ? "Bester Rüde" : "Beste Hündin");
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
				.filter(x -> (!(x.getBOB() == null) && !x.getBOB().isEmpty() && x.getRasse()
						.equals(ende.getRasse()) && x.getGeschlecht().equals(ende.getGeschlechtEnde())))
				.findFirst().orElse(null) };

		if (!(hund[0] == null)) {
			// hund = (ShowHund) zwData.get();
			textField.setValue(hund[0].getKatalognumer());
			hundeName.setValue(hund[0].getShowHundName());
		}

		textField.addValueChangeListener(event -> {

			if (!(hund[0] == null)) {
				hund[0].setBOB("");
				saveHund(hund[0]);

			}
			hund[0] = (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
					.filter(x -> event.getValue().trim().equals(x.getKatalognumer().trim())).findFirst().orElse(null);

			if (!(hund[0] == null)) {
				// textField.setValue(hund[0].getKatalognumer());
				hundeName.setValue(hund[0].getShowHundName());
				hund[0].setBOB("B");
				saveHund(hund[0]);
			} else {
				hundeName.setValue("");
			}

		});
		platzLayout.addComponent(textField);

		platzLayout.addComponent(hundeName);
		
		groupBOB.setItems(dataBOB);
		groupBOB.setItemCaptionGenerator(item -> item.equals("B") ? "BOB" : "BOS");
		if (!(hund[0] == null)) {
			groupBOB.select(hund[0].getBOB() == null ? "" : hund[0].getBOB());
		}
		
		groupBOB.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		platzLayout.addComponent(groupBOB);
		
		groupBOB.addSelectionListener(event -> {
			hund[0].setBOB(
					event.getFirstSelectedItem().isPresent() ? event.getFirstSelectedItem().get() : null);
			saveHund(hund[0]);
		});


		return platzLayout;

	}

	private void saveHund(ShowHund hund) {
		try {
			db.updateShowHund(hund);
		} catch (Exception e) {
			Notification.show("Fehler beim speichern", Notification.Type.ERROR_MESSAGE);
		}
	}

}
