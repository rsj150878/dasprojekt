package com.app.component;

import java.util.Arrays;
import java.util.List;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.ShowKlassen;
import com.app.printclasses.ShowBewertungsBlatt;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.app.showdata.ShowKlasseEnde;
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

public class ShowKlassenAbschlussComponent extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870614514839201421L;
	private DBShowNeu db;
	private ShowKlasseEnde ende;
	private Show show;

	VerticalLayout panelContent;

	public ShowKlassenAbschlussComponent(DBShowNeu db, Show show, ShowKlasseEnde ende) {
		this.db = db;
		this.ende = ende;
		this.show = show;

		setWidth("100%");
		setCaption("Klassenabschluss " + ende.getKlasseEndeFor().getKlasse().getShowKlasseLangBezeichnung());
		panelContent = new VerticalLayout();
		panelContent.setWidth("100%");

		panelContent.addComponent(buildTab());
		panelContent.addComponent(buildPrintButton());

		setContent(panelContent);
	}

	private Component buildTab() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);

		if (!show.getSchauTyp().equals("W")) {

			layout.addComponent(buildPlatzierungField("1"));

			layout.addComponent(buildPlatzierungField("2"));

			layout.addComponent(buildPlatzierungField("3"));

			layout.addComponent(buildPlatzierungField("4"));

			if (show.getSchauTyp().equals("C")) {
				if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
					layout.addComponent(buildKlubSieger("Klubjugendsieger", "J"));
				} else if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
					layout.addComponent(buildKlubSieger("Klubveteranensieger", "V"));
				}
			}
		}

		return layout;

	}

	private Component buildPrintButton() {
		Button printButton = new Button("Klasse drucken");
		printButton.addClickListener(event -> {
			ShowHund[] arry = ende.getKlasseEndeFor().getKlassenAsStream().filter(p -> p instanceof ShowHund)
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

		ShowHund[] hund = { (ShowHund) ende.getKlasseEndeFor().getKlassenAsStream()
				.filter(x -> dataBaseValue.equals(x.getClubsieger())).findFirst().orElse(null) };

		if (!(hund[0] == null)) {
			// hund = (ShowHund) zwData.get();
			textField.setValue(hund[0].getKatalognumer());
			hundeName.setValue(hund[0].getShowHundName());
		}

		textField.addValueChangeListener(event -> {

			if (!(hund[0] == null)) {
				hund[0].setClubsieger(null);
				saveHund(hund[0]);

			}
			hund[0] = (ShowHund) ende.getKlasseEndeFor().getKlassenAsStream()
					.filter(x -> event.getValue().trim().equals(x.getKatalognumer().trim())).findFirst().orElse(null);

			if (!(hund[0] == null)) {
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

	private Component buildPlatzierungField(String platz) {

		CheckBoxGroup<String> groupResCACA = new CheckBoxGroup<>();
		CheckBoxGroup<String> groupCACA = new CheckBoxGroup<>();

		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(platz + ".");
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende.getKlasseEndeFor().getKlassenAsStream()
				.filter(x -> platz.equals(x.getPlatzierung())).findFirst().orElse(null) };

		if (!(hund[0] == null)) {
			// hund = (ShowHund) zwData.get();
			textField.setValue(hund[0].getKatalognumer());
			hundeName.setValue(hund[0].getShowHundName());
		}

		textField.addValueChangeListener(event -> {

			if (!(hund[0] == null)) {
				hund[0].setPlatzierung(null);
				saveHund(hund[0]);

			}
			hund[0] = (ShowHund) ende.getKlasseEndeFor().getKlassenAsStream()
					.filter(x -> event.getValue().trim().equals(x.getKatalognumer().trim())).findFirst().orElse(null);

			if (!(hund[0] == null)) {
				// textField.setValue(hund[0].getKatalognumer());
				System.out.println("hund " + hund[0].getShowHundName());
				hundeName.setValue(hund[0].getShowHundName());
				hund[0].setPlatzierung(platz);
				saveHund(hund[0]);
			} else {
				hundeName.setValue("");
				groupResCACA.deselectAll();
				groupCACA.deselectAll();
			}

		});
		platzLayout.addComponent(textField);

		platzLayout.addComponent(hundeName);

		if ("1".equals(platz)) {
			List<String> dataCACA = null;
			if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.JUGENDKLASSE)) {
				dataCACA = Arrays.asList("J");
			} else if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.ZWISCHENKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.OFFENEKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)) {
				dataCACA = Arrays.asList("C", "K");
			} else if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
				dataCACA = Arrays.asList("V");
			}

			if (!(dataCACA == null)) {
				groupCACA.setItems(dataCACA);
				groupCACA.setItemCaptionGenerator(item -> getTitelString(item));
				if (!(hund[0] == null)) {
					groupCACA.select(hund[0].getCACA() == null ? "" : hund[0].getCACA());
				}
				groupCACA.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
				platzLayout.addComponent(groupCACA);

				groupCACA.addSelectionListener(event -> {
					if (!(hund[0] == null)) {

						hund[0].setCACA(
								event.getFirstSelectedItem().isPresent() ? event.getFirstSelectedItem().get() : null);
						saveHund(hund[0]);
					}
				});
			}
		}
		if ("2".equals(platz)) {
			List<String> dataCACA = null;
			if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.ZWISCHENKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.OFFENEKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.GEBRAUCHSHUNDEKLASSE)
					|| ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.CHAMPIONKLASSE)) {
				dataCACA = Arrays.asList("R", "K");
			} else if (ende.getKlasseEndeFor().getKlasse().equals(ShowKlassen.VETERANENKLASSE)) {
				dataCACA = Arrays.asList("W");
			}

			if (!(dataCACA == null)) {
				groupResCACA.setItems(dataCACA);
				groupResCACA.setItemCaptionGenerator(item -> getTitelString(item));
				if (!(hund[0] == null)) {
					groupResCACA.select(hund[0].getCACA() == null ? "" : hund[0].getCACA());
				}
				groupResCACA.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

				platzLayout.addComponent(groupResCACA);
				groupResCACA.addSelectionListener(event -> {
					if (!(hund[0] == null)) {

						hund[0].setCACA(
								event.getFirstSelectedItem().isPresent() ? event.getFirstSelectedItem().get() : null);
						saveHund(hund[0]);
					}
				});

			}
		}

		return platzLayout;
	}

	private String getTitelString(String item) {
		String returnString = "";

		switch (item) {
		case ("J"):
			returnString = "Jugendbester";
			break;
		case ("C"):
			returnString = "CACA";
			break;
		case ("R"):
			returnString = "Res. CACA";
			break;
		case ("K"):
			returnString = "Kein Titel";
			break;
		case ("V"):
			returnString = "Veteranensieger";
			break;
		case ("W"):
			returnString = "Res. Veteranensieger";
			break;

		}

		return returnString;

	}

	private void saveHund(ShowHund hund) {
		try {
			db.updateShowHund(hund);
		} catch (Exception e) {
			Notification.show("Fehler beim speichern", Notification.Type.ERROR_MESSAGE);
		}
	}

}
