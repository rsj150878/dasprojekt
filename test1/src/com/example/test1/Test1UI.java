package com.example.test1;

import javax.servlet.ServletContext;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.app.bean.MyBean;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@PreserveOnRefresh
public class Test1UI extends UI {

	private TextField textField_1;

	final AbsoluteLayout layout = new AbsoluteLayout();
	private Button ok;

/*	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Test1UI.class)
	public static class Servlet extends VaadinServlet {
	}
*/
	@Override
	protected void init(VaadinRequest request) {
		WrappedSession session = request.getWrappedSession();
        HttpSession httpSession = ((WrappedHttpSession) session).getHttpSession();
        ServletContext servletContext = httpSession.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        MyBean bean = applicationContext.getBean(MyBean.class);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            layout.addComponent(new Label(bean.getAuthenticated()));
        } else {
            layout.addComponent(new Label(bean.getNotAuthenticated()));
        }

        
		layout.setImmediate(false);
		layout.setWidth("100%");
		layout.setHeight("100%");

		// layout.setMargin(true);
		setContent(layout);
		      
		// loginForm_2

		textField_1 = new TextField();
		textField_1.setImmediate(false);
		textField_1.setWidth("-1px");
		textField_1.setHeight("-1px");
		layout.addComponent(textField_1, "top:316.0px;left:223.0px;");

		// ok
		ok = new Button();
		ok.setCaption("login");
		ok.setImmediate(false);
		ok.setWidth("-1px");
		ok.setHeight("-1px");
		layout.addComponent(ok, "top:314.0px;left:420.0px;");
		
		ok.addClickListener(new Button.ClickListener() {
		    public void buttonClick(ClickEvent event) {
		        Notification.show("login");
		    }
		});

		// test1 xx = new test1();
		// layout.addComponent(xx);
	}

}