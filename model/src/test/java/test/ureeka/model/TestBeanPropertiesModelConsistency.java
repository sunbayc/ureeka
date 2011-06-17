package test.ureeka.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import test.ureeka.model.testmodel.AnotherClassReferringToClassContainingInvalidProperties;
import test.ureeka.model.testmodel.ClassContainingInvalidProperties;
import test.ureeka.model.testmodel.ClassReferringToClassContainingInvalidProperties;
import test.ureeka.model.testmodel.PropertyReferenceClass;
import test.ureeka.model.testmodel.ReactsToChangesClass;
import test.ureeka.model.testmodel.RootClassTwo;
import test.ureeka.model.testmodel.SimpleClass;
import ureeka.model.utils.ValidationHelper;


public class TestBeanPropertiesModelConsistency {

	private RootClassTwo root;

	@Before
	public void setUp() {
		root = new RootClassTwo();
		root.startLifeCycle();
	}

	/**
	 * Sanity check the model
	 */
	@Test
	public void testValidationScenarioOne() {
		root.validate();
		// Should return a no invalid property - RootClassTwo has no mandatory properties
		assertTrue(root.getInvalidProperties(true).size() == 0);
	}

	/**
	 * Ensures that the removal of an object in a property owning PropertyList
	 * removes any references to that object in other PropertyList 
	 */
	@Test
	public void testValidationScenarioTwo() {
		root.getChildren().add(new SimpleClass());
		// Add's a reference to the child added above, but does not take ownership
		root.getReferenceschildren().add(root.getChildren().get(0));
		assertTrue(root.getChildren().size() == 1);
		assertTrue(root.getChildren().size() == root.getReferenceschildren().size());
		root.getChildren().remove(root.getChildren().get(0)); // This should remove both references
		assertTrue(root.getChildren().size() == 0);
		assertTrue(root.getChildren().size() == root.getReferenceschildren().size());
	}

	/**
	 * Ensures that the removal of an object in a non property owning PropertyList
	 * does not have any other effects on the model
	 */
	@Test
	public void testValidationScenarioThree() {
		root.getChildren().add(new SimpleClass());
		// Add's a reference to the child added above, but does not take ownership
		root.getReferenceschildren().add(root.getChildren().get(0));
		assertTrue(root.getChildren().size() == 1);
		assertTrue(root.getChildren().size() == root.getReferenceschildren().size());
		// This should only remove the reference in the references children, as
		// referenceschildren is not configured to behave as a property owner container
		root.getReferenceschildren().remove(root.getReferenceschildren().get(0)); 
		assertTrue(root.getReferenceschildren().size() == 0);
		assertTrue(root.getChildren().size() == 1);
		assertTrue(root.getChildren().size() > root.getReferenceschildren().size());
	}

	/**
	 * Testing the property change event (mostly an example implementation). The Property shadowValueThatChanges
	 * will have the same value as valueThatChanges
	 */
	@Test
	public void testValidationScenarioFour() {
		root.validate();
		// Should return a no invalid property - RootClassTwo has no mandatory properties
		assertTrue(root.getInvalidProperties(true).size() == 0);
		assertTrue(root.shadowValueThatChanges.get() == null);
		root.valueThatChanges.set("somevalue");
		assertTrue(root.shadowValueThatChanges.get().equals("somevalue"));
	}

	/**
	 * Testing the property change event (mostly an example implementation). The value field ReactsToChanges
	 * will null itself if the field valueThatChanges changes 
	 */
	@Test
	public void testValidationScenarioFive() {
		root.validate();
		// Should return a no invalid property - RootClassTwo has no mandatory properties
		assertTrue(root.getInvalidProperties(true).size() == 0);
		root.setReactsToChangesClass(new ReactsToChangesClass(root.valueThatChanges));
		assertTrue(root.getReactsToChangesClass() != null);
		assertTrue(root.getReactsToChangesClass().getValue() != null);
		root.setValueThatChanges("a new value");
		assertTrue(root.getReactsToChangesClass().getValue() == null);
	}

	@Test
	public void testStringInvalidRange() {
		SimpleClass sc = new SimpleClass();
		sc.setFirstField(generateString(Integer.parseInt(sc.firstField.getRange().getUpper()) + 1));
		sc.validate();
		ValidationHelper.validationCheck(sc.getInvalidProperties(true), 1);
	}

	@Test
	public void testStringValidRange() {
		SimpleClass sc = new SimpleClass();
		sc.setFirstField(generateString(Integer.parseInt(sc.firstField.getRange().getUpper())));
		sc.validate();
		ValidationHelper.validationCheck(sc.getInvalidProperties(true), 0);
	}

	@Test
	public void testLongInvalidRange() {
		SimpleClass sc = new SimpleClass();
		sc.setSecondField(sc.secondField.getRange().getUpper() + 1);
		sc.validate();
		ValidationHelper.validationCheck(sc.getInvalidProperties(true), 1);
	}

	@Test
	public void testLongValidRange() {
		SimpleClass sc = new SimpleClass();
		sc.setSecondField(sc.secondField.getRange().getLower());
		sc.validate();
		ValidationHelper.validationCheck(sc.getInvalidProperties(true), 0);
	}

	@Test
	public void testPropertyReference() {
		PropertyReferenceClass referenceClass = new PropertyReferenceClass();
		assertTrue(referenceClass.getListOfObjects().size() == 1);
		assertTrue(referenceClass.getListOfObjects().get(0) == referenceClass.propertyReferenceField.get());
		referenceClass.getListOfObjects().clear();
		assertTrue(referenceClass.getListOfObjects().size() == 0);
		assertTrue(null == referenceClass.propertyReferenceField.get());
	}

	@Test
	public void testPropertyReferenceToMandatoryField() {
		PropertyReferenceClass referenceClass = new PropertyReferenceClass();
		referenceClass.validate();
		ValidationHelper.validationCheck(referenceClass.getInvalidProperties(true), 1);
		referenceClass.startLifeCycle();
		referenceClass.anotherPropertyReferenceField.set(referenceClass.mandatoryField);
		referenceClass.validate();
		// Should still only be one - PropertyReference not being an owningProperty
		ValidationHelper.validationCheck(referenceClass.getInvalidProperties(true), 1);
		
		
	}

	@Test public void testGetInvalidPropertiesInDirectedAcyclicGraph() {
		ClassReferringToClassContainingInvalidProperties o = new ClassReferringToClassContainingInvalidProperties();
		o.invalidPropertyContainer.set(new ClassContainingInvalidProperties());
		o.referenceToAnotherClass.set(new AnotherClassReferringToClassContainingInvalidProperties());
		o.referenceToAnotherClass.get().invalidPropertyContainer.set(o.invalidPropertyContainer.get());
		o.validate();
		ValidationHelper.validationCheck(o.getInvalidProperties(true), 1);
	}
	
	private String generateString(Integer length) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append("a");
		}
		return buffer.toString();
	}

}