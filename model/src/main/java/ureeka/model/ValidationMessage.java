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

/**
 * A message generated in response to the validation of the model.
 */
public class ValidationMessage {

	protected final String name;
	protected final MessageLevel level;
	protected final String label;
	
	public ValidationMessage(String name, MessageLevel level, String label) {
		super();
		this.name = name;
		this.level = level;
		this.label = label;
	}
	
	public ValidationMessage(String name, String label) {
		this(name, MessageLevel.Error, label);
	}
	
	public String getName() {
		return name;
	}

	public MessageLevel getLevel() {
		return level;
	}

	public String getLabel() {
		return label;
	}

	public boolean isOneOf(ValidationMessage... validationMessages) {
		for (ValidationMessage value : validationMessages) {
			if (value.name.equalsIgnoreCase(this.name)) return true;
		}
		return false;
	}
	
	public String formatLabel(Object... parameters) {
		return String.format(label, parameters);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationMessage other = (ValidationMessage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
