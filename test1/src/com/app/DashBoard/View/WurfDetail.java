package com.app.DashBoard.View;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import org.vaadin.data.tx.ItemGenerator;

import com.app.dbIO.DBConnectionMicrosoft;
import com.app.dbIO.HundTransactionalContainerWrapper;
import com.app.dbIO.HundTxListener;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToShortConverter;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class WurfDetail extends CustomComponent {

	private final WurfDetailListener listener;
	private HundTransactionalContainerWrapper txContainer;

	private Grid hundGrid = new Grid();
	private Button commit;
	private Button reset;
	private Button newDog;
	private Button delDog;

	private SQLContainer hundContainer;

	public WurfDetail(Object idWurf, WurfDetailListener listener) {
		this.listener = listener;
		Panel draftsPanel = new Panel();
		draftsPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		draftsPanel.addStyleName(ValoTheme.PANEL_SCROLL_INDICATOR);
		draftsPanel.setSizeFull();

		final VerticalLayout allDrafts = new VerticalLayout();
		allDrafts.setSizeFull();
		allDrafts.setSpacing(true);
		allDrafts.setMargin(true);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		newDog = new Button("neuer Hund", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
					txContainer.addItem();
				
			}
		});

		buttonLayout.addComponent(newDog);

		delDog = new Button("Hund löschen", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				{
					Collection<Object> selectedRowIds = hundGrid
							.getSelectedRows();
					for (Object selectedRowId : selectedRowIds) {
						txContainer.removeItem(selectedRowId);
					}
					hundGrid.select(null);
				}
			}
		});
		buttonLayout.addComponent(delDog);

		commit = new Button("speichern", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				txContainer.commit();
			}
		});
		commit.setEnabled(false);
		buttonLayout.addComponent(commit);

		reset = new Button("änderungen verwerfen", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				{
					txContainer.rollback();
					hundGrid.select(null);
				}
			}
		});
		buttonLayout.addComponent(reset);

		allDrafts.addComponent(buttonLayout);

		TableQuery q1 = new TableQuery("tabHunde",
				DBConnectionMicrosoft.INSTANCE.getConnectionPool(),
				new MSSQLGenerator());
		// SQLContahundContainer = null;

		try {
			hundContainer = new SQLContainer(q1);
			hundContainer.addContainerFilter(new Equal("IDMitgliederWurf",
					idWurf));
		

			txContainer = new HundTransactionalContainerWrapper(hundContainer,
					new HundItemGenerator(hundContainer, idWurf));
			hundGrid.setContainerDataSource(txContainer);
			hundGrid.setColumns("Name", "Zuchtbuchnummer", "IDHundGeschlecht", "Gesundheit", "InterneBemerkung");

			hundGrid.addItemClickListener(new ItemClickEvent.ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent itemClickEvent) {
					{
						if (itemClickEvent.getButton() == MouseEventDetails.MouseButton.LEFT) {
							Object itemId = itemClickEvent.getItemId();
							if (itemId != null) {
								hundGrid.editItem(itemId);
							}
						}

					}
				}
			});

			hundGrid.setEditorEnabled(true);
			hundGrid.setEditorBuffered(false);
			hundGrid.setSizeFull();
			
			ComboBox geschlecht = new ComboBox();
			
			geschlecht.addItem(Short.valueOf("1"));
			geschlecht.setItemCaption(Short.valueOf("1"), "Rüde");
			geschlecht.addItem(Short.valueOf("2"));
			geschlecht.setItemCaption(Short.valueOf("2"), "Hündin");
			Grid.Column geschlechtColumn = hundGrid.getColumn("IDHundGeschlecht");
			geschlechtColumn.setEditorField(geschlecht);
			geschlechtColumn.setConverter(new StringToShortConverter() {
		        @Override
		        public String convertToPresentation(Short value,
		                Class<? extends String> targetType, Locale locale)
		                throws Converter.ConversionException {
		            if (value == null || value.equals(Short.valueOf("1"))) {
		                return "Rüde";
		            } else {
		                return "Hündin";
		            }
		        }
		    });
			
			

			// notifications
			txContainer.addTxListener(new HundTxListener() {
				@Override
				public void transactionStarted(boolean implicit) {
					commit.setEnabled(true);
					reset.setEnabled(true);
				}

				@Override
				public void transactionCommitted() {
					// In unbuffered mode, all editor changes are always
					// propagated to container,
					// this just closes the editor
					hundGrid.cancelEditor();
					try {
						hundContainer.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}

					commit.setEnabled(false);
					reset.setEnabled(false);
					showTrayNotification("Changes committed");
				}

				@Override
				public void transactionRolledBack() {
					hundGrid.cancelEditor();
					commit.setEnabled(false);
					reset.setEnabled(false);
					showTrayNotification("Changes rolled back");
				}

				@Override
				public void specialCommitted() {
					// TODO Auto-generated method stub
					
				}
			});

		} catch (SQLException e) {

		}

		allDrafts.addComponent(hundGrid);
		allDrafts.setExpandRatio(hundGrid, 1);
		draftsPanel.setContent(allDrafts);

		this.setCompositionRoot(draftsPanel);

	}

	public void setTitle(String title) {

		listener.titleChanged(title, WurfDetail.this);
	}

	public interface WurfDetailListener {
		void titleChanged(String newTitle, WurfDetail detail);
	}

	private class HundItemGenerator implements ItemGenerator
		 {
		private SQLContainer hundContainer;
		private Object idWurf;

		public HundItemGenerator(SQLContainer hundContainer, Object idWurf) {
			this.hundContainer = hundContainer;
			this.idWurf = idWurf;

		}

		@Override
		public Item createNewItem(Object itemId) {
			// TODO Auto-generated method stub

			Filter eq = new Equal("IDHunde", itemId);
			hundContainer.addContainerFilter(eq);
			Item newItem = hundContainer.getItem(hundContainer.firstItemId());
			hundContainer.removeContainerFilter(eq);
			System.out.println("itemid " + itemId.toString());
			System.out.println("item " + newItem);
			return newItem;
		}

		@Override
		public Object createNewItemId() {
			// TODO Auto-generated method stub
			Object id = hundContainer.addItem();
			Item newItem = hundContainer.getItemUnfiltered(id);
			
			String dbId = UUID.randomUUID().toString();
			newItem.getItemProperty("Korrekt").setValue(Boolean.TRUE);
			newItem.getItemProperty("IDHunde").setValue(dbId);
			newItem.getItemProperty("Freigabe").setValue(Boolean.TRUE);
			newItem.getItemProperty("PedigreeJA").setValue(Boolean.FALSE);
			newItem.getItemProperty("Loeschen").setValue(Boolean.FALSE);
			newItem.getItemProperty("MehrfachBesitz").setValue(Boolean.FALSE);
			newItem.getItemProperty("Name").setValue("***neuer Hund***");
			newItem.getItemProperty("IDMitgliederWurf").setValue(idWurf);
			System.out.println("in new Item");
			try {
				hundContainer.commit();
				hundContainer.refresh();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//System.out.println("eranstaltungsId.getId()[0]" + veranstaltungsId.getId()[0].toString());

			return dbId; //veranstaltungsId.getId()[0];
		}

		
	}
	
	
	private void showTrayNotification(String text) {
		Notification n = new Notification(text, null, Type.TRAY_NOTIFICATION);
		n.setPosition(Position.BOTTOM_LEFT);
		n.show(Page.getCurrent());
	}
}
