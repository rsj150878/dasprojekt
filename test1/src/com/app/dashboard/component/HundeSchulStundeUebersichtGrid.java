package com.app.dashboard.component;

import java.util.List;

import com.app.auth.Hund;
import com.app.dashboard.event.DashBoardEvent.SearchEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBHund;
import com.app.dbio.DBKurs;
import com.app.kurs.KursStunde;
import com.app.kurs.KursTeilnehmer;
import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class HundeSchulStundeUebersichtGrid extends Grid<KursTeilnehmer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6141955154197613028L;

	private List<KursTeilnehmer> kursTeilnehmer;
	private KursStunde stunde;
	private DBKurs dbKurs;
	
	public HundeSchulStundeUebersichtGrid(KursStunde stunde) {
		super();
		this.stunde = stunde;

		dbKurs = new DBKurs();
		
		setCaption(stunde.getBezeichnung());

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);

		setSizeFull();

		//getHeader().setVisible(false);

		try {

			kursTeilnehmer = dbKurs.getKursTeilnehmerZuKursStunde(stunde);
			setItems(kursTeilnehmer);

		} catch (Exception e) {
			Notification.show("fehler beim aufbau der Stundencontainer");
			e.printStackTrace();
		}

		Column<KursTeilnehmer, Hund> hundColumn = addColumn(KursTeilnehmer::getHund);
		hundColumn.setRenderer(hund -> hund.getRufname() + " - " + hund.getZwingername(), new TextRenderer());
		hundColumn.setCaption("Hund");

		addComponentColumn(teilnehmer -> {
			Button delButton = new Button();
			delButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
			delButton.setIcon(VaadinIcons.TRASH);
			delButton.addClickListener(evt -> {
				try {
					dbKurs.deleteKursTeilnehmer(teilnehmer);
				} catch (Exception e) {
					Notification.show("es ist ein fehler beim löschen passiert");
				}
				kursTeilnehmer.remove(teilnehmer);
				getDataProvider().refreshAll();
			});
			return delButton;
		});

		addColumn(teilnehmer -> {
			CheckBox bezahlt = new CheckBox();
			bezahlt.setValue(teilnehmer.getBezahlt().equals("J") ? true : false);
			bezahlt.addValueChangeListener(evt -> {

				teilnehmer.setBezahlt(bezahlt.getValue() == true ? "J" : "N");
				try {
					dbKurs.saveKursTeilnehmer(teilnehmer);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}});
			return bezahlt;
		}, new ComponentRenderer()).setCaption("bezahlt");

		addColumn(teilnehmer -> {
			TextField hundeFuehrer = new TextField();
			hundeFuehrer.setValue(
					teilnehmer.getAbweichenderHundeFuehrer() == null ? "" : teilnehmer.getAbweichenderHundeFuehrer());
			hundeFuehrer.addValueChangeListener(evt -> {
				teilnehmer.setAbweichenderHundeFuehrer(hundeFuehrer.getValue());
				try {
					dbKurs.saveKursTeilnehmer(teilnehmer);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return hundeFuehrer;

		}, new ComponentRenderer()).setCaption("Hundeführer");

	}

	@Subscribe
	public void searchResult(SearchEvent event) {
		DashBoardEventBus.unregister(this);
		if (!(event.getDogIdResult() == null)) {
			Integer idHund = event.getDogIdResult();
			DBHund dbHund = new DBHund();
			try {
				Hund hund = dbHund.getHundForHundId(idHund);
				KursTeilnehmer neuerTeilnehmer = new KursTeilnehmer();

				neuerTeilnehmer.setHund(hund);
				neuerTeilnehmer.setIdHund(idHund);
				neuerTeilnehmer.setIdKursStunde(stunde.getIdKursStunde());
				neuerTeilnehmer.setVersion(0);
				neuerTeilnehmer.setBezahlt("N");

				dbKurs.saveKursTeilnehmer(neuerTeilnehmer);

				kursTeilnehmer.add(neuerTeilnehmer);
				getDataProvider().refreshAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
