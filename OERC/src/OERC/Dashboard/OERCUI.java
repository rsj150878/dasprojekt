package OERC.Dashboard;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import OERC.Domain.User;
import OERC.Event.DashboardEvent.BrowserResizeEvent;
import OERC.Event.DashboardEvent.CloseOpenWindowsEvent;
import OERC.Event.DashboardEvent.UserLoggedOutEvent;
import OERC.Event.DashboardEvent.UserLoginRequestedEvent;
import OERC.Event.DashboardEventBus;
import OERC.View.LoginView;
import OERC.View.MainView;
import OERC.data.PersonDao;

@Theme("dashboard")
@Widgetset("OERC.Dashboard.widgetset.OercWidgetset")
@Title("QuickTickets Dashboard")
@SuppressWarnings("serial")
public final class OERCUI extends UI {

	/*
	 * This field stores an access to the dummy backend layer. In real
	 * applications you most likely gain access to your beans trough lookup or
	 * injection; and not in the UI but somewhere closer to where they're
	 * actually accessed.
	 */
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

	@Override
	protected void init(final VaadinRequest request) {

		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new BrowserResizeEvent());
			}
		});
	}

	/**
	 * Updates the correct content for this UI based on the current user status.
	 * If the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		if (user != null && "admin".equals(user.getRole())) {
			// Authenticated user
			setContent(new MainView());
			removeStyleName("loginview");
			getNavigator().navigateTo(getNavigator().getState());
		} else {
			setContent(new LoginView());
			addStyleName("loginview");
		}
	}

	@Subscribe
	public void userLoginRequested(final UserLoginRequestedEvent event) {
		User user = null;
		try {
			PersonDao dao = new PersonDao();
			user = dao.loadUserById(event.getUserName());
			;
		} catch (UsernameNotFoundException e) {
			Notification.show("User und/oder passwort falsch", Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();

		} catch (Exception e) {
			Notification.show("Zugriffsfehler", Notification.Type.ERROR_MESSAGE);
			e.printStackTrace();

		}
		VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
		updateContent();
	}

	@Subscribe
	public void userLoggedOut(final UserLoggedOutEvent event) {
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}

	@Subscribe
	public void closeOpenWindows(final CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

	/**
	 * @return An instance for accessing the (dummy) services layer.
	 */

	public static DashboardEventBus getDashboardEventbus() {
		return ((OERCUI) getCurrent()).dashboardEventbus;
	}

	public static boolean getUseProdUrl() {
		return Page.getCurrent().getLocation().toString().contains("retrieverdata.at");
	}

	public static boolean getUseLocalUrl() {
		System.out.println("sldfkj" + Page.getCurrent().getLocation().toString());
		return Page.getCurrent().getLocation().toString().contains("test")
				|| Page.getCurrent().getLocation().toString().contains("localhost");
	}

}
