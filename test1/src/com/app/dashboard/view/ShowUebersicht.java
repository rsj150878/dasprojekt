package com.app.dashboard.view;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.app.dashboard.event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.dbio.DBConnection;
import com.app.dashboard.event.DashBoardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class ShowUebersicht extends TabSheet implements View, //CloseHandler,
		ShowDetail.ShowDetailListener {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";
	private SQLContainer schauContainer;
	private TableQuery q1;

	public ShowUebersicht() {
		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		//setCloseHandler(this);

		DashBoardEventBus.register(this);

		q1 = new TableQuery("schau", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		try {
			schauContainer = new SQLContainer(q1);
			schauContainer.addOrderBy(new OrderBy("datum", false));
			schauContainer.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		addTab(buildDrafts());
	}

	private Item commit() {
		Item returnItem = null;
		try {
			schauContainer.commit();

			schauContainer.refresh();

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

		return returnItem;
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
		// allDrafts.setCaption("Alle Veranstaltungen");

		// VerticalLayout titleAndDrafts = new VerticalLayout();
		// titleAndDrafts.setSizeUndefined();
		// titleAndDrafts.setSpacing(true);
		// titleAndDrafts.addStyleName("drafts");
		// allDrafts.addComponent(titleAndDrafts);
		// allDrafts
		// .setComponentAlignment(titleAndDrafts, Alignment.MIDDLE_CENTER);
		buildDraftsList(allDrafts);

		draftsPanel.setContent(allDrafts);

		return draftsPanel;
	}

	private Component buildDraftsList(VerticalLayout parentLayout) {

		VerticalLayout drafts = new VerticalLayout();
		drafts.setSpacing(true);
		drafts.setSizeUndefined();

		String year = schauContainer.getItem(schauContainer.getIdByIndex(0))
				.getItemProperty("datum").getValue().toString().substring(0, 4);

		Label yearLabel = new Label(year);
		yearLabel.setSizeUndefined();

		Panel yearPanel = new Panel();
		yearPanel.setSizeUndefined();
		yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		parentLayout.addComponent(yearLabel);
		parentLayout.addComponent(yearPanel);

		yearPanel.setContent(drafts);

		for (Object itemId : schauContainer.getItemIds()) {

			if (!year.equals(schauContainer.getItem(itemId)
					.getItemProperty("datum").getValue().toString()
					.substring(0, 4))) {

				yearPanel.setContent(drafts);

				year = schauContainer.getItem(itemId).getItemProperty("datum")
						.getValue().toString().substring(0, 4);

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

			drafts.addComponent(buildDraftThumb(schauContainer.getItem(itemId)));

		}

		yearPanel.setContent(drafts);

		return drafts;
	}

	private Component buildDraftThumb(Item schauItem) {

		final Item vaItem = schauItem;

		final Button schauButton = new Button(vaItem
				.getItemProperty("bezeichnung").getValue().toString());

		schauButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				openReport(vaItem);

			}
		});

		return schauButton;
	}

	public void openReport(Item toOpenVeranstaltung) {
		ShowDetail detailView = new ShowDetail(
				toOpenVeranstaltung, this);

		String title = toOpenVeranstaltung.getItemProperty("bezeichnung")
						.getValue().toString();
		title += " "
				+ new SimpleDateFormat("dd.MM.yyyy").format(toOpenVeranstaltung
						.getItemProperty("datum").getValue());

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(
				getComponentCount() - 1));

		detailView.setTitle(title);
		setSelectedTab(getComponentCount() - 1);

	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	@Override
	public void titleChanged(String newTitle, ShowDetail detail) {
		// TODO Auto-generated method stub
		getTab(detail).setCaption(newTitle);
			
	}

}
