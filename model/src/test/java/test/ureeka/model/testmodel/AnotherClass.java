package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.MandatoryProperty;
import ureeka.model.Property;

public class AnotherClass extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final Property<String> firstProperty =
		new MandatoryProperty<String>(this, "firstProperty");

	public final Property<AndAnotherClass> andAnotherClass =
		new DefaultProperty<AndAnotherClass>(this, "andAnotherClass");
	
	public final Property<String> notAllowedValue
			= new DefaultProperty<String>(this, "notAllowedValue") {

		private static final long serialVersionUID = 6767281187611116760L;

		public boolean isAllowed() {
			return false;
		};
	};
	
	public String getFirstProperty() {
		return firstProperty.get();
	}

	public void setFirstProperty(String firstProperty) {
		this.firstProperty.set(firstProperty);
	}

	public AndAnotherClass getAndAnotherClass() {
		return andAnotherClass.get();
	}

	public void setAndAnotherClass(AndAnotherClass andAnotherClass) {
		this.andAnotherClass.set(andAnotherClass);
	}

	public String getNotAllowedValue() {
		return notAllowedValue.get();
	}

	public void setNotAllowedValue(String notAllowedValue) {
		this.notAllowedValue.set(notAllowedValue);
	}
	
}
