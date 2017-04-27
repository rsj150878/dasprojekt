package OERC.View;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import com.vaadin.server.Page;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.data.util.converter.StringToShortConverter;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Grid;

public class WurfDetail extends CustomComponent {

	private final WurfDetailListener listener;
	//private HundTransactionalContainerWrapper txContainer;

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
//					txContainer.addItem();
				
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
//						txContainer.removeItem(selectedRowId);
					}
					hundGrid.select(null);
				}
			}
		});
		buttonLayout.addComponent(delDog);

		commit = new Button("speichern", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
//				txContainer.commit();
			}
		});
		commit.setEnabled(false);
		buttonLayout.addComponent(commit);

		reset = new Button("änderungen verwerfen", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				{
//					txContainer.rollback();
					hundGrid.select(null);
				}
			}
		});
		buttonLayout.addComponent(reset);

		allDrafts.addComponent(buttonLayout);

//		TableQuery q1 = new TableQuery("tabHunde",
//				DBConnectionMicrosoft.INSTANCE.getConnectionPool(),
//				new MSSQLGenerator());
		// SQLContahundContainer = null;

		try {
//			hundContainer = new SQLContainer(q1);
			hundContainer.addContainerFilter(new Equal("IDMitgliederWurf",
					idWurf));
		

//			txContainer = new HundTransactionalContainerWrapper(hundContainer,
//					new HundItemGeneratorForWurf(hundContainer, idWurf));
//			hundGrid.setContainerDataSource(txContainer);
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

	
	
	private void showTrayNotification(String text) {
		Notification n = new Notification(text, null, Type.TRAY_NOTIFICATION);
		n.setPosition(Position.BOTTOM_LEFT);
		n.show(Page.getCurrent());
	}
}
