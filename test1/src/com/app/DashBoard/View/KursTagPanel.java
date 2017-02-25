package com.app.DashBoard.View;

import java.util.Iterator;

import com.app.DashBoard.Component.HundeSchulStundeUebersichtGrid;
import com.app.DashBoard.Event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoardWindow.InfoWindow;
import com.app.DashBoardWindow.SearchWindow;
import com.app.dbIO.DBConnection;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class KursTagPanel extends Panel {

	// public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";

	private CssLayout dashboardPanels;
	private final VerticalLayout root;

	private TableQuery kursStundeQuery;
	private SQLContainer kursStundeContainer;
	private Item tagItem;

	public KursTagPanel(Item tagItem) {
		this.tagItem = tagItem;
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		setSizeFull();
		DashBoardEventBus.register(this);

		kursStundeQuery = new TableQuery("kursstunde",
				DBConnection.INSTANCE.getConnectionPool());
		kursStundeQuery.setVersionColumn("version");

		try {
			kursStundeContainer = new SQLContainer(kursStundeQuery);

		} catch (Exception e) {
			Notification.show("fehler beim aufbau der Container");
			e.printStackTrace();
		}

		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);

		Component content = buildContent();
		root.addComponent(content);
		root.setExpandRatio(content, 1);

		// All the open sub-windows should be closed whenever the root layout
		// gets clicked.
		root.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(final LayoutClickEvent event) {
				DashBoardEventBus.post(new CloseOpenWindowsEvent());
			}
		});
	}

	//
	private Component buildContent() {
		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		kursStundeContainer.removeAllContainerFilters();

		kursStundeContainer.addContainerFilter(new Equal("idkurstag", tagItem
				.getItemProperty("idkurstag").getValue()));

		for (Object o : kursStundeContainer.getItemIds()) {
			Item kursStunde = kursStundeContainer.getItem(o);
			dashboardPanels.addComponent(buildKursStunde(kursStunde));
		}

		// dashboardPanels.addComponent(buildTopGrossingMovies());
		// dashboardPanels.addComponent(buildNotes());
		// dashboardPanels.addComponent(buildTop10TitlesByRevenue());
		// dashboardPanels.addComponent(buildPopularMovies());
		//
		return dashboardPanels;
	}

	private Component buildKursStunde(Item kursStunde) {

		HundeSchulStundeUebersichtGrid kurStundeGrid = new HundeSchulStundeUebersichtGrid(
				kursStunde);
		Component contentWrapper = createContentWrapper(kurStundeGrid);
		contentWrapper.addStyleName("top10-revenue");

		return contentWrapper;
	}

	// private Component buildTop10TitlesByRevenue() {
	// Component contentWrapper = createContentWrapper(new TopTenMoviesTable());
	// contentWrapper.addStyleName("top10-revenue");
	// return contentWrapper;
	// }
	//

	private Component createContentWrapper(final Component content) {
		final CssLayout slot = new CssLayout();
		slot.setWidth("100%");
		slot.addStyleName("dashboard-panel-slot");

		CssLayout card = new CssLayout();
		card.setWidth("100%");
		card.addStyleName(ValoTheme.LAYOUT_CARD);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.addStyleName("dashboard-panel-toolbar");
		toolbar.setWidth("100%");

		Label caption = new Label(content.getCaption());
		caption.addStyleName(ValoTheme.LABEL_H4);
		caption.addStyleName(ValoTheme.LABEL_COLORED);
		caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		content.setCaption(null);

		MenuBar tools = new MenuBar();
		tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		MenuItem addDog = tools.addItem("", FontAwesome.PLUS, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				DashBoardEventBus.register(content);
				SearchWindow.open();
			}
		});
		addDog.setStyleName("icon-only");

		MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				if (!slot.getStyleName().contains("max")) {
					selectedItem.setIcon(FontAwesome.COMPRESS);
					toggleMaximized(slot, true);
				} else {
					slot.removeStyleName("max");
					selectedItem.setIcon(FontAwesome.EXPAND);
					toggleMaximized(slot, false);
				}
			}
		});
		max.setStyleName("icon-only");
		
		MenuItem copy = tools.addItem("",FontAwesome.MAIL_FORWARD, new Command() {

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				HundeSchulStundeUebersichtGrid zw = (HundeSchulStundeUebersichtGrid )content;
				InfoWindow.open(zw.copyMailAdressestoClipBoard()); 
			}
		});
		copy.setStyleName("icon-only");

		
//		MenuItem root = tools.addItem("", FontAwesome.MAIL_FORWARD, null);
//		root.addItem("Mail", new Command() {
//			@Override
//			public void menuSelected(final MenuItem selectedItem) {
//				HundeSchulStundeUebersichtGrid zw = (HundeSchulStundeUebersichtGrid )content;
//				zw.copyMailAdressestoClipBoard(); 
//				
//			}
//		});
//		root.addSeparator();
//		root.addItem("Close", new Command() {
//			@Override
//			public void menuSelected(final MenuItem selectedItem) {
//				Notification.show("Not implemented in this demo");
//			}
//		});
//
		toolbar.addComponents(caption, tools);
		toolbar.setExpandRatio(caption, 1);
		toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

		card.addComponents(toolbar, content);
		slot.addComponent(card);
		return slot;
	}

	private void toggleMaximized(final Component panel, final boolean maximized) {
		for (Iterator<Component> it = root.iterator(); it.hasNext();) {
			it.next().setVisible(!maximized);
		}
		dashboardPanels.setVisible(true);

		for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
			Component c = it.next();
			c.setVisible(!maximized);
		}

		if (maximized) {
			panel.setVisible(true);
			panel.addStyleName("max");
		} else {
			panel.removeStyleName("max");
		}
	}

}
