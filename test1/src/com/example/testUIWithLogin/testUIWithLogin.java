package com.example.testUIWithLogin;

import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.app.dbIO.PathHandler;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 * The Application's "main" class
 */
@PreserveOnRefresh
public class testUIWithLogin extends UI {

	private ApplicationContext applicationContext;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = testUIWithLogin.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		WrappedSession session = request.getWrappedSession();
		HttpSession httpSession = ((WrappedHttpSession) session)
				.getHttpSession();
		ServletContext servletContext = httpSession.getServletContext();
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);

		PathHandler.INSTANCE.setPathName(servletContext.getRealPath("WEB-INF/classes"));

		System.out.println("xxx + "
				+ servletContext.getRealPath("WEB-INF/files/kursblat.xml"));
		;

		Navigator navigator = new Navigator(this, this);

		navigator.addView("login", LoginView.class);

		// navigator.addView("user", UserView.class);

		navigator.addView("user", UserMainView.class);

		navigator.addView("register", RegisterPersonView.class);

		navigator.navigateTo("login");
		setNavigator(navigator);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
