package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;

public class ChildOfRoot extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final Property<ChildOfChildOfRoot> child =
		new DefaultProperty<ChildOfChildOfRoot>(this, "child");

	public ChildOfChildOfRoot getChild() {
		return child.get();
	}

	public void setChild(ChildOfChildOfRoot child) {
		this.child.set(child);
	}
	
	
}
