package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.EmptyPropertyEventListener;
import ureeka.model.Property;
import ureeka.model.PropertyEventListener;
import ureeka.model.PropertyList;

public class RootClassTwo extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final PropertyList<SimpleClass> children =
		new PropertyList<SimpleClass>(this, "anotherClasses");
	
	public final PropertyList<SimpleClass> referenceschildren =
		new PropertyList<SimpleClass>(this, "referencesToAnotherClasses");
	
	{
		referenceschildren.setPropertyOwningContainer(false);
	}
	
	public final Property<ReactsToChangesClass> reactsToChangesClass =
		new DefaultProperty<ReactsToChangesClass>(this, "reactsToChangesClass"); 
	
	public final Property<String> valueThatChanges = new DefaultProperty<String>(this, "aValueThatChanges");

	public final Property<String> shadowValueThatChanges = new DefaultProperty<String>(this, "shadowValueThatChanges");
	
	PropertyEventListener<String> listener = new EmptyPropertyEventListener<String>() {
		
		private static final long serialVersionUID = -4219951295704916877L;

		@Override public void valueChanged(Property<String> property) {
			shadowValueThatChanges.set(property.get());
		}
		
	};
	
	{
		valueThatChanges.registerListener(listener);
	}
	
	/**
	 * @return the children
	 */
	public PropertyList<SimpleClass> getChildren() {
		return children;
	}

	/**
	 * @return the referenceschildren
	 */
	public PropertyList<SimpleClass> getReferenceschildren() {
		return referenceschildren;
	}

	public String getValueThatChanges() {
		return valueThatChanges.get();
	}
	
	public void setValueThatChanges(String value) {
		valueThatChanges.set(value);
	}
	
	public void setReactsToChangesClass(ReactsToChangesClass object) {
		reactsToChangesClass.set(object);
	}
	
	public ReactsToChangesClass getReactsToChangesClass() {
		return reactsToChangesClass.get();
	}
	
	public void clearReferenceToReactsToChangesClass() {
		setReactsToChangesClass(null);
	}
	
}
