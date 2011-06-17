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
 * A Property Implementation that will null it's value if the Property which it's passed if the value
 * is removed from the model. Property values that this Object holds will not be validated via this Property.
 */
public class PropertyReference<T> extends DefaultProperty<T> {

	private static final long serialVersionUID = -489203569804934449L;

	public PropertyReference(PropertiesContainer parent, String name) {
		super(parent, name);
	}

	@Override public boolean isPropertyOwner() {
		return false;
	}

	private PropertyEventListener<T> cleanupEventListener = new EmptyPropertyEventListener<T>() {

		private static final long serialVersionUID = 8255360542291364549L;

		@Override
		public void propertyDeleted(Property<T> property, Object container) {
			// If the property has been deleted from the model, then we remove our reference to it
			set(null);
		}

	};

	@SuppressWarnings("unchecked")
	@Override public void set(T value) {
		super.set(value);
		// De-register from previous Property events
		if (getOldValue() != null && getOldValue() instanceof Property<?>) {
			((Property<T>) getOldValue()).deregisterListener(cleanupEventListener);
		}
		// Register to new Property events
		if (get() != null && get() instanceof Property<?>) {
			((Property<T>) get()).registerListener(cleanupEventListener);
		}
	};

}
