package com.app.DashBoard.Component;

import java.sql.SQLException;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.DashBoard.Event.DashBoardEvent.SearchEvent;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.VeranstaltungsStation;
import com.app.enumPackage.VeranstaltungsStufen;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.themes.ValoTheme;

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

	public VeranstaltungsTeilnehmerGrid(final VeranstaltungsStufen defStufe,
			Item stufe) {
		super();
		this.stufe = stufe;
		this.defStufe = defStufe;

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);

		setSizeFull();

		veranstaltungsTeilnehmerQuery = new TableQuery(
				"veranstaltungs_teilnehmer",
				DBConnection.INSTANCE.getConnectionPool());
		veranstaltungsTeilnehmerQuery.setVersionColumn("version");

		dogQuery = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		personQuery = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());

		try {
			veranstaltungsTeilnehmerContainer = new SQLContainer(
					veranstaltungsTeilnehmerQuery);
			veranstaltungsTeilnehmerContainer.addContainerFilter(new Equal(
					"id_stufe", stufe.getItemProperty("id_stufe").getValue()));

			dogContainer = new SQLContainer(dogQuery);
			personContainer = new SQLContainer(personQuery);

		} catch (SQLException e) {
			Notification.show("fehler beim aufbau der Datencontainer");
			e.printStackTrace();
		}

		final GeneratedPropertyContainer cpContainer = new GeneratedPropertyContainer(
				veranstaltungsTeilnehmerContainer);

		cpContainer.addGeneratedProperty("teilnehmerperson",
				new PropertyValueGenerator<String>() {
					private static final long serialVersionUID = -1636752705984592807L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {

						personContainer.removeAllContainerFilters();
						personContainer.addContainerFilter(new Equal(
								"idperson", item.getItemProperty("id_person")
										.getValue()));
						Item personItem = personContainer
								.getItem(personContainer.getIdByIndex(0));
						return personItem.getItemProperty("nachname")
								.getValue().toString()
								+ " "
								+ personItem.getItemProperty("vorname")
										.getValue().toString();
						// return "asdf";
					}

					@Override
					public Class<String> getType() {
						return String.class;
					}
				});

		cpContainer.addGeneratedProperty("teilnehmerhund",
				new PropertyValueGenerator<String>() {
					private static final long serialVersionUID = -1636752705984592807L;

					@Override
					public String getValue(Item item, Object itemId,
							Object propertyId) {

						dogContainer.removeAllContainerFilters();
						dogContainer.addContainerFilter(new Equal("idhund",
								item.getItemProperty("id_hund").getValue()));
						Item dogItem = dogContainer.getItem(dogContainer
								.getIdByIndex(0));
						return dogItem.getItemProperty("rufname").getValue()
								.toString()
								+ " - "
								+ dogItem.getItemProperty("zwingername")
										.getValue().toString();
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
		setEditorEnabled(true);

		setColumns("teilnehmerperson", "teilnehmerhund", "bezahlt",
				"bestanden", "delete", "ges_punkte","sonderwertung", "hundefuehrer");

		if (!(defStufe.getStationen() == null)) {

			TextField gesPunkte = new TextField();
			getColumn("ges_punkte").setEditorField(gesPunkte);
			getColumn("ges_punkte").setEditable(false);

			for (VeranstaltungsStation x : defStufe.getStationen().getStation()) {
				addColumn(x.getUebung());

				final TextField ue = new TextField();
				ue.addValidator(new IntegerRangeValidator(
						"Punkte müssen zwischen " + x.getMinPunkte() + " und "
								+ x.getMaxPunkte() + " liegen", x
								.getMinPunkte(), x.getMaxPunkte()));

				ue.setNullRepresentation("0");
				getColumn(x.getUebung()).setEditorField(ue);

			}
		}

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
							veranstaltungsTeilnehmerContainer.commit();
						} catch (Exception e) {
							Notification.show("fehler beim speichern");
							e.printStackTrace();

						}

					}
				}));

		getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub

				if (defStufe.equals(VeranstaltungsStufen.STUFE_BH)
						|| defStufe
								.equals(VeranstaltungsStufen.TRAININGS_WT_ANFAENGER)
						|| defStufe
								.equals(VeranstaltungsStufen.TRAININGS_WT_FORTGESCHRITTEN)) {
					return;
				}

				Integer result = 0;
				if (getEditorFieldGroup().isValid()) {

					for (int i = 1; i <= 7; i++) {
						TextField zw = (TextField) getEditorFieldGroup()
								.getField("uebung" + i);

						try {
							if (!(zw == null)) {
								result = result
										+ Integer.parseInt(zw.getValue());
							}
						} catch (NumberFormatException ignored) {

						}

					}

					TextField zw = (TextField) getEditorFieldGroup().getField(
							"ges_punkte");

					zw.setValue(result.toString());
				}
			}

			@Override
			public void postCommit(CommitEvent commitEvent)
					throws CommitException {
				// TODO Auto-generated method stub
				try {
					veranstaltungsTeilnehmerContainer.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		});

	}

	@Subscribe
	public void searchResult(SearchEvent event) {
		DashBoardEventBus.unregister(this);
		if (!(event.getDogIdResult() == null)) {
			Object id = veranstaltungsTeilnehmerContainer.addItem();
			Item newItem = veranstaltungsTeilnehmerContainer
					.getItemUnfiltered(id);
			// newItem.getItemProperty("idkursstunde").setValue(
			// stunde.getItemProperty("idkursstunde").getValue());
			newItem.getItemProperty("id_hund").setValue(event.getDogIdResult());
			newItem.getItemProperty("id_stufe").setValue(
					stufe.getItemProperty("id_stufe").getValue());
			newItem.getItemProperty("id_veranstaltung").setValue(
					stufe.getItemProperty("id_veranstaltung").getValue());

			dogContainer.removeAllContainerFilters();
			dogContainer.addContainerFilter(new Equal("idhund", event
					.getDogIdResult()));
			Item dogItem = dogContainer.getItem(dogContainer.getIdByIndex(0));

			newItem.getItemProperty("id_person").setValue(
					dogItem.getItemProperty("idperson").getValue());
			newItem.getItemProperty("bezahlt").setValue("N");
			newItem.getItemProperty("bestanden").setValue("N");

			try {
				veranstaltungsTeilnehmerContainer.commit();
			} catch (Exception e) {
				Notification.show("fehler beim speichern");
				e.printStackTrace();

			}

		}
	}

}
