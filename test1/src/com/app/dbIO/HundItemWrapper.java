package com.app.dbIO;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;


public class HundItemWrapper implements Item, HundTxAware {

	/**
	 * TxItemWrapper is an internal class used by TransactionalContainerWrapper to wrap items
	 * for allowing transactional commit and rollback.
	 */

	    private Item innerItem;
	    private boolean isNew;
	    private Object itemId; 
	    private final HundTransactionalContainerWrapper parent;

	    private final Map<Object, HundTxPropertyWrapper<?>> wrapperMap = new LinkedHashMap<Object, HundTxPropertyWrapper<?>>();

	    HundItemWrapper(HundTransactionalContainerWrapper parent, Item innerItem,
	            Object innerItemId, boolean aNew) {
	        this.parent = parent;
	        this.innerItem = innerItem;
	        itemId = innerItemId;
	        isNew = aNew;
	    }

	    @Override
	    public void startTransaction() {
	        for (HundTxPropertyWrapper<?> propertyWrapper : wrapperMap.values()) {
	            propertyWrapper.startTransaction();
	        }
	    }

	    @Override
	    public void commit() {
	        for (HundTxPropertyWrapper<?> propertyWrapper : wrapperMap.values()) {
	            propertyWrapper.commit();
	        }
	    }

	    @Override
	    public void rollback() {
	        for (HundTxPropertyWrapper<?> propertyWrapper : wrapperMap.values()) {
	            propertyWrapper.rollback();
	        }
	    }

	    @Override
	    public Collection<?> getItemPropertyIds() {
	        return parent.getContainerPropertyIds();
	    }

	    @Override
	    @SuppressWarnings("rawtypes")
	    public boolean addItemProperty(Object id, Property property)
	            throws UnsupportedOperationException {
	        throw new UnsupportedOperationException();
	    }

	    @Override
	    public boolean removeItemProperty(Object id)
	            throws UnsupportedOperationException {
	        throw new UnsupportedOperationException();
	    }

	    @Override
	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    public Property<?> getItemProperty(Object id) {
	        HundTxPropertyWrapper<?> propertyWrapper = wrapperMap.get(id);
	        if(propertyWrapper == null) {
	            propertyWrapper = new HundTxPropertyWrapper(
	                    innerItem.getItemProperty(id));
	            propertyWrapper
	                    .addValueChangeListener(parent.propertyValueChangeListener);
	            wrapperMap.put(id, propertyWrapper);
	        }
	        return propertyWrapper;
	    }

	    public boolean isNew() {
	        return isNew;
	    }

	    public void setNew(boolean aNew) {
	        isNew = aNew;
	    }

	    Item getInnerItem() {
	        return innerItem;
	    }

	    @SuppressWarnings("unchecked")
	    void setInnerItem(Item innerItem) {
	        this.innerItem = innerItem;
	        for (Map.Entry<Object, HundTxPropertyWrapper<?>> idPropertyEntry : wrapperMap
	                .entrySet()) {
	            HundTxPropertyWrapper<?> propertyWrapper = idPropertyEntry.getValue();
	            propertyWrapper.replaceInnerProperty(innerItem
	                    .getItemProperty(idPropertyEntry.getKey()));
	            propertyWrapper.reset();
	        }
	    }

	    Object getItemId() {
	        return itemId;
	    }
	}