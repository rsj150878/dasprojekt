package OERC.View;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

import OERC.Dashboard.DashboardNavigator;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

	Grid xyz = new Grid();
	
    public MainView() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new DashboardMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new DashboardNavigator(content);
    }
}
