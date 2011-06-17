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

/**
 * Represents a chain of agents to execute
 */
public class AgentChain {

	private List<Agent> agents = new ArrayList<Agent>();
	
	public Agent next(Agent agent) {
		if (agent == null) {
			if (agents.size() == 0) {
				return new TerminalAgent();
			} else {
				return agents.get(0);
			}
		}
		if (agents.indexOf(agent) == (agents.size() - 1)) {
			return new TerminalAgent();
		}
		return agents.get(agents.indexOf(agent) + 1);
	}
	
	public AgentChain addEnd(Agent agent) {
		agents.add(agent);
		return this;
	}
	
	public AgentChain addStart(Agent agent) {
		agents.add(0, agent);
		return this;
	}
	
}

/**
 * Intended as the terminator for a chain
 */
class TerminalAgent implements Agent {

	public boolean propertyValueChangedEvent(Property<Object> property, AgentChain chain) {
		return true;
	}
	
	public boolean propertyValidationErrorEvent(Property<Object> property,
			AgentChain chain) {
		return true;
	}
	
	public boolean addValidationError(Property<Object> property,
			ValidationMessage message, Object[] parameters, AgentChain chain) {
		return true;
	}
	
}
