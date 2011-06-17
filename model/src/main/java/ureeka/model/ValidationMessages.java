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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidationMessages implements Map<String, ValidationMessage> {

	private final Map<String, ValidationMessage> _map = new HashMap<String, ValidationMessage>();
	
	private static final ValidationMessages _self = new ValidationMessages();
	
	public static ValidationMessage message(String messageKey) {
		return _self.get(messageKey);
	}
	
	public static ValidationMessages getInstance() {
		return _self;
	}
	
	public ValidationMessages() {
		super();
		addDefaultMessages();
	}
	
	protected ValidationMessages addDefaultMessages() {
		add(new ValidationMessage("Mandatory", "\"%s\" is required"));
		add(new ValidationMessage("MustNotBeProvided", "\"%s\" must not be provided"));
		add(new ValidationMessage("ValueNotWithinAllowableRange", "The value %s of \"%s\" is not within the allowable range %s"));
		add(new ValidationMessage("LessThanMinimumRequired", "at least %s %s must be provided"));
		add(new ValidationMessage("MoreThanMaximumAllowed", "only %s %s may be provided"));
		return this;
	}
	
	public ValidationMessages add(ValidationMessage message) {
		this.put(message.name, message);
		return this;
	}

	public int size() {
		return _map.size();
	}

	public boolean isEmpty() {
		return _map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return _map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return _map.containsValue(value);
	}

	public ValidationMessage get(Object key) {
		if (_map.containsKey(key)) return _map.get(key);
		throw new IllegalArgumentException("No ValidationMessage found for key: " + key);
	}

	public ValidationMessage put(String key, ValidationMessage value) {
		return _map.put(key, value);
	}

	public ValidationMessage remove(Object key) {
		return _map.remove(key);
	}

	public void putAll(Map<? extends String, ? extends ValidationMessage> m) {
		_map.putAll(m);
	}

	public void clear() {
		_map.clear();
	}

	public Set<String> keySet() {
		return _map.keySet();
	}

	public Collection<ValidationMessage> values() {
		return _map.values();
	}

	public Set<java.util.Map.Entry<String, ValidationMessage>> entrySet() {
		return _map.entrySet();
	}

	public boolean equals(Object o) {
		return _map.equals(o);
	}

	public int hashCode() {
		return _map.hashCode();
	}
	
}
