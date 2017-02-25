package com.app.Components.Listener;

import java.sql.SQLException;

import com.app.Components.UserDetail;
import com.app.bean.LandBean;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;

public class UserDetailListener implements Button.ClickListener {

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		Button source = event.getButton();

		if (source.getId().equals("ok")) {

			Component parent = source.getParent();
			while (!(parent instanceof UserDetail)) {
				parent = parent.getParent();
			}

			UserDetail ud = (UserDetail) parent;

			SQLContainer personContainer = ud.getPersonContainer();
			Object x = personContainer.firstItemId();

			personContainer.getItem(x).getItemProperty("nachname")
					.setValue(ud.getNachname().getValue());

			personContainer.getItem(x).getItemProperty("vorname")
					.setValue(ud.getVorname().getValue());

			personContainer.getItem(x).getItemProperty("strasse")
					.setValue(ud.getStrasse().getValue());

			personContainer.getItem(x).getItemProperty("ort")
					.setValue(ud.getOrt().getValue());

			personContainer.getItem(x).getItemProperty("hausnummer")
					.setValue(ud.getHnr().getValue());

			// personContainer.getItem(x).getItemProperty("email")
			// .setValue(ud.getEmail().getValue());

			personContainer.getItem(x).getItemProperty("geb_dat")
					.setValue(ud.getGebdat().getValue());

			personContainer.getItem(x).getItemProperty("plz")
					.setValue(ud.getPlz().getValue());

			LandBean lb = (LandBean) ud.getLand().getValue();
			personContainer.getItem(x).getItemProperty("land")
					.setValue(lb.getLandKurzBezeichnung());

			try {
				personContainer.commit();
				
			} catch (SQLException e) {

			}
		}
	}

}
