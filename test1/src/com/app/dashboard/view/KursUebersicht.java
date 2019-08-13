package com.app.dashboard.view;

import java.text.SimpleDateFormat;
import java.util.List;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBKurs;
import com.app.kurs.Kurs;
import com.app.kurs.KursTag;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class KursUebersicht extends Panel implements View {

	private ComboBox<Kurs> kursAuswahl;

	private DBKurs dbKurs;

	private TabSheet sheet;

	private VerticalLayout root;

	List<Kurs> listKurse;

	public KursUebersicht() {

		setSizeFull();
		// addStyleName("mitglieder");
		DashBoardEventBus.register(this);

		dbKurs = new DBKurs();
		try {
			listKurse = dbKurs.getAllKurse();

		} catch (Exception e) {
			Notification.show("fehler beim ermitteln der Kurse");
			e.printStackTrace();
		}
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("mitglieder");
		setContent(root);
		Responsive.makeResponsive(root);

		root.addComponent(buildToolbar());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void detach() {
		super.detach();
		// A new instance of TransactionsView is created every time it's
		// navigated to so we'll need to clean up references to it on detach.
		DashBoardEventBus.unregister(this);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		header.setSizeFull();
		Responsive.makeResponsive(header);

		Label title = new Label("Kursübersicht");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);
		
		kursAuswahl = new ComboBox<Kurs>();
		kursAuswahl.setWidth(100, Unit.PERCENTAGE);
		kursAuswahl.addStyleName(ValoTheme.COMBOBOX_SMALL);
		kursAuswahl.addStyleName(ValoTheme.LABEL_H1);
		kursAuswahl.setItems(listKurse);
		kursAuswahl.setItemCaptionGenerator(kurs -> kurs.getKursBezeichnung().trim() + "("
				+ new SimpleDateFormat("dd.MM.yyyy").format(kurs.getStartDat()) + " - "
				+ new SimpleDateFormat("dd.MM.yyyy").format(kurs.getEndeDat()) + ")");
		header.addComponent(kursAuswahl);
		
		kursAuswahl.addSelectionListener(event -> buildInfoPanel());
		return header;
	}

	private void buildInfoPanel() {

		List<KursTag> kursTagListe = null;

		if (!(sheet == null)) {
			root.removeComponent(sheet);
		}

		try {
			kursTagListe = dbKurs.getKursTageZuKurs(kursAuswahl.getValue());
		} catch (Exception e) {
			Notification.show("fehler beim ermitteln der Kurse");
			e.printStackTrace();
		}

		sheet = new TabSheet();
		sheet.setSizeFull();
		sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

		kursTagListe.forEach(tag -> sheet.addTab(new KursTagPanel(tag), tag.getBezeichnung()));

		root.addComponent(sheet);
		root.setExpandRatio(sheet, 1);

	}

}
