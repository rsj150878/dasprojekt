package com.app.component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.ShowKlassen;
import com.app.printclasses.ShowBewertungsBlatt;
import com.app.showdata.Show;
import com.app.showdata.ShowGeschlechtEnde;
import com.app.showdata.ShowHund;
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
		panelContent.addComponent(buildMailButton());

		setContent(panelContent);
	}

	private Component buildTab() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);

		if (show.getSchauTyp().equals("C")) {
			layout.addComponent(buildKlubSieger("Klubsieger", "C"));
		}

		if (show.getSchauTyp().equals("I")) {
			layout.addComponent(buildCacib("CACIB", "C"));
			layout.addComponent(buildCacib("Res. Cacib", "R"));
		}

		layout.addComponent(buildBesterHund());

		return layout;

	}

	private Component buildPrintButton() {
		Button printButton = new Button("Geschlecht drucken");
		printButton.addClickListener(event -> {
			ShowHund[] arry = ende.getRingGeschlechtEndeFor().flattened().filter(p -> (p instanceof ShowHund
					&& p.getRasse().equals(ende.getRasse()) && p.getGeschlecht().equals(ende.getGeschlechtEnde())))
					.toArray(ShowHund[]::new);
			ShowBewertungsBlatt blatt = new ShowBewertungsBlatt(show, arry);
			panelContent.addComponent(blatt);

		});

		return printButton;
	}

	private Component buildMailButton() {
		Button mailButton = new Button("Bewertung des Geschlechts mailen");
		mailButton.addClickListener(event -> {
			ShowHund[] arry = ende.getRingGeschlechtEndeFor().flattened().filter(p -> (p instanceof ShowHund
					&& p.getRasse().equals(ende.getRasse()) && p.getGeschlecht().equals(ende.getGeschlechtEnde())))
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

	private Component buildKlubSieger(String klubSiegerFor, String dataBaseValue) {
		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(klubSiegerFor);
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende
				.getRingGeschlechtEndeFor().flattened().filter(x -> (dataBaseValue.equals(x.getClubsieger())
						&& x.getRasse().equals(ende.getRasse()) && x.getGeschlecht().equals(ende.getGeschlechtEnde())))
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

		platzLayout.addComponent(buildClubInfoLabel());

		platzLayout.addComponent(hundeName);

		return platzLayout;

	}

	private Component buildClubInfoLabel() {
		Label infoLabel = new Label();
		infoLabel.setIcon(VaadinIcons.INFO);
		StringBuilder sb = new StringBuilder();
		List<ShowKlassen> erwachsenenKlassen = ShowKlassen.getErwachsenenKlassen();
		erwachsenenKlassen.forEach(zwKlasse -> {

			List<ShowRing> zw = ende.getRingGeschlechtEndeFor().getHundeDerKlasse(ende.getGeschlechtEnde(),
					ende.getRasse(), zwKlasse);

			System.out.println(zw.size());
			List<ShowRing> filteredClub = null;
			if (!(zw == null)) {
				filteredClub = zw.stream().filter(x -> !(x.getMitglied() == null) && x.getMitglied().equals("J"))
						.collect(Collectors.toList());

				filteredClub = filteredClub.stream().filter(x -> !(x.getPlatzierung() == null) && !x.getPlatzierung().isEmpty())
						.collect(Collectors.toList());

				filteredClub.sort(new Comparator<ShowRing>() {

					@Override

					public int compare(ShowRing m1, ShowRing m2) {

						String platzierung1 = (m1.getPlatzierung() == null ? "999" : m1.getPlatzierung());
						String platzierung2 = (m2.getPlatzierung() == null ? "999" : m2.getPlatzierung());

						Integer platzierung1Int = Integer.valueOf(platzierung1);
						Integer platzierung2Int = Integer.valueOf(platzierung2);

						return platzierung1Int.compareTo(platzierung2Int);
					}

				});

			}
			sb.append(zwKlasse.getShowKlasseKurzBezeichnung() + ": "
					+ (!(filteredClub == null) && filteredClub.size() > 0 ? filteredClub.get(0).getKatalogNummer()
							: "--"));
			sb.append("<br>");
		});

		infoLabel.setDescription(sb.toString(), ContentMode.HTML);
		return infoLabel;

	}

	private Component buildBestDogInfoLabel() {
		Label infoLabel = new Label();
		infoLabel.setIcon(VaadinIcons.INFO);
		StringBuilder sb = new StringBuilder();
		List<ShowKlassen> erwachsenenKlassen = ShowKlassen.getBestDogKlassen();
		erwachsenenKlassen.forEach(zwKlasse -> {

			List<ShowRing> zw = ende.getRingGeschlechtEndeFor().getHundeDerKlasse(ende.getGeschlechtEnde(),
					ende.getRasse(), zwKlasse);

			if (!(zw == null)) {
				zw = zw.stream().filter(x -> !(x.getPlatzierung() == null) && !x.getPlatzierung().isEmpty())
						.collect(Collectors.toList());

				zw.sort(new Comparator<ShowRing>() {

					@Override

					public int compare(ShowRing m1, ShowRing m2) {

						String platzierung1 = (m1.getPlatzierung() == null || m1.getPlatzierung().isEmpty() ? "999"
								: m1.getPlatzierung());
						String platzierung2 = (m2.getPlatzierung() == null || m2.getPlatzierung().isEmpty() ? "999"
								: m2.getPlatzierung());

						Integer platzierung1Int = Integer.valueOf(platzierung1);
						Integer platzierung2Int = Integer.valueOf(platzierung2);

						return platzierung1Int.compareTo(platzierung2Int);
					}

				});

			}
			sb.append(zwKlasse.getShowKlasseKurzBezeichnung() + ": "
					+ (!(zw == null) && zw.size() > 0 ? zw.get(0).getKatalogNummer() : "--"));
			sb.append("<br>");
		});

		infoLabel.setDescription(sb.toString(), ContentMode.HTML);
		return infoLabel;

	}

	private Component buildBesterHund() {

		CheckBoxGroup<String> groupBOB = new CheckBoxGroup<>();
		List<String> dataBOB = Arrays.asList("B", "O");

		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(ende.getGeschlechtEnde().equals("R") ? "Bester Rüde" : "Beste Hündin");
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende
				.getRingGeschlechtEndeFor().flattened().filter(x -> (!(x.getBOB() == null) && !x.getBOB().isEmpty()
						&& x.getRasse().equals(ende.getRasse()) && x.getGeschlecht().equals(ende.getGeschlechtEnde())))
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

		platzLayout.addComponent(buildBestDogInfoLabel());

		platzLayout.addComponent(hundeName);

		groupBOB.setItems(dataBOB);
		groupBOB.setItemCaptionGenerator(item -> item.equals("B") ? "BOB" : "BOS");
		if (!(hund[0] == null)) {
			groupBOB.select(hund[0].getBOB() == null ? "" : hund[0].getBOB());
		}

		groupBOB.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		platzLayout.addComponent(groupBOB);

		groupBOB.addSelectionListener(event -> {
			hund[0].setBOB(event.getFirstSelectedItem().isPresent() ? event.getFirstSelectedItem().get() : null);
			saveHund(hund[0]);
		});

		return platzLayout;

	}

	private Component buildCacib(String cacibFor, String dataBaseValue) {
		HorizontalLayout platzLayout = new HorizontalLayout();
		Label platzLabel = new Label();
		platzLabel.setValue(cacibFor);
		platzLayout.addComponent(platzLabel);

		TextField textField = new TextField();
		textField.setWidth(100.0f, Unit.PERCENTAGE);

		Label hundeName = new Label();
		hundeName.setWidth(100.0f, Unit.PERCENTAGE);

		ShowHund[] hund = { (ShowHund) ende
				.getRingGeschlechtEndeFor().flattened().filter(x -> (dataBaseValue.equals(x.getCACIB())
						&& x.getRasse().equals(ende.getRasse()) && x.getGeschlecht().equals(ende.getGeschlechtEnde())))
				.findFirst().orElse(null) };

		if (!(hund[0] == null)) {
			// hund = (ShowHund) zwData.get();
			textField.setValue(hund[0].getKatalognumer());
			hundeName.setValue(hund[0].getShowHundName());
		}

		textField.addValueChangeListener(event -> {

			if (!(hund[0] == null)) {
				hund[0].setCACIB("");
				saveHund(hund[0]);

			}
			hund[0] = (ShowHund) ende.getRingGeschlechtEndeFor().flattened()
					.filter(x -> event.getValue().trim().equals(x.getKatalognumer().trim())).findFirst().orElse(null);

			if (!(hund[0] == null)) {
				// textField.setValue(hund[0].getKatalognumer());
				hundeName.setValue(hund[0].getShowHundName());
				hund[0].setCACIB(dataBaseValue);
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
