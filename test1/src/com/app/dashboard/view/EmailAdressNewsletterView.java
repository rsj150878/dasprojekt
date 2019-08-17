package com.app.dashboard.view;

import java.util.List;
import java.util.Locale;

import com.app.auth.DataProvider;
import com.app.auth.EmailForEmailVerteiler;
import com.app.auth.MitgliederListe;
import com.app.dashboard.event.DashBoardEvent.BrowserResizeEvent;
import com.app.dashboard.event.DashBoardEvent.UpdateUserEvent;
import com.app.dashboard.event.DashBoardEvent.UserNewEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBMail;
import com.app.email.EmailList;
import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.renderers.HtmlRenderer;


@SuppressWarnings({ "serial", "unchecked" })
public class EmailAdressNewsletterView extends VerticalLayout implements View {

	private final Grid <EmailList>table;
	private Button neuesMitglied;
	private List<EmailList> mitgliederListe;
	private DBMail dbMail;

	public EmailAdressNewsletterView() {

		setSizeFull();
		addStyleName("mitglieder");
		DashBoardEventBus.register(this);
		dbMail = new DBMail();

		addComponent(buildToolbar());

		table = buildTable();
		addComponent(table);
		setExpandRatio(table, 1);
	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashBoardEventBus.unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("Mitgliederliste");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		neuesMitglied = buildNeuesMitglied();
		HorizontalLayout tools = new HorizontalLayout(neuesMitglied);
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private Button buildNeuesMitglied() {
		final Button createReport = new Button("neues Adresse");
		createReport
				.setDescription("Create a new report from the selected transactions");
		createReport.setEnabled(true);
		createReport.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				EmailForEmailVerteiler email = new EmailForEmailVerteiler();
				// ProfilePreferencesWindow.open(person, false);
				// System.out.println(" nach open ");
				mitgliederListe.addItem(email);

			}
		});

		return createReport;
	}

	private Grid<EmailList> buildTable() {
		final Grid <EmailList> gridTable = new Grid<>();
		gridTable.setSizeFull();

		gridTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
		gridTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		gridTable.addStyleName(ValoTheme.TABLE_COMPACT);

		gridTable.setColumnReorderingAllowed(false);

//		mitgliederListe = new TempTransactionsContainer(
//				DataProvider.getEmailList());

		mitgliederListe = dbMail.getAllEmails();
		
		gridTable.setItems(mitgliederListe);

		gridTable.setEditorEnabled(true);
		gridTable.setColumnOrder("id","emailAdresse", "newsLetter");
		//gridTable.getColumn("id").setHeaderCaption("id");

		// gridTable.getColumn("familienName").setHeaderCaption("Familienname");
		// gridTable.getColumn("adresse").setHeaderCaption("Adresse");
		// gridTable.getColumn("edit").setHeaderCaption("Hunde");
		gridTable.setColumns("id","emailAdresse", "newsLetter");

		Grid.Column linkColumn = gridTable.getColumn("newsLetter");
		linkColumn.setRenderer(new HtmlRenderer(),
				new Converter<String, String>() {
					@Override
					public String convertToModel(String value,
							Class<? extends String> targetType, Locale locale)
							throws Converter.ConversionException {
						return "not implemented";
					}

					@Override
					public String convertToPresentation(String value,
							Class<? extends String> targetType, Locale locale)
							throws Converter.ConversionException {
						if ("J".equals(value))
							return FontAwesome.CHECK_CIRCLE_O.getHtml();
						else
							return FontAwesome.CIRCLE_O.getHtml();

					}

					@Override
					public Class<String> getModelType() {
						return String.class;
					}

					@Override
					public Class<String> getPresentationType() {
						return String.class;
					}
				});

		// gridTable.setImmediate(true);

		ComboBox editNewsletter = new ComboBox();
		editNewsletter.addItem("J");
		editNewsletter.setItemCaption("J", "Ja");

		editNewsletter.addItem("N");
		editNewsletter.setItemCaption("N", "Nein");
		gridTable.getColumn("newsLetter").setEditorField(editNewsletter);

		gridTable.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub

			}

			@Override
			public void postCommit(CommitEvent commitEvent)
					throws CommitException {
				
				BeanItem<EmailForEmailVerteiler> zw = (BeanItem<EmailForEmailVerteiler>) commitEvent.getFieldBinder().getItemDataSource();
				System.out.println("zw " + zw.getClass().getName());
				EmailForEmailVerteiler emfvt = zw.getBean();
				
				try {
					emfvt.save();
				} catch (Exception e) {
					Notification.show("Fehler beim speichern");
					
				}
				//System.out.println("id" + mitgliederListe.getItem(zw.getItemProperty("id")));
			}

		});

		return gridTable;
	}

	@Subscribe
	public void newUserEvent(UserNewEvent event) {
		System.out.println("in new event");
		MitgliederListe zw = new MitgliederListe();
		zw.setPerson(event.getPerson());
		// mitgliederListe.addItem(zw);

	}

	@Subscribe
	public void updateUserEvent(UpdateUserEvent event) {
		// mitgliederListe.update();
	}

	
	// TODO
	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		if (Page.getCurrent().getBrowserWindowWidth() < 800) {
			table.removeColumn("adresse");

		}
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	
}
