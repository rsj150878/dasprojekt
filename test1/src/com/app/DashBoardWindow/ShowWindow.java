package com.app.DashBoardWindow;

import java.util.List;

import com.app.Components.ShowHundBewertungComponent;
import com.app.DashBoard.Event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBShowNeu;
import com.app.showData.ShowHund;
import com.app.showData.ShowRing;
import com.vaadin.data.HierarchyData;
import com.vaadin.data.provider.InMemoryHierarchicalDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ShowWindow extends Window {

	public static final String ID = "hundesuchwindow";

	public ShowWindow(ShowRing ring) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);

		setResizable(false);
		setClosable(false);
		setHeight(100.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);

//		VerticalLayout content = new VerticalLayout();
//		// content.setSizeFull();
//		content.setMargin(new MarginInfo(true, false, false, false));
//		
		Component x = buildShowGrid();
		//content.addComponent(x);
		//content.setExpandRatio(x, 1);
		
		setContent(x);


	}

	private Component buildShowGrid() {

		Panel searchPanel = new Panel();
		searchPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		searchPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		searchPanel.setSizeUndefined();

		HorizontalLayout panelLayout = new HorizontalLayout();
		panelLayout.setSizeUndefined();
		searchPanel.setContent(panelLayout);

		DBShowNeu db = new DBShowNeu();

		List<ShowRing> ringList = null;

		try {
			ringList = db.getRingeFuerShow(4);
		} catch (Exception e) {
			Notification.show("fehler beim lesen der Daten);");
			e.printStackTrace();
		}
		ShowRing ring = ringList.get(0);

		HierarchyData<ShowRing> ringData = new HierarchyData<>();
		ringData.addItem(null, ring);

		ring.flattened().forEach(zwring -> ringData.addItems(zwring, zwring.getChilds()));

		TreeGrid<ShowRing> showRingTreeGrid = new TreeGrid<>();
		showRingTreeGrid.setDataProvider(new InMemoryHierarchicalDataProvider<ShowRing>(ringData));
		showRingTreeGrid.expand(ring);
		ring.flattened().forEach(zwring -> showRingTreeGrid.expand(zwring));

		showRingTreeGrid.addColumn(ShowRing::getKatalogNummer).setCaption("KatalogNummer");

		panelLayout.addComponent(showRingTreeGrid);

		showRingTreeGrid.setSelectionMode(SelectionMode.SINGLE);
		showRingTreeGrid.addSelectionListener(event -> {

			if (event.getFirstSelectedItem().isPresent()) {
				Object o = event.getFirstSelectedItem().get();
				System.out.println("in event" + o.toString());
				if (o instanceof ShowHund) {
					ShowHund currentHund = (ShowHund) o;
					System.out.println("bin ein showhund");
					// ShowHundBewertungComponent bewertungPart = new
					// ShowHundBewertungComponent(currentHund);
					// panelLayout.addComponent(bewertungPart);
				}
			}
		});
		showRingTreeGrid.setSizeUndefined();
		//showRingTreeGrid.s

		ShowHundBewertungComponent bewertungPart = new ShowHundBewertungComponent();
		panelLayout.addComponent(bewertungPart);
		return searchPanel;

	}

	public static void open(final ShowRing ring) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new ShowWindow(ring);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
