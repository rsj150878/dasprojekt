package com.app.DashBoard.View;

import java.util.Iterator;

import com.app.DashBoard.Component.HundeSchulStundeUebersichtGrid;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoardWindow.SearchWindow;
import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class KursUebersicht extends Panel implements View {

	private TableQuery kursQuery;
	private TableQuery kursTagQuery;

	private SQLContainer kursContainer;
	private SQLContainer kursTagContainer;

	private ComboBox kursAuswahl;

	private TabSheet sheet;

	private VerticalLayout root;

	public KursUebersicht() {

		setSizeFull();
		// addStyleName("mitglieder");
		DashBoardEventBus.register(this);

		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("mitglieder");
		setContent(root);
		Responsive.makeResponsive(root);

		kursQuery = new TableQuery("kurs",
				DBConnection.INSTANCE.getConnectionPool());
		kursQuery.setVersionColumn("version");

		kursTagQuery = new TableQuery("kurstag",
				DBConnection.INSTANCE.getConnectionPool());
		kursTagQuery.setVersionColumn("version");

		try {
			kursContainer = new SQLContainer(kursQuery);
			kursTagContainer = new SQLContainer(kursTagQuery);

		} catch (Exception e) {
			Notification.show("fehler beim aufbau der Container");
			e.printStackTrace();
		}

		root.addComponent(buildToolbar());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

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

		Label title = new Label("Kursübersicht");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		kursAuswahl = new ComboBox();
		kursAuswahl.setItemCaptionPropertyId("kursbezeichnung");
		kursAuswahl.setSizeUndefined();
		kursAuswahl.addStyleName(ValoTheme.COMBOBOX_SMALL);
		kursAuswahl.addStyleName(ValoTheme.LABEL_H1);
		kursAuswahl.setContainerDataSource(kursContainer);
		header.addComponent(kursAuswahl);

		kursAuswahl.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				buildInfoPanel();

			}

		});

		return header;
	}

	private void buildInfoPanel() {

		if (!(sheet == null)) {
			root.removeComponent(sheet);
		}

		kursTagContainer.removeAllContainerFilters();

		Item item = kursContainer.getItem(kursAuswahl.getValue());
		kursTagContainer.addContainerFilter(new Equal("idkurs", item
				.getItemProperty("idkurs").getValue()));

		sheet = new TabSheet();
		sheet.setSizeFull();
		sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		for (Object o : kursTagContainer.getItemIds()) {
			Item tagItem = kursTagContainer.getItem(o);
			sheet.addTab(new KursTagPanel(tagItem),
					tagItem.getItemProperty("bezeichnung").getValue()
							.toString());

		}

		root.addComponent(sheet);
		root.setExpandRatio(sheet, 1);

	}

}
