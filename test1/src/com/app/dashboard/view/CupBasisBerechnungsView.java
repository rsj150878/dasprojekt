package com.app.dashboard.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBConnection;
import com.app.enumdatatypes.ShowKlassen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.filter.Compare.GreaterOrEqual;
import com.vaadin.v7.data.util.filter.Like;
import com.vaadin.v7.data.util.filter.Or;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class CupBasisBerechnungsView extends VerticalLayout implements View, Button.ClickListener {

	private Button startBasisButton;
	private Button kompButton;
	private Button listButton;
	private Button pdfDetails;

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

		pdfDetails = new Button("Details...");
		pdfDetails.setId("PDF");
		mainLayout.addComponent(pdfDetails);
		pdfDetails.addClickListener(this);

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
		} else if (source.getId().equals("PDF")) {
			pdfListe();
		}

	}

	private void komprimierePunkte() {
		try {
			TableQuery q4 = new TableQuery("ausstellungscup_basis", DBConnection.INSTANCE.getConnectionPool());
			q4.setVersionColumn("version");

			SQLContainer basisContainer = new SQLContainer(q4);

			basisContainer.addContainerFilter(new Equal("showjahr", "2017"));
			basisContainer.addOrderBy(new OrderBy("oercid", true));
			
			TableQuery q5 = new TableQuery("ausstellungscup_komprimiert",
					DBConnection.INSTANCE.getConnectionPool());
			q5.setVersionColumn("version");

			SQLContainer kompContainer = new SQLContainer(q5);

			kompContainer.addContainerFilter(new Equal("showjahr", "2017"));
			kompContainer.removeAllItems();
			kompContainer.commit();
		

			Integer alteOercId = new Integer(0);

			ArrayList<Integer> jugendPunkte = new ArrayList<Integer>();
			ArrayList<Integer> erwachsenenPunkte = new ArrayList<Integer>();
			ArrayList<Integer> veteranenPunkte = new ArrayList<Integer>();

			ArrayList<KlassenPunkte> listNeu = new ArrayList<KlassenPunkte>();
			for (Object basisId : basisContainer.getItemIds()) {

				if (!alteOercId.equals(Integer
						.valueOf(basisContainer.getItem(basisId).getItemProperty("oercid").getValue().toString()))
						&& !alteOercId.equals(new Integer(0))) {

					werteDetailAus(alteOercId, jugendPunkte, erwachsenenPunkte, veteranenPunkte, listNeu);

					jugendPunkte = new ArrayList<Integer>();
					erwachsenenPunkte = new ArrayList<Integer>();
					veteranenPunkte = new ArrayList<Integer>();
					listNeu = new ArrayList<>();

				}

				if ("WE".equals(basisContainer.getItem(basisId).getItemProperty("ausklasse").getValue().toString())
						|| "JÜ".equals(
								basisContainer.getItem(basisId).getItemProperty("ausklasse").getValue().toString())
						|| "JU".equals(
								basisContainer.getItem(basisId).getItemProperty("ausklasse").getValue().toString())) {
					jugendPunkte.add((Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue());

					listNeu.add(new KlassenPunkte(
							(Integer) basisContainer.getItem(basisId).getItemProperty("ausstellungscup_basis_id")
									.getValue(),
							"JU", (Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue(), 2));

				} else if ("VE"
						.equals(basisContainer.getItem(basisId).getItemProperty("ausklasse").getValue().toString())) {
					veteranenPunkte.add((Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue());
					listNeu.add(new KlassenPunkte(

							(Integer) basisContainer.getItem(basisId).getItemProperty("ausstellungscup_basis_id")
									.getValue(),
							"VE", (Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue(), 2));

				} else {
					erwachsenenPunkte
							.add((Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue());

					listNeu.add(new KlassenPunkte(

							(Integer) basisContainer.getItem(basisId).getItemProperty("ausstellungscup_basis_id")
									.getValue(),
							"EW", (Integer) basisContainer.getItem(basisId).getItemProperty("punkte").getValue(), 1));

				}

				alteOercId = Integer
						.valueOf(basisContainer.getItem(basisId).getItemProperty("oercid").getValue().toString());
			}

			werteDetailAus(alteOercId, jugendPunkte, erwachsenenPunkte, veteranenPunkte, listNeu);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void werteDetailAus(Integer oercId, ArrayList<Integer> jugendPunkte, ArrayList<Integer> erwachsenenPunkte,
			ArrayList<Integer> veteranenPunkte, ArrayList<KlassenPunkte> listNeu) {

		System.out.println("in auswerten" + oercId);
		String zuWertenIn;

		if ((jugendPunkte.size() + erwachsenenPunkte.size() + veteranenPunkte.size()) >= 3) {
			if (jugendPunkte.size() >= erwachsenenPunkte.size() && veteranenPunkte.size() == 0) {
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
			for (int i = 0; i < Math.min(gesPunkte.size(), 9); i++) {
				System.out.println("pkt: " + gesPunkte.get(i));
				sumPunkte += gesPunkte.get(i);

			}

			Integer sumGesPunkte = new Integer(0);
			for (int i = 0; i < gesPunkte.size(); i++) {
				sumGesPunkte += gesPunkte.get(i);

			}

			// ArrayList<KlassenPunkte> listNeu = new ArrayList<>();
			// for (Integer i : jugendPunkte) {
			// listNeu.add(new KlassenPunkte("JU", i));
			// }
			//
			// for (Integer i : erwachsenenPunkte) {
			// listNeu.add(new KlassenPunkte("EW", i));
			// }
			//
			// for (Integer i : veteranenPunkte) {
			// listNeu.add(new KlassenPunkte("VE", i));
			// }
			//
			// listNeu.sort(Comparator.comparing(KlassenPunkte::getPunkte));
			ComparatorChain chain = new ComparatorChain(
					Arrays.asList(new BeanComparator<Integer>("punkte"), new BeanComparator<Integer>("gewichtung")));

			Collections.sort(listNeu, chain);
			Collections.reverse(listNeu);

			List<KlassenPunkte> wertungListNeu = listNeu.subList(0, Math.min(listNeu.size(), 9));

			if (listNeu.size() > 9) {
				listNeu.subList(9, listNeu.size()).forEach(x -> updateStreichErgebnis(x, "J"));
			}

			wertungListNeu.forEach(x -> updateStreichErgebnis(x, "N"));
			// List<KlassenPunkte> streichErgebnisse =
			// listNeu.subList(Math.min(listNeu.size(), 10), listNeu.size());

			long anzahlJugendNeu = wertungListNeu.stream().filter(x -> x.getKlasseFor().equals("JU")).count();
			long anzahlErwachsenNeu = wertungListNeu.stream().filter(x -> x.getKlasseFor().equals("EW")).count();
			long anzahlVeteranNeu = wertungListNeu.stream().filter(x -> x.getKlasseFor().equals("VE")).count();

			System.out.println("anzahlJugendNeu: " + anzahlJugendNeu);
			System.out.println("anzahlErwachsenNeu: " + anzahlErwachsenNeu);
			System.out.println("veteran: " + anzahlVeteranNeu);

			String zuWertenInNeu;
			if (anzahlJugendNeu >= anzahlErwachsenNeu && anzahlVeteranNeu == 0) {
				zuWertenInNeu = "JU";

			} else if (anzahlVeteranNeu >= anzahlErwachsenNeu) {
				zuWertenInNeu = "VE";
			} else {
				zuWertenInNeu = "EW";
			}

			Integer punkteNeu = wertungListNeu.stream().map(e -> e.getPunkte()).reduce(0, (x, y) -> x + y);
			Integer punkteNeuAlteArt = 0;

			for (KlassenPunkte x : wertungListNeu) {
				System.out.println("punkte:  " + x.getPunkte());
				punkteNeuAlteArt += x.getPunkte();
			}
			if (!zuWertenInNeu.equals(zuWertenIn)) {
				System.out.println("Unterschied bei " + oercId + " neu " + zuWertenInNeu + " alt " + zuWertenIn);

			}

			if (!punkteNeu.equals(sumPunkte)) {
				System.out.println("Unterschied bei " + oercId + " neu " + punkteNeu + " neu/alte art "
						+ punkteNeuAlteArt + " alt " + sumPunkte);

			}

			sumPunkte = punkteNeu;
			zuWertenIn = zuWertenInNeu;

			try {
				TableQuery q4 = new TableQuery("ausstellungscup_komprimiert",
						DBConnection.INSTANCE.getConnectionPool());
				q4.setVersionColumn("version");

				SQLContainer kompContainer = new SQLContainer(q4);

				kompContainer.addContainerFilter(new Equal("showjahr", "2017"));
				kompContainer.addContainerFilter(new Equal("oercid", oercId));

				Item kompContainerItem = null;

				if (kompContainer.size() == 0) {
					kompContainerItem = kompContainer.getItemUnfiltered(kompContainer.addItem());

				} else {
					kompContainerItem = kompContainer.getItem(kompContainer.firstItemId());
				}

				kompContainerItem.getItemProperty("oercid").setValue(oercId);
				kompContainerItem.getItemProperty("zu_werten_in").setValue(zuWertenIn);
				kompContainerItem.getItemProperty("showjahr").setValue("2017");
				kompContainerItem.getItemProperty("punkte").setValue(sumPunkte);
				kompContainerItem.getItemProperty("gespunkte").setValue(sumGesPunkte);
				kompContainer.commit();
				kompContainer.removeAllContainerFilters();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void updateStreichErgebnis(KlassenPunkte updateElement, String wert) {
		try {
			System.out.println("udpate streichergebnis " + updateElement.getAusstellungscup_basis_id());
			TableQuery q4 = new TableQuery("ausstellungscup_basis", DBConnection.INSTANCE.getConnectionPool());
			q4.setVersionColumn("version");

			SQLContainer basisContainer = new SQLContainer(q4);
			basisContainer.addContainerFilter(
					new Equal("ausstellungscup_basis_id", updateElement.getAusstellungscup_basis_id()));

			basisContainer.getItem(basisContainer.firstItemId()).getItemProperty("streichergebnis").setValue(wert);
			basisContainer.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void berechneBasisPunkte() {
		try {
			TableQuery q1 = new TableQuery("schauhund", DBConnection.INSTANCE.getConnectionPool());
			q1.setVersionColumn("version");

			TableQuery q2 = new TableQuery("schau", DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");

			TableQuery q3 = new TableQuery("oerc_hund", DBConnection.INSTANCE.getConnectionPool());
			q3.setVersionColumn("version");

			TableQuery q4 = new TableQuery("ausstellungscup_basis", DBConnection.INSTANCE.getConnectionPool());
			q4.setVersionColumn("version");

			SQLContainer schauContainer = new SQLContainer(q2);
			schauContainer.addContainerFilter(new GreaterOrEqual("datum", new GregorianCalendar(2017, 1, 1).getTime()));
			schauContainer.addContainerFilter(new Or(new Equal("schautyp","I"), new Equal("schautyp","C")));

			SQLContainer schauHundContainer = new SQLContainer(q1);

			SQLContainer oercHundContainer = new SQLContainer(q3);

			SQLContainer basisContainer = new SQLContainer(q4);
			basisContainer.addContainerFilter(new Equal("showjahr", "2017"));

			basisContainer.removeAllItems();
			basisContainer.commit();
			basisContainer.removeAllContainerFilters();

			for (Object schauId : schauContainer.getItemIds()) {
				schauHundContainer.addContainerFilter(
						new Equal("idschau", schauContainer.getItem(schauId).getItemProperty("idschau").getValue()));

				// schauHundContainer.addContainerFilter(new Equal("hundfehlt",
				// "N"));

				int gefunden = 0;
				for (Object schauHundId : schauHundContainer.getItemIds()) {
					// System.out.println("idschauhund: "
					// +
					// schauHundContainer.getItem(schauHundId).getItemProperty("idschauhund").getValue()
					// +
					// schauHundContainer.getItem(schauHundId).getItemProperty("hundfehlt").getValue());

					if (schauHundContainer.getItem(schauHundId).getItemProperty("hundfehlt").getValue() == null
							|| schauHundContainer.getItem(schauHundId).getItemProperty("hundfehlt").getValue()
									.toString().equals("N")) {

						Pattern p = Pattern.compile("(^|\\s)([0-9]+)($|\\s)");
						String zuchtBuchNummer = schauHundContainer.getItem(schauHundId)
								.getItemProperty("zuchtbuchnummer").getValue().toString();
						Matcher m = p.matcher(zuchtBuchNummer);
						// System.out.println("Hund " + zuchtBuchNummer);
						if (m.find()) {

							// System.out.println("gefunden");
							// System.out.println(zuchtBuchNummer.substring(m.start(),
							// m.end()));
							// System.out.println("zuchtbuchnummer " +
							// zuchtBuchNummer.substring(m.start(),
							// m.end()).trim());

							String zbnrFilterValue = "%" + zuchtBuchNummer.substring(m.start(), m.end()).trim() + "%";

							// System.out.println("filter: '" +
							// zbnrFilterValue
							// +
							// "'");
							oercHundContainer.addContainerFilter(new Like("zbnr", zbnrFilterValue));

							oercHundContainer.addContainerFilter(new Like("zuchtbuch", "%HZB%"));

							oercHundContainer.addContainerFilter(new Like("rasse", "%" + schauHundContainer
									.getItem(schauHundId).getItemProperty("rasse").getValue().toString() + "%"));

							if (oercHundContainer.size() > 1) {

								System.out.println("mehr als einen Hund gefunden bei zbnr " + zbnrFilterValue
										+ " schauhundid: " + schauHundContainer.getItem(schauHundId)
												.getItemProperty("idschauhund").getValue().toString());
							}
							if (oercHundContainer.size() > 0) {

								basisContainer.addContainerFilter(new Equal("idschau",
										schauContainer.getItem(schauId).getItemProperty("idschau").getValue()));
								basisContainer.addContainerFilter(new Equal("idschauhund", schauHundContainer
										.getItem(schauHundId).getItemProperty("idschauhund").getValue()));

								Item basisContainerItem = null;

								if (basisContainer.size() == 0) {
									basisContainerItem = basisContainer.getItemUnfiltered(basisContainer.addItem());

								} else {
									basisContainerItem = basisContainer.getItem(basisContainer.firstItemId());
								}

								basisContainerItem.getItemProperty("zbnr_filter_value").setValue(zbnrFilterValue);
								basisContainerItem.getItemProperty("idschau").setValue(
										schauContainer.getItem(schauId).getItemProperty("idschau").getValue());

								basisContainerItem.getItemProperty("idschauhund").setValue(schauHundContainer
										.getItem(schauHundId).getItemProperty("idschauhund").getValue());

								basisContainerItem.getItemProperty("oercid").setValue(oercHundContainer
										.getItem(oercHundContainer.firstItemId()).getItemProperty("oercid").getValue());

								basisContainerItem.getItemProperty("punkte")
										.setValue(rechnePunkte(schauHundContainer.getItem(schauHundId)));

								basisContainerItem.getItemProperty("ausklasse").setValue(
										schauHundContainer.getItem(schauHundId).getItemProperty("klasse").getValue());

								Date showDate = (Date) schauContainer.getItem(schauId).getItemProperty("datum")
										.getValue();
								basisContainerItem.getItemProperty("showjahr")
										.setValue(new SimpleDateFormat("yyyy").format(showDate));

								basisContainer.commit();
								basisContainer.removeAllContainerFilters();
								gefunden += 1;

							}
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
			case "VV":
				summe += 5;
				break;

			case "V":
				summe += 3;
				break;
			case "v":
				if (hundItem.getItemProperty("klasse").getValue()
						.equals(ShowKlassen.JUENGSTENKLASSE.getShowKlassenKurzBezeichnung())
						|| hundItem.getItemProperty("klasse").getValue()
								.equals(ShowKlassen.BABYKLASSE.getShowKlassenKurzBezeichnung())) {
					summe += 3;
				} else {
					summe += 10;
				}
				break;
			case "sg":
				summe += 5;
				break;
			default:
				System.out.println("default formwert " + hundItem.getItemProperty("idschauhund").getValue() + " Wert: "
						+ hundItem.getItemProperty("formwert").getValue().toString());

			}
		}

		if (!(hundItem.getItemProperty("platzierung").getValue() == null)) {

			if (ShowKlassen.platzWirdFuerKlasseBerechnet(hundItem.getItemProperty("klasse").getValue().toString())) {

				switch (hundItem.getItemProperty("platzierung").getValue().toString().trim()) {
				case "1":
					summe += 5;
					// if (hundItem.getItemProperty("klasse").getValue()
					// .equals(ShowKlassen.VETERANENKLASSE.getShowKlassenKurzBezeichnung()))
					// {
					// summe += 4;
					// }
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
					System.out.println("default platzierung " + hundItem.getItemProperty("idschauhund").getValue()
							+ " Wert: " + hundItem.getItemProperty("platzierung").getValue().toString());

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
			case "V":
				summe += 4;
				break;

			default:

				System.out.println("default CACA" + hundItem.getItemProperty("idschauhund").getValue() + " Wert: "
						+ hundItem.getItemProperty("CACA").getValue().toString());

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
				System.out.println("default cacib" + hundItem.getItemProperty("idschauhund").getValue() + " Wert: "
						+ hundItem.getItemProperty("CACIB").getValue().toString());

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
				System.out.println("default bob" + hundItem.getItemProperty("idschauhund").getValue() + "Wert "
						+ hundItem.getItemProperty("BOB").getValue().toString());

			}
		}

		if (!(hundItem.getItemProperty("clubsieger").getValue() == null)) {

			if (hundItem.getItemProperty("klasse").getValue()
					.equals(ShowKlassen.JUGENDKLASSE.getShowKlassenKurzBezeichnung())) {
				summe += 4;
			} else if (hundItem.getItemProperty("klasse").getValue()
					.equals(ShowKlassen.VETERANENKLASSE.getShowKlassenKurzBezeichnung())) {
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
			case "R":
				summe += 15;
				break;
		
			case "3":
				summe += 10;
				break;
		
			default:
				System.out.println("default BOD" + hundItem.getItemProperty("idschauhund").getValue() + " Wert "
						+ hundItem.getItemProperty("BOD").getValue().toString());

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
				System.out.println("default BIS" + hundItem.getItemProperty("idschauhund").getValue() + " Wert "
						+ hundItem.getItemProperty("BIS").getValue().toString());

			}
		}

		if (!(hundItem.getItemProperty("platzehrenring").getValue() == null)) {

			switch (hundItem.getItemProperty("platzehrenring").getValue().toString().trim()) {
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
				System.out.println("default platz ehrenring " + hundItem.getItemProperty("idschauhund").getValue()
						+ " Wert " + hundItem.getItemProperty("platzehrenring").getValue().toString().trim());
			}

		}
		if (!(hundItem.getItemProperty("platzehrenringgruppe").getValue() == null)) {

			switch (hundItem.getItemProperty("platzehrenringgruppe").getValue().toString().trim()) {
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
				System.out.println("default platzehrenringgruppe " + hundItem.getItemProperty("idschauhund").getValue()
						+ " Wert " + hundItem.getItemProperty("platzehrenringgruppe").getValue().toString().trim());
			}

		}

		return summe;

	}

	private void printListe() {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("select idausstellungscup_komprimiert, name, zbnr, ");
			sb.append(
					"rasse, punkte, zu_werten_in, a.oercid as oercid, b.oercid as hundoercid , gespunkte , b.geschlecht ");
			sb.append(", b.mitglied_nachname, b.mitglied_vorname ");
			sb.append("from ausstellungscup_komprimiert a, oerc_hund b ");
			sb.append("where a.oercid = b.oercid");
			sb.append(" and a.showjahr = '2017' ");
			sb.append("order by b.rasse, zu_werten_in,b.geschlecht, a.punkte desc, gespunkte desc ");

			FreeformQuery query = new FreeformQuery(sb.toString(), DBConnection.INSTANCE.getConnectionPool(),
					"idausstellungscup_komprimiert");
			SQLContainer container = new SQLContainer(query);

			for (Object id : container.getItemIds()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(container.getItem(id).getItemProperty("name").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("zbnr").getValue() + ";");

				sb1.append(container.getItem(id).getItemProperty("rasse").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("punkte").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("zu_werten_in").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("oercid").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("hundoercid").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("gespunkte").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("geschlecht").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("mitglied_nachname").getValue() + ";");
				sb1.append(container.getItem(id).getItemProperty("mitglied_vorname").getValue() + ";");
				System.out.println(sb1.toString());

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pdfListe() {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("select idausstellungscup_komprimiert, name, zbnr, ");
			sb.append(
					"rasse, punkte, zu_werten_in, a.oercid as oercid, b.oercid as hundoercid , gespunkte , b.geschlecht ");
			sb.append("from ausstellungscup_komprimiert a, oerc_hund b ");
			sb.append("where a.oercid = b.oercid");
			sb.append(" and a.showjahr = '2017' ");
			sb.append("order by b.rasse, zu_werten_in,b.geschlecht,  a.punkte desc, gespunkte desc ");

			FreeformQuery query = new FreeformQuery(sb.toString(), DBConnection.INSTANCE.getConnectionPool(),
					"idausstellungscup_komprimiert");
			SQLContainer container = new SQLContainer(query);

			String result = "Detailergebnisse.pdf";
			PdfDocument pdf = new PdfDocument(new PdfWriter(result));

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());

			PdfFont bold = PdfFontFactory.createFont(FontConstants.COURIER);
			float[] space = new float[] { 14, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
			System.out.println(space.length);

			Table table = null;

			StringBuilder sb2 = new StringBuilder();
			sb2.append("select idschau, schaukuerzel from schau ");
			sb2.append(" where year(datum) = 2017 ");
			sb2.append(" and schautyp in ('I','C') ");
			sb2.append(" order by datum asc ");

			FreeformQuery querySchau = new FreeformQuery(sb2.toString(), DBConnection.INSTANCE.getConnectionPool(),
					"idschau");
			SQLContainer containerSchau = new SQLContainer(querySchau);

			List schauList = null;

			String grRasse = "";
			String grKlasse = "";
			String grGeschlecht = "";
			for (Object id : container.getItemIds()) {

				System.out.println(container.getItem(id).getItemProperty("oercid").getValue());

				if (!grRasse.equals(container.getItem(id).getItemProperty("rasse").getValue().toString())

						|| (!grKlasse
								.equals(container.getItem(id).getItemProperty("zu_werten_in").getValue().toString())
								&& ("GR".equals(container.getItem(id).getItemProperty("rasse").getValue().toString())
										|| "LR".equals(
												container.getItem(id).getItemProperty("rasse").getValue().toString())
										|| "FCR".equals(
												container.getItem(id).getItemProperty("rasse").getValue().toString())))
						|| (!grGeschlecht
								.equals(container.getItem(id).getItemProperty("geschlecht").getValue().toString())
								&& ("GR".equals(container.getItem(id).getItemProperty("rasse").getValue().toString())
										|| "LR".equals(
												container.getItem(id).getItemProperty("rasse").getValue().toString())
										|| "FCR".equals(container.getItem(id).getItemProperty("rasse").getValue()
												.toString())))) {

					if (!grRasse.equals("")) {

						document.add(table);
						document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
					}

					table = new Table(UnitValue.createPercentArray(space)).useAllAvailableWidth();
					// name, klasse, rasse, geschlecht, ges_punkte,
					// einzel-punkte
					Cell cell1 = new Cell().add(new Paragraph("name")).setFontSize(10);
					table.addHeaderCell(cell1);
					cell1 = new Cell().add(new Paragraph("klasse").setRotationAngle(1.5708)).setFontSize(10)
							.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addHeaderCell(cell1);
					cell1 = new Cell().add(new Paragraph("rasse").setRotationAngle(1.5708)).setFontSize(10)
							.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addHeaderCell(cell1);
					cell1 = new Cell().add(new Paragraph("Geschlecht").setRotationAngle(1.5708)).setFontSize(10)
							.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addHeaderCell(cell1);
					cell1 = new Cell().add(new Paragraph("GesamtPunkte").setRotationAngle(1.5708)).setFontSize(10)
							.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addHeaderCell(cell1);
					cell1 = new Cell().add(new Paragraph("WertungsPunkte").setRotationAngle(1.5708)).setFontSize(10)
							.setHorizontalAlignment(HorizontalAlignment.CENTER);
					table.addHeaderCell(cell1);

					schauList = new ArrayList();
					for (Object schauId : containerSchau.getItemIds()) {
						Cell cell = new Cell().add(new Paragraph(
								containerSchau.getItem(schauId).getItemProperty("schaukuerzel").getValue().toString())
										.setRotationAngle(1.5708))
								.setFontSize(10);
						table.addHeaderCell(cell);
						schauList.add(containerSchau.getItem(schauId).getItemProperty("idschau").getValue());

					}

				}

				grRasse = container.getItem(id).getItemProperty("rasse").getValue().toString();
				grKlasse = container.getItem(id).getItemProperty("zu_werten_in").getValue().toString();
				grGeschlecht = container.getItem(id).getItemProperty("geschlecht").getValue().toString();

				table.addCell(container.getItem(id).getItemProperty("name").getValue().toString()).setFontSize(10);
				table.addCell(container.getItem(id).getItemProperty("zu_werten_in").getValue().toString())
						.setFontSize(10);
				table.addCell(container.getItem(id).getItemProperty("rasse").getValue().toString()).setFontSize(10);
				table.addCell(container.getItem(id).getItemProperty("geschlecht").getValue().toString())
						.setFontSize(10);
				table.addCell(container.getItem(id).getItemProperty("gespunkte").getValue().toString()).setFontSize(10);
				table.addCell(container.getItem(id).getItemProperty("punkte").getValue().toString()).setFontSize(10);

				for (Object schauId : schauList) {
					StringBuilder sb3 = new StringBuilder();
					sb3.append(
							"Select ausstellungscup_basis_id, punkte , streichergebnis, ausklasse from ausstellungscup_basis ");
					sb3.append(" where oercid = ");
					sb3.append(container.getItem(id).getItemProperty("oercid").getValue());
					sb3.append(" and showjahr = '2017' ");
					sb3.append(" and idschau = ");
					sb3.append(schauId);

					FreeformQuery queryDetailSchau = new FreeformQuery(sb3.toString(),
							DBConnection.INSTANCE.getConnectionPool(), "ausstellungscup_basis_id");
					SQLContainer containerDetailSchau = new SQLContainer(queryDetailSchau);

					String punkte;
					String streichErgebnis = "N";
					String ausKlasse = "";

					if (containerDetailSchau.size() == 0) {
						punkte = "--";
					} else if (containerDetailSchau.size() == 1) {
						punkte = containerDetailSchau.getItem(containerDetailSchau.firstItemId())
								.getItemProperty("punkte").getValue().toString();

						streichErgebnis = containerDetailSchau.getItem(containerDetailSchau.firstItemId())
								.getItemProperty("streichergebnis").getValue() == null ? "N"
										: containerDetailSchau.getItem(containerDetailSchau.firstItemId())
												.getItemProperty("streichergebnis").getValue().toString();
						ausKlasse = containerDetailSchau.getItem(containerDetailSchau.firstItemId())
								.getItemProperty("ausklasse").getValue().toString();
					} else {
						punkte = "mr";
						;
					}

					Paragraph p = new Paragraph(punkte).setFontSize(10);

					if (streichErgebnis.equals("J")) {
						p.setLineThrough();
					}

					Cell punkteCell = new Cell().add(p);
					if ("WE".equals(ausKlasse) || "JÜ".equals(ausKlasse) || "JU".equals(ausKlasse)) {
						punkteCell.setBackgroundColor(ColorConstants.LIGHT_GRAY);

					}

					table.addCell(punkteCell);

				}

			}
			document.add(table);
			document.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(result, "application/pdf", new File(result));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("GAPBewertungsblatt", s);
			addComponent(e);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class KlassenPunkte {
		private String klasseFor;
		private Integer punkte;
		private Integer ausstellungscup_basis_id;
		private Integer gewichtung;

		public KlassenPunkte(Integer ausstellungscup_basis_id, String klasseFor, Integer punkte, Integer gewichtung) {
			this.klasseFor = klasseFor;
			this.punkte = punkte;
			this.ausstellungscup_basis_id = ausstellungscup_basis_id;
			this.gewichtung = gewichtung;
		}

		public String getKlasseFor() {
			return klasseFor;
		}

		public void setKlasseFor(String klasseFor) {
			this.klasseFor = klasseFor;
		}

		public Integer getPunkte() {
			return punkte;
		}

		public void setPunkte(Integer punkte) {
			this.punkte = punkte;
		}

		public Integer getAusstellungscup_basis_id() {
			return ausstellungscup_basis_id;
		}

		public void setAusstellungscup_basis_id(Integer ausstellungscup_basis_id) {
			this.ausstellungscup_basis_id = ausstellungscup_basis_id;
		}

		public Integer getGewichtung() {
			return gewichtung;
		}

	}

}
