package com.app.auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.app.dbio.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class DataProvider {


	public static Collection<EmailForEmailVerteiler> getEmailList() {
		List<EmailForEmailVerteiler> orderedList = new ArrayList<EmailForEmailVerteiler>();
		;

		TableQuery q2 = new TableQuery("emaillist",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item personItem;
		SQLContainer personContainer;

		try {

			personContainer = new SQLContainer(q2);
			for (Object id : personContainer.getItemIds()) {
				EmailForEmailVerteiler emailMember = new EmailForEmailVerteiler();
				emailMember.setId(new Integer(personContainer.getItem(id)
						.getItemProperty("id").getValue().toString()));
				emailMember.setEmailAdresse(personContainer.getItem(id)
						.getItemProperty("emailadresse").getValue().toString());
				emailMember.setNewsLetter(personContainer.getItem(id)
						.getItemProperty("newsletter").getValue().toString());
				orderedList.add(emailMember);
			}

		} catch (SQLException e) {

		}

		return orderedList;
	}

}
