package com.app.component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.ShowKlassen;
import com.app.printclasses.ShowBewertungsBlatt;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.app.showdata.ShowRasseEnde;
import com.app.showdata.ShowRing;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
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

public class ShowRasseAbschlussComponent extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3919587212524657461L;

	private DBShowNeu db;
	private ShowRasseEnde ende;
	private Show show;
	VerticalLayout panelContent;

	public ShowRasseAbschlussComponent(DBShowNeu db, Show show, ShowRasseEnde ende) {
		this.db = db;
		this.show = show;
		this.ende = ende;

		setWidth("100%");
		setCaption("Ende " + ende.getRasse().getRassenLangBezeichnung());
		panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponent(buildTab());
		panelContent.addComponent(buildPrintButton());
		panelContent.addComponent(buildMailButton());

		setContent(panelContent);
	}

	private Component buildTab() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);

		if (show.getSchauTyp().equals("C")) {
			layout.addComponent(buildBob("Klubsieger", "C"));
		}

//		if (show.getSchauTyp().equals("I")) {
//			layout.addComponent(buildCacib("CACIB", "C"));
//			layout.addComponent(buildCacib("Res. Cacib", "R"));
//		}
//
//		layout.addComponent(buildBesterHund());

		return layout;

	}

	private Component buildPrintButton() {
		Button printButton = new Button("Rasse drucken");
		printButton.addClickListener(event -> {
			ShowHund[] arry = ende.getRasseEndeFor().flattened().filter(p -> (p instanceof ShowHund
					&& p.getRasse().equals(ende.getRasse())))
					.toArray(ShowHund[]::new);
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, arry);
			panelContent.addComponent(blatt);

		});

		return printButton;
	}

	private Component buildMailButton() {
		Button mailButton = new Button("Bewertung des Geschlechts mailen");
		mailButton.addClickListener(event -> {
			ShowHund[] arry = ende.getRasseEndeFor().flattened().filter(p -> (p instanceof ShowHund
					&& p.getRasse().equals(ende.getRasse())))
					.toArray(ShowHund[]::new);

			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, arry);
			// panelContent.addComponent(blatt);
			try {
				blatt.sendBewertungAsEmail(show, arry);
				Notification.show("Mails erfolgreich verschickt");

			} catch (Exception e) {
				e.printStackTrace();
				Notification.show("Fehler beim mailschicken");
			}

		});
		return mailButton;
	}

	private Component buildBob(String klubSiegerFor, String dataBaseValue) {
		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(klubSiegerFor);
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende
				.getRasseEndeFor().flattened().filter(x -> (dataBaseValue.equals(x.getBOB())
						&& x.getRasse().equals(ende.getRasse()) ))
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
			hund[0] = (ShowHund) ende.getRasseEndeFor().flattened()
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
