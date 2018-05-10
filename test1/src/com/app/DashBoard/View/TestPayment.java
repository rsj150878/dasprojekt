package com.app.DashBoard.View;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import org.apache.cxf.common.util.SystemPropertyAction;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoardWindow.ZahlWindow;
import com.mpay.soap.client.PaymentType;
import com.mpay24.payment.Mpay24;
import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.PaymentException;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentRequest;
import com.mpay24.payment.data.Token;
import com.mpay24.payment.data.TokenRequest;
import com.mpay24.payment.type.CreditCardPaymentType;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.TokenPaymentType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
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
