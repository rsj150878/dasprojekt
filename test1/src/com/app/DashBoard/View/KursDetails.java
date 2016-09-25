package com.app.DashBoard.View;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class KursDetails extends VerticalLayout implements View {

	private TableQuery kursQuery;
	private TableQuery kursTagQuery;
	private TableQuery kursStundeQuery;

	private SQLContainer kursContainer;
	private SQLContainer kursTagContainer;
	private SQLContainer kursStundeContainer;

	private Grid kursGrid;
	private Grid kursTagGrid;
	private Grid kursStundeGrid;

	public KursDetails() {
		setSizeFull();
		addStyleName("mitglieder");
		DashBoardEventBus.register(this);

		addComponent(buildToolbar());

		kursQuery = new TableQuery("kurs",
				DBConnection.INSTANCE.getConnectionPool());
		kursQuery.setVersionColumn("version");

		kursTagQuery = new TableQuery("kurstag",
				DBConnection.INSTANCE.getConnectionPool());
		kursTagQuery.setVersionColumn("version");

		kursStundeQuery = new TableQuery("kursstunde",
				DBConnection.INSTANCE.getConnectionPool());
		kursStundeQuery.setVersionColumn("version");

		try {
			kursContainer = new SQLContainer(kursQuery);
			kursTagContainer = new SQLContainer(kursTagQuery);
			kursStundeContainer = new SQLContainer(kursStundeQuery);

		} catch (Exception e) {
			Notification.show("fehler beim aufbau der Container");
			e.printStackTrace();
		}

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
				Object o = kursContainer.addItem();
				Item item = kursContainer.getItem(o);
				item.getItemProperty("kursbezeichnung").setValue("");
				item.getItemProperty("startdat").setValue(new Date());
				item.getItemProperty("endedat").setValue(new Date());
				try {
					kursContainer.commit();
					kursContainer.refresh();
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
				if (kursGrid.getSelectedRow() == null) {
					Notification.show("den zu löschenden Kurs auswählen");
				} else if (kursTagContainer.size() > 0) {
					Notification
							.show("Kurs kann nicht gelöscht werden es sind noch Tage vorhanden");
				} else {
					kursContainer.removeItem(kursGrid.getSelectedRow());
					try {
						kursContainer.commit();
					} catch (UnsupportedOperationException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Notification.show("fehler beim löschen des Kurses");
					}
				}
			}

		});


		kursLayout.addComponent(buttonLayout);

		kursGrid = new Grid();
		kursGrid.setSizeFull();
		kursGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		kursGrid.setEditorEnabled(true);

		kursGrid.setColumns("kursbezeichnung", "startdat", "endedat");

		kursGrid.setContainerDataSource(kursContainer);

		kursGrid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				kursTagContainer.removeAllContainerFilters();
				if (!(kursGrid.getSelectedRow() == null)) {
					kursTagContainer.addContainerFilter(new Equal("idkurs",
							kursContainer.getItem(kursGrid.getSelectedRow())
									.getItemProperty("idkurs").getValue()));
				}
				kursTagGrid.deselectAll();

			}

		});

		kursGrid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub

			}

			@Override
			public void postCommit(CommitEvent commitEvent)
					throws CommitException {

				try {
					kursContainer.commit();
				} catch (UnsupportedOperationException | SQLException
						| NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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

				if (kursGrid.getSelectedRow() == null) {
					Notification.show("Bitte Kurstag auswählen");

				} else {
					Object o = kursTagContainer.addItem();
					Item item = kursTagContainer.getItemUnfiltered(o);
					item.getItemProperty("bezeichnung").setValue("neuer Tag");
					item.getItemProperty("idkurs").setValue(
							kursContainer.getItem(kursGrid.getSelectedRow())
									.getItemProperty("idkurs").getValue());
					try {
						kursTagContainer.commit();
						kursTagContainer.refresh();
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
				if (kursTagGrid.getSelectedRow() == null) {
					Notification.show("den zu löschenden Tag auswählen");
				} else if (kursStundeContainer.size() > 0) {
					Notification
							.show("Tag kann nicht gelöscht werden es sind noch Stunden vorhanden");
				} else {
					kursTagContainer.removeItem(kursTagGrid.getSelectedRow());
					try {
						kursTagContainer.commit();
					} catch (UnsupportedOperationException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Notification.show("fehler beim löschen des Tages");
					}
				}
			}

		});

		kursTagLayout.addComponent(buttonLayout);

		kursTagGrid = new Grid();
		kursTagGrid.setSizeFull();
		kursTagGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursTagGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursTagGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		kursTagGrid.setEditorEnabled(true);

		kursTagGrid.setColumns("bezeichnung");

		kursTagGrid.setContainerDataSource(kursTagContainer);

		kursTagGrid.addSelectionListener(new SelectionListener() {

			@Override
			public void select(SelectionEvent event) {
				kursStundeContainer.removeAllContainerFilters();
				if (!(kursTagGrid.getSelectedRow() == null)) {
					kursStundeContainer.addContainerFilter(new Equal(
							"idkurstag", kursTagContainer
									.getItem(kursTagGrid.getSelectedRow())
									.getItemProperty("idkurstag").getValue()));
				}
				kursStundeGrid.select(null);

			}

		});

		kursTagGrid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub

			}

			@Override
			public void postCommit(CommitEvent commitEvent)
					throws CommitException {
				try {
					kursTagContainer.commit();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
					throw new CommitException(e);
				} catch (SQLException e) {
					e.printStackTrace();
					throw new CommitException(e);
				}
			}

		});
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
				// TODO Auto-generated method stub

				if (kursTagGrid.getSelectedRow() == null) {
					Notification.show("Bitte Kurstag auswählen");

				} else {
					Object o = kursStundeContainer.addItem();
					Item item = kursStundeContainer.getItemUnfiltered(o);
					item.getItemProperty("bezeichnung").setValue("neue Stunde");
					item.getItemProperty("idkurstag").setValue(
							kursTagContainer
									.getItem(kursTagGrid.getSelectedRow())
									.getItemProperty("idkurstag").getValue());
					try {
						kursStundeContainer.commit();
						kursStundeContainer.refresh();
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
				if (kursStundeGrid.getSelectedRow() == null) {
					Notification.show("Bitte zu löschende Stunde wählen");
				} else {
					kursStundeContainer.removeItem(kursStundeGrid
							.getSelectedRow());
					try {
						kursStundeContainer.commit();
					} catch (Exception e) {
						Notification.show("Fehler beim löschen der Stunde");
					}
				}

			}

		});

		kursStundeLayout.addComponent(buttonLayout);

		kursStundeGrid = new Grid();
		kursStundeGrid.setSizeFull();
		kursStundeGrid.addStyleName(ValoTheme.TABLE_BORDERLESS);
		kursStundeGrid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		kursStundeGrid.addStyleName(ValoTheme.TABLE_COMPACT);

		kursStundeGrid.setEditorEnabled(true);

		kursStundeGrid.setColumns("bezeichnung","startzeit","endzeit");

		kursStundeGrid.setContainerDataSource(kursStundeContainer);

		PopupDateField dateField = new PopupDateField();
		dateField.setDateFormat("HH:mm");
		dateField.setResolution(Resolution.MINUTE);
		//dateField.setConverter(new StringToDateConverter());
		dateField.addStyleName("time-only");

		dateField.setConverter(new MyConverter());
		
		kursStundeGrid.getColumn("startzeit").setEditorField(dateField);
		kursStundeGrid.getColumn("startzeit").setRenderer(new DateRenderer(new SimpleDateFormat("HH:mm")));
		
		PopupDateField endeTimeField = new PopupDateField();
		endeTimeField.setDateFormat("HH:mm");
		endeTimeField.setResolution(Resolution.MINUTE);
		endeTimeField.addStyleName("time-only");
		endeTimeField.setConverter(new MyConverter());
		
		kursStundeGrid.getColumn("endzeit").setEditorField(endeTimeField);
		kursStundeGrid.getColumn("endzeit").setRenderer(new DateRenderer(new SimpleDateFormat("HH:mm")));
		
		
		//field.setResolution(Resolution.MINUTE);
		kursStundeGrid.getEditorFieldGroup().addCommitHandler(
				new CommitHandler() {

					@Override
					public void preCommit(CommitEvent commitEvent)
							throws CommitException {
						// TODO Auto-generated method stub

					}

					@Override
					public void postCommit(CommitEvent commitEvent)
							throws CommitException {

						try {
							kursStundeContainer.commit();
						} catch (UnsupportedOperationException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});

		kursStundeLayout.addComponent(kursStundeGrid);

		return kursStundeLayout;
	}
	
	public class MyConverter implements Converter<Date, Time> {

	    private static final long serialVersionUID = 1L;
	   // public static final MyConverter INSTANCE = new MyConverter();

	    @Override
	    public Time convertToModel(Date value,
	            Class<? extends Time> targetType, Locale locale)
	            throws ConversionException {
	        return value == null ? null : new Time(value.getTime());
	    }

	    @Override
	    public Date convertToPresentation(Time value,
	            Class<? extends Date> targetType, Locale locale)
	            throws ConversionException {
	        return value;
	    }

	    @Override
	    public Class<Time> getModelType() {
	        return Time.class;
	    }

	    @Override
	    public Class<Date> getPresentationType() {
	        return Date.class;
	    }

//	    private Object readResolve() {
//	        return INSTANCE; // preserves singleton property
//	    }

	}


}
