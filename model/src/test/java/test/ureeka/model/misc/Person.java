package test.ureeka.model.misc;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;

public class Person extends AbstractPropertiesOwner {

	private static final long serialVersionUID = 2147522975553946581L;

	public final Property<Person> butler = new DefaultProperty<Person>(this, "butler") {
		
		private static final long serialVersionUID = 1463922488516573542L;

		@Override public boolean isAllowed() {
			return false; // can never have a driver - how sad
		}
		
	};
	
}
