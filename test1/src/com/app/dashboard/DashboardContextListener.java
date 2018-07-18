package com.app.dashboard;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener ( "Context listener for doing something or other." )
public class DashboardContextListener  implements ServletContextListener {

	    // Vaadin app deploying/launching.
	    @Override
	    public void contextInitialized ( ServletContextEvent contextEvent )
	    {
	        ServletContext context = contextEvent.getServletContext();
	        // …
	    }

	    // Vaadin app un-deploying/shutting down.
	    @Override
	    public void contextDestroyed ( ServletContextEvent contextEvent )
	    {
	        ServletContext context = contextEvent.getServletContext();
	        // …
	    }

	}
