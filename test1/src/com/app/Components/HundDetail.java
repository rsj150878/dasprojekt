package com.app.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.app.Components.Listener.HundDetailListener;
import com.app.Components.Listener.PrintButtonListener;
import com.app.bean.RassenBean;
import com.app.enumPackage.Rassen;
import com.app.printClasses.Kursblatt;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class HundDetail extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Button abbruch;
	@AutoGenerated
	private Button ok;
	@AutoGenerated
	private Button print;
	@AutoGenerated
	private ComboBox comboBox_4;
	@AutoGenerated
	private PopupDateField wurfdatum;
	@AutoGenerated
	private PopupDateField bh_datum;
	@AutoGenerated
	private TextField zuchtbuchnummer;
	@AutoGenerated
	private TextField chipnummer;
	@AutoGenerated
	private TextField rufname;
	@AutoGenerated
	private TextField zwingername;
	private Item hundItem;
	private Item person;

	private OptionGroup geschlecht;
	private TextField farbe;
	private TextField zuechter;

	List<Component> printList = new ArrayList<Component>();;

	private BeanItemContainer<RassenBean> rassenContainer = new BeanItemContainer<RassenBean>(
			RassenBean.class);

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public HundDetail() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.wurfdatum.setLocale(new Locale("de", "DE"));
		this.wurfdatum.setResolution(Resolution.DAY);

		this.ok.setId("ok");
		this.abbruch.setId("abbruch");

		HundDetailListener buttonListener = new HundDetailListener();
		this.ok.addClickListener(buttonListener);
		this.abbruch.addClickListener(buttonListener);
		this.print.addClickListener(new PrintButtonListener());

		if (!hundItem.equals(null)) {
			setInit();
		}

		buildRassenComboBox();

	}

	public HundDetail(Item hundItem, Item person) {
		this.hundItem = hundItem;
		this.person = person;
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.wurfdatum.setLocale(new Locale("de", "DE"));
		this.wurfdatum.setResolution(Resolution.DAY);

		this.ok.setId("ok");
		this.abbruch.setId("abbruch");

		HundDetailListener buttonListener = new HundDetailListener();
		this.ok.addClickListener(buttonListener);
		this.abbruch.addClickListener(buttonListener);
		this.print.setImmediate(true);
		// this.print.addClickListener(new PrintButtonListener());
		this.print.addClickListener(new printButtonListener());

		setInit();

	}

	public void setInit() {
		this.chipnummer.setPropertyDataSource(hundItem
				.getItemProperty("chipnummer"));
		this.rufname.setPropertyDataSource(hundItem.getItemProperty("rufname"));
		this.zuchtbuchnummer.setPropertyDataSource(hundItem
				.getItemProperty("zuchtbuchnummer"));
		this.zwingername.setPropertyDataSource(hundItem
				.getItemProperty("zwingername"));
		this.wurfdatum.setPropertyDataSource(hundItem
				.getItemProperty("wurfdatum"));

		this.bh_datum.setPropertyDataSource(hundItem
				.getItemProperty("bh_datum"));
		
		this.farbe.setPropertyDataSource(hundItem.getItemProperty("farbe"));
		this.zuechter.setPropertyDataSource(hundItem
				.getItemProperty("zuechter"));

		this.geschlecht.select(hundItem.getItemProperty("geschlecht").getValue() == null
				|| hundItem.getItemProperty("geschlecht").getValue().toString()
						.equals(new String("R")) ? "Rüde" : "Hündin");

		this.geschlecht.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				hundItem.getItemProperty("geschlecht").setValue(
						(geschlecht.isSelected("Rüde")) ? new String("R")
								: new String("H"));
			}

		});
		// try {
		// SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		// Date date = fmt.parse(hundItem.getItemProperty("wurfdatum")
		// .getValue().toString());

		// this.wurfdatum.setValue(date);
		// } catch (ParseException e) {

		// }

		buildRassenComboBox();
	}

	public void buildRassenComboBox() {

		RassenBean selItem = null;
		for (Rassen o : Rassen.values()) {
			RassenBean addObject = new RassenBean(o.getRassenKurzBezeichnung(),
					o.getRassenLangBezeichnung());

			rassenContainer.addItem(addObject);
			if (hundItem.getItemProperty("rasse").getValue() != null
					&& o.getRassenKurzBezeichnung().equals(
							hundItem.getItemProperty("rasse").getValue()
									.toString())) {
				selItem = addObject;
			}

		}

		comboBox_4.setContainerDataSource(rassenContainer);
		comboBox_4.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox_4.setItemCaptionPropertyId("rassenLangBezeichnung");

		if (selItem != null) {

			comboBox_4.setValue(selItem);
		}

		comboBox_4.setImmediate(true);
		comboBox_4.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				hundItem.getItemProperty("rasse").setValue(
						((RassenBean) comboBox_4.getValue())
								.getRassenKurzBezeichnung());

			}

		});

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

		// zwingername
		zwingername = new TextField("Zwingername");
		zwingername.setImmediate(false);
		zwingername.setDescription("Zwingername");
		zwingername.setWidth("-1px");
		zwingername.setHeight("-1px");
		mainLayout.addComponent(zwingername, "top:40.0px;left:40.0px;");

		// rufname
		rufname = new TextField("Rufname");
		rufname.setImmediate(false);
		rufname.setWidth("-1px");
		rufname.setHeight("-1px");
		rufname.setInputPrompt("Rufname");
		rufname.setNullRepresentation("Rufname");
		mainLayout.addComponent(rufname, "top:40.0px;left:220.0px;");

		// chipnummer
		chipnummer = new TextField("Chipnummer");
		chipnummer.setImmediate(false);
		chipnummer.setDescription("Chipnummer");
		chipnummer.setWidth("-1px");
		chipnummer.setHeight("-1px");
		chipnummer.setNullRepresentation("Chipnummer");
		mainLayout.addComponent(chipnummer, "top:80.0px;left:40.0px;");

		// zuchtbuchnummer
		zuchtbuchnummer = new TextField("Zuchtbuchnummer");
		zuchtbuchnummer.setImmediate(true);
		zuchtbuchnummer.setDescription("Zuchtbuchnummer");
		zuchtbuchnummer.setWidth("-1px");
		zuchtbuchnummer.setHeight("-1px");
		zuchtbuchnummer.setInputPrompt("Zuchtbuchnummer");
		zuchtbuchnummer.setNullRepresentation("Zuchtbuchnummer");
		mainLayout.addComponent(zuchtbuchnummer, "top:80.0px;left:220.0px;");

		// wurfdatum
		wurfdatum = new PopupDateField("Wurfdatum");
		wurfdatum.setImmediate(true);
		wurfdatum.setDescription("Wurfdatum");
		wurfdatum.setWidth("-1px");
		wurfdatum.setHeight("-1px");
		mainLayout.addComponent(wurfdatum, "top:120.0px;left:40.0px;");

		// wurfdatum
		bh_datum = new PopupDateField("BH-Datum");
		bh_datum.setImmediate(true);
		bh_datum.setDescription("BH-Datum");
		bh_datum.setWidth("-1px");
		bh_datum.setHeight("-1px");
		mainLayout.addComponent(bh_datum, "top:120.0px;left:220.0px;");

		// comboBox_4
		comboBox_4 = new ComboBox("Rasse");
		comboBox_4.setImmediate(true);
		comboBox_4.setDescription("Rasse");
		comboBox_4.setWidth("320px");
		comboBox_4.setHeight("-1px");
		mainLayout.addComponent(comboBox_4, "top:160.0px;left:40.0px;");

		geschlecht = new OptionGroup("Geschlecht");
		geschlecht.setImmediate(true);
		geschlecht.addItems("Rüde", "Hündin");
		geschlecht.setMultiSelect(false);
		geschlecht.setWidth("320px");
		geschlecht.setHeight("-1px");
		mainLayout.addComponent(geschlecht, "top:200px;left:40.0px;");

		// Farbe
		farbe = new TextField("Farbe");
		farbe.setImmediate(true);
		farbe.setDescription("Farbe");
		farbe.setWidth("-1px");
		farbe.setHeight("-1px");
		farbe.setInputPrompt("Farbe");
		farbe.setNullRepresentation("Farbe");
		mainLayout.addComponent(farbe, "top:200.0px;left:220.0px;");

		// Züchter
		zuechter = new TextField("Züchter");
		zuechter.setImmediate(true);
		zuechter.setDescription("Züchter");
		zuechter.setWidth("-1px");
		zuechter.setHeight("-1px");
		zuechter.setInputPrompt("Züchter");
		zuechter.setNullRepresentation("Züchter");
		mainLayout.addComponent(zuechter, "top:240.0px;left:220.0px;");

		// ok
		ok = new Button();
		ok.setCaption("OK");
		ok.setImmediate(true);
		ok.setWidth("-1px");
		ok.setHeight("-1px");
		mainLayout.addComponent(ok, "top:280.0px;left:40.0px;");

		// print
		print = new Button();
		print.setCaption("print");
		print.setImmediate(true);
		print.setWidth("-1px");
		print.setHeight("-1px");
		mainLayout.addComponent(print, "top:280.0px;left:120.0px;");

		// abbruch
		abbruch = new Button();
		abbruch.setCaption("Abbruch");
		abbruch.setImmediate(true);
		abbruch.setDescription("abbruch und Änderungen verwerfen");
		abbruch.setWidth("-1px");
		abbruch.setHeight("-1px");
		mainLayout.addComponent(abbruch, "top:280.0px;left:280.0px;");

		return mainLayout;
	}

	private class printButtonListener implements ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub
			Kursblatt x = new Kursblatt(person, hundItem);
			mainLayout.addComponent(x);

		}

	}

}
