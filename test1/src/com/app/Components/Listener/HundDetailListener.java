package com.app.Components.Listener;

import com.app.interfaces.DetailInterface;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

public class HundDetailListener implements Button.ClickListener {

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		Button source = event.getButton();

		Component parent = source.getParent();
		while (!(parent instanceof DetailInterface)) {
			parent = parent.getParent();
		}

	
		DetailInterface dfi = (DetailInterface) parent;

		if (source.getId().equals("ok")) {
			dfi.commit();
		} else if (source.getId().equals("abbruch")) {
			dfi.rollback();
		}

	}

}
