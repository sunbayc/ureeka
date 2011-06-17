package test.ureeka.model.testmodel;

import java.util.Date;
import java.util.List;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.MandatoryProperty;
import ureeka.model.Property;
import ureeka.model.PropertyList;


public class ChildOfChildOfRoot extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final PropertyList<AnotherClass> listOfObjects =
		new PropertyList<AnotherClass>(this, "listOfObjects");

	{
		listOfObjects.setMaximumSize(1);
	}
	
	public final Property<Date> firstProperty =
		new MandatoryProperty<Date>(this, "firstProperty");

	public Date getFirstProperty() {
		return firstProperty.get();
	}
	
	public void setFirstProperty(Date date) {
		firstProperty.set(date);
	}
	
	public List<AnotherClass> getListOfObjects() {
		return listOfObjects;
	}
	
}
