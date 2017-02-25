package com.app.Auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.app.dbIO.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class DataProvider {

	public static Collection<Person> getPersonenList() {
		List<Person> orderedList = new ArrayList<Person>();
		;

		TableQuery q2 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item personItem;
		SQLContainer personContainer;

		try {

			personContainer = new SQLContainer(q2);
			for (Object id : personContainer.getItemIds()) {
				Person person = new Person(Integer.valueOf(personContainer
						.getItem(id).getItemProperty("idperson").toString()));
				orderedList.add(person);
			}
			personItem = personContainer.getItem(personContainer.firstItemId());
		} catch (SQLException e) {

		}

		Collections.sort(orderedList, new Comparator<Person>() {
			@Override
			public int compare(Person o1, Person o2) {
				return o2.getIdPerson().compareTo(o1.getIdPerson());

			}
		});
		return orderedList;
	}

	public static Collection<MitgliederListe> getMitgliederList() {
		List<MitgliederListe> orderedList = new ArrayList<MitgliederListe>();
		;

		TableQuery q2 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item personItem;
		SQLContainer personContainer;

		try {

			personContainer = new SQLContainer(q2);
			for (Object id : personContainer.getItemIds()) {
				MitgliederListe mitgliederListe = new MitgliederListe();

				Person person = new Person(Integer.valueOf(personContainer
						.getItem(id).getItemProperty("idperson").toString()));
				mitgliederListe.setPerson(person);

				orderedList.add(mitgliederListe);
			}
			personItem = personContainer.getItem(personContainer.firstItemId());
		} catch (SQLException e) {

		}

		Collections.sort(orderedList, new Comparator<MitgliederListe>() {
			@Override
			public int compare(MitgliederListe o1, MitgliederListe o2) {
				return o2.getPerson().getIdPerson()
						.compareTo(o1.getPerson().getIdPerson());

			}
		});
		return orderedList;
	}

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
