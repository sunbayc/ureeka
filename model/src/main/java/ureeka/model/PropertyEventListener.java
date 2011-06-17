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

/**
 * A Listener interface for receiving Property notifications 
 * 
 * @param <T>
 */
public interface PropertyEventListener<T> extends Serializable {

	/**
	 * Called when a validation error has occurred for a particular property
	 * 
	 * @param property the Property for which the error occurred 
	 */
	public void validationErrorOccurred(Property<T> property);
	
	/**
	 * Called when the value of a property is changed
	 * 
	 * @param property
	 */
	public void valueChanged(Property<T> property);
	
	/**
	 * Called when the property is deleted (removed permanently) from the model
	 * 
	 * @param property
	 * @param container the container from which the Property is being deleted
	 */
	public void propertyDeleted(Property<T> property, Object container);
		
}
