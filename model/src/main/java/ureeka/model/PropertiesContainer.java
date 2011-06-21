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

import java.util.List;
import java.util.Map;

/**
 * Interface for objects that contain other properties
 */
public interface PropertiesContainer {

	/**
	 * @return true if none of the properties contained by this instance
	 * are in an invalid state
	 */
	public boolean isValid();

	/**
	 * @param property the property to register with this container
	 */
	public void registerWith(Property<?> property);
	
	/**
	 * @param property the property to deregister with this container
	 * @return true if a property was deregistered
	 */
	public boolean deregisterWith(Property<?> property);
	
	/**
	 * @param indicates that this should return properties for this container,
	 * and all child containers
	 * @return properties in an invalid state
	 */
	public List<Property<?>> getInvalidProperties(boolean descend);
	
	/**
	 * @param indicates that this should return properties for this container,
	 * and all child containers
	 * @return properties in a changed state
	 */
	public List<Property<?>> getChangedProperties(boolean descend);

	/**
	 * Enables the visitation of all properties owned by this container
	 * 
	 * @param visitor
	 */
	public void visitProperties(PropertyVisitor visitor);
	
	/**
	 * @param indicates that this should return the properties of this container
	 * and all child containers
	 * @return owned properties as a Map of property name, property or another map
	 * (if representing the properties of a child property container)
	 */
	public Map<String, Object> getAsMap(boolean descend);

	/**
	 * Retrieves the property of the given name
	 * 
	 * @param name name of property to return
	 * @return the property, if found
	 * @throws IllegalArgumentException if the named property is not found
	 */
	public Property<Object> getPropertyByName(String name);
	
}
