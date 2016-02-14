package com.app.Auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;


public class DataProvider {
	
	 public static Collection<Person> getPersonenList() {
	        List<Person> orderedList = new ArrayList<Person>();;
	        
	    	TableQuery q2 = new TableQuery("person",
					DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");
			Item personItem;
			SQLContainer personContainer;

			try {

				personContainer = new SQLContainer(q2);
				for(Object id: personContainer.getItemIds()) {
					Person person = new Person (Integer.valueOf(personContainer.getItem(id).getItemProperty("idperson").toString()));
					orderedList.add(person);
				}
				personItem = personContainer.getItem(personContainer.firstItemId());
			}
			catch (SQLException e) {
				
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
	        List<MitgliederListe> orderedList = new ArrayList<MitgliederListe>();;
	        
	    	TableQuery q2 = new TableQuery("person",
					DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");
			Item personItem;
			SQLContainer personContainer;

			try {

				personContainer = new SQLContainer(q2);
				for(Object id: personContainer.getItemIds()) {
					MitgliederListe mitgliederListe = new MitgliederListe();
					
					Person person = new Person (Integer.valueOf(personContainer.getItem(id).getItemProperty("idperson").toString()));
					mitgliederListe.setPerson(person);
					mitgliederListe.setHunde(person.getAllHunde());
					
					mitgliederListe.setFamilienName(person.getLastName());
					mitgliederListe.setVorName(person.getFirstName());
					mitgliederListe.setAdresse(person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " " + person.getOrt());
										
					orderedList.add(mitgliederListe);
				}
				personItem = personContainer.getItem(personContainer.firstItemId());
			}
			catch (SQLException e) {
				
			}
	        
	        Collections.sort(orderedList, new Comparator<MitgliederListe>() {
	            @Override
	            public int compare(MitgliederListe o1, MitgliederListe o2) {
	            	return o2.getPerson().getIdPerson().compareTo(o1.getPerson().getIdPerson());
	                
	            }
	        });
	        return orderedList;
	    }

}