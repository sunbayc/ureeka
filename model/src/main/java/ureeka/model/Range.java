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
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a declarative approach in providing boundary checking for the value of a property.
 *
 * @param <T>
 */
public class Range<T> implements Serializable {

	private static final long serialVersionUID = -1906682876514524559L;
	
	private final T upper;
	private final T lower;
	
	public Range(T lower, T upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}
	
	/**
	 * Can handle Strings (based on min length, max length - NOT Content!)
	 * and anything implementing comparable (based on content) 
	 * 
	 * @param value the value to test is within the range
	 * @return true if the passed value is in the allowable range
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean contains(T value) {
		
		if (value instanceof String) {
			
			String svalue = (String) value;
			Integer upperAsInt = getUpper() != null ? Integer.parseInt((String) getUpper()) : null;
			Integer lowerAsInt = getLower() != null ? Integer.parseInt((String) getLower()) : null;
			
			if (lowerAsInt != null && svalue.length() < lowerAsInt) return false;
			
			if (upperAsInt != null && svalue.length() > upperAsInt) return false;
			
			return true;
			
		}
		
		if (value instanceof Comparable<?>) {
			
			Comparable nValue = (Comparable) value;
			Comparable nUpper = (Comparable) getUpper();
			Comparable nLower = (Comparable) getLower();
			
			if (nUpper != null && nUpper.compareTo(nValue) < 0) return false;
			
			if (nLower != null && nLower.compareTo(nValue) > 0) return false;
			
			return true;
			
		}
		
		throw new UnsupportedOperationException("No implemention of contains for type: " + value.getClass());
		
	}
	
	public T getUpper() {
		return upper;
	}
	
	public T getLower() {
		return lower;
	}
	
	/**
	 * Generates the default validation message for a range check failure. Override to customise.
	 *  
	 * @param property the property for which the validation message should be generated
	 * @return a number of validation messages
	 */
	public Map<ValidationMessage, Object[]> getValidationMessage(Property<T> property) {
		return buildMap(
				ValidationMessages.message("ValueNotWithinAllowableRange"),
				new Object[] { property.get(), property.getPropertyName(), this.toString() });
	}
	
	protected Map<ValidationMessage, Object[]> buildMap(ValidationMessage message, Object[] parameters) {
		Map<ValidationMessage, Object[]> validationMessage = new HashMap<ValidationMessage, Object[]>();
		validationMessage.put(message, parameters);
		return validationMessage;
	}
	
	@Override
	public String toString() {
		String upperAsString = getUpper() == null ? "any" : getUpper().toString();
		String lowerAsString = getLower() == null ? "any" : getLower().toString();
		return String.format("between %s and %s", lowerAsString, upperAsString);
	}
	
}