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

import java.util.ArrayList;
import java.util.List;


public class ValidationException extends Exception {

	private static final long serialVersionUID = -5958821678266074368L;

	private final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
	
	public ValidationException(ValidationMessage validationMessage,
			Object[] parameters) {
		super();
		validationErrors.add(new ValidationError(validationMessage, parameters));
	}

	public ValidationException() { super(); };
	
	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}
	
	public ValidationError addValidationMessage(ValidationMessage validationMessage, Object[] parameters) {
		if (validationMessage == null) throw new NullPointerException("Passed Validation Message was null");
		ValidationError error = new ValidationError(validationMessage, parameters);
		validationErrors.add(error);
		return error;
	}
	
	public ValidationException addValidationError(ValidationError error) {
		if (error == null) throw new NullPointerException("Passed Validation Error was null");
		validationErrors.add(error);
		return this;
	}
	
	public boolean hasValidationMessage(ValidationMessage message) {
		for (ValidationError error : validationErrors) {
			if (error.getValidationMessage().equals(message)) return true;
		}
		return false;
	}

}