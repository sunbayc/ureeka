package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.Property;
import ureeka.model.PropertyReference;

public class AnotherClassReferringToClassContainingInvalidProperties extends AbstractPropertiesOwner {
	
	private static final long serialVersionUID = -4936323560353177231L;

	public final Property<ClassContainingInvalidProperties> invalidPropertyContainer =
		new PropertyReference<ClassContainingInvalidProperties>(this, "invalidPropertyContainer");
		
}
