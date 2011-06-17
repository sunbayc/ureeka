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
import java.util.List;

/**
 * Provides the default implementation of a PropertyOwner
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractPropertiesOwner extends AbstractProperty
		implements PropertiesContainer, Serializable {

	private static final long serialVersionUID = 5156682360178760498L;

	private List<Property<?>> ownedProperties = new ArrayList<Property<?>>();

	public AbstractPropertiesOwner(String name) {
		this(null, name);
	}

	public AbstractPropertiesOwner(PropertiesContainer parent, String name) {
		super(parent, name);
	}

	public AbstractPropertiesOwner() {
		super();
	}

	public void registerWith(Property property) {
		if (property == null)
			throw new NullPointerException("Passed property was null");
		if (!ownedProperties.contains(property))
			ownedProperties.add(property);
	}

	public boolean deregisterWith(Property property) {
		if (property == null)
			throw new NullPointerException("Passed property was null");
		return ownedProperties.remove(property);
	}

	public void startLifeCycle() {
		visitProperties(new PropertyVisitor() {
			public void visit(Property<?> property) {
				property.startLifeCycle();
			}
		});
		super.startLifeCycle();
	}

	public void visitProperties(PropertyVisitor visitor) {
		for (Property property : ownedProperties) {
			visitor.visit(property);
			// If this isn't a Property Owner, we don't visit the value it contains
			if (property.isPropertyOwner() && property.get() != null) {
				if (property.get() instanceof Property) {
					visitor.visit((Property) property.get());
				}
			}
		}
	}

	public void validate() {

		// This visitor will perform validation on all properties
		visitProperties(new PropertyVisitor() {
			public void visit(Property<?> property) {
				property.validate();
			}
		});

		// Now, broadcast all property validation error events
		visitProperties(new PropertyVisitor() {
			public void visit(Property<?> property) {
				property.broadcastInvalidPropertyEvents();
			}
		});
		
	}
	
	public List<Property<?>> getChangedProperties(final boolean descend) {

		final List<Property<?>> properties = new ArrayList<Property<?>>();

		visitProperties(new PropertyVisitor() {

			public void visit(Property<?> property) {

				if (property.hasChanged()) {
					properties.add(property);
				}

				if (descend) {
					if (property instanceof PropertiesContainer) {
						((PropertiesContainer) property).visitProperties(this);
					}
				}

			}

		});

		return properties;

	}

	public List<Property<?>> getInvalidProperties(final boolean descend) {

		final List<Property<?>> properties = new ArrayList<Property<?>>();

		visitProperties(new PropertyVisitor() {

			public void visit(Property<?> property) {

				if (property.getValidationErrors().size() > 0) {
					properties.add(property);
				}

				if (descend) {
					if (property instanceof PropertiesContainer) {
						((PropertiesContainer) property).visitProperties(this);
					}
				}

			}

		});

		return properties;

	}

	public boolean isValid() {
		// Should we be descending? I say 'yep'
		return getInvalidProperties(true).size() == 0;
	}

}
