package com.app.auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.app.dbio.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class Person extends AbstractPersonClass  {

	public Person() {
		
	}
	
		
	public Collection<Hund> getAllHunde() {
		Collection<Hund> hundeCollection = new ArrayList<Hund>();
		
		TableQuery q1 = new TableQuery("hund",DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");
		SQLContainer hundeContainer;
		try {
			hundeContainer = new SQLContainer(q1);
			hundeContainer.addContainerFilter(new Equal ("idperson",this.idPerson));
			
			for (int i = 0; i < hundeContainer.size(); i++) {
				Item item = hundeContainer.getItem(hundeContainer.getIdByIndex(i));
				final Hund hund = new Hund(this.idPerson, Integer.valueOf(item.getItemProperty("idhund").getValue().toString()));
				hundeCollection.add(hund);
				
			}
			
 		} catch (SQLException e) {
 			
 		}
		
		
		return hundeCollection;
	}

	
}
