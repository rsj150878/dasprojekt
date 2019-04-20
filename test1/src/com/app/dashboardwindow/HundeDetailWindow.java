package com.app.dashboardwindow;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Set;

import com.app.auth.Hund;
import com.app.auth.Person;
import com.app.dashboard.event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.dashboard.event.DashBoardEvent.DogUpdatedEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBHund;
import com.app.enumdatatypes.HundeGeschlechtDataType;
import com.app.enumdatatypes.Rassen;
import com.app.printclasses.Kursblatt;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HundeDetailWindow extends Window {

	public static final String ID = "hundedetailwindow";

	private Binder<Hund> fieldGroup;

	private DateField bhDatumField;
	private TextField chipnummerField;
	private TextField farbeField;
	private RadioButtonGroup<HundeGeschlechtDataType> geschlechtGroup;
	private ComboBox<Rassen> rasseGroup;
	private TextField rufnameField;
	private DateField wurfDatumField;
	private TextField zuchtbuchnummerField;
	private TextField zuechterField;
	private TextField zwingernameField;

	private Hund hund;
	private Collection<Hund> hundeCollection;
	private float height;
	private Person person;
	private Grid<Hund> dogTable;
	private Kursblatt zw;

	private HundeDetailWindow(Person person, Collection<Hund> hundeCollection) {
		if (hundeCollection.size() > 0) {
			this.hund = hundeCollection.iterator().next();

		}
		this.hundeCollection = hundeCollection;
		this.person = person;

		height = 130.0f;
		fieldGroup = new Binder<Hund>(Hund.class);
		initWindow();

		fieldGroup.readBean(hund);

	}

	private HundeDetailWindow(final Hund hund) {

		this.hund = hund;
		height = 90.0f;
		fieldGroup = new Binder<Hund>(Hund.class);
		initWindow();
		fieldGroup.readBean(hund);

	}

	private void initWindow() {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);
		// addStyleName("profile-form");

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);

		setResizable(false);
		setClosable(false);
		setHeight(height, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		// content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		HorizontalLayout horizontalContent = new HorizontalLayout();
		horizontalContent.setSizeFull();
		content.addComponent(horizontalContent);

		float expandRatio = 0;
		if (!(hundeCollection == null)) {
			Component auswahlTab = buildHundeAuswahlTab();
			horizontalContent.addComponent(auswahlTab);
			horizontalContent.setExpandRatio(auswahlTab, 1f);
			expandRatio = 1;
		}

		TabSheet detailsWrapper = new TabSheet();
		detailsWrapper.setSizeFull();
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
		horizontalContent.addComponent(detailsWrapper);
		horizontalContent.setExpandRatio(detailsWrapper, 1f + expandRatio);

		detailsWrapper.addComponent(buildProfileTab());
		detailsWrapper.addComponent(buildPreferencesTab());

		content.addComponent(buildFooter());

	}

	private Component buildPreferencesTab() {
		VerticalLayout root = new VerticalLayout();
		root.setCaption("Preferences");
		root.setIcon(VaadinIcons.COGS);
		root.setSpacing(true);
		root.setMargin(true);
		root.setSizeFull();

		Label message = new Label("Not implemented in this demo");
		message.setSizeUndefined();
		message.addStyleName(ValoTheme.LABEL_LIGHT);
		root.addComponent(message);
		root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

		return root;
	}

	private Component buildHundeAuswahlTab() {

		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(true);
		// layout.setWidth(100.0f, Unit.PERCENTAGE);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSizeUndefined();
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);

		Button newDog = new Button("neuer Hund", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Hund newHund = new Hund(person.getIdPerson());

				hundeCollection.add(newHund);

				dogTable.setItems(hundeCollection);
				dogTable.removeAllColumns();

				dogTable.addColumn(Hund::getRufname);
				dogTable.addColumn(Hund::getZwingername);

				dogTable.select(newHund);

			}

		});
		buttonLayout.addComponent(newDog);
		newDog.addStyleName(ValoTheme.BUTTON_TINY);

		Button delDog = new Button("Hund löschen", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

			}

		});
		buttonLayout.addComponent(delDog);
		delDog.addStyleName(ValoTheme.BUTTON_TINY);

		layout.addComponent(buttonLayout);

		dogTable = new Grid<>(Hund.class);
		dogTable.removeAllColumns();
		dogTable.setSizeFull();
		dogTable.addColumn(Hund::getRufname);
		dogTable.addColumn(Hund::getZwingername);
		dogTable.setItems(hundeCollection);

		dogTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		dogTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		// dogTable.addStyleName(ValoTheme.TABLE_);

		dogTable.addSelectionListener(event -> {
			try {

				fieldGroup.writeBean(hund);
				// Updated user should also be persisted to database. But
				// not in this demo.

				if (!(hund == null)) {
					DBHund dbHund = new DBHund();
					dbHund.saveHund(hund);
				}
				Notification success = new Notification("Hundedaten erfolgreich gespeichert");
				success.setDelayMsec(2000);
				success.setPosition(Position.BOTTOM_CENTER);
				success.show(Page.getCurrent());

			} catch (Exception e) {
				e.printStackTrace();
				Notification.show("Es ist ein Fehler passiert\n" + e.getMessage(), Type.ERROR_MESSAGE);

			}
			Set<Hund> selected = dogTable.getSelectedItems();

			if (selected.size() > 0) {
				hund = (Hund) selected.toArray()[0];
				fieldGroup.readBean(hund);
			}

		});
		layout.addComponent(dogTable);

		layout.setExpandRatio(dogTable, 1);

		return layout;
	}

	private Component buildProfileTab() {
		HorizontalLayout root = new HorizontalLayout();
		root.setCaption("Profile");
		root.setIcon(VaadinIcons.USER);

		// if (!(hundeCollection == null)) {
		// root.setWidth(80.0f, Unit.PERCENTAGE);
		// } else {
		root.setWidth(100.0f, Unit.PERCENTAGE);
		// }
		root.setSpacing(true);
		root.setMargin(true);
		root.addStyleName("profile-form");

		VerticalLayout pic = new VerticalLayout();
		pic.setSizeUndefined();
		pic.setSpacing(true);
		Image profilePic = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
		profilePic.setWidth(100.0f, Unit.PIXELS);
		pic.addComponent(profilePic);

		Button upload = new Button("Change…", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Not implemented in this demo");
			}
		});
		upload.addStyleName(ValoTheme.BUTTON_TINY);
		pic.addComponent(upload);

		root.addComponent(pic);

		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		rufnameField = new TextField("Rufname");
		fieldGroup.forField(rufnameField).bind(Hund::getRufname, Hund::setRufname);
		details.addComponent(rufnameField);

		zwingernameField = new TextField("Zwingername");
		fieldGroup.forField(zwingernameField).bind(Hund::getZwingername, Hund::setZwingername);
		details.addComponent(zwingernameField);

		chipnummerField = new TextField("Chipnummer");
		fieldGroup.forField(chipnummerField).bind(Hund::getChipnummer, Hund::setChipnummer);
		details.addComponent(chipnummerField);

		zuchtbuchnummerField = new TextField("Zuchtbuchnummer");
		fieldGroup.forField(zuchtbuchnummerField).bind(Hund::getZuchtbuchnummer, Hund::setZuchtbuchnummer);
		details.addComponent(zuchtbuchnummerField);

		geschlechtGroup = new RadioButtonGroup<>("Geschlecht");
		geschlechtGroup.setItems(HundeGeschlechtDataType.values());
		geschlechtGroup.setItemCaptionGenerator(HundeGeschlechtDataType::getLangBezeichnung);
		geschlechtGroup.addStyleName("horizontal");
		fieldGroup.forField(geschlechtGroup)
				.withConverter(HundeGeschlechtDataType::getKurzBezeichnung,
						value -> HundeGeschlechtDataType.getDataTypeKurzBezeichnung(value))
				.bind(Hund::getGeschlecht, Hund::setGeschlecht);
		details.addComponent(geschlechtGroup);

		rasseGroup = new ComboBox<>("Rasse");
		rasseGroup.setItems(Rassen.values());
		rasseGroup.setItemCaptionGenerator(Rassen::getRassenLangBezeichnung);
		rasseGroup.addStyleName("horizontal");
		fieldGroup.forField(rasseGroup)
				.withConverter(Rassen::getRassenKurzBezeichnung, value -> Rassen.getRasseForKurzBezeichnung(value))
				.bind(Hund::getRasse, Hund::setRasse);
		details.addComponent(rasseGroup);

		wurfDatumField = new DateField("Wurfdatum");
		wurfDatumField.setWidth("100%");
		wurfDatumField.setDateFormat("dd.MM.yyyy");
		fieldGroup.forField(wurfDatumField).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
				.bind(Hund::getWurfdatum, Hund::setWurfdatum);
		details.addComponent(wurfDatumField);

		farbeField = new TextField("Farbe");
		fieldGroup.forField(farbeField).bind(Hund::getFarbe, Hund::setFarbe);
		details.addComponent(farbeField);

		zuechterField = new TextField("Züchter");
		fieldGroup.forField(zuechterField).bind(Hund::getZuechter, Hund::setZuechter);
		details.addComponent(zuechterField);

		bhDatumField = new DateField("BH-Datum");
		bhDatumField.setWidth("100%");
		bhDatumField.setDateFormat("dd.MM.yyyy");
		fieldGroup.forField(bhDatumField).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
				.bind(Hund::getBhdatum, Hund::setBhdatum);
		details.addComponent(bhDatumField);

		// Label section = new Label("Contact Info");
		// section.addStyleName(ValoTheme.LABEL_H4);
		// section.addStyleName(ValoTheme.LABEL_COLORED);
		// details.addComponent(section);

		return root;
	}

	private Component buildFooter() {
		final GridLayout footer = new GridLayout(3, 1);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		if (!(this.hundeCollection == null)) {

			Button printButton = new Button("Kursblatt");
			printButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

			printButton.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {

					if (!(zw == null)) {
						footer.removeComponent(zw);
					}
					zw = new Kursblatt(person, hund);
					footer.addComponent(zw, 1, 0);

				}

			});

			footer.addComponent(printButton, 0, 0);
			// footer.setComponentAlignment(printButton, Alignment.TOP_LEFT);

		}

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.writeBean(hund);
					// Updated user should also be persisted to database. But
					// not in this demo.

					if (!(hund == null)) {
						DBHund dbHund = new DBHund();
						dbHund.saveHund(hund);
					}
					Notification success = new Notification("Hundedaten erfolgreich gespeichert");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());

					DashBoardEventBus.post(new DogUpdatedEvent());
					close();
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("Es ist ein Fehler passiert", Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();
		footer.addComponent(ok, 2, 0);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		return footer;
	}

	public static void open(final Hund hund) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new HundeDetailWindow(hund);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

	public static void open(final Person person, final Collection<Hund> hund) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new HundeDetailWindow(person, hund);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
