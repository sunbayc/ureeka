package test.ureeka.model.testmodel;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.DefaultProperty;
import ureeka.model.Property;
import ureeka.model.ValidationMessages;

public class ClassContainingInvalidProperties extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -4936323560353177231L;

	public final Property<String> firstProperty = new DefaultProperty<String>(this, "firstProperty") {
		
		private static final long serialVersionUID = 1L;

		@Override public void validate() {
			super.validate();
			addValidationError(ValidationMessages.message("Mandatory"), new Object[] { getPropertyName() });
		}
		
	};

}
