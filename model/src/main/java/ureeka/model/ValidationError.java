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

public class ValidationError {
	
	private final ValidationMessage validationMessage;
	
	private final Object[] parameters;
	
	private final Identifier identifier = new TemporaryIdentifier();
	
	public ValidationError(ValidationMessage validationMessage,
			Object[] parameters) {
		super();
		this.validationMessage = validationMessage;
		this.parameters = parameters;
	}

	/**
	 * @return the message identifying the Validation Error that occurred
	 */
	public ValidationMessage getValidationMessage() {
		return validationMessage;
	}

	/**
	 * @return any parameters associated with the Validation Error (can be null or empty)
	 */
	public Object[] getParameters() {
		return parameters;
	}

	@Override public int hashCode() {
		return identifier.hashCode();
	}

	@Override public boolean equals(Object obj) {
		return identifier.equals(obj);
	}
	
}