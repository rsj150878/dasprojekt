package com.example.testUIWithLogin;

import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.Components.EmailDatenAbgleich;
import com.app.Components.FileImport;
import com.app.Components.HundFuerBesitzer;
import com.app.Components.HundeImport;
import com.app.Components.MitgliederListe;
import com.app.Components.UserDetail;
import com.app.Components.Veranstaltung;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;

public class UserMainViewForm extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private MenuBar mainMenu;

	private Component currentComponent;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public UserMainViewForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String username = authentication.getName();

		System.out.println("username main view" + username);

		final UserDetail ud = new UserDetail();
		final HundFuerBesitzer hdb = new HundFuerBesitzer();
		final MitgliederListe mgl = new MitgliederListe();
		// final Email email = new Email();
		final Veranstaltung veranstaltung = new Veranstaltung();
		final FileImport fileImport = new FileImport();
		final EmailDatenAbgleich emailDatenAbgleich = new EmailDatenAbgleich();
		final HundeImport hundeImport = new HundeImport();

		// mainLayout.addComponent(ud, "top:140.0px;left:0.0px;");
		MenuBar.Command mycommand = new MenuBar.Command() {

			String pos = "top:40.0px;left:20.0px;";

			@Override
			public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {

				if (currentComponent != null) {
					mainLayout.removeComponent(currentComponent);
				}

				// TODO Auto-generated method stub
				if (selectedItem.getText().equals("Meine Daten")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(ud, pos);

					currentComponent = ud;

				} else if (selectedItem.getText().equals("Meine Hunde")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(hdb, pos);
					currentComponent = hdb;

				} else if (selectedItem.getText().equals("Mitglieder")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(mgl, pos);
					currentComponent = mgl;
					mgl.refresh();

				} else if (selectedItem.getText().equals("Email")) {

					// UserDetail ud = new UserDetail();
					// mainLayout.addComponent(email, pos);
					// currentComponent = email;

				} else if (selectedItem.getText().equals("Veranstaltung")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(veranstaltung, pos);
					currentComponent = veranstaltung;
					veranstaltung.refresh();

				} else if (selectedItem.getText().equals("Datenimport")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(fileImport, pos);
					currentComponent = fileImport;

				} else if (selectedItem.getText().equals("Emaildatenabgleich")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(emailDatenAbgleich, pos);
					currentComponent = emailDatenAbgleich;

				} else if (selectedItem.getText().equals("HundeImport")) {

					// UserDetail ud = new UserDetail();
					mainLayout.addComponent(hundeImport, pos);
					currentComponent = hundeImport;

				}

			}

			public void removeAllComponents(Iterator i) {

			}
		};
		{

		}
		;

		MenuBar.MenuItem userDetails = mainMenu.addItem("User", null);
		userDetails.addItem("Meine Daten", null, mycommand);
		userDetails.addItem("Meine Hunde", null, mycommand);

		MenuBar.MenuItem Mitglieder = mainMenu.addItem("Mitglieder", mycommand);

		// MenuBar.MenuItem Email = mainMenu.addItem("Email", mycommand);
		//
		MenuBar.MenuItem Veranstaltung = mainMenu.addItem("Veranstaltung",
				mycommand);

		MenuBar.MenuItem verwaltung = mainMenu.addItem("Verwaltung", null);
		verwaltung.addItem("Datenimport", null, mycommand);
		verwaltung.addItem("Emaildatenabgleich", null, mycommand);
		verwaltung.addItem("HundeImport", null, mycommand);

	}

	public SecurityContext getSecContext() {
		return SecurityContextHolder.getContext();
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

//		// berschrift
//		berschrift = new Label();
//		berschrift.setImmediate(false);
//		berschrift.setWidth("1000px");
//		berschrift.setHeight("98px");
//		berschrift.setValue("Die Webverwaltung");
//		mainLayout.addComponent(berschrift, "top:2.0px;left:20.0px;");

		// mainMenu
		mainMenu = new MenuBar();
		mainMenu.setImmediate(false);
		mainMenu.setWidth("1000px");
		mainMenu.setHeight("-1px");
		mainLayout.addComponent(mainMenu, "top:0.0px;left:20.0px;");

		return mainLayout;
	}

}
