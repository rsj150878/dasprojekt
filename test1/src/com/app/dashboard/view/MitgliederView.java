package com.app.dashboard.view;

import java.util.Collection;

import com.app.auth.MitgliederListe;
import com.app.auth.Person;
import com.app.dashboard.event.DashBoardEvent.BrowserResizeEvent;
import com.app.dashboard.event.DashBoardEvent.UpdateUserEvent;
import com.app.dashboard.event.DashBoardEvent.UserNewEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dashboardwindow.HundeDetailWindow;
import com.app.dashboardwindow.ProfilePreferencesWindow;
import com.app.dbio.DBPerson;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial" })
public class MitgliederView extends VerticalLayout implements View {

	private final Grid<MitgliederListe> table;
	private Button neuesMitglied;
	private Collection<MitgliederListe> mitgliederListe;
	private ListDataProvider<MitgliederListe> listDataProvider;

	public MitgliederView() {

		setSizeFull();
		addStyleName("mitglieder");
		DashBoardEventBus.register(this);

		addComponent(buildToolbar());

		table = buildTable();
		addComponent(table);
		setExpandRatio(table, 1);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashBoardEventBus.unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("Mitgliederliste");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		neuesMitglied = buildNeuesMitglied();
		HorizontalLayout tools = new HorizontalLayout(buildFilter(), neuesMitglied);
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private Button buildNeuesMitglied() {
		final Button createReport = new Button("neuesMitglied");
		createReport.setDescription("Create a new report from the selected transactions");
		createReport.setEnabled(true);
		createReport.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				Person person = new Person();
				ProfilePreferencesWindow.open(person, false);
				System.out.println(" nach open ");

			}
		});

		return createReport;
	}

	private Component buildFilter() {
		final TextField filterField = new TextField();
		filterField.setIcon(VaadinIcons.SEARCH);
		filterField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		filterField.addValueChangeListener(this::onNameFilterTextChange);
		filterField.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				listDataProvider.clearFilters();
			}
		});
		return filterField;
	}

	private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
		// listDataProvider.filteringBySubstring(MitgliederListe::getSearchString,
		// event.getValue());
		listDataProvider.setFilter(MitgliederListe::getSearchString, s -> caseInsensitiveContains(s, event.getValue()));
	}

	private Boolean caseInsensitiveContains(String where, String what) {
		return where.toLowerCase().contains(what.toLowerCase());
	}

	private Grid<MitgliederListe> buildTable() {
		final Grid<MitgliederListe> gridTable = new Grid<>(MitgliederListe.class);
		gridTable.setSizeFull();

		gridTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		gridTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		gridTable.addStyleName(ValoTheme.TABLE_COMPACT);

		gridTable.setColumnReorderingAllowed(true);

		try {
			mitgliederListe = new DBPerson().getMitgliederListe();

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Fehler beim Lesen der Personen");
		}
		listDataProvider = DataProvider.ofCollection(mitgliederListe);

		gridTable.setColumnOrder("vorName", "familienName", "adresse");
		gridTable.getColumn("hunde").setHidden(true);
		gridTable.getColumn("person").setHidden(true);
		gridTable.getColumn("hundeNamen").setHidden(true);
		gridTable.getColumn("searchString").setHidden(true);

		gridTable.setFrozenColumnCount(2);

		gridTable.addColumn(MitgliederListe -> "edit", new ButtonRenderer<>(clickEvent -> {
			MitgliederListe zw = (MitgliederListe) clickEvent.getItem();
			ProfilePreferencesWindow.open(zw.getPerson(), true);

		})).setId("edit");

		gridTable.addColumn(MitgliederListe -> "Hunde", new ButtonRenderer<>(clickEvent -> {
			MitgliederListe zw = (MitgliederListe) clickEvent.getItem();
			HundeDetailWindow.open(zw.getPerson(), zw.getHunde());

		})).setId("hundebutton");

		//
		gridTable.getDefaultHeaderRow().join("edit", "hundebutton").setText("Tools");

		gridTable.setItems(mitgliederListe);
		gridTable.setDataProvider(listDataProvider);

		return gridTable;
	}

	@Subscribe
	public void newUserEvent(UserNewEvent event) {
		MitgliederListe zw = new MitgliederListe();
		zw.setPerson(event.getPerson());
		mitgliederListe.add(zw);
		listDataProvider.refreshAll();

	}

	@Subscribe
	public void updateUserEvent(UpdateUserEvent event) {
		// mitgliederListe.update();
		listDataProvider.refreshAll();
	}

	// TODO
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		if (Page.getCurrent().getBrowserWindowWidth() < 800) {
			table.removeColumn("adresse");

		}
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

}
