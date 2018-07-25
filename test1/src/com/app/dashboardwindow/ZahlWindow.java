package com.app.dashboardwindow;

import java.math.BigDecimal;
import java.util.Random;

import com.app.dashboard.event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.mpay24.payment.Mpay24;
import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentRequest;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ZahlWindow extends Window {

	public static final String ID = "hundesuchwindow";
	
	Mpay24 mpay24;
	Payment response;
	String TID = null;

	public ZahlWindow(String infoText) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);

		setResizable(false);
		setClosable(false);
		setHeight(75.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		// content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		mpay24 = new Mpay24("94739", "64fmQ?udHP", Environment.TEST);
		try {
			response = mpay24.paymentPage(getTestPaymentRequest());

			String redirectURL = response.getRedirectLocation();
			

			//Payment payment = mpay24.paymentDetails(TID);
			// response.

			System.out.println("url: " + redirectURL);

			ExternalResource res = new ExternalResource(redirectURL);
			BrowserFrame object = new BrowserFrame("bezahlung", res);

			object.setSizeFull();

			content.addComponent(object);
			
			content.addComponent(buildFooter());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected PaymentRequest getTestPaymentRequest() {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(new BigDecimal(1));
		TID = String.valueOf(new Random().nextInt(10000000));
		paymentRequest.setTransactionID(TID);
		
		return paymentRequest;
	}

	private Component buildFooter() {
		final HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		//

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// Updated user should also be persisted to database. But
				// not in this demo.

				close();

			}
		});
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		return footer;
	}

	public static void open(String text) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new ZahlWindow(text);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
