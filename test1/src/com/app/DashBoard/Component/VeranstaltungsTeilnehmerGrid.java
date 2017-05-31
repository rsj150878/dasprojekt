package com.app.DashBoard.Component;

import java.sql.SQLException;
import java.util.Locale;

import com.app.DashBoard.Event.DashBoardEvent.SearchEvent;
import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.app.dbIO.HundItemGenerator;
import com.app.dbIO.HundTransactionalContainerWrapper;
import com.app.dbIO.HundTxListener;
import com.app.enumPackage.VeranstaltungsStation;
import com.app.enumPackage.VeranstaltungsStufen;
import com.google.common.eventbus.Subscribe;
import com.vaadin.v7.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.renderers.ButtonRenderer;
import com.vaadin.v7.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.v7.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.GeneratedPropertyContainer;
import com.vaadin.v7.data.util.PropertyValueGenerator;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.RowId;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

public class VeranstaltungsTeilnehmerGrid extends Grid {

	/**
	 * 
	 */
	private static final long serialVersionUID = -709896999326786160L;
	private SQLContainer veranstaltungsTeilnehmerContainer;
	private TableQuery veranstaltungsTeilnehmerQuery;

	private SQLContainer dogContainer;
	private TableQuery dogQuery;

	private SQLContainer personContainer;
	private TableQuery personQuery;

	private Item stufe;
	private final VeranstaltungsStufen defStufe;
	private Integer idHund;

	private HundTransactionalContainerWrapper txContainer;

	public VeranstaltungsTeilnehmerGrid(final VeranstaltungsStufen defStufe, Item stufe) {
		super();
		this.stufe = stufe;
		this.defStufe = defStufe;

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);

		setSizeFull();

		veranstaltungsTeilnehmerQuery = new TableQuery("veranstaltungs_teilnehmer",
				DBConnection.INSTANCE.getConnectionPool());
		veranstaltungsTeilnehmerQuery.setVersionColumn("version");

		dogQuery = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		personQuery = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());

		try {
			veranstaltungsTeilnehmerContainer = new SQLContainer(veranstaltungsTeilnehmerQuery);
			veranstaltungsTeilnehmerContainer
					.addContainerFilter(new Equal("id_stufe", stufe.getItemProperty("id_stufe").getValue()));
			//veranstaltungsTeilnehmerContainer.setAutoCommit(true);

			txContainer = new HundTransactionalContainerWrapper(veranstaltungsTeilnehmerContainer,
					new TeilnehmerItemGenerator(veranstaltungsTeilnehmerContainer));

			dogContainer = new SQLContainer(dogQuery);
			personContainer = new SQLContainer(personQuery);

		} catch (SQLException e) {
			Notification.show("fehler beim aufbau der Datencontainer");
			e.printStackTrace();
		}

		final GeneratedPropertyContainer cpContainer = new GeneratedPropertyContainer(txContainer);

		cpContainer.addGeneratedProperty("teilnehmerperson", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = -1636752705984592807L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {

				personContainer.removeAllContainerFilters();
				personContainer.addContainerFilter(new Equal("idperson", item.getItemProperty("id_person").getValue()));
				Item personItem = personContainer.getItem(personContainer.getIdByIndex(0));
				return personItem.getItemProperty("nachname").getValue().toString() + " "
						+ personItem.getItemProperty("vorname").getValue().toString();
				// return "asdf";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		cpContainer.addGeneratedProperty("teilnehmerhund", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = -1636752705984592807L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {

				dogContainer.removeAllContainerFilters();
				dogContainer.addContainerFilter(new Equal("idhund", item.getItemProperty("id_hund").getValue()));
				Item dogItem = dogContainer.getItem(dogContainer.getIdByIndex(0));
				return dogItem.getItemProperty("rufname").getValue().toString() + " - "
						+ dogItem.getItemProperty("zwingername").getValue().toString();
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		cpContainer.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = -1636752705984592807L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {

				return "del";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});

		setContainerDataSource(cpContainer);

		setColumns("teilnehmerperson", "teilnehmerhund", "bezahlt", "bestanden", "delete", "ges_punkte",
				"sonderwertung", "hundefuehrer");
		
		CheckBox bezahlt = new CheckBox();
		//bezahlt.setConvertedValue("J");
		getColumn("bezahlt").setEditorField(bezahlt);
		bezahlt.addValueChangeListener(e -> txContainer.commit());

		bezahlt.setConverter(new StringToBoolean() );

		CheckBox bestanden = new CheckBox();
		//bezahlt.setConvertedValue("J");
		getColumn("bestanden").setEditorField(bestanden);
		bestanden.addValueChangeListener(e -> txContainer.commit());

		bestanden.setConverter(new StringToBoolean() );
		
		TextField hundeFuehrer = new TextField();
		getColumn("hundefuehrer").setEditorField(hundeFuehrer);
		hundeFuehrer.addValueChangeListener (e-> txContainer.commit());
		
		if (!(defStufe.getStationen() == null)) {

			ReadOnlyField gesPunkte = new ReadOnlyField();
			getColumn("ges_punkte").setEditorField(gesPunkte);
			gesPunkte.setEnabled(false);
			gesPunkte.setReadOnly(true);
			//getColumn("ges_punkte").setEditable(false);
			//getColumn("ges_punkte").
			//getColumn("ges_punkte").s

			for (VeranstaltungsStation x : defStufe.getStationen().getStation()) {

				addColumn(x.getUebung());

				System.out.println("x: " + x);

				if (x.equals(VeranstaltungsStation.WESENSTEST_BEMERKUNG)) {

					final TextArea ue = new TextArea();
					ue.setColumns(60);
					getColumn(x.getUebung()).setEditorField(ue);
					ue.addValueChangeListener(e -> txContainer.commit());

				} else {

					final TextField ue = new TextField();
					ue.addValidator(new IntegerRangeValidator(
							"Punkte müssen zwischen " + x.getMinPunkte() + " und " + x.getMaxPunkte() + " liegen",
							x.getMinPunkte(), x.getMaxPunkte()));

					ue.setNullRepresentation("0");
					getColumn(x.getUebung()).setEditorField(ue);
					ue.addValueChangeListener(e -> {
						rechneGesPunkte();
						txContainer.specialCommit();
					});
				}
			}
		}

		// getColumn("").getEditorField().setp

		getColumn("delete").setRenderer(new ButtonRenderer(new RendererClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5189664162539062746L;

			@Override
			public void click(RendererClickEvent event) {
				cpContainer.removeItem(event.getItemId());
				try {
					veranstaltungsTeilnehmerContainer.commit();
				} catch (Exception e) {
					Notification.show("fehler beim speichern");
					e.printStackTrace();

				}

			}
		}));

		setEditorEnabled(true);
		setEditorBuffered(false);

		// notifications
		txContainer.addTxListener(new HundTxListener() {
			@Override
			public void transactionStarted(boolean implicit) {
				
			}

			@Override
			public void transactionCommitted() {
				// In unbuffered mode, all editor changes are always
				// propagated to container,
				// this just closes the editor
				cancelEditor();
				try {
					veranstaltungsTeilnehmerContainer.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// showTrayNotification("Changes committed");
			}

			@Override
			public void specialCommitted() {
				// In unbuffered mode, all editor changes are always
				// propagated to container,
				// this just closes the editor
				try {
					veranstaltungsTeilnehmerContainer.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// showTrayNotification("Changes committed");
			}
			@Override
			public void transactionRolledBack() {
				cancelEditor();
				// commit.setEnabled(false);
				// reset.setEnabled(false);
				// showTrayNotification("Changes rolled back");
			}
		});

	}

	private void rechneGesPunkte() {
		Item forCalcItem = txContainer.getItem(getEditedItemId());
		Integer result = 0;
		
	
		if (forCalcItem == null) {
			return;
		}
		//getColumn("ges_punkte").setEditable(true);
		
		
		for (int i = 1; i <= 7; i++) {
//			Integer value = (Integer) forCalcItem.getItemProperty("uebung" + i).getValue();

			if (!(forCalcItem.getItemProperty("uebung" + i).getValue() == null)) {
				result += (Integer) forCalcItem.getItemProperty("uebung" + i).getValue();
			}
		}
		//getColumn("ges_punkte").setEditable(false);
		

		forCalcItem.getItemProperty("ges_punkte").setValue(result);

	}

	@Subscribe
	public void searchResult(SearchEvent event) {
		DashBoardEventBus.unregister(this);
		if (!(event.getDogIdResult() == null)) {
			idHund = event.getDogIdResult();
			System.out.println("idhund " + idHund);
			txContainer.addItem();

		}
	}

	public void meldeHundId(Integer hundId) {
		idHund = hundId;
		txContainer.addItem();

	}
	
	public class StringToBoolean implements Converter<Boolean, String> {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public String convertToModel(Boolean value, Class<? extends String> targetType, Locale locale)
	            throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
	        if (value == true) {
	            return "J";
	        } else {
	            return null;
	        }
	    }

	    @Override
	    public Boolean convertToPresentation(String value, Class<? extends Boolean> targetType, Locale locale)
	            throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
	        if (value == null || value.equals("N")) {
	            return false;
	        } else {
	            return true;
	        }
	    }

	    @Override
	    public Class<String> getModelType() {
	        return String.class;
	    }

	    @Override
	    public Class<Boolean> getPresentationType() {
	        return Boolean.class;
	    }
	}

	
	public class ReadOnlyField extends TextField
	{
	    public ReadOnlyField()
	    {
	        super();
	        this.setReadOnly(true);
	    }

	    @Override
	    public void setEnabled(boolean enabled)
	    {
	        // always set to disabled state
	        super.setEnabled(false);
	    }
	}

	private class TeilnehmerItemGenerator implements HundItemGenerator, QueryDelegate.RowIdChangeListener {
		private SQLContainer veranstaltungsTeilnehmerContainer;
		private RowId teilnehmerId;
	
		public TeilnehmerItemGenerator(SQLContainer veranstaltungsTeilnehmerContainer) {
			this.veranstaltungsTeilnehmerContainer = veranstaltungsTeilnehmerContainer;
		}

		@Override
		public Item createNewItem(Object itemId) {

			Filter eq = new Equal("id_teilnehmer", itemId);
			veranstaltungsTeilnehmerContainer.addContainerFilter(eq);
			Item newItem = veranstaltungsTeilnehmerContainer.getItem(veranstaltungsTeilnehmerContainer.firstItemId());
			veranstaltungsTeilnehmerContainer.removeContainerFilter(eq);
			System.out.println("itemid " + itemId.toString());
			System.out.println("item " + newItem);
			return newItem;
		}

		@Override
		public Object createNewItemId() {
			//veranstaltungs
			veranstaltungsTeilnehmerContainer.addRowIdChangeListener(this);
			Object id = veranstaltungsTeilnehmerContainer.addItem();
			Item newItem = veranstaltungsTeilnehmerContainer.getItemUnfiltered(id);
			newItem.getItemProperty("id_hund").setValue(idHund);
			newItem.getItemProperty("id_stufe").setValue(stufe.getItemProperty("id_stufe").getValue());
			newItem.getItemProperty("id_veranstaltung").setValue(stufe.getItemProperty("id_veranstaltung").getValue());

			dogContainer.removeAllContainerFilters();
			System.out.println("idhund 2 " + idHund);
			dogContainer.addContainerFilter(new Equal("idhund", idHund));
			Item dogItem = dogContainer.getItem(dogContainer.getIdByIndex(0));

			System.out.println("id perosn " + dogItem.getItemProperty("idperson").getValue().toString());
			newItem.getItemProperty("id_person").setValue(dogItem.getItemProperty("idperson").getValue());
			newItem.getItemProperty("bezahlt").setValue("N");
			newItem.getItemProperty("bestanden").setValue("N");

			try {
				veranstaltungsTeilnehmerContainer.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return teilnehmerId.getId()[0];
		}

		@Override
		public void rowIdChange(RowIdChangeEvent arg0) {
			teilnehmerId = arg0.getNewRowId();
		}

	}

}
