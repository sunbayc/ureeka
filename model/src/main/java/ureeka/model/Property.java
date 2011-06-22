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

import java.util.Map;

/**
 * A Property is an extended concept of a java bean field, providing functionality
 * to assist with validation, provides valuable meta data and *should* be fairly
 * straightforward to bind user interfaces to.
 *
 * See: C# (Encapsulated Properties)
 * See: https://bean-properties.dev.java.net/tutorial.html
 * See: http://www.matthicks.com/2009/07/death-of-beans.html
 *
 * @param <T>
 */
public interface Property<T> {
	
	/**
	 * @return the developer friendly logical name of the property
	 */
	public String getPropertyName();
	
	/**
	 * @return true if the property is mandatory (i.e. the value of
	 * the property cannot be null)
	 */
	public boolean isMandatory();
	
	/**
	 * @param true if the property is mandatory (i.e. the value of
	 * the property cannot be null)
	 */
	public void setMandatory(boolean mandatory);
	
	/**
	 * @return false if the property must not be provided (i.e. the
	 * of the property must be null) 
	 */
	public boolean isAllowed();
	
	/**
	 * @param false if the property must not be provided (i.e. the
	 * of the property must be null) 
	 */
	public void setAllowed(boolean allowed);
	
	/**
	 * @return true if the property has changed in this lifeCycle
	 */
	public boolean hasChanged();
	
	/**
	 * @param eventListener will receive events from this Property
	 */
	public void registerListener(PropertyEventListener<T> eventListener);
	
	/**
	 * Intended to signal the deletion of this property (and it's contents) from the model
	 * @param container the container that the Property is being deleted
	 */
	public void delete(Object container);
	
	/**
	 * @param eventListener the listener to deregister
	 */
	public void deregisterListener(PropertyEventListener<T> eventListener);
	
	/**
	 * Invokes the validation rules for this Property
	 */
	public void validate();
	
	/**
	 * Broadcasts to any listeners that validation error occurred for a property
	 */
	public void broadcastInvalidPropertyEvents();
	
	/**
	 * @param value the value of this property
	 */
	public void set(T value);
	
	/**
	 * @return the value of this property
	 */
	public T get();
	
	/**
	 * @return the old value of this property (null is an ambiguous response)
	 */
	public T getOldValue();
	
	
	/**
	 * @return the validation errors in the form of the java.util.Formatter
	 * String format (and their parameters, if there are any)
	 */
	public Map<ValidationMessage, Object[]> getValidationErrors();
	
	/**
	 * Adds a validation message
	 * 
	 * @param message 
	 * @param parameters
	 */
	public void addValidationError(ValidationMessage message, Object[] parameters);
	
	/**
	 * @return the container to which this property belongs
	 */
	public PropertiesContainer getContainer();
	
	/**
	 * @param container the container of this property
	 */
	public void setContainer(PropertiesContainer container);
	
	/**
	 * Initialises this property for the beginning of the lifecycle  
	 */
	public void startLifeCycle();
	
	/**
	 * @return true if this Property owns the Property('s) it contains
	 */
	public boolean isPropertyOwner();
	
	/**
	 * @return the range to which the value of this property should 
	 */
	public Range<T> getRange();
	
	/**
	 * Read this as "isEqualToByValue(s)"
	 * 
	 * @return true if the value contained by this Property equals the value of the passed Property
	 */
	public boolean isEqualTo(Property<T> property);
	
}
