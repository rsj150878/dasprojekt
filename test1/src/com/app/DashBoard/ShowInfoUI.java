package com.app.DashBoard;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.ShowKlassen;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.app.DashBoard.DashboardWidgetSet")
@Title("Showinfo")
@SuppressWarnings("serial")
public final class ShowInfoUI extends UI {

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */

	private TableQuery q1;
	private TableQuery q2;
	private TableQuery q3;

	private SQLContainer showContainer;
	private SQLContainer ringContainer;
	private SQLContainer hundContainer;

	Filter showFilter;
	Filter rassenFilter;
	ComboBox showSelect;
	ComboBox rassenSelect;
	Panel scrollPanel;

	// private final DataProvider dataProvider = new DummyDataProvider();
	private final DashBoardEventBus dashboardEventbus = new DashBoardEventBus();

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.GERMANY);

		Responsive.makeResponsive(this);
		// addStyleName(ValoTheme.UI_WITH_MENU);

		q1 = new TableQuery("schau", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		q2 = new TableQuery("schauring",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");

		q3 = new TableQuery("schauhund",
				DBConnection.INSTANCE.getConnectionPool());
		q3.setVersionColumn("version");

		try {
			showContainer = new SQLContainer(q1);

			ringContainer = new SQLContainer(q2);
			hundContainer = new SQLContainer(q3);

			hundContainer.addOrderBy(new OrderBy("sort_kat_nr", true));
			hundContainer.addOrderBy(new OrderBy("katalognummer", true));

		} catch (SQLException e) {

		}

		updateContent();

	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		FormLayout infoFormLayout = new FormLayout();
		infoFormLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		infoFormLayout.setWidth(50.0f, Unit.PERCENTAGE);
		infoFormLayout.setSpacing(true);
		infoFormLayout.setMargin(true);

		final ComboBox yearSelect = new ComboBox("Ausstellungsjahr");
		yearSelect.addItem(2016);
		yearSelect.setItemCaption(2016, "2016");

		infoFormLayout.addComponent(yearSelect);
		infoFormLayout.setComponentAlignment(yearSelect,
				Alignment.MIDDLE_CENTER);

		showSelect = new ComboBox("Schau");
		showSelect.setEnabled(false);
		showSelect.setContainerDataSource(showContainer);
		showSelect.setItemCaptionPropertyId("bezeichnung");

		infoFormLayout.addComponent(showSelect);

		rassenSelect = new ComboBox("Rasse");
		rassenSelect.setEnabled(false);

		rassenSelect
				.addItem(Rassen.GOLDEN_RETRIEVER.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(
				Rassen.GOLDEN_RETRIEVER.getRassenKurzBezeichnung(),
				Rassen.GOLDEN_RETRIEVER.getRassenLangBezeichnung());

		rassenSelect.addItem(Rassen.LABRADOR_RETRIEVER
				.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(
				Rassen.LABRADOR_RETRIEVER.getRassenKurzBezeichnung(),
				Rassen.LABRADOR_RETRIEVER.getRassenLangBezeichnung());

		rassenSelect.addItem(Rassen.CHESAPEAKE_BAY_RETRIEVER
				.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(
				Rassen.CHESAPEAKE_BAY_RETRIEVER.getRassenKurzBezeichnung(),
				Rassen.CHESAPEAKE_BAY_RETRIEVER.getRassenLangBezeichnung());

		rassenSelect.addItem(Rassen.FLAT_COATED_RETRIEVER
				.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(
				Rassen.FLAT_COATED_RETRIEVER.getRassenKurzBezeichnung(),
				Rassen.FLAT_COATED_RETRIEVER.getRassenLangBezeichnung());

		rassenSelect.addItem(Rassen.CURLY_COATED_RETRIEVER
				.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(
				Rassen.CURLY_COATED_RETRIEVER.getRassenKurzBezeichnung(),
				Rassen.CURLY_COATED_RETRIEVER.getRassenLangBezeichnung());

		rassenSelect.addItem(Rassen.NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER
				.getRassenKurzBezeichnung());
		rassenSelect.setItemCaption(Rassen.NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER
				.getRassenKurzBezeichnung(),
				Rassen.NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER
						.getRassenLangBezeichnung());

		infoFormLayout.addComponent(rassenSelect);

		mainLayout.addComponent(infoFormLayout);
		mainLayout.setExpandRatio(infoFormLayout, 1);

		mainLayout.setComponentAlignment(infoFormLayout,
				Alignment.MIDDLE_CENTER);

		scrollPanel = new Panel();
		scrollPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		scrollPanel.setSizeFull();
		scrollPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		mainLayout.addComponent(scrollPanel);
		mainLayout.setComponentAlignment(scrollPanel, Alignment.MIDDLE_CENTER);
		mainLayout.setExpandRatio(scrollPanel, 3);

		yearSelect.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("vlaue " + yearSelect.getValue());
				showContainer
						.addContainerFilter(new Between("datum",
								new GregorianCalendar((int) yearSelect
										.getValue(), 1, 1).getTime(),
								new GregorianCalendar((int) yearSelect
										.getValue(), 12, 31).getTime()));

				showSelect.setContainerDataSource(showContainer);
				showSelect.markAsDirty();
				showSelect.setEnabled(true);

			}

		});

		showSelect.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				rassenSelect.setEnabled(true);
				addShowFilter();

			}

		});

		rassenSelect.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				addRasseFilter();
				buildInfoArea();
			}
		});

		setContent(mainLayout);

	}

	private void addShowFilter() {

		if (!(showFilter == null)) {
			hundContainer.removeContainerFilter(showFilter);
		}

		Item showItem = showContainer.getItem(showSelect.getValue());
		showFilter = new Equal("idschau", showItem.getItemProperty("idschau")
				.getValue());

		hundContainer.addContainerFilter(showFilter);

	}

	private void addRasseFilter() {
		if (!(rassenFilter == null)) {
			hundContainer.removeContainerFilter(rassenFilter);
		}

		rassenFilter = new Equal("rasse", rassenSelect.getValue());
		hundContainer.addContainerFilter(rassenFilter);
	}

	private void buildInfoArea() {

		VerticalLayout infoLayout = new VerticalLayout();
		// infoLayout.setSizeUndefined();
		scrollPanel.setContent(infoLayout);

		if (hundContainer.size() == 0) {
			Label noDog = new Label();
			noDog.setContentMode(ContentMode.HTML);
			noDog.setValue("<center>Leider waren keine Hunde gemeldet</center>");
			infoLayout.addComponent(noDog);
			infoLayout.setComponentAlignment(noDog, Alignment.MIDDLE_CENTER);

		} else {

			String geschlecht = "";
			String currentKlasse = "";
			for (int i = 0; i < hundContainer.size(); i++) {
				Item currentItem = hundContainer.getItem(hundContainer
						.getIdByIndex(i));

				VerticalLayout hundLayout = new VerticalLayout();
				// hundLayout.setSizeFull();

				if (!geschlecht.equals(currentItem
						.getItemProperty("geschlecht").getValue())) {
					Label klassenLabel = new Label();
					klassenLabel.setContentMode(ContentMode.HTML);
					klassenLabel
							.setValue("<center>"
									+ (currentItem
											.getItemProperty("geschlecht")
											.getValue().toString().equals("R") ? "Rüden"
											: "Hündinnen") + "</center>");
					klassenLabel.addStyleName(ValoTheme.LABEL_H2);
					hundLayout.addComponent(klassenLabel);
					hundLayout.setComponentAlignment(klassenLabel,
							Alignment.TOP_CENTER);

					currentKlasse = currentItem.getItemProperty("geschlecht")
							.getValue().toString();
					geschlecht = currentItem.getItemProperty("geschlecht")
							.getValue().toString();
					currentKlasse = "";
				}

				if (!currentKlasse.equals(currentItem.getItemProperty("klasse")
						.getValue())) {
					Label klassenLabel = new Label();
					klassenLabel.setContentMode(ContentMode.HTML);
					System.out.println("klasse: "
							+ currentItem.getItemProperty("klasse").getValue()
									.toString().trim());
					klassenLabel
							.setValue("<center>"
									+ ShowKlassen
											.getLangBezeichnungFuerKurzBezeichnung(currentItem
													.getItemProperty("klasse")
													.getValue().toString()
													.trim()) + "</center>");
					klassenLabel.addStyleName(ValoTheme.LABEL_H2);
					hundLayout.addComponent(klassenLabel);
					hundLayout.setComponentAlignment(klassenLabel,
							Alignment.TOP_CENTER);

					currentKlasse = currentItem.getItemProperty("klasse")
							.getValue().toString();
				}

				Label hund = new Label();
				hund.setContentMode(ContentMode.HTML);
				String name = "<center><b>"
						+ currentItem.getItemProperty("katalognummer")
								.getValue() + "</b> "
						+ currentItem.getItemProperty("name").getValue()
						+ "</center>";
				hund.setValue(name.trim());
				hund.setReadOnly(true);
				hund.addStyleName(ValoTheme.LABEL_H3);
				hundLayout.addComponent(hund);
				hundLayout.setComponentAlignment(hund, Alignment.TOP_CENTER);

				Label zbnrWt = new Label();
				zbnrWt.setContentMode(ContentMode.HTML);
				zbnrWt.setValue("<center>"
						+ currentItem.getItemProperty("zuchtbuchnummer")
								.getValue()
						+ ", gew. am "
						+ new SimpleDateFormat("dd.MM.yyyy").format(currentItem
								.getItemProperty("wurftag").getValue())
						+ "</center>");
				hundLayout.addComponent(zbnrWt);
				hundLayout.setComponentAlignment(zbnrWt, Alignment.TOP_CENTER);

				Label besitzer = new Label();
				besitzer.setContentMode(ContentMode.HTML);
				besitzer.setValue("<center><b>Besitzer: </b>"
						+ currentItem.getItemProperty("besitzershow")
								.getValue() + "</center>");
				hundLayout.addComponent(besitzer);
				hundLayout
						.setComponentAlignment(besitzer, Alignment.TOP_CENTER);

				Label zuechter = new Label();
				zuechter.setContentMode(ContentMode.HTML);
				zuechter.setValue("<center><b>Züchter: </b>"
						+ currentItem.getItemProperty("zuechter").getValue()
						+ "</center>");
				hundLayout.addComponent(zuechter);
				hundLayout
						.setComponentAlignment(zuechter, Alignment.TOP_CENTER);

				Label vater = new Label();
				vater.setContentMode(ContentMode.HTML);
				vater.setValue("<center><b>Vater: </b>"
						+ currentItem.getItemProperty("vater").getValue()
						+ "</center>");
				hundLayout.addComponent(vater);
				hundLayout.setComponentAlignment(vater, Alignment.TOP_CENTER);

				Label mutter = new Label();
				mutter.setContentMode(ContentMode.HTML);
				mutter.setValue("<center><b>Mutter: </b>"
						+ currentItem.getItemProperty("mutter").getValue()
						+ "</center>");
				hundLayout.addComponent(mutter);
				hundLayout.setComponentAlignment(mutter, Alignment.TOP_CENTER);

				// System.out.println("katalognummer" + currentItem.getItemPro)
				TextArea beschreibung = new TextArea();
				beschreibung.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				beschreibung.setWidth(50.f, Unit.PERCENTAGE);

				if (!(currentItem.getItemProperty("hundfehlt").getValue() == null)) {
					if (currentItem.getItemProperty("hundfehlt").getValue()
							.toString().equals("J"))
						beschreibung.setValue("Hund fehlt");
					else
						beschreibung.setValue(""
								+ currentItem.getItemProperty("bewertung")
										.getValue());
				}

				StringBuilder sb = new StringBuilder();
				String bewertung = "";

				if (!(currentItem.getItemProperty("hundfehlt").getValue() == null)
						&& currentItem.getItemProperty("hundfehlt").getValue()
								.equals("N")
						&& !(currentItem.getItemProperty("formwert").getValue() == null)) {

					switch (currentItem.getItemProperty("formwert").getValue()
							.toString()) {
					case "vv":
						sb.append("vielversprechend");
						break;
					case "v":
						if (currentItem.getItemProperty("klasse").getValue()
								.toString().equals("JÜ"))
							sb.append("versprechend");
						else
							sb.append("V");
						break;
					case "sg":
						sb.append("SG");
						break;
					case "g":
						sb.append("gut");
						break;
					case "gen":
						sb.append("genügend");
						break;
					case "ob":
						sb.append("ohne bewertung");
						break;
					default:
						sb.append("");

					}
					if (!(currentItem.getItemProperty("platzierung").getValue() == null))
						sb.append(currentItem.getItemProperty("platzierung")
								.getValue());

					if (!(currentItem.getItemProperty("CACA").getValue() == null)) {
						if (currentItem.getItemProperty("CACA").getValue()
								.equals("J"))
							sb.append(", JB");
						else
							sb.append(currentItem.getItemProperty("CACA")
									.getValue().equals("C") ? ", CACA"
									: ", Res.CACA");
					}

					if (!(currentItem.getItemProperty("CACIB").getValue() == null)) {
						sb.append(currentItem.getItemProperty("CACIB")
								.getValue().equals("C") ? ", CACIB"
								: ", Res.CACIB");
					}

					if (!(currentItem.getItemProperty("BOB").getValue() == null)) {
						sb.append(currentItem.getItemProperty("BOB").getValue()
								.equals("B") ? ", BOB" : ", BOS");
					}
					// sb.append((!(currentItem.getItemProperty("CACA").getValue()
					// == null &&))

				}

				Label formwert = new Label();
				formwert.setContentMode(ContentMode.HTML);
				formwert.setValue("<center><b>Bewertung: </b>" + sb.toString()
						+ "</center>");
				hundLayout.addComponent(formwert);
				hundLayout
						.setComponentAlignment(formwert, Alignment.BOTTOM_CENTER);

				beschreibung.setReadOnly(true);
				hundLayout.addComponent(beschreibung);
				hundLayout.setComponentAlignment(beschreibung,
						Alignment.MIDDLE_CENTER);

				infoLayout.addComponent(hundLayout);
				infoLayout.setComponentAlignment(hundLayout,
						Alignment.MIDDLE_CENTER);
			}
		}

	}
	/**
	 * @return An instance for accessing the (dummy) services layer.
	 */
	// public static DataProvider getDataProvider() {
	// return ((DashboardUI) getCurrent()).dataProvider;
	// }

}