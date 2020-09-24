package com.app.component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.BobDataType;
import com.app.enumdatatypes.CacaDataType;
import com.app.enumdatatypes.CacibDataType;
import com.app.enumdatatypes.FormWertErwachsen;
import com.app.enumdatatypes.FormWertJuengsten;
import com.app.enumdatatypes.ShowKlassen;
import com.app.printclasses.ShowBewertungsBlatt;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.app.showdata.ShowRing;
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
	private CheckBox jBobVBob;
	private CheckBox veroeffentlichen;
	private TextField besitzerEmail;

	private TextArea bewertung;

	private RadioButtonGroup<FormWertJuengsten> formwertJuengsten;
	private RadioButtonGroup<FormWertErwachsen> formwertErw;
	private RadioButtonGroup<String> platzierung;
	private RadioButtonGroup<CacaDataType> cacaButtonGroup;
	private RadioButtonGroup<BobDataType> bobButtonGroup;
	private RadioButtonGroup<CacibDataType> cacibButtonGroup;

	private DBShowNeu db;

	private ShowHund hund;
	private Show show;
	private ShowRing ring;
	VerticalLayout panelContent;

	public ShowHundBewertungComponent(DBShowNeu db, Show show, ShowRing ring, ShowHund hund) {
		this.hund = hund;
		this.show = show;
		this.db = db;
		this.ring = ring;
		// setHeight("100%");
		setWidth("100%");
		StringBuilder caption = new StringBuilder();
		caption.append("Hundebewertung: Hund ");
		caption.append(ring.getPositionOfDog(hund));
		caption.append(" von ");
		caption.append(ring.getNumberOfDogsOfRing());
		setCaption(caption.toString());
		panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponentsAndExpand(buildAllgemeinInfo(), buildAllgemeinInfo3(), buildAllgemeinInfo4(),
				buildBewertung(), buildFormwertInfo(), buildPrintButton());

		setContent(panelContent);

		setEditingFieldsEnabled(!hundFehlt.getValue());

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
			setEditingFieldsEnabled(!hundFehlt.getValue());
		});

		mitglied = new CheckBox("Mitglied");
		mitglied.setValue(hund.getMitglied() != null && hund.getMitglied().equals("J") ? true : false);
		mitglied.setEnabled(false);
		ersteZeile.addComponent(mitglied);

		return ersteZeile;
	}

	private Component buildAllgemeinInfo4() {

		HorizontalLayout ersteZeile = new HorizontalLayout();
		ersteZeile.setWidth("100%");

		besitzerEmail = new TextField("Besitzer-Email");
		besitzerEmail.setValue(hund.getBesitzerEmail() == null ? "" : hund.getBesitzerEmail());

		ersteZeile.addComponent(besitzerEmail);
		besitzerEmail.addValueChangeListener(event -> {
			hund.setBesitzerEmail(besitzerEmail.getValue());
			saveHund();
		});

		veroeffentlichen = new CheckBox("Ergebnis veröffentlichen?");
		veroeffentlichen.setValue(hund.getVeroeffentlichen());
		ersteZeile.addComponent(veroeffentlichen);

		veroeffentlichen.addValueChangeListener(event -> {
			hund.setVeroeffentlichen(veroeffentlichen.getValue());
			saveHund();
		});

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
		HorizontalLayout buttonLeisteLayout = new HorizontalLayout();
		buttonLeisteLayout.setWidth("100%");

		Button printButton = new Button("Bewertung drucken");
		printButton.addClickListener(event -> {
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, hund);
			panelContent.addComponent(blatt);

		});

		buttonLeisteLayout.addComponent(printButton);

		Button resetButton = new Button("Bewertung zurücksetzen");

		resetButton.addClickListener(event -> {

			if (!(klubSieger == null)) {
				klubSieger.setValue(false);
			}

			if (!(formwertJuengsten == null)) {
				formwertJuengsten.setSelectedItem(null);
			}

			if (!(formwertErw == null)) {
				formwertErw.setSelectedItem(null);
			}

			if (!(platzierung == null)) {
				platzierung.setSelectedItem(null);
			}

			if (!(cacaButtonGroup == null)) {
				cacaButtonGroup.setSelectedItem(null);
			}

			if (!(bobButtonGroup == null)) {
				bobButtonGroup.setSelectedItem(null);
			}

			if (!(cacibButtonGroup == null)) {
				cacibButtonGroup.setSelectedItem(null);
			}

		});

		buttonLeisteLayout.addComponent(resetButton);

		Button mailButton = new Button("Bewertung mailen");
		mailButton.addClickListener(event -> {
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, hund);
			// panelContent.addComponent(blatt);
			try {
				blatt.sendBewertungAsEmail(show, hund);
				Notification.show("Mail erfolgreich verschickt");

			} catch (Exception e) {
				e.printStackTrace();
				Notification.show("Fehler beim mailschicken");
			}

		});

		buttonLeisteLayout.addComponent(mailButton);

		Button urkundeDruckButton = new Button("Zertifikat drucken");
		urkundeDruckButton.addClickListener(event -> {
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, "ZF", hund);
			panelContent.addComponent(blatt);

		});

		buttonLeisteLayout.addComponent(urkundeDruckButton);

		return buttonLeisteLayout;
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
					hund.setFormwert(
							formwertJuengsten.getValue() == null ? null : formwertJuengsten.getValue().getFormwert());
					saveHund();
				});
			} else {
				formwertErw = new RadioButtonGroup<>("Formwert");
				formwertErw.setItems(Arrays.asList(FormWertErwachsen.values()));
				formwertErw.setItemCaptionGenerator(FormWertErwachsen::getFormWertBezeichnung);
				formwertErw.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				formwertErw.setSelectedItem(FormWertErwachsen.getFormwertForKurzBezeichnung(hund.getFormwert()));

				formwertErw.addValueChangeListener(event -> {
					hund.setFormwert(formwertErw.getValue() == null ? null : formwertErw.getValue().getFormwert());
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
					hund.setCACA(
							cacaButtonGroup.getValue() == null ? null : cacaButtonGroup.getValue().getDataBaseValue());
					saveHund();
				});

				layout.addComponent(cacaButtonGroup);

				bobButtonGroup = new RadioButtonGroup<>("BOB/BOS");
				bobButtonGroup.setItems(Arrays.asList(BobDataType.values()));
				bobButtonGroup.setItemCaptionGenerator(BobDataType::getLangText);
				bobButtonGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				bobButtonGroup.setSelectedItem(BobDataType.getTextForDataBaseValue(hund.getBOB()));

				bobButtonGroup.addValueChangeListener(event -> {
					hund.setBOB(
							bobButtonGroup.getValue() == null ? null : bobButtonGroup.getValue().getDataBaseValue());
					saveHund();
				});

				layout.addComponent(bobButtonGroup);
			}
			
			if (hund.getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
				jBobVBob = new CheckBox("Junior-Bob");
				jBobVBob
						.setValue(hund.getJBOB() != null && hund.getJBOB().equals("J") ? true : false);
				layout.addComponent(jBobVBob);

				jBobVBob.addValueChangeListener(event -> {
					hund.setJBOB(jBobVBob.getValue() == true ? "J" : "");
					saveHund();
				});

			} else if (hund.getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
				jBobVBob = new CheckBox("Veteranen-Bob");
				jBobVBob
						.setValue(hund.getVBOB() != null && hund.getVBOB().equals("J") ? true : false);
				layout.addComponent(jBobVBob);

				jBobVBob.addValueChangeListener(event -> {
					hund.setVBOB(jBobVBob.getValue() == true ? "J" : "");
					saveHund();
				});
			}

			if (show.getSchauTyp().equals("C")) {
				if (hund.getKlasse().equals(ShowKlassen.ZWISCHENKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.OFFENEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)) {

					klubSieger = new CheckBox("KlubSieger");
					klubSieger
							.setValue(hund.getClubsieger() != null && hund.getClubsieger().equals("C") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "C" : "");
						saveHund();
					});
				} else if (hund.getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
					klubSieger = new CheckBox("KlubJugendSieger");
					klubSieger
							.setValue(hund.getClubsieger() != null && hund.getClubsieger().equals("J") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "J" : "");
						saveHund();
					});

				} else if (hund.getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
					klubSieger = new CheckBox("KlubVeteranenSieger");
					klubSieger
							.setValue(hund.getClubsieger() != null && hund.getClubsieger().equals("V") ? true : false);
					layout.addComponent(klubSieger);

					klubSieger.addValueChangeListener(event -> {
						hund.setClubsieger(klubSieger.getValue() == true ? "V" : "");
						saveHund();
					});

				}
			} else if (show.getSchauTyp().equals("I")) {
				if (hund.getKlasse().equals(ShowKlassen.ZWISCHENKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.OFFENEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
						|| hund.getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)) {

					cacibButtonGroup = new RadioButtonGroup<>("CACIB");
					cacibButtonGroup.setItems(Arrays.asList(CacibDataType.values()));
					cacibButtonGroup.setItemCaptionGenerator(CacibDataType::getLangText);
					cacibButtonGroup.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

					cacibButtonGroup.setSelectedItem(CacibDataType.getTextForDataBaseValue(hund.getCACIB()));

					cacibButtonGroup.addValueChangeListener(event -> {
						hund.setCACIB(cacibButtonGroup.getValue() == null ? null
								: cacibButtonGroup.getValue().getDataBaseValue());
						saveHund();
					});

					layout.addComponent(cacibButtonGroup);

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

	private void setEditingFieldsEnabled(boolean enabled) {

		if (!(klubSieger == null)) {
			klubSieger.setEnabled(enabled);
		}

		if (!(bewertung == null)) {
			bewertung.setEnabled(enabled);
		}

		if (!(formwertJuengsten == null)) {
			formwertJuengsten.setEnabled(enabled);
		}

		if (!(formwertErw == null)) {
			formwertErw.setEnabled(enabled);
		}

		if (!(platzierung == null)) {
			platzierung.setEnabled(enabled);
		}

		if (!(cacaButtonGroup == null)) {
			cacaButtonGroup.setEnabled(enabled);
		}

		if (!(bobButtonGroup == null)) {
			bobButtonGroup.setEnabled(enabled);
		}

		if (!(cacibButtonGroup == null)) {
			cacibButtonGroup.setEnabled(enabled);
		}

	}
}
