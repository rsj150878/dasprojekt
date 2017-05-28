package com.app.DashBoard.View;

import java.util.Iterator;
import java.util.List;

import com.app.DashBoard.Event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBShowNeu;
import com.app.showData.Show;
import com.app.showData.ShowRing;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowBewertungUebersicht extends TabSheet implements View, // CloseHandler,
		ShowRingBewertungView.ShowRingBewertungListener {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";

	List<Show> showList;

	public ShowBewertungUebersicht() {
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
		addTab(buildDrafts());
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

		final ComboBox<ShowRing> boxTest = new ComboBox();
		boxTest.setItems(workShow.getRinge());
		boxTest.setItemCaptionGenerator(ShowRing::getKatalogNummer);

		layout.addComponent(boxTest);

		schauButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (!boxTest.getSelectedItem().isPresent()) {
					Notification.show("Bitte ring auswählen");
				} else {
					openReport(workShow, boxTest.getSelectedItem().get());

				}
			}
		});

		return layout;
	}

	public void openReport(Show show, ShowRing ring) {
		ShowRingBewertungView detailView = new ShowRingBewertungView(show, ring, this);

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(getComponentCount() - 1));

		detailView.setTitle(ring.getKatalogNummer());
		setSelectedTab(getComponentCount() - 1);

	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	@Override
	public void titleChanged(String newTitle, ShowRingBewertungView detail) {
		// TODO Auto-generated method stub
		getTab(detail).setCaption(newTitle);

	}

}
