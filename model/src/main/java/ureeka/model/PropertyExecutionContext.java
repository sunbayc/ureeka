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
 * Provides an Execution Context under which a lifeCycle executes
 */
public class PropertyExecutionContext {

	private static ThreadLocal<PropertyExecutionContext> context =
		new ThreadLocal<PropertyExecutionContext>();
	
	private final AgentChain agentChain; 
	
	public PropertyExecutionContext() {
		this(new AgentChain());
	}
	
	public PropertyExecutionContext(AgentChain agentChain) {
		context.set(this);
		this.agentChain = agentChain;
	}
	
	public static PropertyExecutionContext getCurrentExecutionContext() {
		return context.get();
	}
	
	public static void clearCurrentExecutionContext() {
		context.set(null);
	}

	public AgentChain getAgentChain() {
		return agentChain;
	}
	
	public void clearContext() {
		clearCurrentExecutionContext();
	}
	
}
