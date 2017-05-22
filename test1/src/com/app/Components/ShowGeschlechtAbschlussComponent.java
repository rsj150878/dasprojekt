package com.app.Components;

import com.app.dbIO.DBShowNeu;
import com.app.enumPackage.ShowKlassen;
import com.app.showData.ShowGeschlechtEnde;
import com.app.showData.ShowHund;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ShowGeschlechtAbschlussComponent extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919587212524657461L;

	private DBShowNeu db;
	private ShowGeschlechtEnde ende;

	public ShowGeschlechtAbschlussComponent(DBShowNeu db, ShowGeschlechtEnde ende) {
		this.db = db;
		this.ende = ende;

		setWidth("100%");
		setCaption("Ende " + ende.getRasse().getRassenLangBezeichnung() + " - "
				+ ("R".equals(ende.getGeschlechtEnde()) ? "Rüden" : "Hündinnen"));
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponent(buildTab());

		setContent(panelContent);
	}

	private Component buildTab() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);

		layout.addComponent(buildKlubSieger("Klubsieger", "C"));

		// layout.addComponent(buildPlatzierungField("2"));

		return layout;

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

		System.out.println("aufbau feld");;
		//TODO Rasse und geschlecht noch dazu bauen
		ShowHund[] hund = { (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
				.filter(x -> dataBaseValue.equals(x.getClubsieger())).findFirst().orElse(null) };

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
			hund[0] = (ShowHund) ende.getRingGeschlechtEndeFor().getKlassenAsStream()
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

	private void saveHund(ShowHund hund) {
		try {
			db.updateShowHund(hund);
		} catch (Exception e) {
			Notification.show("Fehler beim speichern", Notification.Type.ERROR_MESSAGE);
		}
	}

}
