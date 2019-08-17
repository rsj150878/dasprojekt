package com.app.dashboardwindow;

import com.app.dashboard.event.DashBoardEvent.CloseOpenWindowsEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class InfoWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7929404767934044084L;
	public static final String ID = "hundesuchwindow";

	public InfoWindow(String infoText) {
		addStyleName("profile-window");
		setId(ID);
		Responsive.makeResponsive(this);

		setModal(true);
		addCloseShortcut(KeyCode.ESCAPE, null);

		setResizable(false);
		setClosable(false);
		setHeight(50.0f, Unit.PERCENTAGE);
		setWidth(100.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		// content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		TextArea info = new TextArea();
		info.setSizeFull();
		info.setValue(infoText);
		info.setReadOnly(true);
		content.addComponent(info);
		content.setExpandRatio(info, 1);

		content.addComponent(buildFooter());

	}

	private Component buildFooter() {
		final HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		//

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(event -> close());
		
		ok.focus();
		footer.addComponent(ok);
		footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);

		return footer;
	}

	public static void open(String text) {
		DashBoardEventBus.post(new CloseOpenWindowsEvent());
		Window w = new InfoWindow(text);
		UI.getCurrent().addWindow(w);
		w.focus();
	}

}
