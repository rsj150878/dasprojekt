package com.app.Components;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;

public class MainComponent extends CustomComponent {

	public MainComponent() {

	}

	public Component registerComponent(String detail) {

		Component currentComponent = null;
		
		switch (detail) {
		case ("Userdetail"):
			UserDetail ud = new UserDetail();
			ud.attach();
			currentComponent = ud;
			break;

		}

		return currentComponent;
	}
}
