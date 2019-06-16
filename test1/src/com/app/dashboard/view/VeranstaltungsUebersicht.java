package com.app.dashboard.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.app.auth.User;
import com.app.dashboard.event.DashBoardEvent.NeueVeranstaltung;
import com.app.dashboard.event.DashBoardEvent.ReportsCountUpdatedEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.enumdatatypes.VeranstaltungsTypen;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.google.common.eventbus.Subscribe;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.ContentMode;
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
public class VeranstaltungsUebersicht extends TabSheet
		implements View, CloseHandler, VeranstaltungsDetailViewNeu.VeranstaltungsDetailListener {

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";

	private DBVeranstaltung dbVeranstaltung = new DBVeranstaltung();
	private List<Veranstaltung> veranstaltungsList;

	public VeranstaltungsUebersicht() {
		setSizeFull();
		addStyleName("reports");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		setCloseHandler(this);

		DashBoardEventBus.register(this);

		try {
			veranstaltungsList = dbVeranstaltung.getAllAktiveVeranstaltungen();

		} catch (Exception e) {
			Notification.show("Fehler beim ermitteln der Veranstaltungen");
			e.printStackTrace();

		}

		addTab(buildDrafts());
	}

	// private Item commit() {
	// Item returnItem = null;
	// try {
	// veranstaltungsStufenContainer.commit();
	// veranstaltungsStufenContainer.refresh();
	//
	// if (veranstaltungsId != null) {
	// veranstaltungsContainer
	// .addContainerFilter(new Equal("id_veranstaltung",
	// veranstaltungsId.getId()[0].toString()));
	// returnItem =
	// veranstaltungsContainer.getItem(veranstaltungsContainer.getIdByIndex(0));
	// veranstaltungsContainer.removeAllContainerFilters();
	// }
	//
	// } catch (SQLException ee) {
	// ee.printStackTrace();
	// }
	//
	// return returnItem;
	// }

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

		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

		if (user.getRole().contains("admin")
		// || (!(view.getRollen() == null) &&
		// (user.getRole().contains(view.getRollen())))
		) {
			allDrafts.addComponent(buildCreateBox());
		}

		buildDraftsList(allDrafts);

		draftsPanel.setContent(allDrafts);

		return draftsPanel;
	}

	private Component buildDraftsList(VerticalLayout parentLayout) {

		HorizontalLayout drafts = new HorizontalLayout();
		drafts.setSpacing(true);
		drafts.setSizeUndefined();

		System.out.println(
				"veranstaltungsdatum " + new SimpleDateFormat("yyyy").format(veranstaltungsList.get(0).getDatum()));

		String year = new SimpleDateFormat("yyyy").format(veranstaltungsList.get(0).getDatum());

		Label yearLabel = new Label(year);
		yearLabel.setSizeUndefined();

		Panel yearPanel = new Panel();
		yearPanel.setSizeUndefined();
		yearPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		yearPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);

		parentLayout.addComponent(yearLabel);
		parentLayout.addComponent(yearPanel);

		yearPanel.setContent(drafts);

		for (Veranstaltung itemId : veranstaltungsList) {

			if (!year.equals(new SimpleDateFormat("yyyy").format(itemId.getDatum()))) {

				yearPanel.setContent(drafts);

				year = new SimpleDateFormat("yyyy").format(itemId.getDatum());

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

			drafts.addComponent(buildDraftThumb(itemId));

		}

		yearPanel.setContent(drafts);

		return drafts;
	}

	private Component buildDraftThumb(Veranstaltung veranstaltungsItem) {

		final Veranstaltung vaItem = veranstaltungsItem;

		VerticalLayout draftThumb = new VerticalLayout();
		draftThumb.setWidth(160.0f, Unit.PIXELS);
		draftThumb.setHeight(200.0f, Unit.PIXELS);
		draftThumb.addStyleName("draft-thumb");

		draftThumb.setSpacing(true);

		StringBuilder sb = new StringBuilder();
		sb.append("<p align = \"center\"><b>");
		sb.append(vaItem.getTyp().getVeranstaltungsTypBezeichnung());

		sb.append("</b><br>");
		sb.append(new SimpleDateFormat("dd.MM.yyyy").format(vaItem.getDatum()));
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
				if (event.getButton() == MouseButton.LEFT && event.getChildComponent() != delete) {

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

	public void openReport(Veranstaltung toOpenVeranstaltung) {

		VeranstaltungsDetailViewNeu detailView = new VeranstaltungsDetailViewNeu(toOpenVeranstaltung.getTyp(),
				toOpenVeranstaltung, this);

		String title = toOpenVeranstaltung.getTyp().getVeranstaltungsTypBezeichnung();

		title += " " + new SimpleDateFormat("dd.MM.yyyy").format(toOpenVeranstaltung.getDatum());

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(getComponentCount() - 1));

		detailView.setTitle(title);
		setSelectedTab(getComponentCount() - 1);

	}

	@Subscribe
	public void addReport(NeueVeranstaltung neueVeranstaltung) {

		Veranstaltung newVeranstaltung = new Veranstaltung();
		newVeranstaltung.setTyp(neueVeranstaltung.getVeranstaltungsTyp());

		newVeranstaltung.setName("neue Veranstaltung");
		newVeranstaltung.setRichter("neuer Richter");
		newVeranstaltung.setVeranstalter("neuer Richter");
		newVeranstaltung.setVeranstaltungsort("neuer Veranstaltungsort");
		newVeranstaltung.setVeranstaltungsleiter("neuer Veranstaltungsleiter");
		newVeranstaltung.setDatum(new Date());

		try {
			dbVeranstaltung.saveVeranstaltung(newVeranstaltung);

			for (Integer stufe : neueVeranstaltung.getVeranstaltungsTyp().getVeranstaltungsStufen()) {
				VeranstaltungsStufe zwVeranstaltungsStufe = new VeranstaltungsStufe();

				zwVeranstaltungsStufe.setIdVeranstaltung(newVeranstaltung.getId_veranstaltung());
				zwVeranstaltungsStufe.setStufenTyp(VeranstaltungsStufen.getBezeichnungForId(stufe));
				dbVeranstaltung.saveVeranstaltungStufe(zwVeranstaltungsStufe);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("fehler beim erstellen der Veranstaltung");
		}

		
		VeranstaltungsDetailViewNeu detailView = new VeranstaltungsDetailViewNeu(
				neueVeranstaltung.getVeranstaltungsTyp(), newVeranstaltung, this);

		String title = newVeranstaltung.getTyp().getVeranstaltungsTypBezeichnung();

		title += " " + new SimpleDateFormat("dd.MM.yyyy").format(newVeranstaltung.getDatum());

		addTab(detailView).setClosable(true);

		DashBoardEventBus.post(new ReportsCountUpdatedEvent(getComponentCount() - 1));

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
		confirmDialog.addCloseShortcut(KeyCode.ESCAPE, null);
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
				// saveComponent.commit();
				removeComponent(tabContent);
				DashBoardEventBus.post(new ReportsCountUpdatedEvent(getComponentCount() - 1));
				Notification.show("Die Veranstaltung wurde gespeichert", Type.TRAY_NOTIFICATION);
			}
		});
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

		Button discard = new Button("Discard Changes", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				confirmDialog.close();
				removeComponent(tabContent);
				DashBoardEventBus.post(new ReportsCountUpdatedEvent(getComponentCount() - 1));
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

		public PopupTextFieldContent() {

			layout.setSizeUndefined();
			layout.setSpacing(true);
			layout.setMargin(true);

			layout.addComponent(titel);

			ClickListener listener = new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					DashBoardEventBus.post(new NeueVeranstaltung((VeranstaltungsTypen) event.getButton().getData()));
				}

			};

			layout.setComponentAlignment(titel, Alignment.MIDDLE_CENTER);
		
			for (VeranstaltungsTypen x:VeranstaltungsTypen.values()) {
				if (x.getIsAktiv()) {
					Button newVa = new Button (x.getVeranstaltungsTypBezeichnung());
					layout.addComponent(newVa);
					layout.setComponentAlignment(newVa, Alignment.MIDDLE_CENTER);
					newVa.setData(x);
					newVa.addClickListener(listener);
				}
			}
			
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
	public void titleChanged(final String newTitle, final VeranstaltungsDetailViewNeu editor) {
		getTab(editor).setCaption(newTitle);
	}

}
