package com.app.dashboard.view;

import java.util.Arrays;
import java.util.List;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.VeranstaltungsStation;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class VeranstaltungPunkteEingabeView extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Veranstaltung> vaList;

	private NativeSelect<Veranstaltung> selectBox;
	private TextField startNr;
	private ComboBox<VeranstaltungsStation> station;
	private TextField punkteEingabe;
	private VeranstaltungsTeilnehmer teilnehmer;
	private DBVeranstaltung db;
	private Label infoField;
	private Label infoField2;

	public VeranstaltungPunkteEingabeView() {

		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		// setCloseHandler(this);

		DashBoardEventBus.register(this);
		db = new DBVeranstaltung();
		try {
			vaList = db.getAllAktiveVeranstaltungen();

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("fehler beim lesen der shows");
		}
		setContent(buildDrafts());
	}

	private Component buildDrafts() {
		Panel draftsPanel = new Panel("Alle Veranstaltungen");
		draftsPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		draftsPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		draftsPanel.setSizeUndefined();

		final VerticalLayout allDrafts = new VerticalLayout();
		allDrafts.setSizeFull();
		allDrafts.setSpacing(true);
		allDrafts.setMargin(true);

		allDrafts.addComponents(buildSelectionBox(), buildStartNrEingabe(), buildStationEingabe(), buildPunkteEingabe(),
				buildInfoField(), buildInfoField2());

		draftsPanel.setContent(allDrafts);

		return draftsPanel;
	}

	private Component buildSelectionBox() {

		selectBox = new NativeSelect<>("alle aktiven Veranstaltungen");

		selectBox.setItemCaptionGenerator(item -> item.getName().trim() + " " + item.getDatum());
		selectBox.setItems(vaList);
		selectBox.addValueChangeListener(event -> {
			infoField.setValue(null);
			infoField2.setValue(null);
			startNr.setValue("");
			punkteEingabe.setValue("");
		});

		return selectBox;
	}

	private Component buildStartNrEingabe() {

		startNr = new TextField("Startnr");

		startNr.addValueChangeListener(event -> {
			try {
				if (event.getValue() != null && !event.getValue().equals("")
						&& Integer.parseInt(event.getValue()) > 0) {
					teilnehmer = db.getTeilnehmerZuStartnr(selectBox.getSelectedItem().get(),
							new Integer(event.getValue()));

					if (teilnehmer != null) {
						StringBuilder sb = new StringBuilder();
						sb.append("Ü1: ");
						sb.append(teilnehmer.getUebung1().toString());

						sb.append(", Ü2: ");
						sb.append(teilnehmer.getUebung2().toString());
						sb.append(", Ü3: ");
						sb.append(teilnehmer.getUebung3().toString());
						sb.append(", Ü4: ");
						sb.append(teilnehmer.getUebung4().toString());
						sb.append(", Ü5: ");
						sb.append(teilnehmer.getUebung5().toString());
						sb.append(", Ü6: ");
						sb.append(teilnehmer.getUebung6().toString());
						sb.append(", Ü7: ");
						sb.append(teilnehmer.getUebung7().toString());
						sb.append(", Ü8: ");
						sb.append(teilnehmer.getUebung7().toString());

						infoField.setValue(sb.toString());

						infoField2.setValue(teilnehmer.getGesPunkte().toString());
						VeranstaltungsStufe stufe = db.getStufeZuId(teilnehmer.getIdStufe());

						VeranstaltungsStufen zw = VeranstaltungsStufen.getBezeichnungForId(stufe.getStufenTyp());
						if (zw.getStationen() != null) {
							List<VeranstaltungsStation> stationList = Arrays.asList(zw.getStationen().getStation());
							station.setItems(stationList);

						}

					} else {
						Notification error = new Notification("Teilnehmer konnte nicht ermittelt werden",
								Notification.Type.ERROR_MESSAGE);
						error.setDelayMsec(2000);
						error.setPosition(Position.BOTTOM_CENTER);
						error.show(Page.getCurrent());

					}

				}
			} catch (Exception e) {
				Notification.show("Teilnehmer konnte nicht ermittelt werden");
				e.printStackTrace();
				infoField.setValue(null);
				infoField2.setValue(null);
			}
		});
		return startNr;
	}

	private Component buildStationEingabe() {
		station = new ComboBox<>("Station/Übung");
		station.setItemCaptionGenerator(VeranstaltungsStation::getUebung);

		return station;
	}

	private Component buildPunkteEingabe() {
		punkteEingabe = new TextField("Punkte");
		punkteEingabe.addValueChangeListener(event -> {
			if (event.getValue() != null && !event.getValue().equals("") && Integer.parseInt(event.getValue()) > 0) {

				Integer punkte = Integer.valueOf(event.getValue());

				if (station.getSelectedItem().get().getMinPunkte() > punkte
						|| station.getSelectedItem().get().getMaxPunkte() < punkte) {
					Notification error = new Notification(
							"Die Punkte müssen zwischen " + station.getSelectedItem().get().getMinPunkte() + " und "
									+ station.getSelectedItem().get().getMaxPunkte() + " liegen",
							Notification.Type.ERROR_MESSAGE);
					error.setDelayMsec(2000);
					error.setPosition(Position.BOTTOM_CENTER);
					error.show(Page.getCurrent());

				} else {
					teilnehmer.setPunkteFuerUebung(station.getSelectedItem().get().getStationNr(), punkte);
					try {
						db.saveTeilnehmer(teilnehmer);
					} catch (Exception e) {
						Notification.show("Fehler beim Speichern der Punkte");
						e.printStackTrace();
					}
				}
			}

		});

		return punkteEingabe;
	}

	private Component buildInfoField() {
		infoField = new Label("");
		return infoField;
	}

	private Component buildInfoField2() {
		infoField2 = new Label("");
		return infoField2;
	}
}
