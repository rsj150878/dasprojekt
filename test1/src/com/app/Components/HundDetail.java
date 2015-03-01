package com.app.Components;

import java.util.Locale;

import com.app.Components.Listener.HundDetailListener;
import com.app.bean.RassenBean;
import com.app.enumPackage.Rassen;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
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
	private TextField zuchtbuchnummer;
	@AutoGenerated
	private TextField chipnummer;
	@AutoGenerated
	private TextField rufname;
	@AutoGenerated
	private TextField zwingername;
	private Item hundItem;

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

		if (!hundItem.equals(null)) {
			setInit();
		}

		buildRassenComboBox();

	}

	public HundDetail(Item hundItem) {
		this.hundItem = hundItem;
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

			System.out.println("in schleife" + o.getRassenKurzBezeichnung()
					+ o.getRassenLangBezeichnung());

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

		System.out.println(" nach setzen "
				+ comboBox_4.getItemCaptionPropertyId());

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
		zwingername = new TextField();
		zwingername.setImmediate(false);
		zwingername.setDescription("Zwingername");
		zwingername.setWidth("-1px");
		zwingername.setHeight("-1px");
		mainLayout.addComponent(zwingername, "top:40.0px;left:40.0px;");

		// rufname
		rufname = new TextField();
		rufname.setImmediate(false);
		rufname.setWidth("-1px");
		rufname.setHeight("-1px");
		rufname.setInputPrompt("Rufname");
		rufname.setNullRepresentation("Rufname");
		mainLayout.addComponent(rufname, "top:40.0px;left:220.0px;");

		// chipnummer
		chipnummer = new TextField();
		chipnummer.setImmediate(false);
		chipnummer.setDescription("Chipnummer");
		chipnummer.setWidth("-1px");
		chipnummer.setHeight("-1px");
		chipnummer.setNullRepresentation("Chipnummer");
		mainLayout.addComponent(chipnummer, "top:76.0px;left:40.0px;");

		// zuchtbuchnummer
		zuchtbuchnummer = new TextField();
		zuchtbuchnummer.setImmediate(false);
		zuchtbuchnummer.setDescription("Zuchtbuchnummer");
		zuchtbuchnummer.setWidth("-1px");
		zuchtbuchnummer.setHeight("-1px");
		zuchtbuchnummer.setInputPrompt("Zuchtbuchnummer");
		zuchtbuchnummer.setNullRepresentation("Zuchtbuchnummer");
		mainLayout.addComponent(zuchtbuchnummer, "top:80.0px;left:220.0px;");

		// wurfdatum
		wurfdatum = new PopupDateField();
		wurfdatum.setImmediate(true);
		wurfdatum.setDescription("Wurfdatum");
		wurfdatum.setWidth("-1px");
		wurfdatum.setHeight("-1px");
		mainLayout.addComponent(wurfdatum, "top:120.0px;left:40.0px;");

		// comboBox_4
		comboBox_4 = new ComboBox();
		comboBox_4.setImmediate(false);
		comboBox_4.setDescription("Rasse");
		comboBox_4.setWidth("320px");
		comboBox_4.setHeight("-1px");
		mainLayout.addComponent(comboBox_4, "top:160.0px;left:40.0px;");

		// ok
		ok = new Button();
		ok.setCaption("OK");
		ok.setImmediate(true);
		ok.setWidth("-1px");
		ok.setHeight("-1px");
		mainLayout.addComponent(ok, "top:274.0px;left:40.0px;");

		// print
		print = new Button();
		print.setCaption("print");
		print.setImmediate(true);
		print.setWidth("-1px");
		print.setHeight("-1px");
		mainLayout.addComponent(print, "top:274.0px;left:120.0px;");

		// abbruch
		abbruch = new Button();
		abbruch.setCaption("Abbruch");
		abbruch.setImmediate(true);
		abbruch.setDescription("abbruch und �nderungen verwerfen");
		abbruch.setWidth("-1px");
		abbruch.setHeight("-1px");
		mainLayout.addComponent(abbruch, "top:274.0px;left:286.0px;");

		return mainLayout;
	}

}
