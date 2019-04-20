package com.app.dbio;

import java.io.Serializable;

import com.vaadin.v7.data.Item;


	/**
	 * ItemGenerator is an interface for providing new Item IDs and new items for
	 * the TransactionalContainerWrapper. The methods will be called when any of the addItem*
	 * methods of the wrapper are called.
	 */
	public interface HundItemGenerator extends Serializable {

	    /**
	     * Returns a new item to be added to the TransactionalContainerWrapper.
	     *
	     * @param itemId
	     *            Item ID for the Item to be generated
	     * @return Item to be added
	     */
	    Item createNewItem(Object itemId);

	    /**
	     * Returns a new Item ID for a new Item to be added to the
	     * TransactionalContainerWrapper.
	     *
	     * @return Item ID for the new item
	     */
	    Object createNewItemId();

	}


