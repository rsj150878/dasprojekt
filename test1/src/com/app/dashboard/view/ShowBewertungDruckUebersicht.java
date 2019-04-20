package com.app.dashboard.view;

import java.util.Iterator;
import java.util.List;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.Rassen;
import com.app.printclasses.ShowPrintBewertungUebersicht;
import com.app.showdata.Show;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowBewertungDruckUebersicht extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782833824655568251L;

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";

	List<Show> showList;
	Component currentPrintComponent;

	public ShowBewertungDruckUebersicht() {
		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		// setCloseHandler(this);

		DashBoardEventBus.register(this);
		DBShowNeu db = new DBShowNeu();
		try {
			showList = db.getShows();

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("fehler beim lesen der shows");
		}
		setContent(buildDrafts());
	}

	private Component buildDrafts() {

		Panel draftsPanel = new Panel("Alle Shows");
		draftsPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		draftsPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		draftsPanel.setSizeUndefined();

		final VerticalLayout allDrafts = new VerticalLayout();
		allDrafts.setSizeFull();
		allDrafts.setSpacing(true);
		allDrafts.setMargin(true);
		buildDraftsList(allDrafts);

		draftsPanel.setContent(allDrafts);

		return draftsPanel;
	}

	private Component buildDraftsList(VerticalLayout parentLayout) {

		VerticalLayout drafts = new VerticalLayout();
		drafts.setSpacing(true);
		drafts.setSizeUndefined();

		String year = showList.get(0).getSchauDate().toString().substring(0, 4);

		Label yearLabel = new Label(year);
		yearLabel.setSizeUndefined();

		Panel yearPanel = new Panel();
		yearPanel.setSizeUndefined();
		yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		parentLayout.addComponent(yearLabel);
		parentLayout.addComponent(yearPanel);

		yearPanel.setContent(drafts);

		Iterator<Show> i = showList.iterator();

		while (i.hasNext()) {
			Show workShow = i.next();

			if (!year.equals(workShow.getSchauDate().toString().substring(0, 4))) {

				yearPanel.setContent(drafts);

				year = workShow.getSchauDate().toString().substring(0, 4);

				yearLabel = new Label(year);
				yearLabel.setSizeUndefined();

				yearPanel = new Panel();
				yearPanel.setSizeUndefined();
				yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
				yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

				parentLayout.addComponent(yearLabel);
				parentLayout.addComponent(yearPanel);

				drafts = new VerticalLayout();
				drafts.setSpacing(true);
				drafts.setSizeUndefined();

			}

			drafts.addComponent(buildDraftThumb(workShow));

		}

		yearPanel.setContent(drafts);

		return drafts;
	}

	private Component buildDraftThumb(Show workShow) {

		HorizontalLayout layout = new HorizontalLayout();

		final Show vaItem = workShow;

		final Button schauButton = new Button(vaItem.getSchaubezeichnung().toString());

		layout.addComponent(schauButton);

		final ComboBox<Rassen> boxTest = new ComboBox<>();
		boxTest.setItems(Rassen.values());
		boxTest.setItemCaptionGenerator(Rassen::getRassenLangBezeichnung);

		layout.addComponent(boxTest);

		schauButton.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -571490624070148204L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!boxTest.getSelectedItem().isPresent()) {
					Notification.show("Bitte ring auswählen");
				} else {

					if (!(currentPrintComponent == null)) {
						layout.removeComponent(currentPrintComponent);
					}

					ShowPrintBewertungUebersicht uebersicht = new ShowPrintBewertungUebersicht(workShow,
							boxTest.getSelectedItem().get());
					layout.addComponent(uebersicht);
				}
			}
		});

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

}