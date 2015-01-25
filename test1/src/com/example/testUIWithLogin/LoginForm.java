package com.example.testUIWithLogin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Ondrej Kvasnovsky
 */
public class LoginForm extends GridLayout {

    private TextField txtLogin = new TextField("Login: ");
    private PasswordField txtPassword = new PasswordField("Password: ");
    private Button btnLogin = new Button("Login");
    private Button btnRegister = new Button("Registrieren");
    
    public LoginForm() {
    	super(3,3);
    	super.setWidth(400, Sizeable.Unit.PIXELS);
    	super.setHeight(200, Sizeable.Unit.PIXELS);
    	VerticalLayout x = new VerticalLayout();
        x.addComponent(txtLogin);
        x.addComponent(txtPassword);
        x.addComponent(btnLogin);
        x.addComponent(btnRegister);

        this.addComponent(x,1,1);
        this.setComponentAlignment(x, Alignment.MIDDLE_CENTER);
        LoginFormListener loginFormListener = getLoginFormListener();

        btnLogin.setId("login");
        btnLogin.addClickListener(loginFormListener);
        btnRegister.setId("registrieren");
        btnRegister.addClickListener(loginFormListener);
     
    }

    public LoginFormListener getLoginFormListener() {
    	testUIWithLogin ui = (testUIWithLogin) UI.getCurrent();
        ApplicationContext context = ui.getApplicationContext();
        return context.getBean(LoginFormListener.class);
    }

    public TextField getTxtLogin() {
        return txtLogin;
    }

    public PasswordField getTxtPassword() {
        return txtPassword;
    }
}
