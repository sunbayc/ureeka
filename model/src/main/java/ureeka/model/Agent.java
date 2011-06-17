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
 * An agent is a super observer, which will receive all and any events that occur on a Property
 * within the context of a PropertyExecutionContext.
 * 
 * @see PropertyExecutionContext
 */
public interface Agent {
	
	/**
	 * Called prior to a message being added to a Property
	 * 
	 * @param property the property on which the validation error occurred
	 * @param message the message to be added
	 * @param parameters the message parameters
	 * @param chain the chain of Agents
	 * @return false if the adding of this message should be vetoed
	 */
	public boolean addValidationError(Property<Object> property,
			ValidationMessage message, Object[] parameters, AgentChain chain);
	
	/**
	 * Called when a validation error has occurred for a particular property
	 * 
	 * @param property the Property for which the error occurred
	 * @param chain the chain of Agents
	 * @return false if the broadcast of this event should be suppressed 
	 */
	public boolean propertyValidationErrorEvent(Property<Object> property, AgentChain chain);
	
	/**
	 * Called when the value of a property is changed
	 * 
	 * @param property
	 * @param chain the chain of Agents
	 * @return false if the broadcast of this event should be suppressed
	 */
	public boolean propertyValueChangedEvent(Property<Object> property, AgentChain chain);
	
}
