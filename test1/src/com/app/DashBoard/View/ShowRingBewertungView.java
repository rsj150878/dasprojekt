package com.app.DashBoard.View;

import java.util.ArrayList;
import java.util.List;

import com.app.Components.ShowGeschlechtAbschlussComponent;
import com.app.Components.ShowHundBewertungComponent;
import com.app.Components.ShowKlassenAbschlussComponent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBShowNeu;
import com.app.showData.Show;
import com.app.showData.ShowGeschlechtEnde;
import com.app.showData.ShowHund;
import com.app.showData.ShowKlasseEnde;
import com.app.showData.ShowRing;
import com.vaadin.data.HierarchyData;
import com.vaadin.data.provider.InMemoryHierarchicalDataProvider;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowRingBewertungView extends Panel implements View, Handler {

	Component bewertungPart = null;

	DBShowNeu db = null;
	ComboBox<ShowRing> boxTest;
	List<ShowRing> source;

	Show show ;
	ShowRing ring;
	private final ShowRingBewertungListener listener;


	Action action_vor = new ShortcutAction("Alt+PfeilRechts", ShortcutAction.KeyCode.ARROW_RIGHT,
			new int[] { ShortcutAction.ModifierKey.ALT });
	Action action_zurueck = new ShortcutAction("Alt+PfeilLinks", ShortcutAction.KeyCode.ARROW_LEFT,
			new int[] { ShortcutAction.ModifierKey.ALT });

	public ShowRingBewertungView(Show show, ShowRing ring, ShowRingBewertungListener listener) {
		
		this.show = show;
		this.ring = ring;
		this.listener = listener;
		
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("reports");

		// addComponent(buildToolbar());
		setContent(buildWorkingArea());
		addActionHandler(this);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("MeineHunde");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		header.addComponent(title);

		return header;
	}

	private Component buildWorkingArea() {
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		// mainVerticalLayout.setSizeFull();

		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainVerticalLayout.addComponent(mainLayout);
		// ShowRing ring = new ShowRing();
		// alle datena ufbauen....
		db = new DBShowNeu();

		try {
			ring.addShowKlassen(db.getKlassenForShow(ring, show.getIdSchau(), ring));
		} catch (Exception e) {
			Notification.show("fehler beim lesen der Daten);");
			e.printStackTrace();
		}

		HierarchyData<ShowRing> ringData = new HierarchyData<>();
		ringData.addItem(null, ring);

		ring.flattened().forEach(zwring -> ringData.addItems(zwring, zwring.getChilds()));

		TreeGrid<ShowRing> showRingTreeGrid = new TreeGrid<>();
		showRingTreeGrid.setDataProvider(new InMemoryHierarchicalDataProvider<ShowRing>(ringData));
		showRingTreeGrid.expand(ring);
		ring.flattened().forEach(zwring -> showRingTreeGrid.expand(zwring));

		showRingTreeGrid.addColumn(ShowRing::getKatalogNummer).setCaption("KatalogNummer");

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
		showRingTreeGrid.addStyleName(ValoTheme.TREETABLE_COMPACT);
		// showRingTreeGrid.s

		// mainLayout.addComponent(showRingTreeGrid);

		// mainLayout.addComponent(bewertungPart);

		ListSelect<ShowRing> test = new ListSelect<>();
		// test.setDataProvider(new DataProvider<ShowRing>(ringData));
		source = new ArrayList<>();
		ring.flattened().forEach(zwring -> source.add(zwring));
		test.setItems(source);

		test.setItemCaptionGenerator(ShowRing::getKatalogNummer);

		Button zurueck = new Button("zurueck");
		zurueck.setWidth(100.0f, Unit.PERCENTAGE);
		mainLayout.addComponent(zurueck);

		boxTest = new ComboBox<>();
		boxTest.setItems(source);
		boxTest.setItemCaptionGenerator(ShowRing::getKatalogNummer);
		// boxTest.addStyleName(ValoTheme.COMBOBOX_LARGE);
		boxTest.setWidth(100.0f, Unit.PERCENTAGE);
		mainLayout.addComponent(boxTest);

		Button vor = new Button("vor");
		vor.setWidth(100.0f, Unit.PERCENTAGE);
		mainLayout.addComponent(vor);
		// boxTest.gets

		// source.g
		// boxTest.select

		zurueck.addClickListener(event -> {
			if (boxTest.getSelectedItem().isPresent()) {
				int index = source.indexOf(boxTest.getSelectedItem().get());
				if (index > 0) {
					boxTest.setSelectedItem(source.get(index - 1));
				}
			} else
				boxTest.setSelectedItem(source.get(0));
		});

		vor.addClickListener(event -> {
			if (boxTest.getSelectedItem().isPresent()) {
				int index = source.indexOf(boxTest.getSelectedItem().get());
				if (index < source.size()) {
					boxTest.setSelectedItem(source.get(index + 1));
				}
			} else
				boxTest.setSelectedItem(source.get(0));
			;
		});

		boxTest.addSelectionListener(event -> {

			if (!(bewertungPart == null)) {
				mainVerticalLayout.removeComponent(bewertungPart);
			}
			if (boxTest.getSelectedItem().isPresent() && boxTest.getSelectedItem().get() instanceof ShowHund) {
				bewertungPart = new ShowHundBewertungComponent(db, show, (ShowHund) boxTest.getSelectedItem().get());
				mainVerticalLayout.addComponent(bewertungPart);
			} else if (boxTest.getSelectedItem().isPresent()
					&& boxTest.getSelectedItem().get() instanceof ShowKlasseEnde) {
				bewertungPart = new ShowKlassenAbschlussComponent (db, show, (ShowKlasseEnde) boxTest.getSelectedItem().get());
				mainVerticalLayout.addComponent(bewertungPart);

			} else if (boxTest.getSelectedItem().isPresent()
					&& boxTest.getSelectedItem().get() instanceof ShowGeschlechtEnde) {
				bewertungPart = new ShowGeschlechtAbschlussComponent (db, show, (ShowGeschlechtEnde) boxTest.getSelectedItem().get());
				mainVerticalLayout.addComponent(bewertungPart);

			}

		});

		return (mainVerticalLayout);

	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return new Action[] { action_vor, action_zurueck };
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {

		if (action == action_vor) {
			if (boxTest.getSelectedItem().isPresent()) {
				int index = source.indexOf(boxTest.getSelectedItem().get());
				if (index < source.size()) {
					boxTest.setSelectedItem(source.get(index + 1));
				}
			} else
				boxTest.setSelectedItem(source.get(0));

		}

		if (action == action_zurueck) {
			if (boxTest.getSelectedItem().isPresent()) {
				int index = source.indexOf(boxTest.getSelectedItem().get());
				if (index > 0) {
					boxTest.setSelectedItem(source.get(index - 1));
				}
			} else
				boxTest.setSelectedItem(source.get(0));

		}
	}
	
	public void setTitle(String title) {

		listener.titleChanged(title, ShowRingBewertungView.this);
	}

	public interface ShowRingBewertungListener {
		void titleChanged(String newTitle, ShowRingBewertungView detail);
	}
	
	



}