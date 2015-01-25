package com.example.testUIWithLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.app.Auth.AuthManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * @author Ondrej Kvasnovsky
 */
@Component
public class LoginFormListener implements Button.ClickListener {

	@Autowired
	private AuthManager authManager;

	@Override
	public void buttonClick(Button.ClickEvent event) {
		try {
			Button source = event.getButton();
			if (source.getId().equals("login")) {

				LoginForm parent = (LoginForm) source.getParent().getParent();
				
				String username = parent.getTxtLogin().getValue();
				String password = parent.getTxtPassword().getValue();

				UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(
						username, password);

				Authentication result = authManager.authenticate(request);
				
				SecurityContextHolder.getContext().setAuthentication(result);

				Navigator navigator = UI.getCurrent().getNavigator();
				navigator.navigateTo("user");
			} else if (source.getId().equals("registrieren")) {
				
				Navigator navigator = UI.getCurrent().getNavigator();
				navigator.navigateTo("register");
				
			}
			

		} catch (AuthenticationException e) {
			Notification.show("Authentication failed: " + e.getMessage());
		}

	}
}
