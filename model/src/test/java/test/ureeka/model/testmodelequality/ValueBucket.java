package test.ureeka.model.testmodelequality;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;

public class ValueBucket extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -8792004564186998363L;

	public final Property<String> firstValue = new DefaultProperty<String>(this, "firstValue");
	
	public final Property<ValueBucket> secondValue = new DefaultProperty<ValueBucket>(this, "secondValue");
	
}
