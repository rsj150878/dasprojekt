package com.app.DashBoardWindow;

import java.sql.SQLException;

import com.app.DashBoard.Event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.DashBoard.Event.DashBoardEvent.SearchEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.filter.SimpleStringFilter;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.v7.ui.Grid.HeaderCell;
import com.vaadin.v7.ui.Grid.HeaderRow;

public class SearchWindow extends Window {

	public static final String ID = "hundesuchwindow";

	private Grid searchGrid;

	private SQLContainer dogContainer;
	private TableQuery dogQuery;

	private SQLContainer personContainer;
	private TableQuery personQuery;

	public SearchWindow() {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);

		setResizable(false);
		setClosable(false);
		setHeight(100.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		// content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		dogQuery = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		personQuery = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());

		try {
			dogContainer = new SQLContainer(dogQuery);
			personContainer = new SQLContainer(personQuery);
			dogContainer.addReference(personContainer, "idperson", "idperson");
		} catch (SQLException e) {
			Notification.show("Fehler beim Aufbau des SQLContainers");
			e.printStackTrace();

		}

		Component x = buildSearchGrid();
		content.addComponent(x);
		content.setExpandRatio(x, 1);

		content.addComponent(buildFooter());

	}

	private Component buildFooter() {
		final HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		//
		Button abbruch = new Button("Abbruch");
		abbruch.addStyleName(ValoTheme.BUTTON_PRIMARY);
		abbruch.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// Updated user should also be persisted to database. But
				// not in this demo.
				DashBoardEventBus.post(new SearchEvent(null));

				close();

			}
		});
		footer.addComponent(abbruch);

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// Updated user should also be persisted to database. But
				// not in this demo.

				Item selectedItem = (Item) searchGrid.getContainerDataSource().getItem(searchGrid.getSelectedRow());
				DashBoardEventBus.post(new SearchEvent(new Integer(
						selectedItem.getItemProperty("idhund").getValue()
								.toString())));
				close();

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		return footer;
	}

	private Component buildSearchGrid() {

		Panel searchPanel = new Panel();
		searchPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		searchPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		searchPanel.setSizeFull();

		VerticalLayout panelLayout = new VerticalLayout();
		searchPanel.setContent(panelLayout);

		searchGrid = new Grid();

		searchGrid.setSizeFull();
		searchGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		searchGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		searchGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		final GeneratedPropertyContainer cpContainer = new GeneratedPropertyContainer(
				dogContainer);

		cpContainer.addGeneratedProperty("besitzer",
				new PropertyValueGenerator<String>() {
					private static final long serialVersionUID = -1636752705984592807L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {
						personContainer.removeAllContainerFilters();
						personContainer.addContainerFilter(new Equal(
								"idperson", item.getItemProperty("idperson")
										.getValue()));
						Item personItem = personContainer
								.getItem(personContainer.getIdByIndex(0));
						return personItem.getItemProperty("nachname")
								.getValue().toString()
								+ " "
								+ personItem.getItemProperty("vorname")
										.getValue().toString();
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});

		searchGrid.setContainerDataSource(cpContainer);
		searchGrid.setColumns("zwingername", "rufname", "besitzer");

		HeaderRow filterRow = searchGrid.appendHeaderRow();

		// Set up a filter for all columns
		HeaderCell cell = filterRow.getCell("zwingername");
		cell.setComponent(buildFilter("zwingername", cpContainer));

		cell = filterRow.getCell("rufname");
		cell.setComponent(buildFilter("rufname", cpContainer));

		panelLayout.addComponent(searchGrid);
		return searchPanel;
	}

	private Component buildFilter(final String filterName,
			final GeneratedPropertyContainer cpContainer) {
		// Have an input field to use for filter
		TextField filterField = new TextField();
		filterField.setColumns(8);
		filterField.setInputPrompt("Filter");
		filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);

		// Update filter When the filter input is changed
		filterField.addTextChangeListener(new TextChangeListener() {

			Filter filter;

			@Override
			public void textChange(TextChangeEvent event) {
				// TODO Auto-generated method stub
				if (!(filter == null)) {
					cpContainer.removeContainerFilter(filter);
				}
				//
				// (Re)create the filter if necessary
				if (!event.getText().isEmpty()) {
					filter = new SimpleStringFilter(filterName,
							event.getText(), true, false);
					cpContainer.addContainerFilter(filter);
				}

			}
			// Can't modify filters so need to replace
		});

		return filterField;

	}

	public static void open() {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new SearchWindow();
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
