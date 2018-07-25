package com.app.dashboard.view;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBConnection;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;

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
