package com.app.DashBoard.View;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.app.Auth.DataProvider;
import com.app.Auth.MitgliederListe;
import com.app.Auth.Person;
import com.app.DashBoard.Event.DashBoardEvent.BrowserResizeEvent;
import com.app.DashBoard.Event.DashBoardEvent.UpdateUserEvent;
import com.app.DashBoard.Event.DashBoardEvent.UserNewEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoardWindow.FilterableSortableListContainer;
import com.app.DashBoardWindow.HundeDetailWindow;
import com.app.DashBoardWindow.ProfilePreferencesWindow;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.sort.Sort;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
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
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class MitgliederView extends VerticalLayout implements View {

	private final Grid table;
	private Button neuesMitglied;
	private TempTransactionsContainer mitgliederListe;

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
				Person person = new Person();
				ProfilePreferencesWindow.open(person, false);
				System.out.println(" nach open ");

			}
		});

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
		final Grid gridTable = new Grid();
		gridTable.setSizeFull();

		gridTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		gridTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		gridTable.addStyleName(ValoTheme.TABLE_COMPACT);

		gridTable.setColumnReorderingAllowed(true);

		mitgliederListe = new TempTransactionsContainer(
				DataProvider.getMitgliederList());

		//gridTable.setContainerDataSource(mitgliederListe);

		GeneratedPropertyContainer gpc =
			    new GeneratedPropertyContainer(mitgliederListe);
		gpc.addGeneratedProperty("edit",
			    new PropertyValueGenerator<String>() {

			    @Override
			    public String getValue(Item item, Object itemId,
			                           Object propertyId) {
			        return "Edit"; // The caption
			    }

			    @Override
			    public Class<String> getType() {
			        return String.class;
			    }
			});
			
		gpc.addGeneratedProperty("hundeforbutton",
			    new PropertyValueGenerator<String>() {

			    @Override
			    public String getValue(Item item, Object itemId,
			                           Object propertyId) {
			        return "Hunde"; // The caption
			    }

			    @Override
			    public Class<String> getType() {
			        return String.class;
			    }
			});
		gridTable.setContainerDataSource(gpc);

		gridTable.sort(Sort.by("familienName", SortDirection.ASCENDING).then(
				"vorName", SortDirection.ASCENDING));

		gridTable.setColumnOrder("vorName", "familienName", "adresse", "edit",
				"hundeforbutton");
		gridTable.getColumn("vorName").setHeaderCaption("Vorname");

		gridTable.getColumn("familienName").setHeaderCaption("Familienname");
		gridTable.getColumn("adresse").setHeaderCaption("Adresse");
		//gridTable.getColumn("edit").setHeaderCaption("Hunde");
		gridTable.setColumns("familienName","vorName", "adresse", "edit", "hundeforbutton");
		gridTable.getColumn("edit").setHeaderCaption("bearb");
		gridTable.getColumn("hundeforbutton").setHeaderCaption("Hunde");

		gridTable.getDefaultHeaderRow().join("edit", "hundeforbutton")
				.setText("Tools");

		gridTable.setFrozenColumnCount(2);

		gridTable.getColumn("edit").setRenderer(
				new ButtonRenderer(new RendererClickListener() {

					@Override
					public void click(RendererClickEvent event) {
						MitgliederListe zw = (MitgliederListe) event
								.getItemId();
						ProfilePreferencesWindow.open(zw.getPerson(), true);

					}

				}));
		
		gridTable.getColumn("hundeforbutton").setRenderer(
				new ButtonRenderer(new RendererClickListener() {

					@Override
					public void click(RendererClickEvent event) {
						MitgliederListe zw = (MitgliederListe) event
								.getItemId();
						HundeDetailWindow.open(zw.getPerson(),
								zw.getHunde());

					}

				}));

//		gridTable
//				.getColumn("edit")
//				.setRenderer(
//						new EditButtonValueRenderer(
//								new RendererClickListener() {
//
//									@Override
//									public void click(
//											final RendererClickEvent event) {
//
//										// System.out.println("item: " +
//										// event.getItem());
//										MitgliederListe zw = (MitgliederListe) event
//												.getItemId();
//										ProfilePreferencesWindow.open(
//												zw.getPerson(), true);
//									}
//								})).setWidth(80);
//
//		gridTable
//				.getColumn("hundeforbutton")
//				.setRenderer(
//						new EditButtonValueRenderer(
//								new RendererClickListener() {
//
//									@Override
//									public void click(
//											final RendererClickEvent event) {
//										MitgliederListe zw = (MitgliederListe) event
//												.getItemId();
//										HundeDetailWindow.open(zw.getPerson(),
//												zw.getHunde());
//									}
//								})).setWidth(80);
//
//		gridTable.setCellStyleGenerator(new CellStyleGenerator() {
//			@Override
//			public String getStyle(final CellReference cellReference) {
//				if (cellReference.getPropertyId().equals("edit")) {
//					return "link-icon-user";
//				}
//				if (cellReference.getPropertyId().equals("hundeforbutton"))
//					return "link-icon-hund";
//				else {
//					return null;
//				}
//			}
//		});
//		gridTable.addItemClickListener(new ItemClickListener() {
//
//			@Override
//			public void itemClick(ItemClickEvent event) {
//				if (event.isDoubleClick()) {
//
//					// Long movieId = (Long) item.getItemProperty("movieId")
//					// .getValue();
//					// MovieDetailsWindow.open(DashboardUI.getDataProvider()
//					// .getMovie(movieId), null, null);
//				}
//
//			}
//
//		});

		gridTable.setImmediate(true);

		return gridTable;
	}

	@Subscribe
	public void newUserEvent(UserNewEvent event) {
		System.out.println("in new event");
		MitgliederListe zw = new MitgliederListe();
		zw.setPerson(event.getPerson());
		mitgliederListe.addItem(zw);

	}

	@Subscribe
	public void updateUserEvent(UpdateUserEvent event) {
		mitgliederListe.update();
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
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		if (Page.getCurrent().getBrowserWindowWidth() < 800) {
			table.removeColumn("adresse");

		}
	}

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

		public void update() {
			fireItemSetChange();
		}

		//
		// // This is only temporarily overridden until issues with
		// // BeanComparator get resolved.
		@Override
		public void sort(final Object[] propertyId, final boolean[] ascending) {
			if (ascending.length != 0) {
				final boolean sortAscending = ascending[0];
				final Object sortContainerPropertyId = propertyId[0];
			
				Collections.sort(getBackingList(),
						new Comparator<MitgliederListe>() {
							@Override
							public int compare(final MitgliederListe o1,
									final MitgliederListe o2) {

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
						}

				);
				fireItemSetChange();
			}
		}

	}

}
