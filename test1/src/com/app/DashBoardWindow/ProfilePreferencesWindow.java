package com.app.DashBoardWindow;

import com.app.Auth.Person;
import com.app.Auth.User;
import com.app.DashBoard.Event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.DashBoard.Event.DashBoardEvent.ProfileUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEvent.UpdateUserEvent;
import com.app.DashBoard.Event.DashBoardEvent.UserNewEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
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
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
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

	private final BeanFieldGroup<Person> fieldGroupPerson;
	private final BeanFieldGroup<User> fieldGroupUser;
	/*
	 * Fields for editing the User object are defined here as class members.
	 * They are later bound to a FieldGroup by calling
	 * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
	 * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
	 * the fields with the user object.
	 */
	@PropertyId("firstName")
	private TextField firstNameField;
	@PropertyId("lastName")
	private TextField lastNameField;
	@PropertyId("title")
	private TextField titleField;
	@PropertyId("male")
	private OptionGroup sexField;
	@PropertyId("gebdat")
	private DateField birthDate;
	@PropertyId("email")
	private TextField emailField;
	@PropertyId("strasse")
	private TextField strasseField;
	@PropertyId("hausnummer")
	private TextField hausnummerField;
	@PropertyId("plz")
	private TextField plzField;
	@PropertyId("ort")
	private TextField ortField;

	@PropertyId("phone")
	private TextField phoneField;
	@PropertyId("mobnr")
	private TextField mobNrField;

	@PropertyId("land")
	private ComboBox landField;
	@PropertyId("website")
	private TextField websiteField;
	@PropertyId("bio")
	private TextArea bioField;
	@PropertyId("newsletter")
	private OptionGroup newsletter;
	@PropertyId("email2")
	private TextField emailField2;
	@PropertyId("newsletter2")
	private OptionGroup newsletter2;
	@PropertyId("email3")
	private TextField emailField3;
	@PropertyId("newsletter3")
	private OptionGroup newsletter3;

	private final User user;
	private final Person person;
	private final boolean update;

	private ProfilePreferencesWindow(final User user,
			final boolean preferencesTabOpen) {

		this.user = user;
		this.person = null;
		this.update = false;

		initWindow(preferencesTabOpen);

		fieldGroupPerson = null;

		fieldGroupUser = new BeanFieldGroup<User>(User.class);
		fieldGroupUser.bindMemberFields(this);
		fieldGroupUser.setItemDataSource(user);

	}

	private ProfilePreferencesWindow(final Person person, final boolean update) {

		this.user = null;
		this.person = person;
		this.update = update;

		initWindow(false);

		fieldGroupUser = null;

		fieldGroupPerson = new BeanFieldGroup<Person>(Person.class);
		fieldGroupPerson.bindMemberFields(this);
		fieldGroupPerson.setItemDataSource(person);

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
		root.setIcon(FontAwesome.COGS);
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
		root.setIcon(FontAwesome.USER);
		root.setWidth(100.0f, Unit.PERCENTAGE);
		root.setSpacing(true);
		root.setMargin(true);
		root.addStyleName("profile-form");

		VerticalLayout pic = new VerticalLayout();
		pic.setSizeUndefined();
		pic.setSpacing(true);
		Image profilePic = new Image(null, new ThemeResource(
				"img/profile-pic-300px.jpg"));
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

		sexField = new OptionGroup("Anrede");
		sexField.addItem("M");
		sexField.setItemCaption("M", "Herr");
		sexField.addItem("F");
		sexField.setItemCaption("F", "Frau");
		sexField.addStyleName("horizontal");
		details.addComponent(sexField);

		titleField = new TextField("Titel");
		details.addComponent(titleField);

		firstNameField = new TextField("Vorname");
		details.addComponent(firstNameField);
		lastNameField = new TextField("Familienname");
		details.addComponent(lastNameField);

		birthDate = new PopupDateField("GeburtsDatum");
		birthDate.setWidth("100%");
		birthDate.setResolution(Resolution.DAY);
		birthDate.setDateFormat("dd.MM.yyyy");
		details.addComponent(birthDate);

		Label section = new Label("Contact Info");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		emailField = new TextField("Email");
		emailField.setWidth("100%");
		emailField.setRequired(true);
		emailField.setNullRepresentation("");
		details.addComponent(emailField);

		newsletter = new OptionGroup("Newsletter");
		newsletter.addItem("J");
		newsletter.setItemCaption("J", "Ja");
		newsletter.addItem("N");
		newsletter.setItemCaption("N", "Nein");
		newsletter.addStyleName("horizontal");
		details.addComponent(newsletter);

		phoneField = new TextField("Telefon");
		phoneField.setWidth("100%");
		phoneField.setNullRepresentation("");
		details.addComponent(phoneField);

		mobNrField = new TextField("Mobil");
		mobNrField.setWidth("100%");
		mobNrField.setNullRepresentation("");
		details.addComponent(mobNrField);

		section = new Label("Addresse");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		landField = new ComboBox("Land");
		landField.addItem("AT");
		landField.setItemCaption("AT", "Österreich");
		landField.addItem("DE");
		landField.setItemCaption("DE", "Deutschland");
		landField.addItem("CH");
		landField.setItemCaption("CH", "Schweiz");
		landField.addStyleName("horizontal");
		details.addComponent(landField);

		strasseField = new TextField("Strasse");
		strasseField.setWidth("100%");
		strasseField.setNullRepresentation("");
		details.addComponent(strasseField);

		hausnummerField = new TextField("Hausnummer");
		hausnummerField.setWidth("100%");
		hausnummerField.setNullRepresentation("");
		details.addComponent(hausnummerField);

		plzField = new TextField("Postleitzahl");
		plzField.setWidth("100%");
		plzField.setNullRepresentation("");
		details.addComponent(plzField);

		ortField = new TextField("Ort");
		ortField.setWidth("100%");
		ortField.setNullRepresentation("");
		details.addComponent(ortField);

		section = new Label("Zusatzinfos");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		emailField2 = new TextField("Email 2");
		emailField2.setWidth("100%");
		emailField2.setNullRepresentation("");
		details.addComponent(emailField2);

		newsletter2 = new OptionGroup("Newsletter 2");
		newsletter2.addItem("J");
		newsletter2.setItemCaption("J", "Ja");
		newsletter2.addItem("N");
		newsletter2.setItemCaption("N", "Nein");
		newsletter2.addStyleName("horizontal");
		details.addComponent(newsletter2);

		emailField3 = new TextField("Email 3");
		emailField3.setWidth("100%");
		emailField3.setNullRepresentation("");
		details.addComponent(emailField3);

		newsletter3 = new OptionGroup("Newsletter 3");
		newsletter3.addItem("J");
		newsletter3.setItemCaption("J", "Ja");
		newsletter3.addItem("N");
		newsletter3.setItemCaption("N", "Nein");
		newsletter3.addStyleName("horizontal");
		details.addComponent(newsletter3);

		websiteField = new TextField("Website");
		websiteField.setInputPrompt("http://");
		websiteField.setWidth("100%");
		websiteField.setNullRepresentation("");
		details.addComponent(websiteField);

		bioField = new TextArea("Zusatztext");
		bioField.setWidth("100%");
		bioField.setRows(4);
		bioField.setNullRepresentation("");
		details.addComponent(bioField);

		return root;
	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {

					// Updated user should also be persisted to database. But
					// not in this demo.

					if (user == null) {
						fieldGroupPerson.commit();
						person.commit();
					} else {
						fieldGroupUser.commit();
						user.commit();
					}

					Notification success = new Notification(
							"Profile updated successfully");
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
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("Error while updating profile",
							Type.ERROR_MESSAGE);
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