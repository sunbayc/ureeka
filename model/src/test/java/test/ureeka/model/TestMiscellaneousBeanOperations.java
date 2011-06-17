package test.ureeka.model;

import org.junit.Before;
import org.junit.Test;

import test.ureeka.model.misc.Person;
import ureeka.model.PropertiesContainer;
import ureeka.model.Property;
import ureeka.model.PropertyVisitor;
import ureeka.model.utils.ValidationHelper;


public class TestMiscellaneousBeanOperations {

	private Person root;
	
	@Before
	public void setUp() {
		root = new Person();
		root.startLifeCycle();
	}
	
	@Test public void testScenarioOne() {
		root.butler.set(new Person());
		root.butler.get().butler.set(new Person());
		root.butler.get().butler.get().butler.set(new Person());
		root.validate();
		// Person is not allowed a butler, and each butler is a person, so three validation errors
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 3);
	}
	
	@Test public void testScenarioTwo() {
		root.butler.set(new Person());
		root.butler.get().butler.set(new Person());
		root.butler.get().butler.get().butler.set(new Person());
		// Person is not allowed a butler, and each butler is a person
		// But now we run our 'NullNonAllowableFieldsVisitor', and it should
		// nullify the values set above
		root.visitProperties(new PropertyVisitor() {
			
			public void visit(Property<?> property) {
				// Visit the properties of this property, if it's a container
				if (property instanceof PropertiesContainer) {
					((PropertiesContainer) property).visitProperties(this);
				}
				if (!property.isAllowed() && property.get() != null) {
					property.set(null);
				}
				
			}
		});
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 0);
	}
	
}
