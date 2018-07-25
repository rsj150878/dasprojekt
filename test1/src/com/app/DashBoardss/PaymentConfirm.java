package com.app.dashboard;

import java.util.Locale;

import com.app.dashboard.event.DashBoardEventBus;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.app.DashBoard.DashboardWidgetsetNeu")
@Title("ShowInfo")
@SuppressWarnings("serial")
public final class PaymentConfirm extends UI {

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */

	// private final DataProvider dataProvider = new DummyDataProvider();
	private final DashBoardEventBus dashboardEventbus = new DashBoardEventBus();

	@Override
	protected void init(final VaadinRequest request) {
		setLocale(Locale.GERMANY);

		Responsive.makeResponsive(this);

		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent();


	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		FormLayout infoFormLayout = new FormLayout();
		infoFormLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		infoFormLayout.setWidth(50.0f, Unit.PERCENTAGE);
		infoFormLayout.setSpacing(true);
		infoFormLayout.setMargin(true);

		Label field = new Label();
		field.setValue(Page.getCurrent().getLocation().toString());

		infoFormLayout.addComponent(field);

		mainLayout.addComponent(infoFormLayout);

		setContent(mainLayout);

	}

}
