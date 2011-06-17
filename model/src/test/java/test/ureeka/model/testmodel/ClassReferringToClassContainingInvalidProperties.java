package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;

public class ClassReferringToClassContainingInvalidProperties extends AbstractPropertiesOwner {
	
	private static final long serialVersionUID = -4936323560353177231L;

	public final Property<ClassContainingInvalidProperties> invalidPropertyContainer =
		new DefaultProperty<ClassContainingInvalidProperties>(this, "invalidPropertyContainer");
	
	public final Property<AnotherClassReferringToClassContainingInvalidProperties> referenceToAnotherClass =
		new DefaultProperty<AnotherClassReferringToClassContainingInvalidProperties>(this, "referenceToAnotherClass");
	
	
	
}
