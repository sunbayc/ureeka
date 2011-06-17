package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;
import ureeka.model.Range;

public class SimpleClass extends AbstractPropertiesOwner {

	private static final long serialVersionUID = 6472591246820054224L;
	
	public final Property<String> firstField = new DefaultProperty<String>(this, "firstField") {
		
		private static final long serialVersionUID = 1921517962165518847L;

		public Range<String> getRange() {
			return new Range<String>("1", "3");
		}
		
	};
	
	public final Property<Long> secondField = new DefaultProperty<Long>(this, "secondField") {
		
		private static final long serialVersionUID = 1921517962165518847L;

		public Range<Long> getRange() {
			return new Range<Long>(1L, 10L);
		}
		
	};
	
	public String getFirstField() {
		return firstField.get();
	}
	
	public void setFirstField(String value) {
		firstField.set(value);
	}
	
	public Long getSecondField() {
		return secondField.get();
	}
	
	public void setSecondField(Long secondField) {
		this.secondField.set(secondField);
	}
	
}