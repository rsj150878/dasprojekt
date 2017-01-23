package com.app.DashBoard.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CupBasisBerechnungsView extends VerticalLayout implements View,
		Button.ClickListener {

	private Button startBasisButton;
	private Button kompButton;
	private Button listButton;

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

		Label title = new Label("ShowCup");
		title.setSizeUndefined();

		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		header.addComponent(title);

		return header;
	}

	private Component buildWorkingArea() {
		VerticalLayout mainLayout = new VerticalLayout();

		startBasisButton = new Button("Berechne BasisPunkte...");
		startBasisButton.setId("BASIS");
		mainLayout.addComponent(startBasisButton);
		startBasisButton.addClickListener(this);

		kompButton = new Button("komprimiere Ergebnisse...");
		kompButton.setId("KOMP");
		mainLayout.addComponent(kompButton);
		kompButton.addClickListener(this);

		listButton = new Button("Liste...");
		listButton.setId("LIST");
		mainLayout.addComponent(listButton);
		listButton.addClickListener(this);

		return (mainLayout);

	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub

		AbstractComponent source = (AbstractComponent) event.getSource();
		if (source.getId().equals("BASIS")) {
			berechneBasisPunkte();
		} else if (source.getId().equals("KOMP")) {
			komprimierePunkte();
		} else if (source.getId().equals("LIST")) {
			printListe();
		}

	}

	private void komprimierePunkte() {
		try {
			TableQuery q4 = new TableQuery("ausstellungscup_basis",
					DBConnection.INSTANCE.getConnectionPool());
			q4.setVersionColumn("version");

			SQLContainer basisContainer = new SQLContainer(q4);

			basisContainer.addContainerFilter(new Equal("showjahr", "2016"));
			basisContainer.addOrderBy(new OrderBy("oercid", true));

			Integer alteOercId = new Integer(0);

			ArrayList<Integer> jugendPunkte = new ArrayList<Integer>();
			ArrayList<Integer> erwachsenenPunkte = new ArrayList<Integer>();
			ArrayList<Integer> veteranenPunkte = new ArrayList<Integer>();

			for (Object basisId : basisContainer.getItemIds()) {

				if (!alteOercId.equals(Integer.valueOf(basisContainer
						.getItem(basisId).getItemProperty("oercid").getValue()
						.toString()))
						&& !alteOercId.equals(new Integer(0))) {

					werteDetailAus(alteOercId, jugendPunkte, erwachsenenPunkte,
							veteranenPunkte);

					jugendPunkte = new ArrayList<Integer>();
					erwachsenenPunkte = new ArrayList<Integer>();
					veteranenPunkte = new ArrayList<Integer>();
				}

				if ("JÜ".equals(basisContainer.getItem(basisId)
						.getItemProperty("ausklasse").getValue().toString())
						|| "JU".equals(basisContainer.getItem(basisId)
								.getItemProperty("ausklasse").getValue()
								.toString())) {
					jugendPunkte.add((Integer) basisContainer.getItem(basisId)
							.getItemProperty("punkte").getValue());

				} else if ("VE".equals(basisContainer.getItem(basisId)
						.getItemProperty("ausklasse").getValue().toString())) {
					veteranenPunkte.add((Integer) basisContainer
							.getItem(basisId).getItemProperty("punkte")
							.getValue());
				} else {
					erwachsenenPunkte.add((Integer) basisContainer
							.getItem(basisId).getItemProperty("punkte")
							.getValue());

				}

				alteOercId = Integer.valueOf(basisContainer.getItem(basisId)
						.getItemProperty("oercid").getValue().toString());
			}

			werteDetailAus(alteOercId, jugendPunkte, erwachsenenPunkte,
					veteranenPunkte);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void werteDetailAus(Integer oercId,
			ArrayList<Integer> jugendPunkte,
			ArrayList<Integer> erwachsenenPunkte,
			ArrayList<Integer> veteranenPunkte) {

		System.out.println("in auswerten" + oercId);
		String zuWertenIn;

		if ((jugendPunkte.size() + erwachsenenPunkte.size() + veteranenPunkte
				.size()) >= 3) {
			if (jugendPunkte.size() >= erwachsenenPunkte.size()
					&& veteranenPunkte.size() == 0) {
				zuWertenIn = "JU";

			} else if (veteranenPunkte.size() >= erwachsenenPunkte.size()) {
				zuWertenIn = "VE";
			} else {
				zuWertenIn = "EW";
			}

			ArrayList<Integer> gesPunkte = new ArrayList<Integer>();
			gesPunkte.addAll(jugendPunkte);
			gesPunkte.addAll(erwachsenenPunkte);
			gesPunkte.addAll(veteranenPunkte);

			Collections.sort(gesPunkte);
			Collections.reverse(gesPunkte);

			Integer sumPunkte = new Integer(0);
			for (int i = 0; i < Math.min(gesPunkte.size(), 8); i++) {
				System.out.println("pkt: " + gesPunkte.get(i));
				sumPunkte += gesPunkte.get(i);

			}

			Integer sumGesPunkte = new Integer(0);
			for (int i = 0; i < gesPunkte.size(); i++) {
				sumGesPunkte += gesPunkte.get(i);

			}

			try {
				TableQuery q4 = new TableQuery("ausstellungscup_komprimiert",
						DBConnection.INSTANCE.getConnectionPool());
				q4.setVersionColumn("version");

				SQLContainer kompContainer = new SQLContainer(q4);

				kompContainer.addContainerFilter(new Equal("showjahr", "2016"));
				kompContainer.addContainerFilter(new Equal("oercid", oercId));

				Item kompContainerItem = null;

				if (kompContainer.size() == 0) {
					kompContainerItem = kompContainer
							.getItemUnfiltered(kompContainer.addItem());

				} else {
					kompContainerItem = kompContainer.getItem(kompContainer
							.firstItemId());
				}

				kompContainerItem.getItemProperty("oercid").setValue(oercId);
				kompContainerItem.getItemProperty("zu_werten_in").setValue(
						zuWertenIn);
				kompContainerItem.getItemProperty("showjahr").setValue("2016");
				kompContainerItem.getItemProperty("punkte").setValue(sumPunkte);
				kompContainerItem.getItemProperty("gespunkte").setValue(
						sumGesPunkte);
				kompContainer.commit();
				kompContainer.removeAllContainerFilters();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void berechneBasisPunkte() {
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
			basisContainer.addContainerFilter(new Equal("showjahr", "2016"));

			basisContainer.removeAllItems();
			basisContainer.commit();
			basisContainer.removeAllContainerFilters();

			for (Object schauId : schauContainer.getItemIds()) {
				schauHundContainer.addContainerFilter(new Equal("idschau",
						schauContainer.getItem(schauId)
								.getItemProperty("idschau").getValue()));

				schauHundContainer.addContainerFilter(new Equal("hundfehlt",
						"N"));

				int gefunden = 0;
				for (Object schauHundId : schauHundContainer.getItemIds()) {
					Pattern p = Pattern.compile("(^|\\s)([0-9]+)($|\\s)");
					String zuchtBuchNummer = schauHundContainer
							.getItem(schauHundId)
							.getItemProperty("zuchtbuchnummer").getValue()
							.toString();
					Matcher m = p.matcher(zuchtBuchNummer);
					// System.out.println("Hund " + zuchtBuchNummer);
					if (m.find()) {

						// System.out.println("gefunden");
						// System.out.println(zuchtBuchNummer.substring(m.start(),
						// m.end()));
						// System.out.println("zuchtbuchnummer " +
						// zuchtBuchNummer.substring(m.start(),
						// m.end()).trim());

						String zbnrFilterValue = "%"
								+ zuchtBuchNummer.substring(m.start(), m.end())
										.trim() + "%";

						// System.out.println("filter: '" + zbnrFilterValue +
						// "'");
						oercHundContainer.addContainerFilter(new Like("zbnr",
								zbnrFilterValue));

						oercHundContainer.addContainerFilter(new Like(
								"zuchtbuch", "%HZB%"));

						oercHundContainer.addContainerFilter(new Like("rasse",
								"%"
										+ schauHundContainer
												.getItem(schauHundId)
												.getItemProperty("rasse")
												.getValue().toString() + "%"));

						if (oercHundContainer.size() > 1) {

							System.out
									.println("mehr als einen Hund gefunden bei zbnr "
											+ zbnrFilterValue
											+ " schauhundid: "
											+ schauHundContainer
													.getItem(schauHundId)
													.getItemProperty(
															"idschauhund")
													.getValue().toString());
						}
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

							basisContainerItem.getItemProperty(
									"zbnr_filter_value").setValue(
									zbnrFilterValue);
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
													.getItemProperty("oercid")
													.getValue());

							basisContainerItem.getItemProperty("punkte")
									.setValue(
											rechnePunkte(schauHundContainer
													.getItem(schauHundId)));

							basisContainerItem.getItemProperty("ausklasse")
									.setValue(
											schauHundContainer
													.getItem(schauHundId)
													.getItemProperty("klasse")
													.getValue());

							Date showDate = (Date) schauContainer
									.getItem(schauId).getItemProperty("datum")
									.getValue();
							basisContainerItem.getItemProperty("showjahr")
									.setValue(
											new SimpleDateFormat("yyyy")
													.format(showDate));

							basisContainer.commit();
							basisContainer.removeAllContainerFilters();
							gefunden += 1;

						}

						oercHundContainer.removeAllContainerFilters();

					}
				}

				// System.out.println("hunde gefunden " + gefunden);

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

			if (ShowKlassen.platzWirdFuerKlasseBerechnet(hundItem
					.getItemProperty("klasse").getValue().toString())) {

				switch (hundItem.getItemProperty("platzierung").getValue()
						.toString().trim()) {
				case "1":
					summe += 5;
					if (hundItem.getItemProperty("klasse").getValue()
							.equals(ShowKlassen.VETERANENKLASSE)) {
						summe += 4;
					}
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
				default:
					System.out.println("default");
				}

			}
		}

		if (!(hundItem.getItemProperty("CACA").getValue() == null)) {
			switch (hundItem.getItemProperty("CACA").getValue().toString()) {
			case "C":
				summe += 5;
				break;
			case "R":
				summe += 4;
				break;
			case "J":
				summe += 4;
				break;

			default:

			}
		}

		if (!(hundItem.getItemProperty("CACIB").getValue() == null)) {
			switch (hundItem.getItemProperty("CACIB").getValue().toString()) {
			case "C":
				summe += 8;
				break;
			case "R":
				summe += 6;
				break;
			default:

			}
		}

		if (!(hundItem.getItemProperty("BOB").getValue() == null)) {
			switch (hundItem.getItemProperty("BOB").getValue().toString()) {
			case "B":
				summe += 10;
				break;
			case "O":
				summe += 8;
				break;
			default:

			}
		}

		if (!(hundItem.getItemProperty("clubsieger").getValue() == null)) {

			if (hundItem.getItemProperty("klasse").getValue()
					.equals(ShowKlassen.JUGENDKLASSE)) {
				summe += 4;
			} else {
				summe += 8;
			}

		}

		if (!(hundItem.getItemProperty("bestehrenring").getValue() == null)) {
			summe += 10;
		}

		if (!(hundItem.getItemProperty("BOD").getValue() == null)) {
			switch (hundItem.getItemProperty("BOD").getValue().toString()) {
			case "B":
				summe += 20;
				break;
			default:
			}
		}

		if (!(hundItem.getItemProperty("BIS").getValue() == null)) {
			switch (hundItem.getItemProperty("BIS").getValue().toString()) {
			case "B":
				summe += 20;
				break;
			case "R":
				summe += 15;
				break;

			default:
			}
		}

		if (!(hundItem.getItemProperty("platzehrenring").getValue() == null)) {

			switch (hundItem.getItemProperty("platzehrenring").getValue()
					.toString().trim()) {
			case "1":
				summe += 10;
				break;
			case "2":
				summe += 9;
				break;
			case "3":
				summe += 8;
				break;
			case "4":
				summe += 7;
				break;
			case "5":
				summe += 6;
				break;
			default:
				System.out.println("default");
			}

		}

		return summe;

	}

	private void printListe() {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("select idausstellungscup_komprimiert, name,");
			sb.append("rasse, punkte, zu_werten_in, a.oercid as oercid, b.oercid as hundoercid , gespunkte , b.geschlecht ");
			sb.append("from ausstellungscup_komprimiert a, oerc_hund b ");
			sb.append("where a.oercid = b.oercid");
			sb.append(" and a.showjahr = '2016' ");
			sb.append("order by b.rasse, a.punkte");

			FreeformQuery query = new FreeformQuery(sb.toString(),
					DBConnection.INSTANCE.getConnectionPool(),
					"idausstellungscup_komprimiert");
			SQLContainer container = new SQLContainer(query);

			for (Object id : container.getItemIds()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(container.getItem(id).getItemProperty("name")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("rasse")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("punkte")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id)
						.getItemProperty("zu_werten_in").getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("oercid")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("hundoercid")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("gespunkte")
						.getValue()
						+ ";");
				sb1.append(container.getItem(id).getItemProperty("geschlecht")
						.getValue()
						+ ";");
				System.out.println(sb1.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
