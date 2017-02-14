package com.app.dbIO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.data.tx.ItemGenerator;
import org.vaadin.data.tx.TxListener;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;

/**
 * TransactionalContainerWrapper is a Vaadin Container which wraps around
 * another container to provide transactional commits and rollbacks for item set
 * and property value changes. TransactionalContainerWrapper supports sorting
 * and filtering, and it assumes that the underlying container implements the
 * {@link Indexed} interface.
 * <p>
 * You may start a transaction explicitly by callind startTransaction() - the
 * other option is to use implicit transaction start by adding or removing an
 * item or by modifying any property value.
 * </p>
 * Transaction should be finalized by calling either commit() to apply the
 * changes to the underlying container, or rollback() to discard the changes and
 * re-sync with the underlying container.
 */
public class HundTransactionalContainerWrapper extends AbstractInMemoryContainer<Object, Object, HundTxItemWrapper>
		implements Container.Filterable, Container.SimpleFilterable, Container.Sortable, HundTxAware {

	/**
	 * State of the transaction.
	 */
	public static enum TxState {
		/**
		 * Transaction has been started either explicitly or implicitly.
		 */
		STARTED,
		/**
		 * Transaction has not been started.
		 */
		NOT_STARTED,
		/**
		 * Transaction has been started and uncommitted changes are pending.
		 */
		UNCOMMITTED
	}

	private final Indexed innerContainer;
	private final ItemGenerator itemGenerator;

	private final Map<Object, HundTxItemWrapper> allItems = new LinkedHashMap<Object, HundTxItemWrapper>();
	private final Map<Object, HundTxItemWrapper> deletedById = new HashMap<Object, HundTxItemWrapper>();

	private final List<HundTxListener> transactionListeners = new ArrayList<HundTxListener>();

	private TxState state;

	final Property.ValueChangeListener propertyValueChangeListener = new Property.ValueChangeListener() {
		@Override
		public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
			startImplicitTransaction();
		}
	};

	/**
	 * Creates a new TransactionalContainerWrapper and wraps the given
	 * container. When using this constructor there will not be support for
	 * adding new items to the container.
	 *
	 * @param innerContainer
	 *            Container to wrap.
	 */
	public HundTransactionalContainerWrapper(Indexed innerContainer) {
		this(innerContainer, null);
	}

	/**
	 * Creates a new TransactionalContainerWrapper and wraps the given
	 * container. When using this constructor there will be support for adding
	 * new items to the container.
	 *
	 * @param innerContainer
	 *            Container to wrap.
	 * @param itemGenerator
	 *            New Item generator implementation.
	 */
	public HundTransactionalContainerWrapper(Indexed innerContainer, ItemGenerator itemGenerator) {
		this.innerContainer = innerContainer;
		this.itemGenerator = itemGenerator;

		reset();
	}

	/**
	 * Resets the container state. Discards all changes and re-syncs with the
	 * underlying container.
	 */
	public void reset() {
		allItems.clear();
		for (Object id : innerContainer.getItemIds()) {
			Item item = innerContainer.getItem(id);
			allItems.put(id, new HundTxItemWrapper(this, item, id, false));
		}
		getAllItemIds().clear();
		getAllItemIds().addAll(allItems.keySet());
		state = TxState.NOT_STARTED;
		filterAll();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.AbstractInMemoryContainer#getUnfilteredItem(java
	 * .lang.Object)
	 */
	@Override
	protected HundTxItemWrapper getUnfilteredItem(Object itemId) {
		return allItems.get(itemId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.tx.poc.container.TxAware#startTransaction()
	 */
	@Override
	public void startTransaction() {
		if (state == TxState.NOT_STARTED) {
			for (HundTxItemWrapper txItemWrapper : allItems.values()) {
				txItemWrapper.startTransaction();
			}
			for (HundTxListener transactionListener : transactionListeners) {
				transactionListener.transactionStarted(false);
			}
			state = TxState.STARTED;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.tx.poc.container.TxAware#commit()
	 */
	@Override
	public void commit() {
		if (state != TxState.NOT_STARTED) {
			for (Object deletedId : deletedById.keySet()) {
				innerContainer.removeItem(deletedId);
			}
			Object previousItemId = null;
			for (Object itemId : getAllItemIds()) {
				HundTxItemWrapper txItemWrapper = allItems.get(itemId);
				// if (txItemWrapper.isNew()) {
				// Item actualItem = innerContainer.addItem(
				// txItemWrapper.getItemId());
				// if (actualItem == null) {
				// throw new RuntimeException("Addition of new item "
				// + txItemWrapper.getInnerItem() + " failed");
				// }
				//
				// for (Object propId : actualItem.getItemPropertyIds()) {
				// @SuppressWarnings("unchecked")
				// Property<Object> itemProperty = actualItem
				// .getItemProperty(propId);
				// if (!itemProperty.isReadOnly()) {
				// Object newValue = txItemWrapper.getItemProperty(
				// propId).getValue();
				// itemProperty.setValue(newValue);
				// }
				// }
				// txItemWrapper.setInnerItem(actualItem);
				// txItemWrapper.setNew(false);
				// } else {
				txItemWrapper.commit();
				// }
				previousItemId = itemId;
			}
			state = TxState.NOT_STARTED;
			for (HundTxListener transactionListener : transactionListeners) {
				transactionListener.transactionCommitted();
			}
		}
	}

	public void specialCommit() {
		if (state != TxState.NOT_STARTED) {
			for (Object deletedId : deletedById.keySet()) {
				innerContainer.removeItem(deletedId);
			}
			Object previousItemId = null;
			for (Object itemId : getAllItemIds()) {
				HundTxItemWrapper txItemWrapper = allItems.get(itemId);
				//
				txItemWrapper.commit();
				previousItemId = itemId;
			}
			state = TxState.NOT_STARTED;
			for (HundTxListener transactionListener : transactionListeners) {
				transactionListener.specialCommitted();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.tx.poc.container.TxAware#rollback()
	 */
	@Override
	public void rollback() {
		if (state == TxState.UNCOMMITTED) {
			reset();
			super.fireItemSetChange();
			for (HundTxListener transactionListener : transactionListeners) {
				transactionListener.transactionRolledBack();
			}
		}
		state = TxState.NOT_STARTED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Property<?> getContainerProperty(Object itemId, Object propertyId) {
		Item item = getItem(itemId);
		return item == null ? null : item.getItemProperty(propertyId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container#getContainerPropertyIds()
	 */
	@Override
	public Collection<?> getContainerPropertyIds() {
		return innerContainer.getContainerPropertyIds();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container#getType(java.lang.Object)
	 */
	@Override
	public Class<?> getType(Object propertyId) {
		return innerContainer.getType(propertyId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#removeAllItems()
	 */
	@Override
	public boolean removeAllItems() {
		for (Map.Entry<Object, HundTxItemWrapper> idItemEntry : allItems.entrySet()) {
			HundTxItemWrapper itemWrapper = idItemEntry.getValue();
			if (!itemWrapper.isNew()) {
				deletedById.put(idItemEntry.getKey(), itemWrapper);
			}
		}
		internalRemoveAllItems();
		super.fireItemSetChange();
		startImplicitTransaction();
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#removeItem(java.lang.
	 * Object )
	 */
	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException {
		HundTxItemWrapper removed = allItems.remove(itemId);
		if (removed == null) {
			return false;
		}
		if (!removed.isNew()) {
			deletedById.put(removed.getItemId(), removed);
		}
		internalRemoveItem(itemId);
		super.fireItemSetChange();
		startImplicitTransaction();
		return true;
	}

	private void startImplicitTransaction() {
		filterAll();
		if (state == TxState.NOT_STARTED) {
			for (HundTxListener transactionListener : transactionListeners) {
				transactionListener.transactionStarted(true);
			}
		}
		state = TxState.UNCOMMITTED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#addItem()
	 */
	@Override
	public Object addItem() throws UnsupportedOperationException {
		Object newItemId = itemGenerator.createNewItemId();
		addItem(newItemId);
		return newItemId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.AbstractInMemoryContainer#addItem(java.lang.Object)
	 */
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		return addItemAt(size(), itemId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#addItemAt(int)
	 */
	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		Object newItemId = itemGenerator.createNewItemId();
		addItemAt(index, newItemId);
		return newItemId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#addItemAt(int,
	 * java.lang.Object)
	 */
	@Override
	public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
		if (itemGenerator == null) {
			throw new UnsupportedOperationException();
		}
		if (newItemId == null) {
			return null;
		}
		if (index < 0) {
			return null;
		}
		if (allItems.containsKey(newItemId) || deletedById.containsKey(newItemId)) {
			return null;
		}
		Item newItem = itemGenerator.createNewItem(newItemId);
		HundTxItemWrapper newItemWrapper = new HundTxItemWrapper(this, newItem, newItemId, true);
		allItems.put(newItemId, newItemWrapper);
		internalAddItemAt(index, newItemId, newItemWrapper, true);
		startImplicitTransaction();
		return newItemWrapper;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.AbstractInMemoryContainer#addItemAfter(java.lang
	 * .Object)
	 */
	@Override
	public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
		Object newItemId = itemGenerator.createNewItemId();
		addItemAfter(previousItemId, newItemId);
		return newItemId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.AbstractInMemoryContainer#addItemAfter(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
		return addItemAt(indexOfId(previousItemId) + 1, newItemId);
	}

	/**
	 * Adds the given {@link TxListener}
	 *
	 * @param listener
	 *            Listener to add
	 * @return true if successfull, false otherwise
	 */
	public boolean addTxListener(HundTxListener listener) {
		return transactionListeners.add(listener);
	}

	/**
	 * Removes the given {@link TxListener}
	 *
	 * @param listener
	 *            Listener to remove
	 * @return true if successfull, false otherwise
	 */
	public boolean removeTxListener(TxListener listener) {
		return transactionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container.Sortable#sort(java.lang.Object[],
	 * boolean[])
	 */
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		sortContainer(propertyId, ascending);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container.Sortable#getSortableContainerPropertyIds()
	 */
	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return getSortablePropertyIds();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Container.Filterable#addContainerFilter(com.vaadin.data
	 * .Container.Filter)
	 */
	@Override
	public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
		addFilter(filter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Container.Filterable#removeContainerFilter(com.vaadin
	 * .data.Container.Filter)
	 */
	@Override
	public void removeContainerFilter(Filter filter) {
		removeFilter(filter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.Container.Filterable#removeAllContainerFilters()
	 */
	@Override
	public void removeAllContainerFilters() {
		removeAllFilters();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#getContainerFilters()
	 */
	@Override
	public Collection<Filter> getContainerFilters() {
		return super.getContainerFilters();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Container.SimpleFilterable#addContainerFilter(java.lang
	 * .Object, java.lang.String, boolean, boolean)
	 */
	@Override
	public void addContainerFilter(Object propertyId, String filterString, boolean ignoreCase,
			boolean onlyMatchPrefix) {
		try {
			addFilter(new SimpleStringFilter(propertyId, filterString, ignoreCase, onlyMatchPrefix));
		} catch (UnsupportedFilterException e) {
			// the filter instance created here is always valid for in-memory
			// containers
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.Container.SimpleFilterable#removeContainerFilters(java
	 * .lang.Object)
	 */
	@Override
	public void removeContainerFilters(Object propertyId) {
		super.removeFilters(propertyId);
	}
}
