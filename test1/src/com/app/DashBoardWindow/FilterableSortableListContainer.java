package com.app.DashBoardWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.vaadin.maddon.ListContainer;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.UnsupportedFilterException;

/**
 * A filterable ({@link Container.Filterable}) version of {@link ListContainer}.
 *
 * @author John Ryan
 *
 * @param <T>
 *            the type of beans in the backed list
 */
public class FilterableSortableListContainer<T> extends ListContainer<T>
		implements Filterable, Sortable {

	private static final long serialVersionUID = 6410519255465731727L;

	private Set<Filter> filters = new HashSet<Filter>();

	private List<T> filteredItems = new ArrayList<T>();

	public FilterableSortableListContainer(Class<T> type) {
		super(type);
	}

	public FilterableSortableListContainer(Collection<T> backingList) {
		super(backingList);
	}

	private void addFilter(Filter filter) {
		filters.add(filter);
		filterContainer();
	}

	private Set<Filter> getFilters() {
		return filters;
	}

	private void removeAllFilters() {
		if (filters.isEmpty()) {
			return;
		}
		filters.clear();
		filterContainer();

	}

	private void removeFilter(Filter filter) {
		filters.remove(filter);
		filterContainer();
	}

	private void filterContainer() {
		applyFilters();
		super.fireItemSetChange();
	}

	private void applyFilters() {
		filteredItems = new ArrayList<T>();
		if (isFiltered()) {
			boolean appliedFilter = false;
			for (T itemId : super.getBackingList()) {
				if (passesFilters(itemId)) {
					filteredItems.add(itemId);
					appliedFilter = true;
				}
			}
		}
	}

	private boolean passesFilters(T itemId) {
		if (isFiltered()) {
			Item item = super.getItem(itemId);
			for (Filter f : getFilters()) {
				if (!f.passesFilter(itemId, item)) {
					return false;
				}
			}
		}
		return true;

	}

	private boolean isFiltered() {
		return filters == null ? false : filters.size() > 0;
	}

	private boolean contains(T itemId) {
		return getBackingList().contains(itemId);
	}

	/**
	 * If the parent {@link ListContainer} wants to fire an ItemSetChange, we
	 * need to refilter.
	 *
	 * @see com.vaadin.data.util.AbstractContainer#fireItemSetChange()
	 *
	 */
	@Override
	protected void fireItemSetChange() {
		applyFilters();
		super.fireItemSetChange();
	}

	@Override
	protected List<T> getBackingList() {
		return isFiltered() ? filteredItems : super.getBackingList();
	}

	@Override
	public T getIdByIndex(int index) {
		return getBackingList().get(index);
	}

	@Override
	public Item getItem(Object itemId) {
		if (itemId == null) {
			return null;
		}
		if (isFiltered() && !filteredItems.contains(itemId)) {
			return null;
		}
		return super.getItem(itemId);
	}

	@Override
	public Collection<T> getItemIds() {
		return getBackingList();
	}

	@Override
	public List<T> getItemIds(int startIndex, int numberOfItems) {
		return getBackingList().subList(startIndex, startIndex + numberOfItems);
	}

	@Override
	public int indexOfId(Object itemId) {
		return getBackingList().indexOf(itemId);
	}

	@Override
	public int size() {
		return getBackingList().size();
	}

	@Override
	public boolean containsId(Object itemId) {
		return contains((T) itemId);
	}

	@Override
	public void addContainerFilter(Filter filter)
			throws UnsupportedFilterException {
		addFilter(filter);
	}

	@Override
	public void removeContainerFilter(Filter filter) {
		removeFilter(filter);
	}

	@Override
	public void removeAllContainerFilters() {
		removeAllFilters();
	}

	@Override
	public Collection<Filter> getContainerFilters() {
		return getFilters();
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (isFiltered()) {
			Comparator c = new NullComparator();
			if (!ascending[0]) {
				c = new ReverseComparator(c);
			}
			BeanComparator<T> bc = new BeanComparator<T>(
					propertyId[0].toString(), c);
			Collections.sort(filteredItems, bc);
		} else {
			super.sort(propertyId, ascending);
		}
	}

}
