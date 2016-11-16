package com.app.DashBoard.Component;

import java.sql.SQLException;
import java.util.Locale;

import com.app.DashBoard.Event.DashBoardEvent.SearchEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class HundeSchulStundeUebersichtGrid extends Grid {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6141955154197613028L;

	private TableQuery kursTeilnehmerQuery;

	private SQLContainer kursTeilnehmerContainer;
	private Item stunde;

	private SQLContainer dogContainer;
	private TableQuery dogQuery;

	private SQLContainer personContainer;
	private TableQuery personQuery;

	public HundeSchulStundeUebersichtGrid(Item stunde) {
		super();
		this.stunde = stunde;

		setCaption(stunde.getItemProperty("bezeichnung").getValue().toString());

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);

		setSizeFull();

		getHeader().setVisible(false);
		// setRowHeaderMode(RowHeaderMode.INDEX);

		kursTeilnehmerQuery = new TableQuery("kursteilnehmer",
				DBConnection.INSTANCE.getConnectionPool());
		kursTeilnehmerQuery.setVersionColumn("version");

		dogQuery = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		personQuery = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());

		try {
			kursTeilnehmerContainer = new SQLContainer(kursTeilnehmerQuery);
			kursTeilnehmerContainer.addContainerFilter(new Equal(
					"idkursstunde", stunde.getItemProperty("idkursstunde")
							.getValue()));

			dogContainer = new SQLContainer(dogQuery);
			personContainer = new SQLContainer(personQuery);

		} catch (SQLException e) {
			Notification.show("fehler beim aufbau der Stundencontainer");
			e.printStackTrace();
		}

		final GeneratedPropertyContainer cpContainer = new GeneratedPropertyContainer(
				kursTeilnehmerContainer);

		cpContainer.addGeneratedProperty("kursteilnehmer",
				new PropertyValueGenerator<String>() {
					private static final long serialVersionUID = -1636752705984592807L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {

						dogContainer.removeAllContainerFilters();
						dogContainer.addContainerFilter(new Equal("idhund",
								item.getItemProperty("idhund").getValue()));
						Item dogItem = dogContainer.getItem(dogContainer
								.getIdByIndex(0));
						return dogItem.getItemProperty("rufname").getValue()
								.toString()
								+ " - "
								+ dogItem.getItemProperty("zwingername")
										.getValue().toString();
						// return "asdf";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});

		cpContainer.addGeneratedProperty("delete",
				new PropertyValueGenerator<String>() {
					private static final long serialVersionUID = -1636752705984592807L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {

						return "del";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});

		setContainerDataSource(cpContainer);
		setColumns("kursteilnehmer", "kursbezahlt", "abwfuehrer", "delete");
		getColumn("delete").setRenderer(
				new ButtonRenderer(new RendererClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -5189664162539062746L;

					@Override
					public void click(RendererClickEvent event) {
						cpContainer.removeItem(event.getItemId());
						try {
							kursTeilnehmerContainer.commit();
						} catch (Exception e) {
							Notification.show("fehler beim speichern");
							e.printStackTrace();

						}

					}
				}));

		getColumn("kursbezahlt").setRenderer(new HtmlRenderer(),
				new Converter<String, String>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -1289049726888267048L;

					@Override
					public String convertToModel(String value,
							Class<? extends String> targetType, Locale locale)
							throws Converter.ConversionException {
						return "not implemented";
					}

					@Override
					public String convertToPresentation(String value,
							Class<? extends String> targetType, Locale locale)
							throws Converter.ConversionException {
						return value.equals("J") ? FontAwesome.CHECK_CIRCLE_O
								.getHtml() : FontAwesome.CIRCLE_O.getHtml();
					}

					@Override
					public Class<String> getModelType() {
						return String.class;
					}

					@Override
					public Class<String> getPresentationType() {
						return String.class;
					}
				});

		setEditorEnabled(true);

		getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6469506458961134644L;

			@Override
			public void preCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub

			}

			@Override
			public void postCommit(CommitEvent commitEvent)
					throws CommitException {
				try {
					kursTeilnehmerContainer.commit();
				} catch (Exception e) {
					Notification.show("fehler beim speichern");
					e.printStackTrace();

				}

			}

		});

		OptionGroup bezahltGroup = new OptionGroup();
		bezahltGroup.addItem("J");
		bezahltGroup.setItemCaption("J", "Ja");
		bezahltGroup.addItem("N");
		bezahltGroup.setItemCaption("N", "Nein");

		getColumn("kursbezahlt").setEditorField(bezahltGroup);

	}

	public String copyMailAdressestoClipBoard() {
		StringBuilder sb = new StringBuilder();

		
		for (Object to : kursTeilnehmerContainer.getItemIds()) {
			Item teilnehmerItem = kursTeilnehmerContainer.getItem(to);
			dogContainer.removeAllContainerFilters();
			dogContainer.addContainerFilter(new Equal("idhund", teilnehmerItem
					.getItemProperty("idhund").getValue()));

			Item dogItem = dogContainer.getItem(dogContainer.getIdByIndex(0));

			personContainer.removeAllContainerFilters();
			personContainer.addContainerFilter(new Equal("idperson", dogItem
					.getItemProperty("idperson").getValue()));
			
			Item personItem = personContainer.getItem(personContainer.getIdByIndex(0));
			
			if (!(personItem.getItemProperty("email") == null)) {
				sb.append(personItem.getItemProperty("email").getValue());
				sb.append(";");
			}
			
		}


		return sb.toString();
	}
	
	@Subscribe
	public void searchResult(SearchEvent event) {
		DashBoardEventBus.unregister(this);
		if (!(event.getDogIdResult() == null)) {
			Object id = kursTeilnehmerContainer.addItem();
			Item newItem = kursTeilnehmerContainer.getItemUnfiltered(id);
			newItem.getItemProperty("idkursstunde").setValue(
					stunde.getItemProperty("idkursstunde").getValue());
			newItem.getItemProperty("idhund").setValue(event.getDogIdResult());
			try {
				kursTeilnehmerContainer.commit();
			} catch (Exception e) {
				Notification.show("fehler beim speichern");
				e.printStackTrace();

			}

		}
	}

}
