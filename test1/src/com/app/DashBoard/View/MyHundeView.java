package com.app.DashBoard.View;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MyHundeView extends VerticalLayout implements View {

	HorizontalLayout hunde;
	
	public MyHundeView() {
		DashBoardEventBus.register(this);
        	
        setSizeFull();
		addStyleName("myhundeview");
	
		addComponent(buildToolbar());

		VerticalLayout titleAndDrafts = new VerticalLayout();
		titleAndDrafts.setSizeUndefined();
		titleAndDrafts.setSpacing(true);
		titleAndDrafts.addStyleName("drafts");
		addComponent(titleAndDrafts);
		setComponentAlignment(titleAndDrafts, Alignment.MIDDLE_CENTER);

		titleAndDrafts.addComponent(buildHundeList());
	
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

		hunde = new HorizontalLayout();
		hunde.setSpacing(true);

		hunde.addComponent(buildHundeThumb());
		hunde.addComponent(buildCreateBox());

		return hunde;
	}

	private Component buildHundeThumb() {
		HundeThumbClass hundeThumb = new HundeThumbClass();
		return hundeThumb;
	}

	private Component buildCreateBox() {
		VerticalLayout createBox = new VerticalLayout();
		createBox.setWidth(160.0f, Unit.PIXELS);
		createBox.setHeight(200.0f, Unit.PIXELS);
		createBox.addStyleName("create");

		Button create = new Button("Create New");
		create.addStyleName(ValoTheme.BUTTON_PRIMARY);
		create.addClickListener(new ClickListener() {
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
		public HundeThumbClass() {
			VerticalLayout hundeThumb = new VerticalLayout();
			hundeThumb.setSpacing(true);

			hundeThumb.addStyleName("draft-thumb");
			Image draft = new Image(null, new ThemeResource(
					"img/draft-report-thumb.png"));
			draft.setWidth(160.0f, Unit.PIXELS);
			draft.setHeight(200.0f, Unit.PIXELS);
			draft.setDescription("Click to edit");
			hundeThumb.addComponent(draft);
			Label draftTitle = new Label(
					"Monthly revenue<br><span>Last modified 1 day ago</span>",
					ContentMode.HTML);
			draftTitle.setSizeUndefined();
			hundeThumb.addComponent(draftTitle);

			final Button delete = new Button("×");
			delete.setDescription("Delete draft");
			delete.setPrimaryStyleName("delete-button");
			delete.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					Notification.show("Not implemented in this demo");
				}
			});
			hundeThumb.addComponent(delete);

			hundeThumb.addLayoutClickListener(new LayoutClickListener() {
				@Override
				public void layoutClick(final LayoutClickEvent event) {
					if (event.getButton() == MouseButton.LEFT
							&& event.getChildComponent() != delete) {

					}
				}
			});

			setCompositionRoot(hundeThumb);

		}

	}

}
