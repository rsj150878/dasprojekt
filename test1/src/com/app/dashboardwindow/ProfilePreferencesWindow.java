package com.app.dashboardwindow;

import java.time.ZoneId;

import com.app.auth.Person;
import com.app.auth.User;
import com.app.dashboard.event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.dashboard.event.DashBoardEvent.ProfileUpdatedEvent;
import com.app.dashboard.event.DashBoardEvent.UpdateUserEvent;
import com.app.dashboard.event.DashBoardEvent.UserNewEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBPerson;
import com.app.enumdatatypes.JaNeinDataType;
import com.app.enumdatatypes.LaenderDataType;
import com.app.enumdatatypes.MenschGeschlechtDataType;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window {

	public static final String ID = "profilepreferenceswindow";

	private final Binder<Person> fieldGroupPerson;

	private TextField firstNameField;
	private TextField lastNameField;
	private TextField titleField;
	private RadioButtonGroup<MenschGeschlechtDataType> sexField;
	private DateField birthDate;
	private TextField emailField;
	private TextField strasseField;
	private TextField hausnummerField;
	private TextField plzField;
	private TextField ortField;
	private TextField phoneField;
	private TextField mobNrField;
	private ComboBox<LaenderDataType> landField;
	private TextField websiteField;
	private TextArea bioField;
	private RadioButtonGroup<JaNeinDataType> newsletter;
	private TextField emailField2;
	private RadioButtonGroup<JaNeinDataType> newsletter2;
	private TextField emailField3;
	private RadioButtonGroup<JaNeinDataType> newsletter3;

	//private final Person abstractPerson;
	private final Person person;
	private final User user;
	private final boolean update;

	private ProfilePreferencesWindow(final User user, final boolean preferencesTabOpen) {

		this.user = user;
		this.person = user.getPerson();

		this.update = false;
		fieldGroupPerson = new Binder<Person>(Person.class);

		initWindow(preferencesTabOpen);

		fieldGroupPerson.readBean(person);

	}

	private ProfilePreferencesWindow(final Person person, final boolean update) {

		this.person = person;
		this.update = update;
		this.user = null;
		fieldGroupPerson = new Binder<Person>(Person.class);

		initWindow(false);

		fieldGroupPerson.readBean(person);

	}

	private void initWindow(final boolean preferencesTabOpen) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setClosable(false);
		setHeight(90.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		TabSheet detailsWrapper = new TabSheet();
		detailsWrapper.setSizeFull();
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
		detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
		content.addComponent(detailsWrapper);
		content.setExpandRatio(detailsWrapper, 1f);

		detailsWrapper.addComponent(buildProfileTab());
		detailsWrapper.addComponent(buildPreferencesTab());

		if (preferencesTabOpen) {
			detailsWrapper.setSelectedTab(1);
		}

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

	private Component buildProfileTab() {
		HorizontalLayout root = new HorizontalLayout();
		root.setCaption("Profile");
		root.setIcon(VaadinIcons.USER);
		root.setWidth(100.0f, Unit.PERCENTAGE);
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

		sexField = new RadioButtonGroup<>("Anrede");
		sexField.setItems(MenschGeschlechtDataType.values());
		sexField.setItemCaptionGenerator(MenschGeschlechtDataType::getLangText);
		sexField.addStyleName("horizontal");
		details.addComponent(sexField);

		titleField = new TextField("Titel");
		details.addComponent(titleField);

		firstNameField = new TextField("Vorname");
		details.addComponent(firstNameField);

		lastNameField = new TextField("Familienname");
		details.addComponent(lastNameField);

		birthDate = new DateField("GeburtsDatum");
		birthDate.setWidth("100%");
		birthDate.setDateFormat("dd.MM.yyyy");
		details.addComponent(birthDate);

		Label section = new Label("Contact Info");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		emailField = new TextField("Email");
		emailField.setWidth("100%");

		details.addComponent(emailField);

		newsletter = new RadioButtonGroup<>("Newsletter");
		newsletter.setItems(JaNeinDataType.values());
		newsletter.setItemCaptionGenerator(JaNeinDataType::getLangText);
		newsletter.addStyleName("horizontal");
		details.addComponent(newsletter);

		phoneField = new TextField("Telefon");
		phoneField.setWidth("100%");
		details.addComponent(phoneField);

		mobNrField = new TextField("Mobil");
		mobNrField.setWidth("100%");
		details.addComponent(mobNrField);

		section = new Label("Addresse");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		landField = new ComboBox<>("Land");
		landField.setItems(LaenderDataType.values());
		landField.setItemCaptionGenerator(LaenderDataType::getLangText);

		landField.addStyleName("horizontal");
		details.addComponent(landField);

		strasseField = new TextField("Strasse");
		strasseField.setWidth("100%");
		details.addComponent(strasseField);

		hausnummerField = new TextField("Hausnummer");
		hausnummerField.setWidth("100%");
		details.addComponent(hausnummerField);

		plzField = new TextField("Postleitzahl");
		plzField.setWidth("100%");
		details.addComponent(plzField);

		ortField = new TextField("Ort");
		ortField.setWidth("100%");
		details.addComponent(ortField);

		section = new Label("Zusatzinfos");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		emailField2 = new TextField("Email 2");
		emailField2.setWidth("100%");
		details.addComponent(emailField2);

		newsletter2 = new RadioButtonGroup<>("Newsletter 2");
		newsletter2.setItems(JaNeinDataType.values());
		newsletter2.setItemCaptionGenerator(JaNeinDataType::getLangText);
		newsletter2.addStyleName("horizontal");

		details.addComponent(newsletter2);

		emailField3 = new TextField("Email 3");
		emailField3.setWidth("100%");
		details.addComponent(emailField3);

		newsletter3 = new RadioButtonGroup<>("Newsletter 3");
		newsletter3.setItems(JaNeinDataType.values());
		newsletter3.setItemCaptionGenerator(JaNeinDataType::getLangText);
		newsletter3.addStyleName("horizontal");
		details.addComponent(newsletter3);

		websiteField = new TextField("Website");
		websiteField.setPlaceholder("http://");
		websiteField.setWidth("100%");
		details.addComponent(websiteField);

		bioField = new TextArea("Zusatztext");
		bioField.setWidth("100%");
		bioField.setRows(4);
		details.addComponent(bioField);

		bindFields();

		return root;
	}

	private void bindFields() {

		fieldGroupPerson.forField(emailField3).bind(Person::getEmail3, Person::setEmail3);
		fieldGroupPerson.forField(newsletter3)
				.withConverter(JaNeinDataType::getKurzText,
						value -> JaNeinDataType.getJaNeinDataTypeForKurzbezeichnung(value))
				.bind(Person::getNewsletter3, Person::setNewsletter3);
		fieldGroupPerson.forField(websiteField).bind(Person::getWebsite, Person::setWebsite);
		fieldGroupPerson.forField(bioField).bind(Person::getBio, Person::setBio);
		fieldGroupPerson.forField(strasseField).bind(Person::getStrasse, Person::setStrasse);
		fieldGroupPerson.forField(landField).asRequired("bitte Land auswählen")
				.withConverter(LaenderDataType::getKurzText,
						value -> LaenderDataType.getLaenderDataTypeForKurzbezeichnung(value))
				.bind(Person::getLand, Person::setLand);
		fieldGroupPerson.forField(mobNrField).bind(Person::getMobnr, Person::setMobnr);
		fieldGroupPerson.forField(phoneField).bind(Person::getPhone, Person::setPhone);
		fieldGroupPerson.forField(newsletter)
				.withConverter(JaNeinDataType::getKurzText,
						value -> JaNeinDataType.getJaNeinDataTypeForKurzbezeichnung(value))
				.bind(Person::getNewsletter, Person::setNewsletter);
		fieldGroupPerson.forField(emailField).asRequired("Bitte Email angeben").bind(Person::getEmail,
				Person::setEmail);
		fieldGroupPerson.forField(birthDate).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
				.bind(Person::getGebdat, Person::setGebdat);
		fieldGroupPerson.forField(lastNameField).bind(Person::getLastName,
				Person::setLastName);
		fieldGroupPerson.forField(firstNameField).bind(Person::getFirstName,
				Person::setFirstName);
		fieldGroupPerson.forField(titleField).bind(Person::getTitle, Person::setTitle);
		fieldGroupPerson.forField(sexField)
				.withConverter(MenschGeschlechtDataType::getKurzText,
						value -> MenschGeschlechtDataType.getMenschGeschlechtDataTypeForKurzbezeichnung(value))
				.bind(Person::getMale, Person::setMale);
		fieldGroupPerson.forField(hausnummerField).bind(Person::getHausnummer,
				Person::setHausnummer);
		fieldGroupPerson.forField(plzField).bind(Person::getPlz, Person::setPlz);
		fieldGroupPerson.forField(ortField).bind(Person::getOrt, Person::setOrt);
		fieldGroupPerson.forField(emailField2).bind(Person::getEmail2, Person::setEmail2);
		fieldGroupPerson.forField(newsletter2)
				.withConverter(JaNeinDataType::getKurzText,
						value -> JaNeinDataType.getJaNeinDataTypeForKurzbezeichnung(value))
				.bind(Person::getNewsletter2, Person::setNewsletter2);

	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button cancel = new Button("Cancel");
		cancel.addStyleName(ValoTheme.BUTTON_PRIMARY);
		cancel.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				close();

			}
		});
		cancel.focus();
		footer.addComponent(cancel);
		footer.setComponentAlignment(cancel, Alignment.TOP_LEFT);

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {

					if (fieldGroupPerson.validate().isOk()) {
						fieldGroupPerson.writeBeanIfValid(person);
						DBPerson dbio = new DBPerson();
						dbio.savePersion(person);

						Notification success = new Notification("Profile updated successfully");
						success.setDelayMsec(2000);
						success.setStyleName("bar success small");
						success.setPosition(Position.BOTTOM_CENTER);
						success.show(Page.getCurrent());

						if (user == null) {
							if (update == true) {
								DashBoardEventBus.post(new UpdateUserEvent());
							} else {
								DashBoardEventBus.post(new UserNewEvent(person));
							}
						} else {
							DashBoardEventBus.post(new ProfileUpdatedEvent());
						}

						close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("Error while updating profile", Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		return footer;
	}

	public static void open(final User user, final boolean preferencesTabActive) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

	public static void open(final Person person, final boolean update) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new ProfilePreferencesWindow(person, update);
		UI.getCurrent().addWindow(w);
		w.focus();
	}
}