package test.ureeka.model.testmodel;

import java.util.List;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.MandatoryProperty;
import ureeka.model.Property;
import ureeka.model.PropertyList;
import ureeka.model.PropertyReference;


public class PropertyReferenceClass extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -4615645073949530327L;

	private PropertyList<DefaultProperty<String>> listOfObjects =
		new PropertyList<DefaultProperty<String>>(this, "listOfObjects");

	public final Property<Property<String>> propertyReferenceField =
		new PropertyReference<Property<String>>(this, "propertyReferenceField");
	
	public final Property<Property<String>> anotherPropertyReferenceField =
		new PropertyReference<Property<String>>(this, "anotherPropertyReferenceField");
	
	public final Property<String> mandatoryField = new MandatoryProperty<String>(this, "mandatoryField");
	
	public List<DefaultProperty<String>> getListOfObjects() {
		return listOfObjects;
	}

	public PropertyReferenceClass() {
		super();
		listOfObjects.add(new DefaultProperty<String>(this, "firstElementInListOfObjects"));
		listOfObjects.get(0).set("testValue");
		propertyReferenceField.set(listOfObjects.get(0));
	}
	
}
