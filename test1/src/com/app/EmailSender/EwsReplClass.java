package com.app.EmailSender;

import java.util.ArrayList;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.EmailAddressKey;
import microsoft.exchange.webservices.data.core.enumeration.property.PhoneNumberKey;
import microsoft.exchange.webservices.data.core.enumeration.property.PhysicalAddressKey;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.enumeration.service.FileAsMapping;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Contact;
import microsoft.exchange.webservices.data.core.service.item.ContactGroup;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.GroupMember;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.PhysicalAddressEntry;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.ProgressBar;

public class EwsReplClass {

	private SQLContainer sqlContainer;
	public final static EwsReplClass INSTANCE = new EwsReplClass();

	private static int EWS_TYP_CONTACT = 1;
	private static int EWS_TYP_GROUP_MEMBER = 2;

	private ArrayList ewsList = new ArrayList();

	protected EwsReplClass() {
		ewsList.add(new String[] { "stefan@retrieverebreichsdorf.at",
				"LVwbf2005" });
		ewsList.add(new String[] { "simon@retrieverebreichsdorf.at", "simon11" });

	}

	public void speichereVerknuefung(Item person) {
		try {

			for (int i = 0; i < ewsList.size(); i++) {
				String[] zwString = (String[]) ewsList.get(i);
				ExchangeService service = getServiceForEmail(zwString[0],
						zwString[1]);

				speichereVerknuepfungForEmail(person, service, zwString[0],
						zwString[1]);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void speichereVerknuepfung(SQLContainer personCollection,
			ProgressBar indicator) {
		try {
			float y = 0;
			float size = personCollection.size() * ewsList.size();
			for (int i = 0; i < ewsList.size(); i++) {
				String[] zwString = (String[]) ewsList.get(i);
				ExchangeService service = getServiceForEmail(zwString[0],
						zwString[1]);

				for (Object o : personCollection.getItemIds()) {
					
					Item personItem = (Item) personCollection.getItem(o);
					speichereVerknuepfungForEmail(personItem, service,
							zwString[0], zwString[1]);
					y++;
					indicator.setValue(new Float(y / size));

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ExchangeService getServiceForEmail(String email, String password)
			throws Exception {
		ExchangeService service = new ExchangeService(
				ExchangeVersion.Exchange2010_SP2);

		ExchangeCredentials credentials = new WebCredentials(email, password);
		service.setCredentials(credentials);
		service.autodiscoverUrl(email);
		return service;
	}

	private void speichereVerknuepfungForEmail(Item person,
			ExchangeService service, String email, String password) {

		Boolean newContact = false;
		TableQuery q1 = new TableQuery("ews",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		// Notification.show("starte Replikation für Email " + email,
		// Notification.Type.HUMANIZED_MESSAGE);

		try {

			sqlContainer = new SQLContainer(q1);

			sqlContainer.addContainerFilter(new Equal("idperson", person
					.getItemProperty("idperson").getValue()));
			sqlContainer.addContainerFilter(new Equal("ews_fuer", email));
			sqlContainer.addContainerFilter(new Equal("typ", EWS_TYP_CONTACT));

			Contact contact = null;
			PhysicalAddressEntry paEntry1 = null;

			if (sqlContainer.size() == 0) {
				contact = new Contact(service);
				paEntry1 = new PhysicalAddressEntry();
				newContact = true;

			} else {
				System.out.println("ID: "
						+ sqlContainer.getItem(sqlContainer.firstItemId())
								.getItemProperty("ews_id").getValue()
								.toString().trim());

				contact = Contact.bind(
						service,
						new ItemId(sqlContainer
								.getItem(sqlContainer.firstItemId())
								.getItemProperty("ews_id").getValue()
								.toString()));
				paEntry1 = contact.getPhysicalAddresses().getPhysicalAddress(
						PhysicalAddressKey.Home);
			}

			contact.setGivenName(person.getItemProperty("vorname").getValue()
					.toString());
			contact.setFileAs(FileAsMapping.SurnameCommaGivenNameCompany);

			contact.setSurname(person.getItemProperty("nachname").getValue()
					.toString());
			// contact.setSubject("Contact Details");
			contact.setDisplayName(person.getItemProperty("nachname")
					.getValue().toString()
					+ ", "
					+ person.getItemProperty("vorname").getValue().toString());
			// contact.update(ConflictResolutionMode.AlwaysOverwrite);

			paEntry1.setStreet(person.getItemProperty("strasse").getValue()
					.toString().trim()
					+ " "
					+ person.getItemProperty("hausnummer").getValue()
							.toString().trim());
			paEntry1.setCity(person.getItemProperty("ort").getValue()
					.toString().trim());
			paEntry1.setPostalCode(person.getItemProperty("plz").getValue()
					.toString().trim());

			if (!(person.getItemProperty("email").getValue() == null)) {
				contact.getEmailAddresses().setEmailAddress(
						EmailAddressKey.EmailAddress1,
						new EmailAddress(person.getItemProperty("email")
								.getValue().toString()));
			}

			if (person.getItemProperty("telnr").getValue() == null) {
				contact.getPhoneNumbers().setPhoneNumber(
						PhoneNumberKey.HomePhone, "n/a");
			} else {
				contact.getPhoneNumbers().setPhoneNumber(
						PhoneNumberKey.HomePhone,
						person.getItemProperty("telnr").getValue().toString());
			}

			if (person.getItemProperty("mobnr").getValue() == null) {
				contact.getPhoneNumbers().setPhoneNumber(
						PhoneNumberKey.MobilePhone, "n/a");

			} else {
				contact.getPhoneNumbers().setPhoneNumber(
						PhoneNumberKey.MobilePhone,
						person.getItemProperty("mobnr").getValue().toString());

			}

			if (newContact) {
				contact.getPhysicalAddresses().setPhysicalAddress(
						PhysicalAddressKey.Home, paEntry1);

				contact.save();

				Object x = sqlContainer.addItem();
				Item ews = sqlContainer.getItemUnfiltered(x);

				ews.getItemProperty("typ").setValue(EWS_TYP_CONTACT);
				ews.getItemProperty("ews_fuer").setValue(email);
				ews.getItemProperty("ews_id").setValue(
						contact.getId().getUniqueId());
				ews.getItemProperty("idperson").setValue(
						person.getItemProperty("idperson").getValue());

				sqlContainer.commit();
			} else {

				contact.update(ConflictResolutionMode.AlwaysOverwrite);

			}

			// Notification.show("Replikation für Email " + email + " erledigt",
			// Notification.Type.HUMANIZED_MESSAGE);
			sqlContainer.removeAllContainerFilters();

			// System.out.println("EmailAddressKey.EmailAddress1" +
			// EmailAddressKey.EmailAddress1.name());

			Folder folder = Folder.bind(service, WellKnownFolderName.Contacts);
			ItemView view = new ItemView(999);
			FindItemsResults<microsoft.exchange.webservices.data.core.service.item.Item> findResults = service
					.findItems(folder.getId(), view);

			for (microsoft.exchange.webservices.data.core.service.item.Item item : findResults
					.getItems()) {

				if (item.getSubject().equals("newsletter") && !(person.getItemProperty("email").getValue() == null)) {
					ContactGroup contactGroup = ContactGroup.bind(service,
							item.getId());

					sqlContainer.addContainerFilter(new Equal("idperson",
							person.getItemProperty("idperson").getValue()));
					sqlContainer
							.addContainerFilter(new Equal("ews_fuer", email));
					sqlContainer.addContainerFilter(new Equal("typ",
							EWS_TYP_GROUP_MEMBER));
					sqlContainer.addContainerFilter(new Equal("zusatz",
							EmailAddressKey.EmailAddress1.name()));

					GroupMember gm = null;

					if (sqlContainer.size() > 0) {
						gm = contactGroup.getMembers().find(
								sqlContainer
										.getItem(sqlContainer.firstItemId())
										.getItemProperty("ews_id").getValue()
										.toString());
					}

					if (gm != null) {
						contactGroup.getMembers().remove(gm);
						sqlContainer.removeItem(sqlContainer.firstItemId());
						contactGroup
								.update(ConflictResolutionMode.AlwaysOverwrite);
					}

					gm = new GroupMember(contact, EmailAddressKey.EmailAddress1);

					// contactGroup.tryGetProperty(propertyDefinition,
					// propertyValue)
					if (person.getItemProperty("newsletter").getValue()
							.toString().equals("J")) {
						contactGroup.getMembers().add(gm);
					}
					contactGroup.update(ConflictResolutionMode.AlwaysOverwrite);

					contactGroup = ContactGroup.bind(service, item.getId());

					String key = null;
					for (GroupMember x : contactGroup.getMembers()) {
						if (x.getAddressInformation()
								.getAddress()
								.equals(contact
										.getEmailAddresses()
										.getEmailAddress(
												EmailAddressKey.EmailAddress1)
										.getAddress())) {
							key = x.getKey();
						}

					}

					System.out.println("key: " + key);
					if ((sqlContainer.size() == 0)
							&& person.getItemProperty("newsletter").getValue()
									.toString().equals("J")) {
						Object x = sqlContainer.addItem();
						Item ews = sqlContainer.getItemUnfiltered(x);

						ews.getItemProperty("typ").setValue(
								EWS_TYP_GROUP_MEMBER);
						ews.getItemProperty("ews_fuer").setValue(email);
						ews.getItemProperty("ews_id").setValue(key);
						ews.getItemProperty("idperson").setValue(
								person.getItemProperty("idperson").getValue());
						ews.getItemProperty("zusatz").setValue(
								EmailAddressKey.EmailAddress1.name());

					}

					sqlContainer.commit();

				}

				sqlContainer.removeAllContainerFilters();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
