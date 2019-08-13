package com.app.dashboard.view;

import java.util.Iterator;
import java.util.List;

import com.app.dashboard.component.HundeSchulStundeUebersichtGrid;
import com.app.dashboard.event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dashboardwindow.SearchWindow;
import com.app.dbio.DBKurs;
import com.app.kurs.KursStunde;
import com.app.kurs.KursTag;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class KursTagPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285761827506965841L;

	// public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";

	private CssLayout dashboardPanels;
	private final VerticalLayout root;

	private KursTag kursTag;
	private List<KursStunde> kursStunden;

	public KursTagPanel(KursTag kursTag) {
		this.kursTag = kursTag;
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		setSizeFull();
		DashBoardEventBus.register(this);

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
		root.addLayoutClickListener(event -> DashBoardEventBus.post(new CloseOpenWindowsEvent()));
	}

	//
	private Component buildContent() {
		dashboardPanels = new CssLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);

		DBKurs dbKurs = new DBKurs();
		try {
			kursStunden = dbKurs.getKursStundenZuKursTag(kursTag);
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Fehler beim lesen der kursStunden");
		}

		kursStunden.forEach(stunde -> dashboardPanels.addComponent(buildKursStunde(stunde)));

		return dashboardPanels;
	}

	private Component buildKursStunde(KursStunde kursStunde) {

		HundeSchulStundeUebersichtGrid kurStundeGrid = new HundeSchulStundeUebersichtGrid(kursStunde);
		Component contentWrapper = createContentWrapper(kurStundeGrid);
		contentWrapper.addStyleName("top10-revenue");

		return contentWrapper;
	}

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
		MenuItem addDog = tools.addItem("", VaadinIcons.PLUS, selectedItem -> {
			DashBoardEventBus.register(content);
			SearchWindow.open();
		});

		addDog.setStyleName("icon-only");

		MenuItem max = tools.addItem("", VaadinIcons.EXPAND, selectedItem -> {
			if (!slot.getStyleName().contains("max")) {
				selectedItem.setIcon(VaadinIcons.COMPRESS);
				toggleMaximized(slot, true);
			} else {
				slot.removeStyleName("max");
				selectedItem.setIcon(VaadinIcons.EXPAND);
				toggleMaximized(slot, false);
			}
		});

		max.setStyleName("icon-only");

		MenuItem copy = tools.addItem("", VaadinIcons.ARROW_FORWARD, selectedItem -> {
			HundeSchulStundeUebersichtGrid zw = (HundeSchulStundeUebersichtGrid) content;
			//InfoWindow.open(zw.copyMailAdressestoClipBoard());
		});
		copy.setStyleName("icon-only");
		
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
