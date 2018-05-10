package OERC.Dashboard;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class AbstractOERCUi extends UI {

	@Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		
	}

	public static boolean getUseProdUrl() {
		return Page.getCurrent().getLocation().toString().contains("retrieverdata.at");
	}

	public static boolean getUseLocalUrl() {
		return Page.getCurrent().getLocation().toString().contains("test")
				|| Page.getCurrent().getLocation().toString().contains("localhost");
	}

}
