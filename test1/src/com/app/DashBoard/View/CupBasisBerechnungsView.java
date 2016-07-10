package com.app.DashBoard.View;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.ShowKlassen;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Compare.GreaterOrEqual;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CupBasisBerechnungsView extends VerticalLayout implements View,
		Button.ClickListener {

	private Button startButton;

	public CupBasisBerechnungsView() {
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());
		addComponent(buildWorkingArea());

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
		VerticalLayout mainLayout = new VerticalLayout();

		startButton = new Button("Berechne...");
		mainLayout.addComponent(startButton);

		startButton.addClickListener(this);

		return (mainLayout);

	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub

		try {
			TableQuery q1 = new TableQuery("schauhund",
					DBConnection.INSTANCE.getConnectionPool());
			q1.setVersionColumn("version");

			TableQuery q2 = new TableQuery("schau",
					DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");

			TableQuery q3 = new TableQuery("oerc_hund",
					DBConnection.INSTANCE.getConnectionPool());
			q3.setVersionColumn("version");

			TableQuery q4 = new TableQuery("ausstellungscup_basis",
					DBConnection.INSTANCE.getConnectionPool());
			q4.setVersionColumn("version");

			SQLContainer schauContainer = new SQLContainer(q2);
			schauContainer.addContainerFilter(new GreaterOrEqual("datum",
					new GregorianCalendar(2016, 1, 1).getTime()));

			SQLContainer schauHundContainer = new SQLContainer(q1);

			SQLContainer oercHundContainer = new SQLContainer(q3);

			SQLContainer basisContainer = new SQLContainer(q4);

			for (Object schauId : schauContainer.getItemIds()) {
				schauHundContainer.addContainerFilter(new Equal("idschau",
						schauContainer.getItem(schauId)
								.getItemProperty("idschau").getValue()));

				System.out.println("schau");

				int gefunden = 0;
				for (Object schauHundId : schauHundContainer.getItemIds()) {
					Pattern p = Pattern.compile("(^|\\s)([0-9]+)($|\\s)");
					String zuchtBuchNummer = schauHundContainer
							.getItem(schauHundId)
							.getItemProperty("zuchtbuchnummer").getValue()
							.toString();
					Matcher m = p.matcher(zuchtBuchNummer);
					System.out.println("Hund");
					if (m.find()) {

						// System.out.println("zuchtbuchnummer " +
						// zuchtBuchNummer.substring(m.start(),
						// m.end()).trim());

						String zbnrFilterValue = schauHundContainer
								.getItem(schauHundId).getItemProperty("rasse")
								.getValue().toString()
								+ "%"
								+ zuchtBuchNummer.substring(m.start(), m.end())
										.trim();

						oercHundContainer.addContainerFilter(new Like("zbnr",
								zbnrFilterValue));

						if (oercHundContainer.size() > 0) {

							basisContainer.addContainerFilter(new Equal(
									"idschau", schauContainer.getItem(schauId)
											.getItemProperty("idschau")
											.getValue()));
							basisContainer.addContainerFilter(new Equal(
									"idschauhund", schauHundContainer
											.getItem(schauHundId)
											.getItemProperty("idschauhund")
											.getValue()));

							Item basisContainerItem = null;

							if (basisContainer.size() == 0) {
								basisContainerItem = basisContainer
										.getItemUnfiltered(basisContainer
												.addItem());

							} else {
								basisContainerItem = basisContainer
										.getItem(basisContainer.firstItemId());
							}

							basisContainerItem.getItemProperty("idschau")
									.setValue(
											schauContainer.getItem(schauId)
													.getItemProperty("idschau")
													.getValue());

							basisContainerItem.getItemProperty("idschauhund")
									.setValue(
											schauHundContainer
													.getItem(schauHundId)
													.getItemProperty(
															"idschauhund")
													.getValue());

							basisContainerItem
									.getItemProperty("oercid")
									.setValue(
											oercHundContainer
													.getItem(
															oercHundContainer
																	.firstItemId())
													.getItemProperty("hundenr")
													.getValue());

							basisContainerItem.getItemProperty("punkte")
									.setValue(
											rechnePunkte(schauHundContainer
													.getItem(schauHundId)));

							basisContainer.commit();
							basisContainer.removeAllContainerFilters();

						}

						oercHundContainer.removeAllContainerFilters();

					}
				}

				System.out.println("hunde gefunden " + gefunden);

				schauHundContainer.removeAllContainerFilters();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int rechnePunkte(Item hundItem) {

		int summe = 0;

		if (!(hundItem.getItemProperty("formwert").getValue() == null)) {
			switch (hundItem.getItemProperty("formwert").getValue().toString()) {
			case "vv":
				summe += 5;
				break;
			case "v":
				if (hundItem.getItemProperty("klasse").getValue()
						.equals(ShowKlassen.JUENGSTENKLASSE)) {
					summe += 3;
				} else {
					summe += 10;
				}
				break;
			case "sg":
				summe += 5;
				break;
			default:

			}
		}

		if (!(hundItem.getItemProperty("platzierung").getValue() == null)) {
			System.out.println("in platzierung berechnen");
			
			if (ShowKlassen.platzWirdFuerKlasseBerechnet(hundItem.getItemProperty("klasse").getValue().toString())) {

				System.out.println("klasse wird gewertet");
				switch (hundItem.getItemProperty("platzierung").getValue()
						.toString().trim()) {
				case "1":
					summe += 5;
					break;
				case "2":
					summe += 4;
					break;
				case "3":
					summe += 3;
					break;
				case "4":
					summe += 2;
					break;
				default: System.out.println("default");
				}

			}
		}

		return summe;

	}

}
