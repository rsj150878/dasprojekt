package com.app.Components;

import java.sql.SQLException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.dbIO.DBConnection;
import com.app.interfaces.DetailInterface;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;

public class HundFuerBesitzer extends CustomComponent implements DetailInterface {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private HorizontalSplitPanel horizontalSplitPanel_1;
	@AutoGenerated
	private AbsoluteLayout TableLayout;
	@AutoGenerated
	private Table table_1;
	@AutoGenerated
	private Button addHund;

	private SQLContainer personContainer;

	private SQLContainer hundContainer;
	
	private SQLContainer userContainer;

	private Object idPerson;

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	@SuppressWarnings("deprecation")
	public HundFuerBesitzer() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		TableQuery q2 = new TableQuery("user",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		
		
		TableQuery q1 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");
		try {
			personContainer = new SQLContainer(q1);
			userContainer = new SQLContainer(q2);

			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authentication = context.getAuthentication();
			String username = authentication.getName();

			System.out.println("username" + username);

			userContainer.addContainerFilter(new Equal("email", username));
			
			personContainer.addContainerFilter(new Equal("iduser", userContainer.getItem(userContainer.firstItemId()).getItemProperty("iduser").getValue()));
			Object persItemID = personContainer.firstItemId();
			idPerson = personContainer.getItem(persItemID)
					.getItemProperty("idperson").getValue();

			TableQuery q3 = new TableQuery("hund",
					DBConnection.INSTANCE.getConnectionPool());
			q3.setVersionColumn("version");
			hundContainer = new SQLContainer(q3);
			hundContainer.addContainerFilter(new Equal("idperson", idPerson));
			hundContainer.setAutoCommit(false);

			this.table_1.setContainerDataSource(hundContainer);
			this.table_1.setSelectable(true);
			this.table_1.setVisibleColumns("zwingername");

			this.addHund.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					// hundFuerBesitzerContainer.addItem()

					hundContainer.setAutoCommit(false);
					Object x = hundContainer.addItem();

					hundContainer.getItemUnfiltered(x)
							.getItemProperty("zwingername")
							.setValue("neuer hund");

					hundContainer.getItemUnfiltered(x)
							.getItemProperty("idperson").setValue(idPerson);
					// Object y = hundFuerBesitzerContainer.addItem();
					// hundFuerBesitzerContainer.getItem(y).getItemProperty("idhund").setValue(hundContainer.getItem(x).getItemProperty("idhund").getValue());
					// hundFuerBesitzerContainer.getItem(y).getItemProperty("idperson").setValue(personContainer.getItem(persItemID).getItemProperty("idperson").getValue());

					table_1.setContainerDataSource(hundContainer);
					table_1.select(x);
					table_1.setSelectable(true);
					table_1.setVisibleColumns("zwingername");

				}

			});

			this.table_1.addListener(new ItemClickListener() {

				@Override
				public void itemClick(ItemClickEvent event) {
					// TODO Auto-generated method stub
					if (event.getButton() == MouseButton.LEFT) {
						// you can handle left/right/middle -mouseclick

						// Object itemId = event.getItemId();
						// HundDetail hundDetail = new
						// HundDetail(table_1.getItem(itemId));

						// horizontalSplitPanel_1.setSecondComponent(hundDetail);

					}

				}

			});

			this.table_1
					.addValueChangeListener(new Property.ValueChangeListener() {

						@Override
						public void valueChange(ValueChangeEvent event) {
							// TODO Auto-generated method stub
							Object itemID = table_1.getValue();

							HundDetail hundDetail = new HundDetail(table_1
									.getItem(itemID));

							horizontalSplitPanel_1
									.setSecondComponent(hundDetail);

						}
					});

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	public void commit() {
		try {

			hundContainer.commit();

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	public void rollback() {
		try {

			hundContainer.rollback();
			horizontalSplitPanel_1.setSecondComponent(null);
			
			
			System.out.println("bin in rolback");

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");

		// horizontalSplitPanel_1
		horizontalSplitPanel_1 = buildHorizontalSplitPanel_1();
		mainLayout.addComponent(horizontalSplitPanel_1,
				"top:-20.0px;left:0.0px;");

		return mainLayout;
	}

	@AutoGenerated
	private HorizontalSplitPanel buildHorizontalSplitPanel_1() {
		// common part: create layout
		horizontalSplitPanel_1 = new HorizontalSplitPanel();
		horizontalSplitPanel_1.setImmediate(true);
		horizontalSplitPanel_1.setWidth("800px");
		horizontalSplitPanel_1.setHeight("480px");

		// TableLayout
		TableLayout = buildTableLayout();
		horizontalSplitPanel_1.addComponent(TableLayout);

		return horizontalSplitPanel_1;
	}

	@AutoGenerated
	private AbsoluteLayout buildTableLayout() {
		// common part: create layout
		TableLayout = new AbsoluteLayout();
		TableLayout.setImmediate(false);
		TableLayout.setWidth("400px");
		TableLayout.setHeight("480px");

		// addHund
		addHund = new Button();
		addHund.setCaption("Hund hinzufügen");
		addHund.setImmediate(false);
		addHund.setDescription("Hund hinzufügen");
		addHund.setWidth("180px");
		addHund.setHeight("-1px");
		TableLayout.addComponent(addHund, "top:420.0px;left:0.0px;");

		// table_1
		table_1 = new Table();
		table_1.setImmediate(true);
		table_1.setWidth("260px");
		table_1.setHeight("-1px");
		TableLayout.addComponent(table_1, "top:20.0px;left:0.0px;");

		return TableLayout;
	}

}
