package com.app.DashBoard.View;

import java.sql.SQLException;

import com.app.DashBoard.Event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnectionMicrosoft;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;

public class WurfUebersicht extends TabSheet implements View, // CloseHandler,
		WurfDetail.WurfDetailListener {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";
	private SQLContainer wurfContainer;
	private FreeformQuery q1;

	public WurfUebersicht() {
		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		// setCloseHandler(this);

		DashBoardEventBus.register(this);

		addTab(buildDrafts());
	}

	private Item commit() {
		Item returnItem = null;
		try {
			wurfContainer.commit();

			wurfContainer.refresh();

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

		return returnItem;
	}

	private Component buildDrafts() {

		Panel draftsPanel = new Panel("Alle Würfe");
		draftsPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		draftsPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		draftsPanel.setSizeFull();

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

	private void buildDraftsList(VerticalLayout parentLayout) {
		Grid wurfGrid = new Grid();

		q1 = new FreeformQuery(
				"select wurf.IDMitgliederWurf,mitglied.Nachname, mitglied.Vorname, mitglied.Zwingername, wurf.Wurfname from "
						+ "tabMitgliederWurf wurf, tabMitglieder mitglied where wurf.IDMitglieder = mitglied.IDMitglieder",
				DBConnectionMicrosoft.INSTANCE.getConnectionPool(),
				"IDMitgliederWurf"); // , new MSSQLGenerator());
		// q1.setVersionColumn("version");

		try {
			wurfContainer = new SQLContainer(q1);
			wurfContainer.addOrderBy(new OrderBy("IDMitgliederWurf", false));
			wurfContainer.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		GeneratedPropertyContainer cp = new GeneratedPropertyContainer(
				wurfContainer);

		wurfGrid.setContainerDataSource(cp);
		wurfGrid.setSizeFull();
		wurfGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		wurfGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		wurfGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		cp.addGeneratedProperty("bearb", new PropertyValueGenerator<String>() {

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				return "Wurf bearbeiten"; // The caption
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		wurfGrid.setColumns("Nachname", "Vorname", "Zwingername", "Wurfname",
				"bearb");
		wurfGrid.getColumn("bearb").setRenderer(
				new ButtonRenderer(new RendererClickListener() {

					@Override
					public void click(RendererClickEvent event) {
						openReport(event.getItemId());

					}

				}));

		parentLayout.addComponent(wurfGrid);

	}

	public void openReport(Object itemId) {
		WurfDetail detailView = new WurfDetail(wurfContainer.getItem(itemId)
				.getItemProperty("IDMitgliederWurf").getValue(), this);

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(
				getComponentCount() - 1));

		detailView.setTitle("test");
		setSelectedTab(getComponentCount() - 1);

	}

	// @Override
	// public void onTabClose(final TabSheet tabsheet, final Component
	// tabContent) {
	// Label message = new Label(
	// "You have not saved this report. Do you want to save or discard any changes you've made to this report?");
	// message.setWidth("25em");
	//
	// final Window confirmDialog = new Window("Unsaved Changes");
	// confirmDialog.setId(CONFIRM_DIALOG_ID);
	// confirmDialog.setCloseShortcut(KeyCode.ESCAPE, null);
	// confirmDialog.setModal(true);
	// confirmDialog.setClosable(false);
	// confirmDialog.setResizable(false);
	//
	// VerticalLayout root = new VerticalLayout();
	// root.setSpacing(true);
	// root.setMargin(true);
	// confirmDialog.setContent(root);
	//
	// HorizontalLayout footer = new HorizontalLayout();
	// footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
	// footer.setWidth("100%");
	// footer.setSpacing(true);
	//
	// root.addComponents(message, footer);
	//
	// Button ok = new Button("Save", new ClickListener() {
	// @Override
	// public void buttonClick(final ClickEvent event) {
	// confirmDialog.close();
	// VeranstaltungsDetailViewNeu saveComponent = (VeranstaltungsDetailViewNeu)
	// tabContent;
	// saveComponent.commit();
	// removeComponent(tabContent);
	// DashBoardEventBus.post(new ReportsCountUpdatedEvent(
	// getComponentCount() - 1));
	// Notification.show("Die Veranstaltung wurde gespeichert",
	// Type.TRAY_NOTIFICATION);
	// }
	// });
	// ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
	//
	// Button discard = new Button("Discard Changes", new ClickListener() {
	// @Override
	// public void buttonClick(final ClickEvent event) {
	// confirmDialog.close();
	// removeComponent(tabContent);
	// DashBoardEventBus.post(new ReportsCountUpdatedEvent(
	// getComponentCount() - 1));
	// }
	// });
	// discard.addStyleName(ValoTheme.BUTTON_DANGER);
	//
	// Button cancel = new Button("Cancel", new ClickListener() {
	// @Override
	// public void buttonClick(final ClickEvent event) {
	// confirmDialog.close();
	// }
	// });
	//
	// footer.addComponents(discard, cancel, ok);
	// footer.setExpandRatio(discard, 1);
	//
	// getUI().addWindow(confirmDialog);
	// confirmDialog.focus();
	// }

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	@Override
	public void titleChanged(String newTitle, WurfDetail detail) {
		// TODO Auto-generated method stub
		getTab(detail).setCaption(newTitle);

	}

}
