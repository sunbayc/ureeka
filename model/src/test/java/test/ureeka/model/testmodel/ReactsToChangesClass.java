package test.ureeka.model.testmodel;

import ureeka.model.EmptyPropertyEventListener;
import ureeka.model.Property;

public class ReactsToChangesClass {
	
	private String value;
	
	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	public ReactsToChangesClass(Property<?> dependsOn) {
		super();
		value = "defaultValue";
		dependsOn.registerListener(new EmptyPropertyEventListener() {
			@Override public void valueChanged(Property property) {
				value = null;
			}
		});
	}

	public String getValue() {
		return value;
	}
	

}
