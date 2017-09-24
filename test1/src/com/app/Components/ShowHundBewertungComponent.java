package com.app.Components;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import com.app.dbIO.DBShowNeu;
import com.app.enumPackage.BobDataType;
import com.app.enumPackage.CacaDataType;
import com.app.enumPackage.FormWertErwachsen;
import com.app.enumPackage.FormWertJuengsten;
import com.app.enumPackage.ShowKlassen;
import com.app.printClasses.ShowBewertungsBlatt;
import com.app.showData.Show;
import com.app.showData.ShowHund;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowHundBewertungComponent extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6381775281153472700L;
	private TextField katalogNr;
	private TextField showHundName;
	private DateField wurftag;
	private TextField zuchtbuchnummer;
	private TextField besitzershow;
	private CheckBox hundFehlt;
	private CheckBox mitglied;

	private CheckBox klubSieger;

	private TextArea bewertung;

	private RadioButtonGroup<FormWertJuengsten> formwertJuengsten;
	private RadioButtonGroup<FormWertErwachsen> formwertErw;
	private RadioButtonGroup<String> platzierung;
	private RadioButtonGroup<CacaDataType> cacaButtonGroup;
	private RadioButtonGroup<BobDataType> bobButtonGroup;

	private DBShowNeu db;

	private ShowHund hund;
	private Show show;
	VerticalLayout panelContent;

	public ShowHundBewertungComponent(DBShowNeu db, Show show, ShowHund hund) {
		this.hund = hund;
		this.show = show;
		this.db = db;
		// setHeight("100%");
		setWidth("100%");
		setCaption("Hundebewertung");
		panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponentsAndExpand(buildAllgemeinInfo(), buildAllgemeinInfo3(), buildBewertung(),
				buildFormwertInfo(), buildPrintButton());

		setContent(panelContent);

		// Set the size as undefined at all levels
		// panelContent.setSizeUndefined();

	}

	private Component buildAllgemeinInfo() {

		HorizontalLayout ersteZeile = new HorizontalLayout();
		ersteZeile.setWidth("100%");
		katalogNr = new TextField("Katalognr");
		katalogNr.setEnabled(false);
		katalogNr.setWidth("50%");
		katalogNr.setValue(hund.getKatalognumer());
		ersteZeile.addComponent(katalogNr);

		showHundName = new TextField("Hundename");
		showHundName.setEnabled(false);
		showHundName.setWidth("100%");
		showHundName.setValue(hund.getShowHundName());
		ersteZeile.addComponent(showHundName);

		zuchtbuchnummer = new TextField("Zuchtbuchnummer");
		zuchtbuchnummer.setEnabled(false);
		zuchtbuchnummer.setWidth("50%");
		zuchtbuchnummer.setValue(hund.getZuchtbuchnummer());
		ersteZeile.addComponent(zuchtbuchnummer);

		return ersteZeile;
	}

	private Component buildAllgemeinInfo3() {

		HorizontalLayout ersteZeile = new HorizontalLayout();
		ersteZeile.setWidth("100%");

		wurftag = new DateField("Wurftag");
		wurftag.setEnabled(false);
		Instant instant = Instant.ofEpochMilli(hund.getWurftag().getTime());
		LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

		wurftag.setValue(res);
		ersteZeile.addComponent(wurftag);

		besitzershow = new TextField("Besitzer");
		besitzershow.setEnabled(false);
		besitzershow.setValue(hund.getBesitzershow());

		ersteZeile.addComponent(besitzershow);

		hundFehlt = new CheckBox("hund fehlt");
		hundFehlt.setValue(hund.getHundfehlt() != null && hund.getHundfehlt().equals("J") ? true : false);
		ersteZeile.addComponent(hundFehlt);

		hundFehlt.addValueChangeListener(event -> {
			hund.setHundfehlt(hundFehlt.getValue() == true ? "J" : "N");
			saveHund();
		});

		mitglied = new CheckBox("Mitglied");
		mitglied.setValue(hund.getMitglied() != null && hund.getMitglied().equals("J") ? true : false);
		mitglied.setEnabled(false);
		ersteZeile.addComponent(mitglied);

		return ersteZeile;
	}

	private Component buildBewertung() {
		bewertung = new TextArea("Beschreibung");
		bewertung.setWidth("100%");
		bewertung.addStyleName(ValoTheme.TEXTAREA_TINY);
		bewertung.setValue(hund.getBewertung() == null ? "" : hund.getBewertung());

		bewertung.addValueChangeListener(event -> {
			hund.setBewertung(bewertung.getValue());
			saveHund();
		});

		return bewertung;
	}

	private Component buildPrintButton() {
		Button printButton = new Button("Bewertung");
		printButton.addClickListener(event -> {
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, hund);
			panelContent.addComponent(blatt);

		});

		return printButton;
	}

	private Component buildFormwertInfo() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("100%");
		if (!(hund == null)) {
			if (hund.getKlasse().equals(ShowKlassen.BABYKLASSE)) {
				// es gibt keinen Formwert
				Label text = new Label("in der Babyklasse gibt es keinen Formwert");
				layout.addComponent(text);
			} else if (hund.getKlasse().equals(ShowKlassen.JUENGSTENKLASSE)) {
				formwertJuengsten = new RadioButtonGroup<>("Formwert");
				formwertJuengsten.setItems(Arrays.asList(FormWertJuengsten.values()));
				formwertJuengsten.setItemCaptionGenerator(FormWertJuengsten::getFormWertBezeichnung);
				formwertJuengsten.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
				formwertJuengsten.setSelectedItem(FormWertJuengsten.getFormwertForKurzBezeichnung(hund.getFormwert()));
				layout.addComponent(formwertJuengsten);

				formwertJuengsten.addValueChangeListener(event -> {
					hund.setFormwert(formwertJuengsten.getValue().getFormwert());
					saveHund();
				});
			} else {
				formwertErw = new RadioButtonGroup<>("Formwert");
				formwertErw.setItems(Arrays.asList(FormWertErwachsen.values()));
				formwertErw.setItemCaptionGenerator(FormWertErwachsen::getFormWertBezeichnung);
				formwertErw.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				formwertErw.setSelectedItem(FormWertErwachsen.getFormwertForKurzBezeichnung(hund.getFormwert()));

				formwertErw.addValueChangeListener(event -> {
					hund.setFormwert(formwertErw.getValue().getFormwert());
					saveHund();
				});

				layout.addComponent(formwertErw);
			}

			platzierung = new RadioButtonGroup<>("Platzierung");
			platzierung.setItems("1", "2", "3", "4");
			platzierung.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
			platzierung.setSelectedItem(hund.getPlatzierung());

			platzierung.addValueChangeListener(event -> {
				hund.setPlatzierung(platzierung.getValue());
				saveHund();
			});

			layout.addComponent(platzierung);

			if (hund.getKlasse().equals(ShowKlassen.ZWISCHENKLASSE) || hund.getKlasse().equals(ShowKlassen.OFFENEKLASSE)
					|| hund.getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
					|| hund.getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)
					|| hund.getKlasse().equals(ShowKlassen.JUGENDKLASSE)
					|| hund.getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
				cacaButtonGroup = new RadioButtonGroup<>("JugendBester/CACA/Res. CACA/Veteranensieger");
				cacaButtonGroup.setItems(Arrays.asList(CacaDataType.values()));
				cacaButtonGroup.setItemCaptionGenerator(CacaDataType::getLangText);
				cacaButtonGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				cacaButtonGroup.setSelectedItem(CacaDataType.getTextForDataBaseValue(hund.getCACA()));

				cacaButtonGroup.addValueChangeListener(event -> {
					hund.setCACA(cacaButtonGroup.getValue().getDataBaseValue());
					saveHund();
				});

				layout.addComponent(cacaButtonGroup);

				bobButtonGroup = new RadioButtonGroup<>("BOB/BOS");
				bobButtonGroup.setItems(Arrays.asList(BobDataType.values()));
				bobButtonGroup.setItemCaptionGenerator(BobDataType::getLangText);
				bobButtonGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				bobButtonGroup.setSelectedItem(BobDataType.getTextForDataBaseValue(hund.getBOB()));

				bobButtonGroup.addValueChangeListener(event -> {
					hund.setBOB(bobButtonGroup.getValue().getDataBaseValue());
					saveHund();
				});

				layout.addComponent(cacaButtonGroup);
			}

			if (show.getSchauTyp().equals("C")) {
				if (hund.getKlasse().equals(ShowKlassen.ZWISCHENKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.OFFENEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)) {

					klubSieger = new CheckBox("KlubSieger");
					klubSieger.setValue(hund.getClubsieger() != null && hund.getHundfehlt().equals("C") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "C" : "");
						saveHund();
					});
				} else if (hund.getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
					klubSieger = new CheckBox("KlubJugendSieger");
					klubSieger.setValue(hund.getClubsieger() != null && hund.getHundfehlt().equals("J") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "J" : "");
						saveHund();
					});

				} else if (hund.getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
					klubSieger = new CheckBox("KlubVeteranenSieger");
					klubSieger.setValue(hund.getClubsieger() != null && hund.getHundfehlt().equals("V") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "V" : "");
						saveHund();
					});

				}
			}

		}

		return layout;

	}

	private void saveHund() {
		try {
			db.updateShowHund(hund);
		} catch (Exception e) {
			Notification.show("Fehler beim speichern", Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
