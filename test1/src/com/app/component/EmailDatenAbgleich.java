package com.app.component;

import com.app.EmailSender.EwsReplClass;
import com.app.dbIO.DBConnection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class EmailDatenAbgleich extends CustomComponent implements
		ClickListener {

	private VerticalLayout mainLayout;

	private SQLContainer personContainer;
	private Button startRepl;
	private final ProgressBar indicator = new ProgressBar();

	public EmailDatenAbgleich() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	private void buildMainLayout() {
		mainLayout = new VerticalLayout();
		startRepl = new Button("starte Replikation");
		mainLayout.addComponent(startRepl);

		//indicator.setIndeterminate(true);
		indicator.setEnabled(false);

		mainLayout.addComponent(indicator);
		startRepl.addClickListener(this);
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

}
