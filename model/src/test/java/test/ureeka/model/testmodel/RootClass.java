package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.MandatoryProperty;
import ureeka.model.Property;

public class RootClass extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final Property<AnotherClass> rootsAnotherClass =
		new DefaultProperty<AnotherClass>(this, "rootsAnotherClass");
	
	public final Property<ChildOfRoot> child =
		new MandatoryProperty<ChildOfRoot>(this, "child");

	public final Property<String> dummyProperty =
		new DefaultProperty<String>(this, "dummyProperty");
	
	public final Property<String> someProperty =
		new DefaultProperty<String>(this, "someProperty") {
		
		private static final long serialVersionUID = 673778623995076274L;

		// Only mandatory if dummyProperty is set, and the
		// child property of childOfRoot is not null
		public boolean isMandatory() {
			
			if (dummyProperty.get() == null)
				return false;
			
			if (child.get() != null)
				return child.get().getChild() != null;
			
			return false;
			
		};
		
	};
	
	public AnotherClass getRootsAnotherClass() {
		return rootsAnotherClass.get();
	}

	public void setRootsAnotherClass(AnotherClass rootsAnotherClass) {
		this.rootsAnotherClass.set(rootsAnotherClass);
	}

	public ChildOfRoot getChildOfRoot() {
		return child.get();
	}

	public void setChildOfRoot(ChildOfRoot childOfRoot) {
		this.child.set(childOfRoot);
	}

	public String getDummyProperty() {
		return dummyProperty.get();
	}

	public void setDummyProperty(String dummyProperty) {
		this.dummyProperty.set(dummyProperty);
	}

	public String getSomeProperty() {
		return someProperty.get();
	}

	public void setSomeProperty(String someProperty) {
		this.someProperty.set(someProperty);
	}
	
}
