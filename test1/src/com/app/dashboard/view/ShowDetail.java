package com.app.dashboard.view;

import com.app.component.ShowInfoForm;
import com.app.dbio.DBConnection;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.filter.Or;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.v7.event.SelectionEvent.SelectionListener;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Grid.SelectionMode;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;

public class ShowDetail extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Item currentShowItem;
	private VerticalLayout mainLayout;

	private TableQuery q2;
	private TableQuery q3;

	private final ShowDetailListener listener;

	public ShowDetail(Item currentShowItem, ShowDetailListener listener) {

		// TODO add user code here

		this.currentShowItem = currentShowItem;
		this.listener = listener;
		buildMainLayout();

		Panel mainPanel = new Panel();
		mainPanel.setContent(mainLayout);
		mainPanel.setSizeFull();
		mainPanel.setStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		setCompositionRoot(mainPanel);

	}

	private void buildMainLayout() {

		q2 = new TableQuery("veranstaltungs_stufe", DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");

		q3 = new TableQuery("veranstaltungs_teilnehmer", DBConnection.INSTANCE.getConnectionPool());
		q3.setVersionColumn("version");

		// common part: create layout
		mainLayout = new VerticalLayout();

		mainLayout.setMargin(true);

		Component secondPanel = createSecondPanel();
		mainLayout.addComponent(secondPanel);
		mainLayout.setExpandRatio(secondPanel, 1);

		Component thirdPanel = createThirdPanel();
		mainLayout.addComponent(thirdPanel);
		mainLayout.setExpandRatio(thirdPanel, 2);

	}

	private Panel createSecondPanel() {

		Panel secondLine = new Panel();
		secondLine.addStyleName(ValoTheme.PANEL_BORDERLESS);

		VerticalLayout secondLineLayout = new VerticalLayout();

		secondLineLayout.setWidth("100%");
		secondLineLayout.setHeight("100%");
		secondLineLayout.setResponsive(true);
		secondLineLayout.setSpacing(true);

		final TextField nameVeranstaltung = new TextField("Veranstaltungsname");
		nameVeranstaltung.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		nameVeranstaltung.setWidth("100%");
		;
		nameVeranstaltung.setMaxLength(90);
		nameVeranstaltung.setPropertyDataSource(currentShowItem.getItemProperty("bezeichnung"));

		secondLineLayout.addComponent(nameVeranstaltung);

		HorizontalLayout infoZeilenLayout = new HorizontalLayout();
		infoZeilenLayout.setSpacing(true);
		infoZeilenLayout.setResponsive(true);
		infoZeilenLayout.setWidth("100%");

		final PopupDateField datumVeranstaltung = new PopupDateField("Datum der Veranstaltung");
		datumVeranstaltung.setPropertyDataSource(currentShowItem.getItemProperty("datum"));
		datumVeranstaltung.setImmediate(true);

		datumVeranstaltung.setDateFormat("dd.MM.yyyy");
		infoZeilenLayout.addComponent(datumVeranstaltung);

		final OptionGroup showType = new OptionGroup("Typ Schau");
		showType.addItem("C");
		showType.setItemCaption("C", "Clubschau");
		showType.addItem("I");
		showType.setItemCaption("I", "Internat. Ausstellung");
		showType.setPropertyDataSource(currentShowItem.getItemProperty("schautyp"));
		showType.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		showType.setEnabled(false);

		infoZeilenLayout.addComponent(showType);

		secondLineLayout.addComponent(infoZeilenLayout);

		secondLine.setContent(secondLineLayout);

		return secondLine;
	}

	private Panel createThirdPanel() {

		Panel thirdLine = new Panel();
		thirdLine.addStyleName(ValoTheme.PANEL_BORDERLESS);

		Accordion thirdLineAccordion = new Accordion();
		thirdLineAccordion.addStyleName(ValoTheme.ACCORDION_BORDERLESS);

		thirdLineAccordion.addTab(createAllgemeinTab(), "Allgemein/Ehrenring");
		thirdLineAccordion.addTab(buildBreedPanelForShow("GR"), "Golden Retriever");

		thirdLine.setContent(thirdLineAccordion);

		return thirdLine;
	}

	private VerticalLayout createAllgemeinTab() {
		VerticalLayout thirdLineLayout = new VerticalLayout();

		thirdLineLayout.setWidth("100%");
		thirdLineLayout.setHeight("100%");
		thirdLineLayout.setResponsive(true);
		thirdLineLayout.setSpacing(true);

		thirdLineLayout.addComponent(createAllgemeinComponent("BIS", "B", "BIS", null, null));
		thirdLineLayout.addComponent(createAllgemeinComponent("Res.-BIS", "R", "BIS", null, null));
		thirdLineLayout.addComponent(createAllgemeinComponent("BOD", "B", "BOD", null, null));
		thirdLineLayout.addComponent(createAllgemeinComponent("Res.-BOD", "R", "BOD", null, null));
		thirdLineLayout.addComponent(createAllgemeinComponent("3. -BOD", "3", "BOD", null, null));

		if (currentShowItem.getItemProperty("schautyp").getValue().toString().equals("I")) {
			TabSheet ehrenRingPlatzierungen = new TabSheet();
			ehrenRingPlatzierungen.addTab(createEhrenRingTab("JÜ", "platzehrenring"), "Jüngsten");
			ehrenRingPlatzierungen.addTab(createEhrenRingTab("JU", "platzehrenring"), "Jugend");
			ehrenRingPlatzierungen.addTab(createEhrenRingTab("GRP", "platzehrenringgruppe"), "Gruppe 8");
			ehrenRingPlatzierungen.addTab(createEhrenRingTab("VE", "platzehrenring"), "Veteran");

			thirdLineLayout.addComponent(ehrenRingPlatzierungen);
		} else {
			thirdLineLayout.addComponent(createAllgemeinComponent("Best Baby", "A", "bestehrenring", null, null));
			thirdLineLayout.addComponent(createAllgemeinComponent("Bester Jüngster", "J", "bestehrenring", "JÜ", null));
			thirdLineLayout.addComponent(createAllgemeinComponent("Bester Junghund", "B", "bestehrenring", "JU", null));
			thirdLineLayout
					.addComponent(createAllgemeinComponent("Bester Gebrauchshund", "G", "bestehrenring", "GB", null));
			thirdLineLayout.addComponent(createAllgemeinComponent("Bester Veteran", "V", "bestehrenring", "VE", null));

		}

		return thirdLineLayout;
	}

	private VerticalLayout createEhrenRingTab(String klasse, String fieldName) {
		VerticalLayout ehrenRingLayout = new VerticalLayout();
		ehrenRingLayout.setWidth("100%");
		ehrenRingLayout.setHeight("100%");
		ehrenRingLayout.setResponsive(true);
		ehrenRingLayout.setSpacing(true);
		;

		ehrenRingLayout.addComponent(createAllgemeinComponent("1. Platz", "1", fieldName, klasse, null));
		ehrenRingLayout.addComponent(createAllgemeinComponent("2. Platz", "2", fieldName, klasse, null));
		ehrenRingLayout.addComponent(createAllgemeinComponent("3. Platz", "3", fieldName, klasse, null));
		ehrenRingLayout.addComponent(createAllgemeinComponent("4. Platz", "4", fieldName, klasse, null));
		ehrenRingLayout.addComponent(createAllgemeinComponent("5. Platz", "5", fieldName, klasse, null));

		return ehrenRingLayout;
	}

	private Component createAllgemeinComponent(String fieldName, final String dbValue, final String dbField,
			final String klasse, final String breed) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setHeight("100%");
		layout.setResponsive(true);
		layout.setSpacing(true);

		Label fieldNameLabel = new Label(fieldName);
		fieldNameLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		fieldNameLabel.addStyleName(ValoTheme.LABEL_SMALL);

		layout.addComponent(fieldNameLabel);
		layout.setExpandRatio(fieldNameLabel, 1);

		final TextField inputField = new TextField();
		inputField.setImmediate(true);
		inputField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		layout.addComponent(inputField);
		layout.setExpandRatio(inputField, 1);

		final Label dogName = new Label();
		dogName.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		dogName.addStyleName(ValoTheme.LABEL_SMALL);

		inputField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				TableQuery query = new TableQuery("schauhund", DBConnection.INSTANCE.getConnectionPool());

				try {
					SQLContainer resultContainer = new SQLContainer(query);
					resultContainer.addContainerFilter(
							new Equal("idschau", currentShowItem.getItemProperty("idschau").getValue().toString()));

					Filter q = new Equal(dbField, dbValue);
					resultContainer.addContainerFilter(q);

					Filter klassenFilter = null;

					if (!(klasse == null)) {
						if (klasse.equals("GRP")) {
							klassenFilter = new Or(new Equal("klasse", "JU"), new Equal("klasse", "ZK"), new Equal("klasse", "OF"),
									new Equal("klasse", "GK"), new Equal("klasse", "CH"), new Equal("klasse", "VE"));
						} else {
							klassenFilter = new Equal("klasse", klasse);
						}
						resultContainer.addContainerFilter(klassenFilter);

					}

					if (resultContainer.size() > 0) {
						resultContainer.getItem(resultContainer.firstItemId()).getItemProperty(dbField).setValue(null);

					}

					resultContainer.removeContainerFilter(q);

					if (!(klasse == null)) {
						resultContainer.removeContainerFilter(klassenFilter);
					}

					resultContainer.addContainerFilter(new Equal("katalognummer", inputField.getValue()));

					String dogNameString = "";
					if (resultContainer.size() > 0) {

						dogNameString = resultContainer.getItem(resultContainer.firstItemId()).getItemProperty("name")
								.getValue().toString() + " ("
								+ resultContainer.getItem(resultContainer.firstItemId())
										.getItemProperty("zuchtbuchnummer").getValue().toString()
								+ ")";

						resultContainer.getItem(resultContainer.firstItemId()).getItemProperty(dbField)
								.setValue(dbValue);

					}

					dogName.setValue(dogNameString);
					resultContainer.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		TableQuery query = new TableQuery("schauhund", DBConnection.INSTANCE.getConnectionPool());

		try {
			SQLContainer resultContainer = new SQLContainer(query);
			resultContainer.addContainerFilter(
					new Equal("idschau", currentShowItem.getItemProperty("idschau").getValue().toString()));
			resultContainer.addContainerFilter(new Equal(dbField, dbValue));

			Filter klassenFilter = null;
			if (!(klasse == null)) {
				if (klasse.equals("GRP")) {
					klassenFilter = new Or(new Equal("klasse", "JU"), new Equal("klasse", "ZK"), new Equal("klasse", "OF"),
							new Equal("klasse", "GK"), new Equal("klasse", "CH"), new Equal("klasse", "VE"));
				} else {
					klassenFilter = new Equal("klasse", klasse);
				}
				resultContainer.addContainerFilter(klassenFilter);

			}

			if (resultContainer.size() > 0) {

				inputField.setValue(resultContainer.getItem(resultContainer.firstItemId())
						.getItemProperty("katalognummer").getValue().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		layout.addComponent(dogName);
		layout.setExpandRatio(dogName, 3);

		return layout;
	}

	private Component buildBreedPanelForShow(String breed) {

		VerticalLayout mainBreedLayout = new VerticalLayout();
		mainBreedLayout.setSizeFull();
		mainBreedLayout.setResponsive(true);
		mainBreedLayout.setSpacing(true);

		TabSheet klassen = new TabSheet();
		klassen.addTab(buildBreedClass(breed, "R", "WE"), "Rüden Welpen");
		klassen.addTab(buildBreedClass(breed, "R", "JÜ"), "Rüden Jüngsten");
		mainBreedLayout.addComponent(klassen);
		return mainBreedLayout;

	}

	public Component buildBreedClass(String breed, String sex, String showClass) {

		HorizontalLayout mainBreedClassLayout = new HorizontalLayout();

		try {

			// mainBreedClassLayout.setWidth("100%");
			// mainBreedClassLayout.setHeight("100%");
			mainBreedClassLayout.setSizeUndefined();
			mainBreedClassLayout.setResponsive(true);
			mainBreedClassLayout.setSpacing(true);
			TableQuery query = new TableQuery("schauhund", DBConnection.INSTANCE.getConnectionPool());

			final SQLContainer resultContainer = new SQLContainer(query);

			resultContainer.addContainerFilter(
					new Equal("idschau", currentShowItem.getItemProperty("idschau").getValue().toString()));
			resultContainer.addContainerFilter(new Equal("rasse", breed));
			resultContainer.addContainerFilter(new Equal("geschlecht", sex));
			resultContainer.addContainerFilter(new Equal("klasse", showClass));
			resultContainer.addOrderBy(new OrderBy("sort_kat_nr", true));

			final Grid breedGrid = new Grid();
			breedGrid.setContainerDataSource(resultContainer);
			breedGrid.setColumns("katalognummer", "name");
			breedGrid.setSelectionMode(SelectionMode.SINGLE);
			breedGrid.getColumn("katalognummer").setHeaderCaption("katnr");

			mainBreedClassLayout.addComponent(breedGrid);
			//mainBreedClassLayout.setExpandRatio(breedGrid, 1);

			final ShowInfoForm showForm = new ShowInfoForm(showClass);
			mainBreedClassLayout.addComponent(showForm);
			//mainBreedClassLayout.setExpandRatio(showForm, 2);

			breedGrid.addSelectionListener(new SelectionListener() {


				@Override
				public void select(com.vaadin.v7.event.SelectionEvent event) {
					showForm.setDataSource(resultContainer.getItem(breedGrid.getSelectedRow()));
	
				}

			});
		} catch (Exception e) {
			e.printStackTrace();

		}

		return mainBreedClassLayout;

	}

	public void setTitle(String title) {

		listener.titleChanged(title, ShowDetail.this);
	}

	public interface ShowDetailListener {
		void titleChanged(String newTitle, ShowDetail detail);
	}

	
}
