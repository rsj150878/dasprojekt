package com.app.DashBoard.View;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import org.apache.cxf.common.util.SystemPropertyAction;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.mpay.soap.client.PaymentType;
import com.mpay24.payment.Mpay24;
import com.mpay24.payment.Mpay24.Environment;
import com.mpay24.payment.PaymentException;
import com.mpay24.payment.data.Payment;
import com.mpay24.payment.data.PaymentRequest;
import com.mpay24.payment.data.TokenRequest;
import com.mpay24.payment.type.CreditCardPaymentType;
import com.mpay24.payment.type.PaymentTypeData;
import com.mpay24.payment.type.TokenPaymentType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.ui.BrowserFrame;
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
		addComponent(buildWorkingArea());

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

	private Component buildWorkingArea() {
		VerticalLayout mainLayout = new VerticalLayout();
		// ShowRing ring = new ShowRing();
		TID = getRandomTransactionId();

		String keyStoreLocation = SystemPropertyAction.getProperty("javax.net.ssl.keyStore");
		if (keyStoreLocation != null) {
			System.out.println("KEY_STORE_SYSTEM_PROPERTY_SET");
		} else {
			keyStoreLocation = SystemPropertyAction.getProperty("user.home") + "/.keystore";
			System.out.println("KEY_STORE_NOT_SET");
		}

		mpay24 = new Mpay24("", "", Environment.TEST);
		try {
			response = mpay24.paymentPage(getTestPaymentRequest());

			String redirectURL = response.getRedirectLocation();

			Payment payment = mpay24.paymentDetails(TID);
			// response.

			System.out.println("url: " + redirectURL);

			ExternalResource res = new ExternalResource(redirectURL);
			BrowserFrame object = new BrowserFrame("bezahlung", res);

			object.setSizeFull();

			final WorkThread thread = new WorkThread();
			thread.start();

			mainLayout.addComponent(object);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mainLayout;

	}

	protected PaymentRequest getTestPaymentRequest() {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(new BigDecimal(1));
		paymentRequest.setTransactionID(TID);
		
		return paymentRequest;
	}

	protected PaymentTypeData getVisaTestData() throws ParseException {
		CreditCardPaymentType paymentType = new CreditCardPaymentType();
		paymentType.setPan("4444333322221111");
		paymentType.setCvc("123");
		paymentType.setExpiry(new Date("12/2016"));
		paymentType.setBrand(CreditCardPaymentType.Brand.VISA);
		return paymentType;
	}

	private PaymentTypeData getTokenPaymentType(String token) {
		return new TokenPaymentType(token);
	}

	private TokenRequest getTestTokenRequest(String language) {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setPaymentType(PaymentType.CC);
		tokenRequest.setTemplateSet("DEFAULT");
		tokenRequest.setStyle("DEFAULT");
		tokenRequest.setLanguage(language);
		return tokenRequest;
	}

	class WorkThread extends Thread {
		// Volatile because read in another thread in access()

		@Override
		public void run() {

			try {

				int schleife = 0;
				while (schleife < 50) {

					System.out.println("Status: " + response.getState());

					Payment payment = mpay24.paymentDetails(TID);
					System.out.println(" status neu: " + payment.getState());

					sleep(2000);
					schleife++;
				}
				// Show the "all done" for a while
				sleep(2000); // Sleep for 2 seconds
			} catch (InterruptedException e) {
			} catch (PaymentException e) {
				e.printStackTrace();
			}
			
			// Update the UI thread-safely
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					// Restore the state to initial

					// Stop polling
					UI.getCurrent().setPollInterval(-1);

				}
			});
		}

	}
	
	private String getRandomTransactionId() {
		return String.valueOf(new Random().nextInt(10000000));
}
}
