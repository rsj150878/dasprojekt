package OERC.View;

import java.util.Collection;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import OERC.Domain.Veranstaltungen;
import OERC.Event.DashboardEventBus;
import OERC.data.VeranstaltungDao;

public class OercVeranstaltungUebersicht extends TabSheet implements View // ,
																			// //
																			// CloseHandler,
// WurfDetail.WurfDetailListener
{

	/**
	* 
	*/

	private Grid<Veranstaltungen> vaGrid;
	private static final long serialVersionUID = 5317013525651368996L;

	public static final String CONFIRM_DIALOG_ID = "confirm-dialog";

	private Collection<Veranstaltungen> veranstaltungCollection;
	private ListDataProvider<Veranstaltungen> dataProvider;
	private VeranstaltungDao vDao;

	public OercVeranstaltungUebersicht() {
		setSizeFull();
		vDao = new VeranstaltungDao();
		addStyleName("transactions");
		addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		// setCloseHandler(this);

		DashboardEventBus.register(this);

		vaGrid = buildDraftsList();
		HorizontalLayout gridLayout = new HorizontalLayout();
		
		gridLayout.addComponentsAndExpand(vaGrid);
		// gridLayout.setExpandRatio(vaGrid, 1);
		addTab(gridLayout, "Alle Veranstaltungen");
		// setExpandRatio(allDrafts, 1);

	}

	private Grid<Veranstaltungen> buildDraftsList() {
		Grid<Veranstaltungen> vaGrid = new Grid<Veranstaltungen>(Veranstaltungen.class);

		vaGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		vaGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		vaGrid.addStyleName(ValoTheme.TABLE_COMPACT);
		// vaGrid.setSizeFull();

		try {
			veranstaltungCollection = vDao.getAllVeranstaltungen();
			dataProvider = DataProvider.ofCollection(veranstaltungCollection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		vaGrid.setColumns("bezeichnung", "anfangDatum", "endDatum");

		vaGrid.addColumn(Veranstaltungen -> "edit", new ButtonRenderer(clickEvent -> {
			Veranstaltungen zw = (Veranstaltungen) clickEvent.getItem();
			openReport(zw);

		})).setId("edit");

		vaGrid.setItems(veranstaltungCollection);
		// vaGrid.setDataProvider(dataProvider);

		return vaGrid;
	}

	public void openReport(Veranstaltungen toOpenVeranstaltung) {
		// addTab(detailView).setClosable(true);
		//
		// DashboardEventBus.post(new ReportsCountUpdatedEvent(
		// getComponentCount() - 1));
		//
		// detailView.setTitle("test");
		setSelectedTab(getComponentCount() - 1);

	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}

	// @Override
	// public void titleChanged(String newTitle, WurfDetail detail) {
	// // TODO Auto-generated method stub
	// getTab(detail).setCaption(newTitle);
	//
	// }

}
