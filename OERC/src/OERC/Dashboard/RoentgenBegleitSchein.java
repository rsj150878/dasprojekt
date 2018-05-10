package OERC.Dashboard;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import OERC.Domain.Hund;
import OERC.Domain.Person;
import OERC.Event.DashboardEvent.BrowserResizeEvent;
import OERC.Event.DashboardEventBus;
import OERC.data.HundDao;
import OERC.data.PersonDao;
import OERC.enums.RetrieverRassen;
import OERC.print.RoentgenBegleitScheinPrint;

@Theme("dashboard")
@Widgetset("OERC.Dashboard.widgetset.OercWidgetset")
@Title("ÖRC Röntgenbegleitschein")
@SuppressWarnings("serial")
public final class RoentgenBegleitSchein extends AbstractOERCUi {

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();
	ComboBox<RetrieverRassen> rassenAuswahl;
	TextField zuchtBuchNummer;
	TextField hundName;
	TextField Name;
	TextField besTitel;
	TextField chipnummer;
	TextField besVorName;
	TextField besNachName;
	TextField besStrasse;
	TextField besPlz;
	TextField besOrt;
	TextField telefon;
	DateField wurfDatum;

	Person person;
	Hund hund;

	RoentgenBegleitScheinPrint rbsp = null;

	@Override
	protected void init(final VaadinRequest request) {

		// DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		// addStyleName(ValoTheme.UIUI_WITH_MENU);

		updateContent();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});
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

		rassenAuswahl = new ComboBox<>("Bitte wählen Sie die Rasse Ihres Hundes:");
		rassenAuswahl.setItems(RetrieverRassen.values());
		rassenAuswahl.setItemCaptionGenerator(RetrieverRassen::getRassenLangBezeichnung);
		rassenAuswahl.setScrollToSelectedItem(true);
		rassenAuswahl.setEmptySelectionAllowed(false);
		rassenAuswahl.setSelectedItem(RetrieverRassen.CHESAPEAKE_BAY_RETRIEVER);
		infoFormLayout.addComponent(rassenAuswahl);

		zuchtBuchNummer = new TextField("Bitte die Zuchtbuchnummer angeben:");
		zuchtBuchNummer.setMaxLength(5);
		infoFormLayout.addComponent(zuchtBuchNummer);

		Button suchHund = new Button("Hund suchen");
		infoFormLayout.addComponent(suchHund);

		suchHund.addClickListener(p -> suchHund());
		suchHund.setClickShortcut(KeyCode.ENTER);

		hundName = new TextField("Hundename");
		infoFormLayout.addComponent(hundName);

		chipnummer = new TextField("Chipnummer");
		infoFormLayout.addComponent(chipnummer);

		wurfDatum = new DateField("Wurfdatum");
		wurfDatum.setDateFormat("dd.MM.yyyy");
		wurfDatum.setLenient(true);
		infoFormLayout.addComponent(wurfDatum);

		besTitel = new TextField("Titel");
		infoFormLayout.addComponent(besTitel);

		besVorName = new TextField("Vorname");
		infoFormLayout.addComponent(besVorName);

		besNachName = new TextField("Nachname");
		infoFormLayout.addComponent(besNachName);

		besStrasse = new TextField("Besitzer Strasse");
		infoFormLayout.addComponent(besStrasse);

		besPlz = new TextField("Besitzer PLZ");
		infoFormLayout.addComponent(besPlz);

		besOrt = new TextField("Besitzer Ort");
		infoFormLayout.addComponent(besOrt);

		telefon = new TextField("Telefon");
		infoFormLayout.addComponent(telefon);

		Button print = new Button("Erstelle Röntgenbegleitschein");
		infoFormLayout.addComponent(print);

		print.addClickListener(p -> {

			if (!(rbsp == null)) {
				infoFormLayout.removeComponent(rbsp);
			}
			rbsp = new RoentgenBegleitScheinPrint(person, hund, rassenAuswahl.getSelectedItem().get());
			infoFormLayout.addComponent(rbsp);
		});

		infoFormLayout.setComponentAlignment(rassenAuswahl, Alignment.MIDDLE_CENTER);
		infoFormLayout.setComponentAlignment(suchHund, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(infoFormLayout);
		mainLayout.setComponentAlignment(infoFormLayout, Alignment.MIDDLE_CENTER);

		
		setContent(mainLayout);

	}

	private void suchHund() {
		HundDao dao = new HundDao();
		PersonDao pDao = new PersonDao();
		try {
			hund = dao.getHundForZuchtbuch(rassenAuswahl.getSelectedItem().get(), zuchtBuchNummer.getValue());

			if (hund == null) {
				Notification.show("Leider wurde der Hund nicht gefunden");
			} else {
				person = pDao.getPersonForHundeId(hund.getIdHund());

				hundName.setValue(hund.getName());
				hundName.addValueChangeListener(event -> hund.setName(event.getValue()));

				chipnummer.setValue(hund.getChipNummer());
				chipnummer.addValueChangeListener(event -> hund.setChipNummer(event.getValue()));

				wurfDatum.setValue(fromDate(hund.getWurfTag()));
				wurfDatum.addValueChangeListener(
						event -> hund.setWurfTag(java.sql.Timestamp.valueOf(event.getValue().atStartOfDay())));

				if (person == null) {
					person = new Person();
					person.setTitel("");
					person.setNachname("");
					person.setVorname("");
					person.setStrasse("");
					person.setPlz("");
					person.setOrt("");
					person.setTelefon1("");
					person.setTelefon2("");

				}

				besTitel.setValue(person.getTitel());
				besNachName.setValue(person.getNachname());
				besVorName.setValue(person.getVorname());

				besStrasse.setValue(person.getStrasse());
				besPlz.setValue(person.getPlz());
				besOrt.setValue(person.getOrt());
				telefon.setValue(person.getTelefon1() == null || person.getTelefon1().isEmpty() ? person.getTelefon2()
						: person.getTelefon1());

				besTitel.addValueChangeListener(event -> person.setTitel(event.getValue()));
				besNachName.addValueChangeListener(event -> person.setNachname(event.getValue()));
				besVorName.addValueChangeListener(event -> person.setVorname(event.getValue()));
				besStrasse.addValueChangeListener(event -> person.setStrasse(event.getValue()));
				telefon.addValueChangeListener(event -> {
					if (person.getTelefon1() == null || person.getTelefon1().isEmpty())
						person.setTelefon2(event.getValue());
					else
						person.setTelefon1(event.getValue());
				});
				besPlz.addValueChangeListener(event -> person.setPlz(event.getValue()));
				besOrt.addValueChangeListener(event -> person.setOrt(event.getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Fehler beim Db-Zugriff " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	public static LocalDate fromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

}
