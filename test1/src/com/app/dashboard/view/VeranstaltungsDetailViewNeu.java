package com.app.dashboard.view;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.app.dashboard.component.VeranstaltungsTeilnehmerGrid;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dashboardwindow.SearchWindow;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.DokumentGehoertZuType;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.enumdatatypes.VeranstaltungsTypen;
import com.app.filestorage.HundeDokumenteImporter;
import com.app.printclasses.BewertungsListeNeu;
import com.app.printclasses.JungHundePruefung2017;
import com.app.printclasses.StarterListe;
import com.app.printclasses.Urkunde;
import com.app.printclasses.UrkundeTrainingsWorkingtest;
import com.app.printclasses.VtBestaetigungsBlatt;
import com.app.showdata.ShowHund;
import com.app.showdata.ShowRing;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsRichter;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;

public class VeranstaltungsDetailViewNeu extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3128669844525510581L;
	private TextField nameVeranstaltung;
	private TextField nameRichter;
	private TextField nameVeranstaltungsLeiter;
	private TextField veranstalter;
	private TextField veranstaltungsOrt;
	private DateField datumVeranstaltung;

	private TextField telnrLeiter;
	private TextField plzLeiter;
	private TextField strasseLeiter;
	private TextField ortLeiter;

	private Veranstaltung currentVeranstaltungsItem;

	private final VeranstaltungsDetailListener listener;

	private ComboBox<VeranstaltungsRichter> vaRichter;

	@AutoGenerated
	private VerticalLayout mainLayout;

	Component currentPrintComponent = null;
	GridLayout secondLineLayout = null;
	VeranstaltungsTypen defTyp;

	Binder<Veranstaltung> vaBinder;

	private TabSheet stufenSheet;

	private HundeDokumenteImporter hundeDokumenteImporter = new HundeDokumenteImporter();
	DBVeranstaltung db;
	List<VeranstaltungsStufe> stufenList;

	/**
	 * The constructor should first build the main layout, set the composition root
	 * and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual editor.
	 */
	public VeranstaltungsDetailViewNeu(VeranstaltungsTypen typ, Veranstaltung currentVeranstaltungsItem,
			VeranstaltungsDetailListener listener) {

		this.currentVeranstaltungsItem = currentVeranstaltungsItem;
		this.listener = listener;
		this.defTyp = typ;
		vaBinder = new Binder<Veranstaltung>(Veranstaltung.class);
		db = new DBVeranstaltung();

		buildMainLayout();

		Panel mainPanel = new Panel();
		mainPanel.setContent(mainLayout);
		mainPanel.setSizeFull();
		mainPanel.setStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		setCompositionRoot(mainPanel);
		vaBinder.setBean(currentVeranstaltungsItem);
		if (currentVeranstaltungsItem.getVaRichter() != null) {
			vaRichter.setItems(currentVeranstaltungsItem.getVaRichter());
		}

	}

	private void buildMainLayout() {

		try {
			stufenList = db.getStufenZuVaId(currentVeranstaltungsItem.getId_veranstaltung());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// common part: create layout
		mainLayout = new VerticalLayout();

		mainLayout.setMargin(true);

		Component secondPanel = createSecondPanel();
		mainLayout.addComponent(secondPanel);
		mainLayout.setExpandRatio(secondPanel, 1);

		Component thirdPanel = createThirdPanel();
		mainLayout.addComponent(thirdPanel);
		mainLayout.setExpandRatio(thirdPanel, 2);

		// setCompositionRoot(mainLayout);

	}

	private Panel createSecondPanel() {

		Panel secondLine = new Panel();

		secondLineLayout = new GridLayout(5, 4);
		secondLineLayout.setWidth("100%");
		secondLineLayout.setHeight("100%");

		nameVeranstaltung = new TextField("Veranstaltungsname");
		nameVeranstaltung.setValueChangeMode(ValueChangeMode.EAGER);
		nameVeranstaltung.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(nameVeranstaltung).bind(Veranstaltung::getName, Veranstaltung::setName);

		secondLineLayout.addComponent(nameVeranstaltung, 0, 0);

		datumVeranstaltung = new DateField("Datum der Veranstaltung");
		datumVeranstaltung.setDateFormat("dd.MM.yyyy");

		vaBinder.forField(datumVeranstaltung).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
				.bind(Veranstaltung::getDatum, Veranstaltung::setDatum);

		datumVeranstaltung.addValueChangeListener(evt -> {

			if (evt.isUserOriginated()) {
				System.out.println("event fired");
				String title = currentVeranstaltungsItem.getTyp().getVeranstaltungsTypBezeichnung();
				title += " " + new SimpleDateFormat("dd.MM.yyyy").format(currentVeranstaltungsItem.getDatum());
				setTitle(title);
				saveVeranstaltung();

			}

		}

		);

		secondLineLayout.addComponent(datumVeranstaltung, 1, 0);

		nameRichter = new TextField("Name Richter");
		nameRichter.setMaxLength(45);
		nameRichter.setValueChangeMode(ValueChangeMode.EAGER);
		nameRichter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(nameRichter).bind(Veranstaltung::getRichter, Veranstaltung::setRichter);
		secondLineLayout.addComponent(nameRichter, 2, 0);

		veranstalter = new TextField("Veranstalter");
		veranstalter.setMaxLength(45);
		veranstalter.setValueChangeMode(ValueChangeMode.EAGER);
		veranstalter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(veranstalter).bind(Veranstaltung::getVeranstalter, Veranstaltung::setVeranstalter);
		secondLineLayout.addComponent(veranstalter, 3, 0);

		veranstaltungsOrt = new TextField("Veranstaltungsort");
		veranstaltungsOrt.setMaxLength(45);
		veranstaltungsOrt.setValueChangeMode(ValueChangeMode.EAGER);
		veranstaltungsOrt.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(veranstaltungsOrt).bind(Veranstaltung::getVeranstaltungsort,
				Veranstaltung::setVeranstaltungsort);
		secondLineLayout.addComponent(veranstaltungsOrt, 4, 0);

		nameVeranstaltungsLeiter = new TextField("Veranstaltungs-/Prüfungsleiter");
		nameVeranstaltungsLeiter.setMaxLength(45);
		nameVeranstaltungsLeiter.setValueChangeMode(ValueChangeMode.EAGER);
		nameVeranstaltungsLeiter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(nameVeranstaltungsLeiter).bind(Veranstaltung::getVeranstaltungsleiter,
				Veranstaltung::setVeranstaltungsleiter);
		secondLineLayout.addComponent(nameVeranstaltungsLeiter, 0, 1);

		strasseLeiter = new TextField("Strasse Prüfungsleiter");
		strasseLeiter.setMaxLength(45);
		strasseLeiter.setValueChangeMode(ValueChangeMode.EAGER);
		strasseLeiter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(strasseLeiter).bind(Veranstaltung::getStrasse_leiter, Veranstaltung::setStrasse_leiter);
		secondLineLayout.addComponent(strasseLeiter, 1, 1);

		plzLeiter = new TextField("PLZ Prüfungsleiter");
		plzLeiter.setMaxLength(45);
		plzLeiter.setValueChangeMode(ValueChangeMode.EAGER);
		plzLeiter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(plzLeiter).bind(Veranstaltung::getPlz_leiter, Veranstaltung::setPlz_leiter);
		secondLineLayout.addComponent(plzLeiter, 2, 1);

		ortLeiter = new TextField("Ort Prüfungsleiter");
		ortLeiter.setMaxLength(45);
		ortLeiter.setValueChangeMode(ValueChangeMode.EAGER);
		ortLeiter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(ortLeiter).bind(Veranstaltung::getOrt_leiter, Veranstaltung::setOrt_leiter);
		secondLineLayout.addComponent(ortLeiter, 3, 1);

		telnrLeiter = new TextField("Telefon Prüfungsleiter");
		telnrLeiter.setMaxLength(45);
		telnrLeiter.setValueChangeMode(ValueChangeMode.EAGER);
		telnrLeiter.addValueChangeListener(evt -> {
			if (evt.isUserOriginated())
				saveVeranstaltung();
		});
		vaBinder.forField(telnrLeiter).bind(Veranstaltung::getTel_nr_leiter, Veranstaltung::setTel_nr_leiter);
		secondLineLayout.addComponent(telnrLeiter, 4, 1);
		secondLine.setContent(secondLineLayout);

		Button printBewertungsListeButton = new Button();
		printBewertungsListeButton.setCaption("Bewertungsliste ÖRC");
		printBewertungsListeButton.addStyleName(ValoTheme.BUTTON_SMALL);

		printBewertungsListeButton.addClickListener(evt -> {

			if (!(currentPrintComponent == null)) {
				secondLineLayout.removeComponent(currentPrintComponent);
			}

			BewertungsListeNeu bewertungsListe = new BewertungsListeNeu(currentVeranstaltungsItem);
			secondLineLayout.addComponent(bewertungsListe);
			currentPrintComponent = bewertungsListe;
		}

		);
		secondLineLayout.addComponent(printBewertungsListeButton, 0, 2);

		if (defTyp.equals(VeranstaltungsTypen.RBP_2017_WASSER) || defTyp.equals(VeranstaltungsTypen.GAP_PRÜFUNG)) {

			this.hundeDokumenteImporter.setGehoertZu(currentVeranstaltungsItem.getId_veranstaltung());
			this.hundeDokumenteImporter.setGehoertZuType(DokumentGehoertZuType.VERANSTALTUNG);
			Upload upload = new Upload("starte Urkundenupload hier", this.hundeDokumenteImporter);
			upload.setButtonCaption("Urkunde");
			upload.addSucceededListener(this.hundeDokumenteImporter);
			upload.addFailedListener(this.hundeDokumenteImporter);
			upload.setId("upload");
			secondLineLayout.addComponent(upload, 1, 2, 3, 2);
			;
		}

		if (defTyp.equals(VeranstaltungsTypen.WESENSTEST)) {

			Button showUebertrag = new Button("übertrage Daten in show");
			showUebertrag.addClickListener(event -> uebertrageToShow());
			secondLineLayout.addComponent(showUebertrag, 4, 2, 4, 2);
			;
		}

		Button printStarterListe = new Button();
		printStarterListe.setCaption("Starterliste Extern");
		printStarterListe.addStyleName(ValoTheme.BUTTON_SMALL);
		printStarterListe.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (!(currentPrintComponent == null)) {
					secondLineLayout.removeComponent(currentPrintComponent);
				}

				StarterListe starterListe = new StarterListe(currentVeranstaltungsItem, false);
				secondLineLayout.addComponent(starterListe);
				currentPrintComponent = starterListe;
			}

		});
		secondLineLayout.addComponent(printStarterListe, 0, 3);

		Button printStarterListeIntern = new Button();
		printStarterListeIntern.setCaption("Starterliste Intern");
		printStarterListeIntern.addStyleName(ValoTheme.BUTTON_SMALL);
		printStarterListeIntern.addClickListener(evt -> {

			if (!(currentPrintComponent == null)) {
				secondLineLayout.removeComponent(currentPrintComponent);
			}

			StarterListe starterListe = new StarterListe(currentVeranstaltungsItem, true);
			secondLineLayout.addComponent(starterListe);
			currentPrintComponent = starterListe;

		});

		secondLineLayout.addComponent(printStarterListeIntern, 1, 3);

		vaRichter = new ComboBox<>();
		vaRichter.setItemCaptionGenerator(VeranstaltungsRichter::getRichterName);
		vaRichter.setNewItemProvider(inputString -> {

			VeranstaltungsRichter newRichter = new VeranstaltungsRichter();
			newRichter.setRichterName(inputString);
			newRichter.setLfdNummer(currentVeranstaltungsItem.getVaRichter().size() + 1);
			newRichter.setIdVeranstaltung(currentVeranstaltungsItem.getId_veranstaltung());

			try {
				db.saveRichter(newRichter);
			} catch (Exception e) {
				e.printStackTrace();
				Notification.show("fehler beim speichern");
			}
			currentVeranstaltungsItem.getVaRichter().add(newRichter);

			// Update combobox content
			vaRichter.setItems(currentVeranstaltungsItem.getVaRichter());

			return Optional.of(newRichter);
		});

		secondLineLayout.addComponent(vaRichter, 2, 3);

		if (defTyp.equals(VeranstaltungsTypen.JUNGHUNDEPRUEFUNG)) {

			Button jungHundePruefungKurz = new Button();
			jungHundePruefungKurz.setCaption("Urkunde kurz");
			jungHundePruefungKurz.addStyleName(ValoTheme.BUTTON_SMALL);
			jungHundePruefungKurz.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					if (!(currentPrintComponent == null)) {
						secondLineLayout.removeComponent(currentPrintComponent);
					}
					//
					// JungHundePruefung2017 urkunde = new
					// JungHundePruefung2017(currentVeranstaltungsItem, true);
					// secondLineLayout.addComponent(urkunde);
					// currentPrintComponent = urkunde;
				}

			});
			secondLineLayout.addComponent(jungHundePruefungKurz, 3, 3);

			Button jungHundePruefungLang = new Button();
			jungHundePruefungLang.setCaption("Urkunde lang");
			jungHundePruefungLang.addStyleName(ValoTheme.BUTTON_SMALL);
			jungHundePruefungLang.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					if (!(currentPrintComponent == null)) {
						secondLineLayout.removeComponent(currentPrintComponent);
					}

					// JungHundePruefung2017 urkunde = new
					// JungHundePruefung2017(currentVeranstaltungsItem, false);
					// secondLineLayout.addComponent(urkunde);
					// currentPrintComponent = urkunde;
				}

			});

			secondLineLayout.addComponent(jungHundePruefungLang, 4, 3);

		}

		return secondLine;
	}

	public void saveVeranstaltung() {
		try {
			vaBinder.writeBean(currentVeranstaltungsItem);
			this.db.saveVeranstaltung(currentVeranstaltungsItem);

		} catch (Exception e) {
			Notification.show("Fehler beim speichern");
			e.printStackTrace();
		}
	}

	public void setTitle(String title) {

		listener.titleChanged(title, VeranstaltungsDetailViewNeu.this);
	}

	private Component createThirdPanel() {
		stufenSheet = new TabSheet();

		for (VeranstaltungsStufe zw : stufenList) {

			AnmeldungsPanel myAnmeldungsPanel = new AnmeldungsPanel(zw.getStufenTyp(), currentVeranstaltungsItem, zw);

			stufenSheet.addTab(myAnmeldungsPanel, zw.getStufenTyp().getBezeichnung());

			stufenSheet.addSelectedTabChangeListener(event -> { // TODO
																// Auto-generated
																// method stub
				TabSheet ts = (TabSheet) event.getSource();
				Iterator<?> i = ts.iterator();
				while (i.hasNext()) {
					Object o = i.next();
					if (o instanceof AnmeldungsPanel) {
						AnmeldungsPanel anmeldungsPanel = (AnmeldungsPanel) o;
						anmeldungsPanel.removePrintComponent();

					}

				}
			});

		}

		DropTargetExtension<TabSheet> dropTarget = new DropTargetExtension<>(stufenSheet);
		dropTarget.setDropEffect(DropEffect.MOVE);
		dropTarget.addDropListener(event -> {
			System.out.println(event.getComponent().getCaption());
			System.out.println("drop target");
		});
		return stufenSheet;
	}

	public class AnmeldungsPanel extends CustomComponent {
		Veranstaltung veranstaltung;
		VeranstaltungsStufe veranstaltungsStufe;
		VeranstaltungsStufen defStufe;

		Component anmeldungsGrid;
		VerticalLayout anmeldungsPanelLayout;
		VerticalLayout tableLayout;
		HorizontalLayout buttonLeiste;
		Component currentPrintComponent = null;

		public AnmeldungsPanel(VeranstaltungsStufen defStufeUeber, Veranstaltung veranstaltungUeber,
				VeranstaltungsStufe veranstaltungsStufeUeber) {
			// super();
			this.veranstaltung = veranstaltungUeber;
			this.veranstaltungsStufe = veranstaltungsStufeUeber;
			this.defStufe = defStufeUeber;

			anmeldungsPanelLayout = new VerticalLayout();
			anmeldungsPanelLayout.setSizeFull();

			buttonLeiste = new HorizontalLayout();
			tableLayout = new VerticalLayout();

			Button neuerTeilnehmer = new Button();
			neuerTeilnehmer.setCaption("neuer Teilnehmer");
			neuerTeilnehmer.addStyleName(ValoTheme.BUTTON_SMALL);

			neuerTeilnehmer.addClickListener(evt -> {
				DashBoardEventBus.register(anmeldungsGrid);
				SearchWindow.open();
			});

			buttonLeiste.addComponent(neuerTeilnehmer);

			Button printRichterBlattButton = new Button();
			printRichterBlattButton.setCaption("Richterblatt");
			printRichterBlattButton.addStyleName(ValoTheme.BUTTON_SMALL);

			printRichterBlattButton.addClickListener(evt -> {
				if (!(currentPrintComponent == null)) {
					anmeldungsPanelLayout.removeComponent(currentPrintComponent);
				}

				Class<? extends CustomComponent> printClass = defStufe.getRichterBlatt();

				if (!(printClass == null)) {
					try {
						Component printObject = printClass
								.getConstructor(Veranstaltung.class, VeranstaltungsStufe.class)
								.newInstance(veranstaltung, veranstaltungsStufe);
						anmeldungsPanelLayout.addComponent(printObject);
						currentPrintComponent = printObject;
					} catch (Exception e) {
						e.printStackTrace();
						Notification.show("fehler beim laden der Druckklasse");
					}
				} else {
					Notification.show("kein Richterblatt hinterlegt");
				}

			});

			buttonLeiste.addComponent(printRichterBlattButton);

			Button printUrkundenButton = new Button();
			printUrkundenButton.setCaption("Urkunden");
			printUrkundenButton.addStyleName(ValoTheme.BUTTON_SMALL);

			printUrkundenButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					if (!(currentPrintComponent == null)) {
						anmeldungsPanelLayout.removeComponent(currentPrintComponent);
					}

					if (defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_ANFAENGER)
							|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_FORTGESCHRITTEN)
							|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_EINSTEIGER)
							|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_LEICHT)
							|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_MITTEL)) {
						UrkundeTrainingsWorkingtest urkunde = new UrkundeTrainingsWorkingtest(veranstaltung,
								veranstaltungsStufe);
						anmeldungsPanelLayout.addComponent(urkunde);
						currentPrintComponent = urkunde;

					}
					else if (defStufe.equals(VeranstaltungsStufen.JUNGHUNDEPRUEFUNG)) {
						JungHundePruefung2017 urkunde = new JungHundePruefung2017(veranstaltung,veranstaltungsStufe);
						anmeldungsPanelLayout.addComponent(urkunde);
						currentPrintComponent = urkunde;
					}
					else if (defStufe.equals(VeranstaltungsStufen.WESENSTEST_GRUPPE_ALLGEMEIN)) {
						// WesentestBewertungsblatt urkunde = new
						// WesentestBewertungsblatt(veranstaltung,
						// veranstaltungsStufe);
						// anmeldungsPanelLayout.addComponent(urkunde);
						// currentPrintComponent = urkunde;

					} else if (defStufe.equals(VeranstaltungsStufen.STUFE_BH_VT_ONLY)) {
						VtBestaetigungsBlatt urkunde = new VtBestaetigungsBlatt(veranstaltung, veranstaltungsStufe);
						anmeldungsPanelLayout.addComponent(urkunde);
						currentPrintComponent = urkunde;

					} else {
						Urkunde urkunde = new Urkunde(veranstaltung, veranstaltungsStufe);
						anmeldungsPanelLayout.addComponent(urkunde);
						currentPrintComponent = urkunde;

					}

				}

			});

			buttonLeiste.addComponent(printUrkundenButton);

			Button printMeldungButton = new Button();
			printMeldungButton.setCaption("Meldeblätter drucken");
			printMeldungButton.addStyleName(ValoTheme.BUTTON_SMALL);
			printMeldungButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					if (!(currentPrintComponent == null)) {
						anmeldungsPanelLayout.removeComponent(currentPrintComponent);
					}
					if (defStufe.equals(VeranstaltungsStufen.STUFE_BH)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_BGH1)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_BGH2)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_BGH3)) {

						// BHMeldeBlatt meldeBlatt = new
						// BHMeldeBlatt(veranstaltung,
						// veranstaltungsStufe);
						// anmeldungsPanelLayout.addComponent(meldeBlatt);
						// currentPrintComponent = meldeBlatt;
					} else if (defStufe.equals(VeranstaltungsStufen.STUFE_RBP1)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_RBP2)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_RBP3)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_RBP4_M_WASSER)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_RBP4_O_WASSER)) {

					} else if (defStufe.equals(VeranstaltungsStufen.STUFE_GAP1)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_GAP2)
							|| defStufe.equals(VeranstaltungsStufen.STUFE_GAP3)) {

					} else if (defStufe.equals(VeranstaltungsStufen.WESENSTEST_GRUPPE_ALLGEMEIN)) {
						// WesentestFormwert meldeBlatt = new
						// WesentestFormwert(veranstaltung,
						// veranstaltungsStufe);
						// anmeldungsPanelLayout.addComponent(meldeBlatt);
						// currentPrintComponent = meldeBlatt;

					}

				}

			});
			buttonLeiste.addComponent(printMeldungButton);

			anmeldungsGrid = new VeranstaltungsTeilnehmerGrid(defStufe, veranstaltungsStufe);

			anmeldungsPanelLayout.addComponent(buttonLeiste);
			tableLayout.addComponent(anmeldungsGrid);
			anmeldungsPanelLayout.addComponent(tableLayout);
			anmeldungsPanelLayout.setSpacing(false);

			setCompositionRoot(anmeldungsPanelLayout);
		}

		public void removePrintComponent() {
			if (!(this.currentPrintComponent == null)) {
				this.anmeldungsPanelLayout.removeComponent(this.currentPrintComponent);
			}
		}

		public Component getMeldeComponent() {
			return this.anmeldungsGrid;
		}

	}

	public void refresh() {
		this.mainLayout.removeAllComponents();
		this.buildMainLayout();
	}

	public interface VeranstaltungsDetailListener {
		void titleChanged(String newTitle, VeranstaltungsDetailViewNeu detail);
	}

	private void uebertrageToShow() {

		// DBShowNeu db = new DBShowNeu();
		//
		// TableQuery q3;
		//
		// SQLContainer teilnehmerContainer;
		//
		// try {
		//
		// q3 = new TableQuery("veranstaltungs_teilnehmer",
		// DBConnection.INSTANCE.getConnectionPool());
		// q3.setVersionColumn("version");
		//
		// teilnehmerContainer = new SQLContainer(q3);
		//
		// Show show = db.getShowForVeranstaltung(Integer
		// .valueOf(currentVeranstaltungsItem.getItemProperty("id_veranstaltung").getValue().toString()));
		// System.out.println("id: " + show.getIdSchau());
		// show.setSchaubezeichnung(defTyp.getVeranstaltungsTypBezeichnung() + "
		// "
		// +
		// currentVeranstaltungsItem.getItemProperty("veranstaltungsort").getValue().toString());
		// show.setSchauTyp(defTyp.getShowTyp());
		// show.setSchauDate(new SimpleDateFormat("yyyy-MM-dd")
		// .parse(currentVeranstaltungsItem.getItemProperty("datum").getValue().toString()));
		// show.setSchauKuerzel("");
		//
		// db.updateShow(show);
		//
		// ShowRing ring = db.getShowRing(show.getIdSchau(), "1");
		//
		// teilnehmerContainer = new SQLContainer(q3);
		//
		// teilnehmerContainer.addContainerFilter(new Equal("id_veranstaltung",
		// currentVeranstaltungsItem.getItemProperty("id_veranstaltung").getValue()));
		//
		// Integer i = 1;
		// for (Object id : teilnehmerContainer.getItemIds()) {
		// ShowHund hund = db.getShowHundForVeranstaltung(show.getIdSchau(),
		// ring,
		// Integer.valueOf(
		// teilnehmerContainer.getItem(id).getItemProperty("id_teilnehmer").getValue().toString()));
		// //baueShowHund(hund, ring, teilnehmerContainer.getItem(id));
		// hund.setKatalognumer(i.toString());
		// hund.setSort_kat_nr(i);
		// db.updateShowHund(hund);
		// i++;
		//
		// }
		//
		// } catch (Exception e) {
		// Notification.show("fehler beim Übertragen");
		// e.printStackTrace();
		// }

	}

	private void baueShowHund(ShowHund hund, ShowRing ring, VeranstaltungsTeilnehmer teilnehmerItem) throws Exception {
		//
		// TableQuery q4;
		// TableQuery q5;
		//
		// q4 = new TableQuery("person",
		// DBConnection.INSTANCE.getConnectionPool());
		// q4.setVersionColumn("version");
		//
		// q5 = new TableQuery("hund",
		// DBConnection.INSTANCE.getConnectionPool());
		// q5.setVersionColumn("version");
		//
		// SQLContainer personContainer;
		// SQLContainer hundContainer;
		//
		// personContainer = new SQLContainer(q4);
		// hundContainer = new SQLContainer(q5);
		//
		// hundContainer.addContainerFilter(new Equal("idhund",
		// teilnehmerItem.getItemProperty("id_hund").getValue()));
		//
		// personContainer
		// .addContainerFilter(new Equal("idperson",
		// teilnehmerItem.getItemProperty("id_person").getValue()));
		//
		// hund.setChipnummer(
		// hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("chipnummer").getValue().toString());
		//
		// hund.setWurftag(new SimpleDateFormat("yyyy-MM-dd").parse(
		// hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("wurfdatum").getValue().toString()));
		//
		// hund.setZuchtbuchnummer(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zuchtbuchnummer")
		// .getValue().toString());
		//
		// hund.setGeschlecht(
		// hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("geschlecht").getValue().toString());
		//
		// hund.setRasse(Rassen.getRasseForKurzBezeichnung(
		// hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("rasse").getValue().toString()));
		//
		// hund.setShowHundName(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zwingername")
		// .getValue().toString());
		//
		// hund.setBesitzershow(
		// personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname").getValue().toString()
		// + " " +
		// personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
		// .getValue().toString());
		// hund.setRingId(ring.getRingId());
		// hund.setVater("");
		// hund.setMutter("");
		// hundContainer.removeAllContainerFilters();
		// personContainer.removeAllContainerFilters();

	}

}
