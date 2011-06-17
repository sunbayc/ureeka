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

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;

public class WeakReferenceSerialisable<T> implements Serializable {

	private static final long serialVersionUID = 643005409098040353L;

	private WeakReference<T> weakReference;

	public WeakReferenceSerialisable(T o) {
		super();
		this.weakReference = new WeakReference<T>(o);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(weakReference.get());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		weakReference = new WeakReference(in.readObject());
	}
	
	public T getValue() {
		return weakReference.get();
	}

}
