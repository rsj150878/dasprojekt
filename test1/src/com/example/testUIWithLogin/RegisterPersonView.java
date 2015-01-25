package com.example.testUIWithLogin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbsoluteLayout;

public class RegisterPersonView extends AbsoluteLayout implements View {

	public RegisterPersonView() {
		RegisterForm registerForm = new RegisterForm();
		addComponent(registerForm);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
