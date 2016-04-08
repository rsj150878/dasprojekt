package com.app.DashBoard.View;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.EmailSender.EwsReplClass;
import com.app.dbIO.DBConnection;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MailReplicateView extends VerticalLayout implements View {

	
	private SQLContainer personContainer;
	private Button startRepl;
	private final ProgressBar indicator = new ProgressBar();
	
	public MailReplicateView() {
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());
		addComponent(buildWorkingArea());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void detach() {
		
		DashBoardEventBus.unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("MeineHunde");
		title.setSizeUndefined();

		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		header.addComponent(title);

		return header;
	}
	
	private Component buildWorkingArea() {
		VerticalLayout workingAreaLayout = new VerticalLayout();
		
		
		startRepl = new Button("starte Replikation");
		workingAreaLayout.addComponent(startRepl);

		//indicator.setIndeterminate(true);
		indicator.setEnabled(false);

		workingAreaLayout.addComponent(indicator);
		startRepl.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				try {
					final WorkThread thread = new WorkThread();
					thread.start();

					// Enable polling and set frequency to 0.5 seconds
					UI.getCurrent().setPollInterval(500);

					// Disable the button until the work is done
					indicator.setEnabled(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		return workingAreaLayout;
	}
	
	// A thread to do some work
		class WorkThread extends Thread {
			// Volatile because read in another thread in access()

			@Override
			public void run() {

				try {
					TableQuery q1 = new TableQuery("person",
							DBConnection.INSTANCE.getConnectionPool());
					personContainer = new SQLContainer(q1);

					EwsReplClass.INSTANCE.speichereVerknuepfung(personContainer,
							indicator);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Show the "all done" for a while
				try {
					sleep(2000); // Sleep for 2 seconds
				} catch (InterruptedException e) {
				}

				// Update the UI thread-safely
				UI.getCurrent().access(new Runnable() {
					@Override
					public void run() {
						// Restore the state to initial

						indicator.setEnabled(false);

						// Stop polling
						UI.getCurrent().setPollInterval(-1);
						Notification.show("Fertig", Notification.Type.HUMANIZED_MESSAGE);

					}
				});
			}
		}

		

}
