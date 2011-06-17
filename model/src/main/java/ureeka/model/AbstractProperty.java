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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Provides the default implementation of a Property
 * 
 * @param <T>
 */
public abstract class AbstractProperty<T> implements Property<T>, Serializable {

	private static final long serialVersionUID = -8020864954279637408L;
	
	private final Identifier propertyIdentifier;

	private PropertiesContainer container;

	private List<WeakReferenceSerialisable<PropertyEventListener<T>>> registeredListeners 
			= new ArrayList<WeakReferenceSerialisable<PropertyEventListener<T>>>();

	private T oldValue;

	private T currentValue;

	private final String name;

	/** Default to not mandatory */
	private boolean mandatory = false;

	/** Default to allowed */
	private boolean allowed = true;

	private boolean hasChanged;
	
	public boolean isPropertyOwner() {
		return true;
	}
	
	public PropertiesContainer getContainer() {
		return container;
	}

	public void setContainer(PropertiesContainer container) {
		this.container = container;
		if (container != null) {
			container.registerWith(this);
		}
	}

	private final Map<ValidationMessage, Object[]> validationErrors = new HashMap<ValidationMessage, Object[]>();

	public AbstractProperty(PropertiesContainer container, String name) {
		this(new TemporaryIdentifier(), name);
		setContainer(container);
	}

	public AbstractProperty(Identifier identifier, String name) {
		super();
		this.propertyIdentifier = identifier;
		this.name = name;
	}

	public AbstractProperty(Identifier identifier) {
		this(identifier, "RootContainer");
	}

	public AbstractProperty() {
		this(new TemporaryIdentifier(), "(root/unknown)");
	}

	public void registerListener(PropertyEventListener<T> eventListener) {
		
		if (eventListener == null) {
			throw new NullPointerException("Passed eventListener was null");
		}

		if (registeredListeners == null) {
			registeredListeners = new ArrayList<WeakReferenceSerialisable<PropertyEventListener<T>>>();
		}
		
		registeredListeners.add(new WeakReferenceSerialisable<PropertyEventListener<T>>(eventListener));
		
	}

	public void deregisterListener(PropertyEventListener<T> eventListener) {
		
		if (registeredListeners == null) {
			registeredListeners = new ArrayList<WeakReferenceSerialisable<PropertyEventListener<T>>>();
		}
		
		Iterator<WeakReferenceSerialisable<PropertyEventListener<T>>> it = registeredListeners.iterator();
		
		while (it.hasNext()) {
			if (it.next().getValue() == eventListener) {
				it.remove();
			}
		}
		
	}

	public T get() {
		return currentValue;
	}

	public void set(T value) {
		Object previousValue = currentValue;
		currentValue = value;
		hasChanged = detectChange();
		if (previousValue != value)
			broadcastPropertyUpdated();
	}

	@SuppressWarnings("unchecked")
	private boolean broadcastPropertyUpdateToAgents() {
		// Broadcast to agents first
		if (PropertyExecutionContext.getCurrentExecutionContext() != null) {
			AgentChain chain = PropertyExecutionContext.getCurrentExecutionContext().getAgentChain();
			return chain.next(null).propertyValueChangedEvent((Property<Object>) this, chain);
		}
		return true;
	}
	
	protected void broadcastPropertyUpdated() {
		if (!broadcastPropertyUpdateToAgents())
			return; // (at least) one of the agents has vetoed this event
		if (registeredListeners != null) {
			for (WeakReferenceSerialisable<PropertyEventListener<T>> reference : registeredListeners) {
				PropertyEventListener<T> l = reference.getValue(); 
				if (l != null) {
					l.valueChanged(this);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean broadcastValidationErrorOccurredToAgents() {
		// Broadcast to agents first
		if (PropertyExecutionContext.getCurrentExecutionContext() != null) {
			AgentChain chain = PropertyExecutionContext.getCurrentExecutionContext().getAgentChain();
			return chain.next(null).propertyValidationErrorEvent((Property<Object>) this, chain);
		}
		return true;
	}
	
	protected void broadcastValidationErrorOccurred() {
		if (!broadcastValidationErrorOccurredToAgents())
			return; // (at least) one of the agents has vetoed this event
		for (WeakReferenceSerialisable<PropertyEventListener<T>> reference : registeredListeners) {
			PropertyEventListener<T> l = reference.getValue(); 
			if (l != null) l.validationErrorOccurred(this);
		}
	}

	public T getOldValue() {
		return oldValue;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	private boolean detectChange() {

		if (oldValue == currentValue) {
			return false;
		}

		return oldValue != null ? !oldValue.equals(currentValue) : true;
	}

	/**
	 * Overrides must call super#{@link #validate()} to perform boilerplate validation.
	 * 
	 * @see ureeka.model.Property#validate()
	 */
	public void validate() {
		
		if (isMandatory() && get() == null) {
			addValidationError(ValidationMessages.message("Mandatory"), new Object[] { getPropertyName() });
		}

		if (!isAllowed() && get() != null) {
			addValidationError(ValidationMessages.message("MustNotBeProvided"), new Object[] { getPropertyName() });
		}
		
		Range<T> range = getRange(); 
		if (range != null && get() != null) {
			if (!range.contains(get())) {
				Map<ValidationMessage, Object[]> rangeValidationMessages = range.getValidationMessage(this);
				for (ValidationMessage message : rangeValidationMessages.keySet()) {
					addValidationError(message, rangeValidationMessages.get(message));
				}
			}
		}
		
	}

	public void broadcastInvalidPropertyEvents() {
		if (validationErrors.size() > 0) {
			broadcastValidationErrorOccurred();
		}
	}

	public Map<ValidationMessage, Object[]> getValidationErrors() {
		return validationErrors;
	}
	
	public void addValidationError(ValidationMessage message, Object[] parameters) {
		if (!broadcastAddValidationErrorToAgents(message, parameters))
			return; // Agents have vetoed this action
		validationErrors.put(message, parameters);
	}
	
	@SuppressWarnings("unchecked")
	private boolean broadcastAddValidationErrorToAgents(ValidationMessage message, Object[] parameters) {
		if (PropertyExecutionContext.getCurrentExecutionContext() != null) {
			AgentChain chain = PropertyExecutionContext.getCurrentExecutionContext().getAgentChain();
			return chain.next(null).addValidationError((Property<Object>) this, message, parameters, chain);
		}
		return true;
	}

	public String getPropertyName() {
		return name;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isMandatory() {
		return (!isAllowed()) ? false : mandatory;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public String toString() {
		if (get() instanceof PropertiesContainer) {
			return String.format("Property: %s, Contents: (%s)", getPropertyName(), get());
		}
		if (get() == null) {
			return String.format("Property: %s, Contains: [Empty]", getPropertyName());
		}
		return get().toString();
	}

	public int hashCode() {
		return propertyIdentifier.hashCode();
	}

	/**
	 * This will utilise the TemporaryIdentifier to work out equality -
	 * that is, all AbstractProperty instances can be considered equal based
	 * on their Identifier, not the value that they contain
	 */
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		AbstractProperty<T> other = (AbstractProperty) obj;
		
		return propertyIdentifier.equals(other.propertyIdentifier);
	}

	public void startLifeCycle() {
		oldValue = currentValue;
		hasChanged = false;
		clearValidationErrors();
	}

	public void delete(Object container) {
		for (WeakReferenceSerialisable<PropertyEventListener<T>> reference : registeredListeners) {
			PropertyEventListener<T> l = reference.getValue(); 
			if (l != null) {
				l.propertyDeleted(this, container);
			}
		}
		registeredListeners.clear(); // Forcibly remove all of the listeners
		if (getContainer() != null)
			getContainer().deregisterWith(this); // de-register with my container (if i have one)
	}

	public void clearValidationErrors() {
		validationErrors.clear();
	}
	
	
	public Range<T> getRange() {
		return null;
	}
	
}
