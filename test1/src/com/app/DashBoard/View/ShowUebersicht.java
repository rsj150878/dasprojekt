package com.app.DashBoard.View;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.app.DashBoard.Event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
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

		System.out.println("veranstaltungsdatum "
				+ schauContainer.getItem(schauContainer.getIdByIndex(0))
						.getItemProperty("datum").getValue().toString());

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

//	@Override
//	public void onTabClose(final TabSheet tabsheet, final Component tabContent) {
//		Label message = new Label(
//				"You have not saved this report. Do you want to save or discard any changes you've made to this report?");
//		message.setWidth("25em");
//
//		final Window confirmDialog = new Window("Unsaved Changes");
//		confirmDialog.setId(CONFIRM_DIALOG_ID);
//		confirmDialog.setCloseShortcut(KeyCode.ESCAPE, null);
//		confirmDialog.setModal(true);
//		confirmDialog.setClosable(false);
//		confirmDialog.setResizable(false);
//
//		VerticalLayout root = new VerticalLayout();
//		root.setSpacing(true);
//		root.setMargin(true);
//		confirmDialog.setContent(root);
//
//		HorizontalLayout footer = new HorizontalLayout();
//		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
//		footer.setWidth("100%");
//		footer.setSpacing(true);
//
//		root.addComponents(message, footer);
//
//		Button ok = new Button("Save", new ClickListener() {
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				confirmDialog.close();
//				VeranstaltungsDetailViewNeu saveComponent = (VeranstaltungsDetailViewNeu) tabContent;
//				saveComponent.commit();
//				removeComponent(tabContent);
//				DashBoardEventBus.post(new ReportsCountUpdatedEvent(
//						getComponentCount() - 1));
//				Notification.show("Die Veranstaltung wurde gespeichert",
//						Type.TRAY_NOTIFICATION);
//			}
//		});
//		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
//
//		Button discard = new Button("Discard Changes", new ClickListener() {
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				confirmDialog.close();
//				removeComponent(tabContent);
//				DashBoardEventBus.post(new ReportsCountUpdatedEvent(
//						getComponentCount() - 1));
//			}
//		});
//		discard.addStyleName(ValoTheme.BUTTON_DANGER);
//
//		Button cancel = new Button("Cancel", new ClickListener() {
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				confirmDialog.close();
//			}
//		});
//
//		footer.addComponents(discard, cancel, ok);
//		footer.setExpandRatio(discard, 1);
//
//		getUI().addWindow(confirmDialog);
//		confirmDialog.focus();
//	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	@Override
	public void titleChanged(String newTitle, ShowDetail detail) {
		// TODO Auto-generated method stub
		getTab(detail).setCaption(newTitle);
			
	}

}
