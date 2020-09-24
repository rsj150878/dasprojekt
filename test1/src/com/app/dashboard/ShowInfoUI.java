package com.app.dashboard;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.Rassen;
import com.app.enumdatatypes.ShowKlassen;
import com.app.printclasses.ShowPrintBewertungUebersicht;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.app.DashBoard.DashboardWidgetsetNeu")
@Title("ShowInfo")
@SuppressWarnings("serial")
public final class ShowInfoUI extends UI {

	/*
	 * This field stores an access to the dummy backend layer. In real applications
	 * you most likely gain access to your beans trough lookup or injection; and not
	 * in the UI but somewhere closer to where they're actually accessed.
	 */

	ComboBox<Show> showSelect;
	ComboBox<Rassen> rassenSelect;
	Panel scrollPanel;

	List<Show> showList;
	DBShowNeu db = new DBShowNeu();

	// private final DataProvider dataProvider = new DummyDataProvider();

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.GERMANY);

		Responsive.makeResponsive(this);
	
		updateContent();

	}

	/**
	 * Updates the correct content for this UI based on the current user status. If
	 * the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		FormLayout infoFormLayout = new FormLayout();
		infoFormLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		infoFormLayout.setWidth(100.0f, Unit.PERCENTAGE);
		infoFormLayout.setSpacing(true);
		infoFormLayout.setMargin(true);

		Integer[] showJahre = { 2016, 2017, 2018, 2019, 2020 };
		final ComboBox<Integer> yearSelect = new ComboBox<>("Ausstellungsjahr");
		yearSelect.setItems(showJahre);
		infoFormLayout.addComponent(yearSelect);
		infoFormLayout.setComponentAlignment(yearSelect, Alignment.MIDDLE_CENTER);

		showSelect = new ComboBox<>("Schau");
		showSelect.setEnabled(false);
		showSelect.setItemCaptionGenerator(Show::getSchaubezeichnung);

		infoFormLayout.addComponent(showSelect);

		rassenSelect = new ComboBox<>("Rasse");
		rassenSelect.setEnabled(false);

		rassenSelect.setItems(Rassen.getOercRassen());
		rassenSelect.setItemCaptionGenerator(Rassen::getRassenLangBezeichnung);

		infoFormLayout.addComponent(rassenSelect);

		mainLayout.addComponent(infoFormLayout);
		mainLayout.setExpandRatio(infoFormLayout, 1);

		mainLayout.setComponentAlignment(infoFormLayout, Alignment.MIDDLE_CENTER);

		scrollPanel = new Panel();
		scrollPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		scrollPanel.setSizeFull();
		scrollPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		mainLayout.addComponent(scrollPanel);
		mainLayout.setComponentAlignment(scrollPanel, Alignment.MIDDLE_CENTER);
		mainLayout.setExpandRatio(scrollPanel, 3);

		yearSelect.addValueChangeListener(event -> {
			System.out.println("vlaue " + yearSelect.getValue());

			try {
				showList = db.getShowsForYear(yearSelect.getValue());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			showSelect.setItems(showList);
	
			showSelect.setValue(null);
			showSelect.setEnabled(true);
			rassenSelect.setEnabled(false);

			rassenSelect.setValue(null);
			scrollPanel.setContent(new VerticalLayout());
		}

		);

		showSelect.addValueChangeListener(evt -> {
			// TODO Auto-generated method stub
			rassenSelect.setEnabled(true);
			rassenSelect.setValue(null);
			scrollPanel.setContent(new VerticalLayout());

		}

		);

		rassenSelect.addValueChangeListener(evt -> {
			if (rassenSelect.getValue() != null) {
				buildInfoArea();
			}
		});

		setContent(mainLayout);

	}

	private void buildInfoArea() {

		VerticalLayout infoLayout = new VerticalLayout();
		// infoLayout.setSizeUndefined();
		scrollPanel.setContent(infoLayout);

		Label klassenLabel = new Label();
		klassenLabel.setContentMode(ContentMode.HTML);
		klassenLabel.setValue("<center>" + "Rüden" + "</center>");
		klassenLabel.addStyleName(ValoTheme.LABEL_H2);
		infoLayout.addComponent(klassenLabel);
		infoLayout.setComponentAlignment(klassenLabel, Alignment.TOP_CENTER);

		for (ShowKlassen x : ShowKlassen.values()) {
			List<ShowHund> hundDerKlassen = null;
			try {
				hundDerKlassen = db.getHundeForKlasse(showSelect.getValue(), x, rassenSelect.getValue(), "R");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (hundDerKlassen.size() > 0) {

				klassenLabel = new Label();
				klassenLabel.setContentMode(ContentMode.HTML);
				klassenLabel.setValue("<center>" + x.getShowKlasseLangBezeichnung() + "</center>");
				klassenLabel.addStyleName(ValoTheme.LABEL_H2);
				infoLayout.addComponent(klassenLabel);
				infoLayout.setComponentAlignment(klassenLabel, Alignment.TOP_CENTER);

				Component zw = printKlasse(showSelect.getValue(), x, hundDerKlassen, "R");
				infoLayout.addComponent(zw);
				infoLayout.setComponentAlignment(zw, Alignment.MIDDLE_CENTER);
			}

		}

		klassenLabel = new Label();
		klassenLabel.setContentMode(ContentMode.HTML);
		klassenLabel.setValue("<center>" + "Hündinnen" + "</center>");
		klassenLabel.addStyleName(ValoTheme.LABEL_H2);
		infoLayout.addComponent(klassenLabel);
		infoLayout.setComponentAlignment(klassenLabel, Alignment.TOP_CENTER);

		for (ShowKlassen x : ShowKlassen.values()) {
			List<ShowHund> hundDerKlassen = null;
			try {
				hundDerKlassen = db.getHundeForKlasse(showSelect.getValue(), x, rassenSelect.getValue(), "H");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (hundDerKlassen.size() > 0) {
				klassenLabel = new Label();
				klassenLabel.setContentMode(ContentMode.HTML);
				klassenLabel.setValue("<center>" + x.getShowKlasseLangBezeichnung() + "</center>");
				klassenLabel.addStyleName(ValoTheme.LABEL_H2);
				infoLayout.addComponent(klassenLabel);
				infoLayout.setComponentAlignment(klassenLabel, Alignment.TOP_CENTER);

				Component zw = printKlasse(showSelect.getValue(), x, hundDerKlassen, "R");

				infoLayout.addComponent(zw);
				infoLayout.setComponentAlignment(zw, Alignment.MIDDLE_CENTER);
			}

		}

	}

	private Component printKlasse(Show show, ShowKlassen klasse, List<ShowHund> hundeDerKlasse, String geschlecht) {

		VerticalLayout hundLayout = new VerticalLayout();

		for (ShowHund zwHund : hundeDerKlasse) {

			Label hund = new Label();
			hund.setContentMode(ContentMode.HTML);
			String name = "<center><b>" + zwHund.getKatalognumer() + "</b> " + (zwHund.getVeroeffentlichen() ? zwHund.getShowHundName():"--")  + "</center>";
			hund.setValue(name.trim());
			hund.addStyleName(ValoTheme.LABEL_H3);
			hundLayout.addComponent(hund);
			hundLayout.setComponentAlignment(hund, Alignment.TOP_CENTER);

			Label zbnrWt = new Label();
			zbnrWt.setContentMode(ContentMode.HTML);
			zbnrWt.setValue("<center>" + (zwHund.getVeroeffentlichen() ? zwHund.getZuchtbuchnummer() :"--") + ", gew. am "
					+ 
					(zwHund.getVeroeffentlichen() ? new SimpleDateFormat("dd.MM.yyyy").format(zwHund.getWurftag()) :"--") + "</center>");
			hundLayout.addComponent(zbnrWt);
			hundLayout.setComponentAlignment(zbnrWt, Alignment.TOP_CENTER);

			Label besitzer = new Label();
			besitzer.setContentMode(ContentMode.HTML);
			besitzer.setValue("<center><b>Besitzer: </b>" + (zwHund.getVeroeffentlichen() ? zwHund.getBesitzershow() :"--") + "</center>");
			hundLayout.addComponent(besitzer);
			hundLayout.setComponentAlignment(besitzer, Alignment.TOP_CENTER);

			Label zuechter = new Label();
			zuechter.setContentMode(ContentMode.HTML);
			zuechter.setValue("<center><b>Züchter: </b>" + (zwHund.getVeroeffentlichen() ? zwHund.getZuechter() :"--") + "</center>");
			hundLayout.addComponent(zuechter);
			hundLayout.setComponentAlignment(zuechter, Alignment.TOP_CENTER);

			Label vater = new Label();
			vater.setContentMode(ContentMode.HTML);
			vater.setValue("<center><b>Vater: </b>" + (zwHund.getVeroeffentlichen() ? zwHund.getVater() :"--") + "</center>");
			hundLayout.addComponent(vater);
			hundLayout.setComponentAlignment(vater, Alignment.TOP_CENTER);

			Label mutter = new Label();
			mutter.setContentMode(ContentMode.HTML);
			mutter.setValue("<center><b>Mutter: </b>" + (zwHund.getVeroeffentlichen() ? zwHund.getMutter() :"--") + "</center>");
			hundLayout.addComponent(mutter);
			hundLayout.setComponentAlignment(mutter, Alignment.TOP_CENTER);

			String bewertung = "";
			try {
				bewertung = ShowPrintBewertungUebersicht.getFormwertText(show, zwHund);
			} catch (Exception e) {

			}

			Label formwert = new Label();
			formwert.setContentMode(ContentMode.HTML);
			formwert.setValue("<center><b>Bewertung: </b>" + (zwHund.getVeroeffentlichen() ? bewertung :"--") + "</center>");
			hundLayout.addComponent(formwert);
			hundLayout.setComponentAlignment(formwert, Alignment.BOTTOM_CENTER);
			
			// System.out.println("katalognummer" + currentItem.getItemPro)
			TextArea beschreibung = new TextArea();
			beschreibung.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
			beschreibung.setWidth(50.f, Unit.PERCENTAGE);
			
			if (!(zwHund.getBewertung() == null)) {

				beschreibung.setValue("" + (zwHund.getVeroeffentlichen() ? zwHund.getBewertung() :"wird nicht veröffentlicht") );

			}

		
		
			
			beschreibung.setReadOnly(true);
			hundLayout.addComponent(beschreibung);
			hundLayout.setComponentAlignment(beschreibung, Alignment.MIDDLE_CENTER);

		}
		return hundLayout;
	}
}
