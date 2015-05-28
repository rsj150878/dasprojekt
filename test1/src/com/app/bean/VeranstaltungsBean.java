package com.app.bean;

import com.vaadin.data.Item;

public class VeranstaltungsBean {

	private String showName;
	private Item dbItem;
	
	public VeranstaltungsBean(String showName, Item dbItem) {
		this.showName = showName;
		this.dbItem = dbItem;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Item getDbItem() {
		return dbItem;
	}

	public void setDbItem(Item dbItem) {
		this.dbItem = dbItem;
	}
	
	
}
