/*
 * ureeka - https://github.com/sunbayc/ureeka
 *
 * Copyright (C) 2011 Sunbay Consulting
 * Copyright (C) 2011 Simon Bronner
 *
 * ureeka is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * ureeka is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ureeka; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package ureeka.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Provides a List container for Properties, and adds some further functionality
 * (change tracking, events, model consistency, basic range validation).
 *
 * @param <E>
 */
public class PropertyList<E> extends AbstractProperty<E> implements
		PropertiesContainer, List<E>, Serializable {

	private static final long serialVersionUID = -9092306002788725252L;

	/** Contains all removed items */
	private final List<E> removedItems = new ArrayList<E>();

	/** Contains all added items */
	private final List<E> addedItems = new ArrayList<E>();

	/** The list implementation to delegate to */
	private final List<E> delegatedList = new ArrayList<E>();

	private final PropertiesContainer parent;

	/** Minimum required size of elements in this list */
	private Integer minimumSize;
	
	/** Maximum allowable size of elements in this list */
	private Integer maximumSize;
	
	/** by default, behave as a property owner */
	private boolean propertyOwningContainer = true;
	
	/**
	 * The default propertyEventListener, utilised for removing references to
	 * properties (Model Consistency Functionality)
	 */
	private DefaultPropertyEventListener<E> propertyEventListener = new DefaultPropertyEventListener<E>(this);
	
	/**
	 * Indicates that this container should behave as a Property Owning container:
	 * It will change the ownership of any property added to it to it's owner, and will
	 * have the property broadcast propertyDeleted events.
	 * 
	 * @see Property
	 * @see PropertyEventListener
	 * 
	 * @return true if this container should behave as a PropertyOwningContainer
	 */
	@Override public boolean isPropertyOwner() {
		return propertyOwningContainer;
	}
	
	/**
	 * @see isChangeOwnershipContainer
	 * 
	 * @param propertyOwningContainer will be true if this container should behave as a PropertyOwningContainer
	 */
	public void setPropertyOwningContainer(boolean propertyOwningContainer) {
		this.propertyOwningContainer = propertyOwningContainer;
	}

	public Integer getMinimumSize() {
		return minimumSize;
	}

	public void setMinimumSize(Integer minimumSize) {
		this.minimumSize = minimumSize;
	}

	public Integer getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(Integer maximumSize) {
		this.maximumSize = maximumSize;
	}
	
	public PropertyList(PropertiesContainer parent, String name) {
		super(parent, name);
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		boolean removed = delegatedList.remove(o);
		if (removed) {
			addToRemovedItems((E) o);
			broadCastPropertyDeleted(o);
		}
		return removed;
	}
	
	public E remove(int index) {
		E removedItem = delegatedList.remove(index);
		if (removedItem != null) {
			addToRemovedItems(removedItem);
			broadCastPropertyDeleted(removedItem);
		}
		
		return removedItem;
	}

	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> c) {
		Object[] allItems = c.toArray(new Object[this.size()]);
		boolean removedAll = delegatedList.removeAll(c);
		if (removedAll) {
			for (Object item : allItems) {
				addToRemovedItems((E) item);
				broadCastPropertyDeleted(item);
			}
		}
		return removedAll;
	}
	
	private void broadCastPropertyDeleted(Object o) {
		// Don't broadcast if this if we're not a Property Owning Container
		if (isPropertyOwner()) {
			if (o instanceof Property) {
				Property<?> p = (Property<?>) o;
				p.delete(this);
			}
		}
	}

	/**
	 * Adds the passed item to the removedItems list
	 * @param o
	 */
	protected void addToRemovedItems(E o) {
		removedItems.add(o);
		itemRemovedEvent(o);
	}
	
	/**
	 * Adds the passed item to the addedItems list
	 * @param o
	 */
	private void addToAddedItems(E o) {
		addedItems.add(o);
		itemAddedEvent(o);
	}
	
	/**
	 * Subclasses can implement this method to be notified when an item has been added
	 * 
	 * @param o
	 */
	public void itemAddedEvent(E o) {
		// Default, do nothing
	}
	
	/**
	 * Subclasses can implement this method to be notified when an item has been removed
	 * 
	 * @param o
	 */
	public void itemRemovedEvent(E o) {
		// Default, do nothing
	}
	
	public boolean add(E e) {
		if (e == null) {
			throw new IllegalArgumentException("You cannot add a null reference to a ProperyList");
		}
		bind(e);
		boolean added = delegatedList.add(e);
		if (added) addToAddedItems(e);
		return added;
	}
	
	public void add(int index, E element) {
		if (element == null) {
			throw new IllegalArgumentException("You cannot add a null reference to a ProperyList");
		}
		bind(element);
		delegatedList.add(index, element);
		addToAddedItems(element);
	}

	public boolean addAll(Collection<? extends E> c) {
		for (E element : c) {
			if (element == null) {
				throw new IllegalArgumentException("You cannot add a null reference to a ProperyList");
			}
			bind(element);
		}
		boolean allAdded = delegatedList.addAll(c);
		if (allAdded) {
			for (E element : c) addToAddedItems(element);
		}
		return allAdded;
	}
	

	public boolean addAll(int index, Collection<? extends E> c) {
		for (E element : c) {
			if (element == null) {
				throw new IllegalArgumentException("You cannot add a null reference to a ProperyList");
			}
			bind(element);
		}
		boolean allAdded = delegatedList.addAll(index, c);
		if (allAdded) {
			for (E element : c) addToAddedItems(element);
		}
		return allAdded;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bind(E element) {
		if (element instanceof Property) {
			Property p = (Property) element;
			if (p.getContainer() == null) {
				// If this property doesn't currently have a container, we just make it's
				// container this PropertyList by default
				p.setContainer(this);
			}
			if (p.getContainer() != this && isPropertyOwner()) {
				p.getContainer().deregisterWith(p); // Deregister with existing parent
				p.setContainer(this);
			} else {
				// Otherwise, we don't change ownership - this is like referring
				// to a property rather than owning it
			}
			p.registerListener(getPropertyEventListener());
		}
	}
	
	/**
	 * @return the event listener that should be utilised to register for events
	 * on any property added to this PropertyList
	 */
	public PropertyEventListener<E> getPropertyEventListener() {
		return propertyEventListener;
	}
	
	/**
	 * @return all items that have been removed from this List
	 */
	public List<E> getRemovedItems() {
		return removedItems;
	}

	/**
	 * @return all items that have been added to this List
	 */
	public List<E> getAddedItems() {
		return addedItems;
	}

	public int size() {
		return delegatedList.size();
	}

	public boolean isEmpty() {
		return delegatedList.isEmpty();
	}

	public boolean contains(Object o) {
		return delegatedList.contains(o);
	}

	/**
	 * Returns an Unmodifiable iterator, as I need to control access to removal
	 */
	public Iterator<E> iterator() {
		return delegatedList.iterator();
	}

	public Object[] toArray() {
		return delegatedList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegatedList.toArray(a);
	}

	public boolean containsAll(Collection<?> c) {
		return delegatedList.containsAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegatedList.retainAll(c);
	}

	public void clear() {
		for (int i = size() - 1; i > -1; i--) remove(i);
	}

	@Override public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		// Unusual condition - can occur if an instance of this class is
		// comparing itself to another during construction 
		if (delegatedList == null) return false;
		if (o instanceof PropertyList) {
			PropertyList<?> pl = (PropertyList<?>) o;
			// Only compare by reference if I don't contain anything...
			// PropertyList are different if they represent different Properties
			if (getDelegatedList().size() == 0) {
				if (getDelegatedList() == pl.getDelegatedList()) return true;
				else return false;
			}
		}
		// Fall back to utilising the delegated list
		return delegatedList.equals(o);
	}
	
	public List<E> getDelegatedList() {
		return delegatedList;
	}

	@Override public int hashCode() {
		return delegatedList.hashCode();
	}

	public E get(int index) {
		return delegatedList.get(index);
	}

	public E set(int index, E element) {
		return delegatedList.set(index, element);
	}

	public int indexOf(Object o) {
		return delegatedList.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return delegatedList.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return delegatedList.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return delegatedList.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return delegatedList.subList(fromIndex, toIndex);
	}

	public boolean isValid() {
		return parent.isValid();
	}

	@Override public boolean hasChanged() {
		return removedItems.size() > 0 || addedItems.size() > 0;
	}

	@Override public void startLifeCycle() {
		clearValidationErrors();
		addedItems.clear();
		removedItems.clear();
		super.startLifeCycle();
	}
	
	@Override public boolean isAllowed() {
		Integer i = getMaximumSize();
		if (i == null) return true;
		if (i < 1) return false;
		return true;
	}
	
	@Override public boolean isMandatory() {
		Integer i = getMinimumSize();
		if (i == null) return false;
		if (i > 0) return true;
		return false;
	}
	
	public void registerWith(Property<?> property) {
		parent.registerWith(property);
	}
	
	
	public boolean deregisterWith(Property<?> property) {
		return parent.deregisterWith(property);
	}

	public List<Property<?>> getInvalidProperties(boolean descend) {
		// do nothing, the parent will already know about them
		return new ArrayList<Property<?>>();
	}

	public List<Property<?>> getChangedProperties(boolean descend) {
		// do nothing, the parent will already know about them
		return new ArrayList<Property<?>>();
	}
	
	@Override public void validate() {
		if (getMinimumSize() != null)
			if (getMinimumSize() > size())
				addValidationError(
						ValidationMessages.message("LessThanMinimumRequired"),
						new Object[] { getMinimumSize(), getPropertyName() });
		if (getMaximumSize() != null)
			if (getMaximumSize() < size())
				addValidationError(
						ValidationMessages.message("MoreThanMaximumAllowed"),
						new Object[] { getMaximumSize(), getPropertyName() });
		// Don't delegate to AbstractPropertyValidate - this validation should suffice
		// super.validate();
	}

	// We're not that kind of property
	@Override public void set(E value) {
		throw new UnsupportedOperationException();
	}
	
	public void visitProperties(PropertyVisitor visitor) { }
	
	public Map<String, Object> getAsMap(boolean descend) {
		return null;
	}

	// Can't do it
	public Property<Object> getPropertyByName(String name) {
		throw new UnsupportedOperationException();
	}
	
}

class DefaultPropertyEventListener<E> extends EmptyPropertyEventListener<E> {
	
	private static final long serialVersionUID = 2835901707721579879L;
	
	private final PropertyList<E> propertyList;
	
	public DefaultPropertyEventListener(PropertyList<E> propertyList) {
		super();
		this.propertyList = propertyList;
	}

	@SuppressWarnings("unchecked")
	@Override public void propertyDeleted(
			Property<E> property,
			Object container) {
		if (container != propertyList) {
			// Remove from the delegated list directly
			propertyList.getDelegatedList().remove(property);
			propertyList.addToRemovedItems((E) property);
		}
	}
	
}
