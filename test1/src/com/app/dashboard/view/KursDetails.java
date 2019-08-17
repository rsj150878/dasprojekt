package com.app.dashboard.view;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBKurs;
import com.app.kurs.Kurs;
import com.app.kurs.KursStunde;
import com.app.kurs.KursTag;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial" })
public class KursDetails extends VerticalLayout implements View {

	private DBKurs dbKurs;

	private List<Kurs> kurse;
	private List<KursTag> kursTage;
	private List<KursStunde> kursStunden;

	private Grid<Kurs> kursGrid;
	private Grid<KursTag> kursTagGrid;
	private Grid<KursStunde> kursStundeGrid;

	public KursDetails() {
		setSizeFull();
		addStyleName("mitglieder");
		DashBoardEventBus.register(this);

		addComponent(buildToolbar());
		dbKurs = new DBKurs();

		Component x = buildWorkingArea();
		addComponent(x);
		setExpandRatio(x, 1);

		// table = buildTable();
		// addComponent(table);
		// setExpandRatio(table, 1);

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
		Responsive.makeResponsive(header);

		Label title = new Label("Kursdetails bearbeiten");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		return header;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public Component buildWorkingArea() {
		Panel mainPanel = new Panel();
		mainPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		mainPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		mainPanel.setSizeFull();

		VerticalLayout panelLayout = new VerticalLayout();
		mainPanel.setContent(panelLayout);

		Component x = buildKursGrid();
		panelLayout.addComponent(x);

		Component kursTagComponent = buildKursTagGrid();
		panelLayout.addComponent(kursTagComponent);

		Component kursStundeComponent = buildKursStundeGrid();
		panelLayout.addComponent(kursStundeComponent);

		return mainPanel;
	}

	public Component buildKursGrid() {
		VerticalLayout kursLayout = new VerticalLayout();
		kursLayout.setSpacing(true);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		Button newButton = new Button("neu");
		buttonLayout.addComponent(newButton);

		newButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				Kurs newKurs = new Kurs();
				newKurs.setKursBezeichnung("");
				newKurs.setStartDat(new Date());
				newKurs.setEndeDat(new Date());
				try {

					dbKurs.saveKurs(newKurs);
					kurse.add(newKurs);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});

		Button delButton = new Button("löschen");
		buttonLayout.addComponent(delButton);

		delButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Kurs selectedItem = kursGrid.getSelectedItems().iterator().next();
				if (selectedItem == null) {
					Notification.show("den zu löschenden Kurs auswählen");
				} else if (kursTage.size() > 0) {
					Notification.show("Kurs kann nicht gelöscht werden es sind noch Tage vorhanden");
				} else {

					kurse.remove(selectedItem);
					try {
						dbKurs.deleteKurs(selectedItem);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Notification.show("fehler beim löschen des Kurses");
					}
				}
			}

		});

		kursLayout.addComponent(buttonLayout);

		kursGrid = new Grid<>();
		kursGrid.setSizeFull();
		kursGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		kursGrid.addColumn(kurs -> {
			TextField oerc1Hund = new TextField();
			oerc1Hund.setValue(kurs.getPreisOerc1Hund() == null ? "" : kurs.getPreisOerc1Hund().toString());
			oerc1Hund.addValueChangeListener(evt -> {
				kurs.setPreisOerc1Hund(Double.valueOf(oerc1Hund.getValue()));
				try {
					dbKurs.saveKurs(kurs);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return oerc1Hund;

		}, new ComponentRenderer()).setCaption("Preis 1. Hund - ÖRC-Mitglied");

		kursGrid.addColumn(kurs -> {
			TextField oerc2Hund = new TextField();
			oerc2Hund.setValue(kurs.getPreisOerc2Hund() == null ? "" : kurs.getPreisOerc2Hund().toString());
			oerc2Hund.addValueChangeListener(evt -> {
				kurs.setPreisOerc2Hund(Double.valueOf(oerc2Hund.getValue()));
				try {
					dbKurs.saveKurs(kurs);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return oerc2Hund;

		}, new ComponentRenderer()).setCaption("Preis 2. Hund/Tag - ÖRC-Mitglied");
		kursGrid.addColumn(kurs -> {
			TextField noerc1Hund = new TextField();
			noerc1Hund.setValue(kurs.getPreisNoerc1Hund() == null ? "" : kurs.getPreisNoerc1Hund().toString());
			noerc1Hund.addValueChangeListener(evt -> {
				kurs.setPreisNoerc1Hund(Double.valueOf(noerc1Hund.getValue()));
				try {
					dbKurs.saveKurs(kurs);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return noerc1Hund;

		}, new ComponentRenderer()).setCaption("Preis 1. Hund - Kein-Mitglied");

		kursGrid.addColumn(kurs -> {
			TextField noerc2Hund = new TextField();
			noerc2Hund.setValue(kurs.getPreisNoerc1Hund() == null ? "" : kurs.getPreisNoerc1Hund().toString());
			noerc2Hund.addValueChangeListener(evt -> {
				kurs.setPreisNoerc2Hund(Double.valueOf(noerc2Hund.getValue()));
				try {
					dbKurs.saveKurs(kurs);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return noerc2Hund;

		}, new ComponentRenderer()).setCaption("Preis 2. Hund/Tag - Kein Mitglied");

		try {
			kurse = dbKurs.getAllKurse();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Notification.show("fehler beim ermitteln der Kurse");

		}

		if (kurse != null) {
			kursGrid.setItems(kurse);

		}
		
		kursGrid.addSelectionListener(event -> {

			Kurs selectedItem = kursGrid.getSelectedItems().iterator().next();
			try {
				kursTage = dbKurs.getKursTageZuKurs(selectedItem);
				if (kursTage != null) {
					kursTagGrid.setItems(kursTage);

				}
			} catch (Exception e) {
				Notification.show("fehler beim ermitteln der tage");
				e.printStackTrace();
			}
			kursTagGrid.deselectAll();

		});

		kursLayout.addComponent(kursGrid);
		return kursLayout;
	}

	public Component buildKursTagGrid() {
		VerticalLayout kursTagLayout = new VerticalLayout();
		kursTagLayout.setSpacing(true);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		Button newKursTagButton = new Button("neuer Kurstag");
		buttonLayout.addComponent(newKursTagButton);

		newKursTagButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				Kurs selectedItem = kursGrid.getSelectedItems().iterator().next();
				if (selectedItem == null) {
					Notification.show("Bitte Kurstag auswählen");

				} else {
					KursTag neuerKursTag = new KursTag();
					neuerKursTag.setBezeichnung("neuer Tag");
					neuerKursTag.setIdKurs(selectedItem.getIdKurs());
					try {
						dbKurs.saveKursTag(neuerKursTag);
						kursTage.add(neuerKursTag);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		});

		Button delButton = new Button("löschen");
		buttonLayout.addComponent(delButton);

		delButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				KursTag selectedItem = kursTagGrid.getSelectedItems().iterator().next();

				if (selectedItem == null) {
					Notification.show("den zu löschenden Tag auswählen");
				} else if (kursStunden.size() > 0) {
					Notification.show("Tag kann nicht gelöscht werden es sind noch Stunden vorhanden");
				} else {
					try {

						dbKurs.deleteKursTag(selectedItem);
						kursTage.remove(selectedItem);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Notification.show("fehler beim löschen des Tages");
					}
				}
			}

		});

		kursTagLayout.addComponent(buttonLayout);

		kursTagGrid = new Grid<>();
		kursTagGrid.setSizeFull();
		kursTagGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursTagGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursTagGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		
		kursTagGrid.addItemClickListener(event -> {
			KursTag selectedItem = kursTagGrid.getSelectedItems().iterator().next();
			if (!(selectedItem == null)) {

				try {
					kursStunden = dbKurs.getKursStundenZuKursTag(selectedItem);
					if (kursStunden != null) {
						kursStundeGrid.setItems(kursStunden);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			}
			kursStundeGrid.select(null);

		}

		);

		kursTagGrid.addColumn(kursTag -> {
			TextField bezeichnung = new TextField();
			bezeichnung.setValue(kursTag.getBezeichnung() == null ? "" : kursTag.getBezeichnung());
			bezeichnung.addValueChangeListener(evt -> {
				kursTag.setBezeichnung(bezeichnung.getValue());
				try {
					dbKurs.saveKursTag(kursTag);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return bezeichnung;

		}, new ComponentRenderer()).setCaption("bezeichnung");

		kursTagLayout.addComponent(kursTagGrid);
		return kursTagLayout;
	}

	public Component buildKursStundeGrid() {
		VerticalLayout kursStundeLayout = new VerticalLayout();
		kursStundeLayout.setSpacing(true);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		Button newKursStundeButton = new Button("neuer Kursstunde");
		buttonLayout.addComponent(newKursStundeButton);

		newKursStundeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				KursTag selectedItem = kursTagGrid.getSelectedItems().iterator().next();
				if (selectedItem == null) {
					Notification.show("Bitte Kurstag auswählen");

				} else {

					KursStunde newKursStunde = new KursStunde();
					newKursStunde.setBezeichnung("");
					newKursStunde.setIdKursTag(selectedItem.getIdKursTag());
					try {
						dbKurs.saveKursStunde(newKursStunde);
						kursStunden.add(newKursStunde);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		});

		Button delButton = new Button("löschen");
		buttonLayout.addComponent(delButton);

		delButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				KursStunde selectedItem = kursStundeGrid.getSelectedItems().iterator().next();
				if (selectedItem == null) {
					Notification.show("Bitte zu löschende Stunde wählen");
				} else {
					try {
						dbKurs.deleteKursStunde(selectedItem);
						kursStunden.remove(selectedItem);
					} catch (Exception e) {
						Notification.show("Fehler beim löschen der Stunde");
					}
				}

			}

		});

		kursStundeLayout.addComponent(buttonLayout);

		kursStundeGrid = new Grid<>();
		kursStundeGrid.setSizeFull();
		kursStundeGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursStundeGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursStundeGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		
		kursStundeGrid.addColumn(kursStunde -> {
			TextField bezeichnung = new TextField();
			bezeichnung.setValue(kursStunde.getBezeichnung() == null ? "" : kursStunde.getBezeichnung());
			bezeichnung.addValueChangeListener(evt -> {
				kursStunde.setBezeichnung(bezeichnung.getValue());
				try {
					dbKurs.saveKursStunde(kursStunde);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return bezeichnung;

		}, new ComponentRenderer()).setCaption("Bezeichnung");

		kursStundeGrid.addColumn(kursStunde -> {
			TextField bezeichnung = new TextField();
			bezeichnung.setValue(kursStunde.getStartZeit() == null ? ""
					: new SimpleDateFormat("HH:mm").format(kursStunde.getStartZeit()));
			bezeichnung.addValueChangeListener(evt -> {
				kursStunde.setStartZeit(Time.valueOf(bezeichnung.getValue()));
				try {
					dbKurs.saveKursStunde(kursStunde);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return bezeichnung;

		}, new ComponentRenderer()).setCaption("Startzeit");

		kursStundeGrid.addColumn(kursStunde -> {
			TextField bezeichnung = new TextField();
			bezeichnung.setValue(kursStunde.getEndZeit() == null ? ""
					: new SimpleDateFormat("HH:mm").format(kursStunde.getEndZeit()));
			bezeichnung.addValueChangeListener(evt -> {
				kursStunde.setEndZeit(Time.valueOf(bezeichnung.getValue()));
				try {
					dbKurs.saveKursStunde(kursStunde);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("fehler beim speichern");
				}
			});
			return bezeichnung;

		}, new ComponentRenderer()).setCaption("Endzeit");

		kursStundeLayout.addComponent(kursStundeGrid);

		return kursStundeLayout;
	}

//	public class MyConverter implements Converter<Date, Time> {
//
//		private static final long serialVersionUID = 1L;
//		// public static final MyConverter INSTANCE = new MyConverter();
//
//		@Override
//		public Time convertToModel(Date value, Class<? extends Time> targetType, Locale locale)
//				throws ConversionException {
//			return value == null ? null : new Time(value.getTime());
//		}
//
//		@Override
//		public Date convertToPresentation(Time value, Class<? extends Date> targetType, Locale locale)
//				throws ConversionException {
//			return value;
//		}
//
//		@Override
//		public Class<Time> getModelType() {
//			return Time.class;
//		}
//
//		@Override
//		public Class<Date> getPresentationType() {
//			return Date.class;
//		}
//
////	    private Object readResolve() {
////	        return INSTANCE; // preserves singleton property
////	    }
//
//	}

}
