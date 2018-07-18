package com.app.auth;

import com.app.dbio.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.RowId;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class EmailForEmailVerteiler implements
		QueryDelegate.RowIdChangeListener {

	private Integer id;
	private String emailAdresse;
	private String newsLetter;
	private String telefonnummer;

	public EmailForEmailVerteiler() {
		id = 0;
		emailAdresse = new String("");
		newsLetter = new String("J");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailAdresse() {
		return emailAdresse;
	}

	public void setEmailAdresse(String emailAdresse) {
		this.emailAdresse = emailAdresse;
	}

	public String getNewsLetter() {
		return newsLetter;
	}

	public void setNewsLetter(String newsLetter) {
		this.newsLetter = newsLetter;
	}

	public void save() throws Exception {
		TableQuery q2 = new TableQuery("emaillist",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item mailItem;
		SQLContainer mailContainer;

		mailContainer = new SQLContainer(q2);

		mailContainer.addRowIdChangeListener(this);
		
		if (id.equals(new Integer(0))) {
			mailItem = mailContainer.getItem(mailContainer.addItem());

		} else {
			mailContainer.addContainerFilter(new Equal("id", id));
			mailItem = mailContainer.getItem(mailContainer.firstItemId());
		}
		
		mailItem.getItemProperty("emailadresse").setValue(emailAdresse);
		mailItem.getItemProperty("newsletter").setValue(newsLetter);
		
		mailContainer.commit();
		
		mailContainer.removeAllContainerFilters();

	}

	@Override
	public void rowIdChange(RowIdChangeEvent arg0) {
		RowId x = arg0.getNewRowId();
		Long newID = (Long) x.getId()[0];

		this.id = new Integer(newID.intValue());

	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

}
