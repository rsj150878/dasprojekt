package com.app.DashBoard.View;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.app.DashBoard.Event.DashBoardEvent.NeueVeranstaltung;
import com.app.DashBoard.Event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.VeranstaltungsTypen;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class VeranstaltungsUebersicht extends TabSheet implements View,
		CloseHandler, QueryDelegate.RowIdChangeListener,
		VeranstaltungsDetailViewNeu.VeranstaltungsDetailListener {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";
	private SQLContainer veranstaltungsContainer;
	private SQLContainer veranstaltungsStufenContainer;
	private TableQuery q1;
	private TableQuery q2;

	private RowId veranstaltungsId;

	public VeranstaltungsUebersicht() {
		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		setCloseHandler(this);

		DashBoardEventBus.register(this);

		q1 = new TableQuery("veranstaltung",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		q2 = new TableQuery("veranstaltungs_stufe",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");

		try {
			veranstaltungsContainer = new SQLContainer(q1);
			veranstaltungsContainer.addOrderBy(new OrderBy("datum", false));
			veranstaltungsStufenContainer = new SQLContainer(q2);

			veranstaltungsContainer.addRowIdChangeListener(this);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		addTab(buildDrafts());
	}

	private Item commit() {
		Item returnItem = null;
		try {
			veranstaltungsContainer.commit();
			veranstaltungsStufenContainer.commit();

			veranstaltungsContainer.refresh();
			veranstaltungsStufenContainer.refresh();

			if (veranstaltungsId != null) {
				veranstaltungsContainer.addContainerFilter(new Equal(
						"id_veranstaltung", veranstaltungsId.getId()[0]
								.toString()));
				returnItem = veranstaltungsContainer
						.getItem(veranstaltungsContainer.getIdByIndex(0));
				veranstaltungsContainer.removeAllContainerFilters();
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}

		return returnItem;
	}

	@Override
	public void rowIdChange(RowIdChangeEvent event) {
		veranstaltungsId = event.getNewRowId();

	}

	private Component buildDrafts() {

		Panel draftsPanel = new Panel("Alle Veranstaltungen");
		draftsPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		draftsPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		draftsPanel.setSizeUndefined();

		final VerticalLayout allDrafts = new VerticalLayout();
		allDrafts.setSizeFull();
		allDrafts.setSpacing(true);
		allDrafts.setMargin(true);
		// allDrafts.setCaption("Alle Veranstaltungen");

		// VerticalLayout titleAndDrafts = new VerticalLayout();
		// titleAndDrafts.setSizeUndefined();
		// titleAndDrafts.setSpacing(true);
		// titleAndDrafts.addStyleName("drafts");
		// allDrafts.addComponent(titleAndDrafts);
		// allDrafts
		// .setComponentAlignment(titleAndDrafts, Alignment.MIDDLE_CENTER);
		allDrafts.addComponent(buildCreateBox());

		buildDraftsList(allDrafts);

		draftsPanel.setContent(allDrafts);

		return draftsPanel;
	}

	private Component buildDraftsList(VerticalLayout parentLayout) {

		HorizontalLayout drafts = new HorizontalLayout();
		drafts.setSpacing(true);
		drafts.setSizeUndefined();

		System.out.println("veranstaltungsdatum "
				+ veranstaltungsContainer
						.getItem(veranstaltungsContainer.getIdByIndex(0))
						.getItemProperty("datum").getValue().toString());

		String year = veranstaltungsContainer
				.getItem(veranstaltungsContainer.getIdByIndex(0))
				.getItemProperty("datum").getValue().toString().substring(0, 4);

		Label yearLabel = new Label(year);
		yearLabel.setSizeUndefined();

		Panel yearPanel = new Panel();
		yearPanel.setSizeUndefined();
		yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		parentLayout.addComponent(yearLabel);
		parentLayout.addComponent(yearPanel);

		yearPanel.setContent(drafts);

		for (Object itemId : veranstaltungsContainer.getItemIds()) {

			if (!year.equals(veranstaltungsContainer.getItem(itemId)
					.getItemProperty("datum").getValue().toString()
					.substring(0, 4))) {

				yearPanel.setContent(drafts);

				year = veranstaltungsContainer.getItem(itemId)
						.getItemProperty("datum").getValue().toString()
						.substring(0, 4);

				yearLabel = new Label(year);
				yearLabel.setSizeUndefined();

				yearPanel = new Panel();
				yearPanel.setSizeUndefined();
				yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
				yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

				parentLayout.addComponent(yearLabel);
				parentLayout.addComponent(yearPanel);

				drafts = new HorizontalLayout();
				drafts.setSpacing(true);
				drafts.setSizeUndefined();

			}

			drafts.addComponent(buildDraftThumb(veranstaltungsContainer
					.getItem(itemId)));

		}

		yearPanel.setContent(drafts);

		return drafts;
	}

	private Component buildDraftThumb(Item veranstaltungsItem) {

		final Item vaItem = veranstaltungsItem;

		VerticalLayout draftThumb = new VerticalLayout();
		draftThumb.setWidth(160.0f, Unit.PIXELS);
		draftThumb.setHeight(200.0f, Unit.PIXELS);
		draftThumb.addStyleName("draft-thumb");

		draftThumb.setSpacing(true);

		StringBuilder sb = new StringBuilder();
		sb.append("<p align = \"center\"><b>");
		sb.append(VeranstaltungsTypen.getVeranstaltungsTypForId(
				Integer.valueOf(vaItem.getItemProperty("typ").getValue()
						.toString())).getVeranstaltungsTypBezeichnung());

		sb.append("</b><br>");
		sb.append(new SimpleDateFormat("dd.MM.yyyy").format(vaItem
				.getItemProperty("datum").getValue()));
		sb.append("</p>");

		Label draftTitle = new Label(sb.toString(), ContentMode.HTML);
		draftTitle.setSizeUndefined();
		draftThumb.addComponent(draftTitle);

		final Button delete = new Button("×");
		delete.setDescription("Delete draft");
		delete.setPrimaryStyleName("delete-button");
		delete.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				Notification.show("Not implemented in this demo");
			}
		});
		draftThumb.addComponent(delete);

		draftThumb.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(final LayoutClickEvent event) {
				if (event.getButton() == MouseButton.LEFT
						&& event.getChildComponent() != delete) {

					openReport(vaItem);
				}
			}
		});

		return draftThumb;
	}

	private Component buildCreateBox() {
		VerticalLayout createBox = new VerticalLayout();
		createBox.setWidth(160.0f, Unit.PIXELS);
		createBox.setHeight(200.0f, Unit.PIXELS);
		createBox.addStyleName("create");

		final PopupView popupView = new PopupView(new PopupTextFieldContent());
		createBox.addComponent(popupView);
		createBox.setComponentAlignment(popupView, Alignment.MIDDLE_CENTER);

		return createBox;
	}

	public void openReport(Item toOpenVeranstaltung) {

		VeranstaltungsTypen openTyp = VeranstaltungsTypen
				.getVeranstaltungsTypForId(Integer.valueOf(toOpenVeranstaltung
						.getItemProperty("typ").getValue().toString()));

		VeranstaltungsDetailViewNeu detailView = new VeranstaltungsDetailViewNeu(
				openTyp, toOpenVeranstaltung, this);

		String title = openTyp.getVeranstaltungsTypBezeichnung();

		title += " "
				+ new SimpleDateFormat("dd.MM.yyyy").format(toOpenVeranstaltung
						.getItemProperty("datum").getValue());

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(
				getComponentCount() - 1));

		detailView.setTitle(title);
		setSelectedTab(getComponentCount() - 1);

	}

	@Subscribe
	public void addReport(NeueVeranstaltung neueVeranstaltung) {

		Item newVeranstaltung = veranstaltungsContainer
				.getItem(veranstaltungsContainer.addItem());
		newVeranstaltung.getItemProperty("typ").setValue(
				neueVeranstaltung.getVeranstaltungsTyp()
						.getVeranstaltungsTypID());

		newVeranstaltung.getItemProperty("name").setValue("neue Veranstaltung");
		newVeranstaltung.getItemProperty("richter").setValue("neuer Richter");
		newVeranstaltung.getItemProperty("veranstalter").setValue(
				"neuer Richter");
		newVeranstaltung.getItemProperty("veranstaltungsort").setValue(
				"neuer Veranstaltungsort");
		newVeranstaltung.getItemProperty("veranstaltungsleiter").setValue(
				"neuer Veranstaltungsleiter");
		newVeranstaltung.getItemProperty("datum").setValue(new Date());

		newVeranstaltung = commit();

		for (Integer stufe : neueVeranstaltung.getVeranstaltungsTyp()
				.getVeranstaltungsStufen()) {
			Object zwVeranstaltungsStufe = veranstaltungsStufenContainer
					.addItem();
			Item zwVeranstaltungsItem = veranstaltungsStufenContainer
					.getItemUnfiltered(zwVeranstaltungsStufe);
			zwVeranstaltungsItem.getItemProperty("id_veranstaltung").setValue(
					newVeranstaltung.getItemProperty("id_veranstaltung")
							.getValue());
			zwVeranstaltungsItem.getItemProperty("stufen_typ").setValue(stufe);

		}

		newVeranstaltung = commit();

		VeranstaltungsDetailViewNeu detailView = new VeranstaltungsDetailViewNeu(
				neueVeranstaltung.getVeranstaltungsTyp(), newVeranstaltung,
				this);

		String title = VeranstaltungsTypen.getVeranstaltungsTypForId(
				Integer.valueOf(newVeranstaltung.getItemProperty("typ")
						.getValue().toString()))
				.getVeranstaltungsTypBezeichnung();

		title += " "
				+ new SimpleDateFormat("dd.MM.yyyy").format(newVeranstaltung
						.getItemProperty("datum").getValue());

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(
				getComponentCount() - 1));

		detailView.setTitle(title);
		setSelectedTab(getComponentCount() - 1);

	}

	@Override
	public void onTabClose(final TabSheet tabsheet, final Component tabContent) {
		Label message = new Label(
				"You have not saved this report. Do you want to save or discard any changes you've made to this report?");
		message.setWidth("25em");

		final Window confirmDialog = new Window("Unsaved Changes");
		confirmDialog.setId(CONFIRM_DIALOG_ID);
		confirmDialog.setCloseShortcut(KeyCode.ESCAPE, null);
		confirmDialog.setModal(true);
		confirmDialog.setClosable(false);
		confirmDialog.setResizable(false);

		VerticalLayout root = new VerticalLayout();
		root.setSpacing(true);
		root.setMargin(true);
		confirmDialog.setContent(root);

		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth("100%");
		footer.setSpacing(true);

		root.addComponents(message, footer);

		Button ok = new Button("Save", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				confirmDialog.close();
				VeranstaltungsDetailViewNeu saveComponent = (VeranstaltungsDetailViewNeu) tabContent;
				saveComponent.commit();
				removeComponent(tabContent);
				DashBoardEventBus.post(new ReportsCountUpdatedEvent(
						getComponentCount() - 1));
				Notification.show("Die Veranstaltung wurde gespeichert",
						Type.TRAY_NOTIFICATION);
			}
		});
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

		Button discard = new Button("Discard Changes", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				confirmDialog.close();
				removeComponent(tabContent);
				DashBoardEventBus.post(new ReportsCountUpdatedEvent(
						getComponentCount() - 1));
			}
		});
		discard.addStyleName(ValoTheme.BUTTON_DANGER);

		Button cancel = new Button("Cancel", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				confirmDialog.close();
			}
		});

		footer.addComponents(discard, cancel, ok);
		footer.setExpandRatio(discard, 1);

		getUI().addWindow(confirmDialog);
		confirmDialog.focus();
	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	// Create a dynamically updating content for the popup
	public class PopupTextFieldContent implements PopupView.Content {

		private final Panel menuPanel = new Panel();
		private final VerticalLayout layout = new VerticalLayout();
		private final Label titel = new Label("Veranstaltungstypauswahl");

		private Button newRbpmWasser = new Button("RBP mit Wasser");
		private Button newRbpoWasser = new Button("RBP ohne Wasser");
		private Button newGap = new Button("GAP");
		private Button newBgh = new Button("BH/BGH");
		private Button newWt = new Button("Train-Wt");
		private Button newWesensTest = new Button("Wesenstest");

		public PopupTextFieldContent() {

			layout.setSizeUndefined();
			layout.setSpacing(true);
			layout.setMargin(true);

			layout.addComponent(titel);

			ClickListener listener = new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					DashBoardEventBus.post(new NeueVeranstaltung(
							(VeranstaltungsTypen) event.getButton().getData()));
				}

			};

			layout.setComponentAlignment(titel, Alignment.MIDDLE_CENTER);
			layout.addComponent(newRbpmWasser);
			layout.setComponentAlignment(newRbpmWasser, Alignment.MIDDLE_CENTER);
			newRbpmWasser.setData(VeranstaltungsTypen.RBP_M_WASSER);
			newRbpmWasser.addClickListener(listener);

			layout.addComponent(newRbpoWasser);
			layout.setComponentAlignment(newRbpoWasser, Alignment.MIDDLE_CENTER);
			newRbpoWasser.setData(VeranstaltungsTypen.RBP_O_WASSER);
			newRbpoWasser.addClickListener(listener);

			layout.addComponent(newGap);
			layout.setComponentAlignment(newGap, Alignment.MIDDLE_CENTER);
			newGap.setData(VeranstaltungsTypen.GAP_PRÜFUNG);
			newGap.addClickListener(listener);

			layout.addComponent(newBgh);
			layout.setComponentAlignment(newBgh, Alignment.MIDDLE_CENTER);
			newBgh.setData(VeranstaltungsTypen.BH_BGH_PRÜFUNG);
			newBgh.addClickListener(listener);

			layout.addComponent(newWt);
			layout.setComponentAlignment(newWt, Alignment.MIDDLE_CENTER);
			newWt.setData(VeranstaltungsTypen.TRAIN_WT);
			newWt.addClickListener(listener);

			layout.addComponent(newWesensTest);
			layout.setComponentAlignment(newWesensTest, Alignment.MIDDLE_CENTER);
			newWesensTest.setData(VeranstaltungsTypen.WESENSTEST);
			newWesensTest.addClickListener(listener);

			menuPanel.setContent(layout);
		}

		@Override
		public final Component getPopupComponent() {
			System.out.println("in popup");
			return menuPanel;
		}

		@Override
		public String getMinimizedValueAsHTML() {
			// TODO Auto-generated method stub
			return "Klick für neue Veranstaltung";
		}

	};

	@Override
	public void titleChanged(final String newTitle,
			final VeranstaltungsDetailViewNeu editor) {
		getTab(editor).setCaption(newTitle);
	}

}
