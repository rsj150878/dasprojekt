package com.example.testUIWithLogin;

import java.sql.SQLException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import com.app.EmailSender.EmailSender;
import com.app.bean.LandBean;
import com.app.dbIO.DBConnection;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class RegisterPersonFormListener implements Button.ClickListener,
		QueryDelegate.RowIdChangeListener {

	private SQLContainer userContainer;
	private Object userItem;
	private RowId idUser;
	private Item us;

	@Override
	public void buttonClick(ClickEvent event) throws AuthenticationException {
		// TODO Auto-generated method stub

		Button source = event.getButton();

		if (source.getId().equals("registrieren")) {

			Component parent = source.getParent();
			while (!(parent instanceof RegisterForm)) {
				parent = parent.getParent();
			}

			RegisterForm regform = (RegisterForm) parent;

			TableQuery q1 = new TableQuery("person",
					DBConnection.INSTANCE.getConnectionPool());
			q1.setVersionColumn("version");

			TableQuery q2 = new TableQuery("user",
					DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");

			try {

				userContainer = new SQLContainer(q2);

				userContainer.setAutoCommit(false);
				userItem = userContainer.addItem();
				userContainer.getItem(userItem).getItemProperty("email")
						.setValue(regform.getEmail().getValue());
				userContainer.getItem(userItem).getItemProperty("password")
						.setValue("neuesPasswort");

				userContainer.addRowIdChangeListener(this);

				userContainer.commit();
				System.out.println("userItem" + userItem);

				SQLContainer personContainer = new SQLContainer(q1);
				personContainer.setAutoCommit(false);
				Object x = personContainer.addItem();
				personContainer.getItem(x).getItemProperty("nachname")
						.setValue(regform.getNachname().getValue());

				personContainer.getItem(x).getItemProperty("vorname")
						.setValue(regform.getVorname().getValue());

				personContainer.getItem(x).getItemProperty("strasse")
						.setValue(regform.getStrasse().getValue());

				personContainer.getItem(x).getItemProperty("ort")
						.setValue(regform.getOrt().getValue());

				personContainer.getItem(x).getItemProperty("hausnummer")
						.setValue(regform.getHausnummer().getValue());

				personContainer.getItem(x).getItemProperty("geb_dat")
						.setValue(regform.getGebdat().getValue());

				personContainer.getItem(x).getItemProperty("plz")
						.setValue(regform.getPLZ().getValue());

				personContainer.getItem(x).getItemProperty("iduser")
						.setValue(new Integer(idUser.getId()[0].toString()));

				LandBean lb = (LandBean) regform.getLand().getValue();
				personContainer.getItem(x).getItemProperty("land")
						.setValue(lb.getLandKurzBezeichnung());

				personContainer.commit();

				String mailText = "User-ID: " + regform.getEmail().getValue()
						+ "\n" + "Passwort: neuesPasswort";

				String[] to = new String[] { regform.getEmail().getValue() };

				if (EmailSender.sendMail("no-reply@retrieverdata.at",
						"waterloo123", mailText, to)) {
					System.out.println("mail ok");
				} else {
					System.out.println("mail nicht ok");
				}

				Navigator navi = UI.getCurrent().getNavigator();
				navi.navigateTo("login");

			} catch (SQLException e) {
		
				if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
					Notification.show("Email bereits registriert");
				} else {
					e.printStackTrace();
				}

			}

		} else if (source.getId().equals("abbrechen")) {
			Navigator navi = UI.getCurrent().getNavigator();
			navi.navigateTo("login");
		}

	}

	@Override
	public void rowIdChange(RowIdChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("event " + event.getNewRowId());
		idUser = event.getNewRowId();

	}
}
