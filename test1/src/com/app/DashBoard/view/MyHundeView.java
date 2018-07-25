package com.app.dashboard.view;

import java.util.Collection;
import java.util.Iterator;

import com.app.auth.Hund;
import com.app.auth.User;
import com.app.dashboard.event.DashBoardEvent.DogUpdatedEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dashboardwindow.HundeDetailWindow;
import com.google.common.eventbus.Subscribe;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MyHundeView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1567788911012389408L;
	GridLayout hunde;

	public MyHundeView() {
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());

		// VerticalLayout titleAndDrafts = new VerticalLayout();
		// titleAndDrafts.setSizeUndefined();
		// titleAndDrafts.setSpacing(true);
		// titleAndDrafts.addStyleName("drafts");
		//
		// titleAndDrafts.addComponent(buildHundeList());
		
		
		Component hundeList = buildHundeList();
		addComponent(hundeList);
		setExpandRatio(hundeList,1);
		

		// setComponentAlignment(titleAndDrafts, Alignment.TOP_CENTER);
		// addComponent(titleAndDrafts);
		//

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of myHundeView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashBoardEventBus.unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("MeineHunde");
		title.setSizeUndefined();

		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		
		header.addComponent(title);

		return header;
	}

	private Component buildHundeList() {
		Panel panel = new Panel();
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		panel.setSizeFull();

		
		hunde = new GridLayout(5, 5);
		hunde.setSizeUndefined();

		hunde.setSpacing(true);

		hunde.addComponent(buildCreateBox());
		// hunde.addComponent(buildHundeThumb());
		User user = (User) VaadinSession.getCurrent().getAttribute(
				User.class.getName());

		Collection<Hund> zwCollection = user.getAllHunde();

		Iterator <Hund>i = zwCollection.iterator();

		while (i.hasNext()) {

			hunde.addComponent(new HundeThumbClass((Hund) i.next()));

		}

		panel.setContent(hunde);
		return panel;
		//return hunde;
		
	}


	private Component buildCreateBox() {
		VerticalLayout createBox = new VerticalLayout();
		createBox.setWidth(160.0f, Unit.PIXELS);
		createBox.setHeight(200.0f, Unit.PIXELS);
		createBox.addStyleName("create");

		Button create = new Button("Create New");
		create.addStyleName(ValoTheme.BUTTON_PRIMARY);
		create.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2018405928715704744L;

			@Override
			public void buttonClick(final ClickEvent event) {
				hunde.addComponent(new HundeThumbClass());

			}
		});

		createBox.addComponent(create);
		createBox.setComponentAlignment(create, Alignment.MIDDLE_CENTER);
		return createBox;
	}

	private class HundeThumbClass extends CustomComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7173341885393595577L;
		private final Hund hund;
		private Label draftTitle;

		public HundeThumbClass() {

			User user = (User) VaadinSession.getCurrent().getAttribute(
					User.class.getName());

			hund = new Hund(user.getIdPerson());

			setCompositionRoot(buildHundeThumb());

		}

	
		public HundeThumbClass(final Hund hund) {
			this.hund = hund;
			setCompositionRoot(buildHundeThumb());
		}

		@Subscribe
		public void updateUserName(final DogUpdatedEvent event) {
		
			
			draftTitle.setValue(hund.getRufname() + "<br>"
					+ hund.getZwingername());
			
			
					
		}

		private Component buildHundeThumb() {

			DashBoardEventBus.register(this);
			VerticalLayout hundeThumb = new VerticalLayout();
			hundeThumb.setSpacing(true);

			hundeThumb.addStyleName("draft-thumb");
			Image draft = new Image(null, new ThemeResource(
					"img/draft-report-thumb.png"));
			draft.setWidth(160.0f, Unit.PIXELS);
			draft.setHeight(200.0f, Unit.PIXELS);
			draft.setDescription("Click to edit");
			hundeThumb.addComponent(draft);
			draftTitle = new Label(hund.getRufname() + "<br>"
					+ hund.getZwingername(), ContentMode.HTML);
			
			draftTitle.setSizeUndefined();
			hundeThumb.addComponent(draftTitle);

			final Button delete = new Button("×");
			delete.setDescription("Delete draft");
			delete.setPrimaryStyleName("delete-button");
			delete.addClickListener(new ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 7468790506051026612L;

				@Override
				public void buttonClick(final ClickEvent event) {
					Notification.show("Not implemented in this demo");
				}
			});

			hundeThumb.addComponent(delete);

			hundeThumb.addLayoutClickListener(new LayoutClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -7796650866572215422L;

				@Override
				public void layoutClick(final LayoutClickEvent event) {
					if (event.getButton() == MouseButton.LEFT
							&& event.getChildComponent() != delete) {

						HundeDetailWindow.open(hund);

					}
				}
			});

			return hundeThumb;

		}
		@Override
		public void detach() {
			super.detach();
			DashBoardEventBus.unregister(this);
		}

	}

}
