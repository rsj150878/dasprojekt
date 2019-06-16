package com.app.dashboard.component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import com.app.auth.Hund;
import com.app.auth.Person;
import com.app.dashboard.event.DashBoardEvent.SearchEvent;
import com.app.dashboard.event.DashBoardEventBus;
import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.BestandenDataType;
import com.app.enumdatatypes.VeranstaltungsStation;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;

public class VeranstaltungsTeilnehmerGrid extends Grid<VeranstaltungsTeilnehmer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -709896999326786160L;
	private VeranstaltungsStufe stufe;
	private Integer idHund;

	private List<VeranstaltungsTeilnehmer> teilnehmerList;
	private DBVeranstaltung dbVeranstaltung;

	private List<VeranstaltungsTeilnehmer> draggedItems = null;
	
	public VeranstaltungsTeilnehmerGrid(final VeranstaltungsStufen defStufe, VeranstaltungsStufe stufe) {
		super();
		this.stufe = stufe;
		dbVeranstaltung = new DBVeranstaltung();

		addStyleName(ValoTheme.TABLE_BORDERLESS);
		addStyleName(ValoTheme.TABLE_NO_STRIPES);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);

		setSizeFull();

		try {

			teilnehmerList = dbVeranstaltung.getAlleTeilnehmerZuStufe(stufe.getIdStufe());
			setItems(teilnehmerList);
		} catch (Exception e) {
			Notification.show("fehler beim aufbau der Datencontainer");
			e.printStackTrace();
		}

		Column<VeranstaltungsTeilnehmer, Person> teilnehmerColumn = addColumn(VeranstaltungsTeilnehmer::getPerson);
		teilnehmerColumn.setRenderer(person -> person.getLastName() + " " + person.getFirstName(), new TextRenderer());
		teilnehmerColumn.setCaption("Teilnehmer");

		Column<VeranstaltungsTeilnehmer, Hund> hundColumn = addColumn(VeranstaltungsTeilnehmer::getHund);
		hundColumn.setRenderer(hund -> hund.getRufname() + " - " + hund.getZwingername(), new TextRenderer());
		hundColumn.setCaption("Hund");

		addComponentColumn(teilnehmer -> {
			Button delButton = new Button();
			delButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
			delButton.setIcon(VaadinIcons.TRASH);
			delButton.addClickListener(evt -> {
				try {
					dbVeranstaltung.deleteTeilnehmer(teilnehmer);
				} catch (Exception e) {
					Notification.show("es ist ein fehler beim löschen passiert");
				}
				teilnehmerList.remove(teilnehmer);
				getDataProvider().refreshAll();
			});
			return delButton;
		});

		addColumn(teilnehmer -> {
			CheckBox bezahlt = new CheckBox();
			bezahlt.setValue(teilnehmer.getBezahlt().equals("J") ? true : false);
			bezahlt.addValueChangeListener(evt -> {

				teilnehmer.setBezahlt(bezahlt.getValue() == true ? "J" : "N");
				saveTeilnehmer(teilnehmer);
			});
			return bezahlt;
		}, new ComponentRenderer()).setCaption("bezahlt");

		addColumn(teilnehmer -> {
			NativeSelect<BestandenDataType> bestanden = new NativeSelect<>();
			bestanden.setItems(BestandenDataType.values());
			bestanden.setItemCaptionGenerator(BestandenDataType::getBezeichnung);
			bestanden.setValue(BestandenDataType.getBestandenDataTypeForDb(teilnehmer.getBestanden()));

			bestanden.addValueChangeListener(evt -> {

				teilnehmer.setBestanden(bestanden.getValue().dbWert);
				saveTeilnehmer(teilnehmer);
			});

			bestanden.setEmptySelectionAllowed(false);
			return bestanden;
		}, new ComponentRenderer()).setCaption("bestanden").setWidth(200);

		addColumn(teilnehmer -> {
			TextField hundeFuehrer = new TextField();
			hundeFuehrer.setValue(teilnehmer.getHundefuehrer() == null ? "" : teilnehmer.getHundefuehrer());
			hundeFuehrer.addValueChangeListener(evt -> {
				teilnehmer.setHundefuehrer(hundeFuehrer.getValue());
				saveTeilnehmer(teilnehmer);
			});
			return hundeFuehrer;

		}, new ComponentRenderer()).setCaption("Hundeführer");
		// hundeFuehrer.addValueChangeListener (e-> txContainer.commit());

		addColumn(teilnehmer -> {

			TextField platzierung = new TextField();
			platzierung.setValue(teilnehmer.getPlatzierung() == null ? "" : teilnehmer.getPlatzierung().toString());
			platzierung.addValueChangeListener(evt -> {
				teilnehmer.setPlatzierung(new Integer(platzierung.getValue().isEmpty() ? "" : platzierung.getValue()));
				saveTeilnehmer(teilnehmer);
			});
			return platzierung;
		}, new ComponentRenderer()).setCaption("platzierung").setComparator((t1, t2) -> {
			return t1.getPlatzierung().compareTo(t2.getPlatzierung());
		});
		// platzierung.addValueChangeListener(e -> txContainer.specialCommit()
		// );

		addColumn(teilnehmer -> {

			TextField startnr = new TextField();
			startnr.setValue(teilnehmer.getStartnr() == null ? "" : teilnehmer.getStartnr().toString());
			startnr.addValueChangeListener(evt -> {
				teilnehmer.setStartnr(new Integer(startnr.getValue().isEmpty() ? "" : startnr.getValue()));
				saveTeilnehmer(teilnehmer);
			});
			return startnr;
		}, new ComponentRenderer()).setCaption("startnr").setComparator((t1, t2) -> {
			return t1.getStartnr().compareTo(t2.getStartnr());
		});

		if (!(defStufe.getStationen() == null)) {

			addColumn(VeranstaltungsTeilnehmer::getGesPunkte).setCaption("Gesamtpunkte");

			for (VeranstaltungsStation x : defStufe.getStationen().getStation()) {

				if (x.equals(VeranstaltungsStation.WESENSTEST_BEMERKUNG)) {

					TextArea ue = new TextArea();
					addColumn(VeranstaltungsTeilnehmer::getBemerkung)
							.setEditorComponent(ue, VeranstaltungsTeilnehmer::setBemerkung).setCaption("Bemerkung");

				} else {

					addColumn(teilnehmer -> {

						TextField ue = new TextField();
						try {

							Method sumInstanceMethod = VeranstaltungsTeilnehmer.class
									.getMethod("get" + x.getUebung().substring(0, 1).toUpperCase()
											+ x.getUebung().substring(1).toLowerCase());

							Integer wert = (Integer) sumInstanceMethod.invoke(teilnehmer);

							if (wert == null)
								wert = new Integer(0);

							if (wert.intValue() < x.getMinPunkte() || wert.intValue() > x.getMaxPunkte()) {
								ue.setComponentError(new UserError("Punkte müssen zwischen " + x.getMinPunkte()
										+ " und " + x.getMaxPunkte() + " liegen"));
							} else {
								ue.setComponentError(null);
							}
							ue.setValue(wert.toString());
							ue.addValueChangeListener(evt -> {
								try {
									Method setMethod = VeranstaltungsTeilnehmer.class
											.getMethod("set" + x.getUebung().substring(0, 1).toUpperCase()
													+ x.getUebung().substring(1).toLowerCase(), Integer.class);

									Integer wertSet = new Integer(ue.getValue().isEmpty() ? "" : ue.getValue());

									if (wertSet.intValue() < x.getMinPunkte()
											|| wertSet.intValue() > x.getMaxPunkte()) {
										ue.setComponentError(new UserError("Punkte müssen zwischen " + x.getMinPunkte()
												+ " und " + x.getMaxPunkte() + " liegen"));
									} else {
										ue.setComponentError(null);
									}

									setMethod.invoke(teilnehmer, wertSet);
									rechneGesPunkte(teilnehmer);
									saveTeilnehmer(teilnehmer);

									getDataProvider().refreshAll();

								} catch (Exception e) {
									e.printStackTrace();
								}
							});

						} catch (Exception e) {
							e.printStackTrace();
						}

						return ue;
					}, new ComponentRenderer()).setCaption(x.getUebung());

					// getColumn(x.getUebung()).setEditorField(ue);
					// ue.addValueChangeListener(e -> {
					// //rechneGesPunkte();
					// //txContainer.specialCommit();
					// });
				}
			}
		} else {
			// PunkteField gesPunkte = new PunkteField(0,999);
			// addColumn(VeranstaltungsTeilnehmer::getGesPunkte).setCaption("Gesamtpunkte").setEditorComponent(gesPunkte,
			// x -> { x);
			addColumn(teilnehmer -> {
				TextField integerField = new TextField();
				integerField.setValue(teilnehmer.getGesPunkte() == null ? "" : teilnehmer.getGesPunkte().toString());
				integerField.addValueChangeListener(evt -> {
					teilnehmer.setGesPunkte(
							new Integer(integerField.getValue().equals("") ? "0" : integerField.getValue()));
					saveTeilnehmer(teilnehmer);
				});
				return integerField;
			}, new ComponentRenderer()).setCaption("gesamtpunkte").setComparator((t1, t2) -> {
				return t1.getGesPunkte().compareTo(t2.getGesPunkte());
			});

		}

		if (defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_ANFAENGER)
				|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_FORTGESCHRITTEN)
				|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_EINSTEIGER)
				|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_LEICHT)
				|| defStufe.equals(VeranstaltungsStufen.TRAININGS_WT_MITTEL)) {
			addColumn(teilnehmer -> {
				TextField sonderWertung = new TextField();
				sonderWertung.setValue(teilnehmer.getSonderWertung() == null ? "" : teilnehmer.getSonderWertung());
				sonderWertung.addValueChangeListener(evt -> {

					teilnehmer.setSonderWertung(sonderWertung.getValue());
					saveTeilnehmer(teilnehmer);
				});
				return sonderWertung;
			}, new ComponentRenderer()).setCaption("sonderWertung");
		}

		this.recalculateColumnWidths();

		GridDragSource<VeranstaltungsTeilnehmer> dragSource = new GridDragSource<>(this);
		dragSource.setEffectAllowed(EffectAllowed.MOVE);

		// dragSource.setDragDataGenerator("text", teilnehmer -> {
		// return teilnehmer.getHundefuehrer() == null ||
		// teilnehmer.getHundefuehrer().isEmpty()
		// ? teilnehmer.getPerson().getLastName() + " " +
		// teilnehmer.getPerson().getFirstName()
		// : teilnehmer.getHundefuehrer() + "\n" +
		// teilnehmer.getHund().getZwingername();
		//
		// });

		dragSource.addGridDragStartListener(event ->
		// Keep reference to the dragged items
		draggedItems = event.getDraggedItems());
		
		dragSource.addGridDragEndListener(event -> {
		    // If drop was successful, remove dragged items from source Grid
		    if (event.getDropEffect() == DropEffect.MOVE) {
		        ((ListDataProvider<VeranstaltungsTeilnehmer>) getDataProvider()).getItems()
		                .removeAll(draggedItems);
		        getDataProvider().refreshAll();

		        // Remove reference to dragged items
		        draggedItems = null;
		    }
		});
	}

	private void saveTeilnehmer(VeranstaltungsTeilnehmer teilnehmer) {
		try {
			dbVeranstaltung.saveTeilnehmer(teilnehmer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rechneGesPunkte(VeranstaltungsTeilnehmer teilnehmer) {

		Integer result = Optional.ofNullable(teilnehmer.getUebung1()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung2()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung3()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung4()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung5()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung6()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung7()).orElse(Integer.valueOf(0))
				+ Optional.ofNullable(teilnehmer.getUebung8()).orElse(Integer.valueOf(0));
		teilnehmer.setGesPunkte(result);

	}

	@Subscribe
	public void searchResult(SearchEvent event) {
		DashBoardEventBus.unregister(this);
		if (!(event.getDogIdResult() == null)) {
			idHund = event.getDogIdResult();
			System.out.println("idhund " + idHund);
			DBHund dbHund = new DBHund();
			DBPerson dbPerson = new DBPerson();
			try {
				Hund hund = dbHund.getHundForHundId(idHund);
				Person person = dbPerson.getPersonForId(hund.getIdperson());
				VeranstaltungsTeilnehmer neuerTeilnehmer = new VeranstaltungsTeilnehmer();

				neuerTeilnehmer.setPerson(person);
				neuerTeilnehmer.setHund(hund);
				neuerTeilnehmer.setIdHund(idHund);
				neuerTeilnehmer.setIdPerson(hund.getIdperson());
				neuerTeilnehmer.setIdVeranstaltung(stufe.getIdVeranstaltung());
				neuerTeilnehmer.setIdStufe(stufe.getIdStufe());
				neuerTeilnehmer.setBezahlt("N");

				neuerTeilnehmer.setPlatzierung(0);
				neuerTeilnehmer.setStartnr(0);
				dbVeranstaltung.saveTeilnehmer(neuerTeilnehmer);

				teilnehmerList.add(neuerTeilnehmer);
				getDataProvider().refreshAll();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
