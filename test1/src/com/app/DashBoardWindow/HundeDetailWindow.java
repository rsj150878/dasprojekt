package com.app.DashBoardWindow;

import com.app.Auth.Hund;
import com.app.DashBoard.Event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.DashBoard.Event.DashBoardEvent.DogUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.enumPackage.Rassen;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HundeDetailWindow extends Window {

	public static final String ID = "hundedetailwindow";

	private final BeanFieldGroup<Hund> fieldGroup;

	@PropertyId("bhdatum")
	private DateField bhDatumField;
	@PropertyId("chipnummer")
	private TextField chipnummerField;
	@PropertyId("farbe")
	private TextField farbeField;
	@PropertyId("geschlecht")
	private OptionGroup geschlechtGroup;
	@PropertyId("rasse")
	private ComboBox rasseGroup;
	@PropertyId("rufname")
	private TextField rufnameField;
	@PropertyId("wurfdatum")
	private DateField wurfDatumField;
	@PropertyId("zuchtbuchnummer")
	private TextField zuchtbuchnummerField;
	@PropertyId("zuechter")
	private TextField zuechterField;
	@PropertyId("zwingername")
	private TextField zwingernameField;

	private final Hund hund;

	private HundeDetailWindow(final Hund hund) {

		this.hund = hund;
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
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

		content.addComponent(buildFooter());

		fieldGroup = new BeanFieldGroup<Hund>(Hund.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(hund);

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

		rufnameField = new TextField("Rufname");
		details.addComponent(rufnameField);

		zwingernameField = new TextField("Zwingername");
		zwingernameField.setNullRepresentation("");
		details.addComponent(zwingernameField);

		chipnummerField = new TextField("Chipnummer");
		details.addComponent(chipnummerField);

		zuchtbuchnummerField = new TextField("Zuchtbuchnummer");
		zuchtbuchnummerField.setNullRepresentation("");
		details.addComponent(zuchtbuchnummerField);

		geschlechtGroup = new OptionGroup("Geschlecht");
		geschlechtGroup.addItem("R");
		geschlechtGroup.setItemCaption("R", "Rüde");
		geschlechtGroup.addItem("H");
		geschlechtGroup.setItemCaption("H", "Hündin");
		geschlechtGroup.addStyleName("horizontal");
		details.addComponent(geschlechtGroup);

		rasseGroup = new ComboBox("Rasse");

		for (Rassen x : Rassen.values()) {
			rasseGroup.addItem(x.getRassenKurzBezeichnung());
			rasseGroup.setItemCaption(x.getRassenKurzBezeichnung(),
					x.getRassenLangBezeichnung());
		}

		rasseGroup.addStyleName("horizontal");
		details.addComponent(rasseGroup);

		wurfDatumField = new PopupDateField("Wurfdatum");
		wurfDatumField.setWidth("100%");
		wurfDatumField.setResolution(Resolution.DAY);
		wurfDatumField.setDateFormat("dd.MM.yyyy");
		details.addComponent(wurfDatumField);

		farbeField = new TextField("Farbe");
		farbeField.setNullRepresentation("");
		details.addComponent(farbeField);

		zuechterField = new TextField("Züchter");
		zuechterField.setNullRepresentation("");
		details.addComponent(zuechterField);

		bhDatumField = new PopupDateField("BH-Datum");
		bhDatumField.setWidth("100%");
		bhDatumField.setResolution(Resolution.DAY);
		bhDatumField.setDateFormat("dd.MM.yyyy");
		details.addComponent(bhDatumField);

		// Label section = new Label("Contact Info");
		// section.addStyleName(ValoTheme.LABEL_H4);
		// section.addStyleName(ValoTheme.LABEL_COLORED);
		// details.addComponent(section);

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
					fieldGroup.commit();
					// Updated user should also be persisted to database. But
					// not in this demo.

					hund.commit();
					Notification success = new Notification(
							"Hundedaten erfolgreich gespeichert");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());

					DashBoardEventBus.post(new DogUpdatedEvent());
					close();
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("Es ist ein Fehler passiert",
							Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
		return footer;
	}

	public static void open(final Hund hund) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new HundeDetailWindow(hund);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
