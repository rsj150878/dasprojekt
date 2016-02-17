package com.app.DashBoard.View;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.app.Auth.DataProvider;
import com.app.Auth.MitgliederListe;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoardWindow.FilterableSortableListContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.sort.Sort;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class MitgliederView extends VerticalLayout implements View {

	private final Grid table;
	private Button neuesMitglied;
	private static final DateFormat DATEFORMAT = new SimpleDateFormat(
			"MM/dd/yyyy hh:mm:ss a");
	private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
	private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city",
			"theater", "room", "title", "seats" };

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
		HorizontalLayout tools = new HorizontalLayout(buildFilter(),
				neuesMitglied);
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private Button buildNeuesMitglied() {
		final Button createReport = new Button("neuesMitglied");
		createReport
				.setDescription("Create a new report from the selected transactions");
		createReport.setEnabled(true);
		createReport.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {

			}
		});
		createReport.setEnabled(false);
		return createReport;
	}

	private Component buildFilter() {
		final TextField filter = new TextField();
		filter.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(final TextChangeEvent event) {
				Filterable data = (Filterable) table.getContainerDataSource();
				data.removeAllContainerFilters();
				data.addContainerFilter(new Filter() {
					@Override
					public boolean passesFilter(final Object itemId,
							final Item item) {

						if (event.getText() == null
								|| event.getText().equals("")) {
							return true;
						}

						return filterByProperty("vorName", item,
								event.getText())
								|| filterByProperty("familienName", item,
										event.getText())
								|| filterByProperty("hundeNamen", item,
										event.getText());

					}

					@Override
					public boolean appliesToProperty(final Object propertyId) {
						if (propertyId.equals("vorName")
								|| propertyId.equals("familienName")
								|| propertyId.equals("hundeNamen"))

						{
							return true;
						}
						return false;
					}
				});
			}
		});

		filter.setInputPrompt("Filter");
		filter.setIcon(FontAwesome.SEARCH);
		filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		filter.addShortcutListener(new ShortcutListener("Clear",
				KeyCode.ESCAPE, null) {
			@Override
			public void handleAction(final Object sender, final Object target) {
				filter.setValue("");
				((Filterable) table.getContainerDataSource())
						.removeAllContainerFilters();
			}
		});
		return filter;
	}

	private Grid buildTable() {
		final Grid table = new Grid() {
			// @Override
			// protected String formatPropertyValue(final Object rowId,
			// final Object colId, final Property<?> property) {
			// String result = super.formatPropertyValue(rowId, colId,
			// property);
			// if (colId.equals("gebdat")) {
			// result = DATEFORMAT.format(((Date) property.getValue()));
			// } else if (colId.equals("price")) {
			// if (property != null && property.getValue() != null) {
			// return "$" + DECIMALFORMAT.format(property.getValue());
			// } else {
			// return "";
			// }
			// }
			// return result;
			// }
		};
		table.setSizeFull();
		
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		// table.setSelectable(true);

		// table.setColumnCollapsingAllowed(true);
		// table.setColumnCollapsible("vorName", false);
		// table.setColumnCollapsible("familienName", false);

		table.setColumnReorderingAllowed(true);

		table.setContainerDataSource(new TempTransactionsContainer(DataProvider
				.getMitgliederList()));
		// table.setContainerDataSource(DataProvider.getMitgliederList());
		table.sort(Sort.by("familienName", SortDirection.ASCENDING).then("vorName",SortDirection.ASCENDING));
		// table.setSortContainerPropertyId("vorname");
		// table.setSortAscending(false);

		// table.setColumnAlignment("seats", Align.RIGHT);
		// table.setColumnAlignment("price", Align.RIGHT);

		table.setColumnOrder("vorName", "familienName", "adresse");
		table.getColumn("vorName").setHeaderCaption("Vorname");
		
		table.getColumn("familienName").setHeaderCaption("Familienname");
		table.getColumn("adresse").setHeaderCaption("Adresse");
		table.setColumns("vorName", "familienName", "adresse");
		
		// table.setVisibleColumns("vorName", "familienName", "adresse");
		// "hunde"); // , "ort", "strasse",
		// "hausnummer");
		// table.setColumnHeaders("Vorname", "Nachname", "Adresse"); //,
		// "Hunde"); // ,
		// "Ort",
		// "Strasse",

		// "Hausnummer"); //, "Seats", "Price");

		// Allow dragging items to the reports menu
		// table.setDragMode(TableDragMode.MULTIROW);
		// table.setMultiSelect(false);

		// table.addActionHandler(new TransactionsActionHandler());
		// table.setNullSelectionAllowed(false);

		table.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {

					// Long movieId = (Long) item.getItemProperty("movieId")
					// .getValue();
					// MovieDetailsWindow.open(DashboardUI.getDataProvider()
					// .getMovie(movieId), null, null);
				}

			}

		});

		table.setImmediate(true);

		return table;
	}

	// TODO
	// private boolean defaultColumnsVisible() {
	// boolean result = true;
	// for (String propertyId : DEFAULT_COLLAPSIBLE) {
	// if (table.isColumnCollapsed(propertyId) == Page.getCurrent()
	// .getBrowserWindowWidth() < 800) {
	// result = false;
	// }
	// }
	// return result;
	// }

	// TODO
	// @Subscribe
	// public void browserResized(final BrowserResizeEvent event) {
	// // Some columns are collapsed when browser window width gets small
	// // enough to make the table fit better.
	// if (defaultColumnsVisible()) {
	// for (String propertyId : DEFAULT_COLLAPSIBLE) {
	// table.setColumnCollapsed(propertyId, Page.getCurrent()
	// .getBrowserWindowWidth() < 800);
	// }
	// }
	// }

	private boolean filterByProperty(final String prop, final Item item,
			final String text) {
		if (item == null || item.getItemProperty(prop) == null
				|| item.getItemProperty(prop).getValue() == null) {
			return false;
		}
		String val = item.getItemProperty(prop).getValue().toString().trim()
				.toLowerCase();
		if (val.contains(text.toLowerCase().trim())) {
			return true;
		}
		return false;
	}

	// void createNewReportFromSelection() {
	// UI.getCurrent().getNavigator()
	// .navigateTo(DashboardViewType.REPORTS.getViewName());
	// DashboardEventBus.post(new TransactionReportEvent(
	// (Collection<Transaction>) table.getValue()));
	// }

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	// private class TransactionsActionHandler implements Handler {
	// private final Action report = new Action("Create Report");
	//
	// private final Action discard = new Action("Discard");
	//
	// private final Action details = new Action("Movie details");
	//
	// @Override
	// public void handleAction(final Action action, final Object sender,
	// final Object target) {
	// if (action == report) {
	// createNewReportFromSelection();
	// } else if (action == discard) {
	// Notification.show("Not implemented in this demo");
	// } else if (action == details) {
	// Item item = ((Table) sender).getItem(target);
	// if (item != null) {
	// Long movieId = (Long) item.getItemProperty("movieId")
	// .getValue();
	// MovieDetailsWindow.open(DashboardUI.getDataProvider()
	// .getMovie(movieId), null, null);
	// }
	// }
	// }
	//
	// @Override
	// public Action[] getActions(final Object target, final Object sender) {
	// return new Action[] { details, report, discard };
	// }
	// }

	private class TempTransactionsContainer extends
			FilterableSortableListContainer<MitgliederListe> {

		public TempTransactionsContainer(
				final Collection<MitgliederListe> collection) {
			super(collection);
		}

		//
		// // This is only temporarily overridden until issues with
		// // BeanComparator get resolved.
		@Override
		public void sort(final Object[] propertyId, final boolean[] ascending) {
			System.out.println("in sort");
			if (ascending.length != 0) {
				final boolean sortAscending = ascending[0];
				final Object sortContainerPropertyId = propertyId[0];
				if (sortAscending) { System.out.println("true"); } else { System.out.println("false"); };
				
				Collections.sort(getBackingList(),
						new Comparator<MitgliederListe>() {
							@Override
							public int compare(final MitgliederListe o1,
									final MitgliederListe o2) {
								
								System.out.println("bin in compare");
								int result = 0;
								if ("vorName".equals(sortContainerPropertyId)) {
									result = o1.getVorName().compareTo(
											o2.getVorName());
								} else if ("familienName"
										.equals(sortContainerPropertyId)) {
									result = o1.getFamilienName().compareTo(
											o2.getFamilienName());
								}

								if (!sortAscending) {
									result *= -1;
								}
								return result;
							}
						});
			}
		}

	}

}
