package com.app.DashBoard;

import java.util.Locale;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("dashboard")
@Widgetset("com.app.DashBoard.DashboardWidgetSet")
@Title("Showinfo")
@SuppressWarnings("serial")
public final class ShowInfoUI extends UI {

    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
	 
    //private final DataProvider dataProvider = new DummyDataProvider();
    private final DashBoardEventBus dashboardEventbus = new DashBoardEventBus();

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.GERMANY);
   
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

      
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
    	VerticalLayout mainLayout = new VerticalLayout();
    	ComboBox yearSelect = new ComboBox("<div text-align:center>Ausstellungsjahr</div>");
    	yearSelect.setCaptionAsHtml(true);
    	
    	yearSelect.addItem(2016);
    	yearSelect.setItemCaption(2016, "2016");
    	
    	mainLayout.addComponent(yearSelect);
    	mainLayout.setComponentAlignment(yearSelect, Alignment.TOP_CENTER);
  
    	setContent(mainLayout);
          }

    
    /**
     * @return An instance for accessing the (dummy) services layer.
     */
//    public static DataProvider getDataProvider() {
//        return ((DashboardUI) getCurrent()).dataProvider;
//    }

    
}