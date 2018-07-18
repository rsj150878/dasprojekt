package com.app.dashboard.view;

import java.util.Random;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dashboardwindow.ZahlWindow;
import com.mpay24.payment.Mpay24;
import com.mpay24.payment.data.Payment;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TestPayment extends VerticalLayout implements View {
	Random random;

	Mpay24 mpay24;
	Payment response;
	String TID = null;

	public TestPayment() {
		random = new Random();
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());
		//addComponent(buildWorkingArea());
		Button zahlbutton = new Button("zahlen");
		zahlbutton.addClickListener(event -> ZahlWindow.open(""));
		addComponent(zahlbutton);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

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

	
}
