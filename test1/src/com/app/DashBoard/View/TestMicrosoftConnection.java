package com.app.DashBoard.View;

import java.sql.SQLException;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnectionMicrosoft;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TestMicrosoftConnection extends VerticalLayout implements View {

	public TestMicrosoftConnection() {
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
		VerticalLayout mainLayout = new VerticalLayout();
		SQLContainer schauContainer;
		TableQuery q1;
		
		q1 = new TableQuery("dbo.TestTAble",
				DBConnectionMicrosoft.INSTANCE.getConnectionPool());
		
		try {
			schauContainer = new SQLContainer(q1);
			
		} catch (SQLException e) {
			
		}
		

		return (mainLayout);

	}

}
