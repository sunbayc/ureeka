package test.ureeka.model;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import test.ureeka.model.testmodel.AndAnotherClass;
import test.ureeka.model.testmodel.AnotherClass;
import test.ureeka.model.testmodel.ChildOfChildOfRoot;
import test.ureeka.model.testmodel.ChildOfRoot;
import test.ureeka.model.testmodel.RootClass;
import ureeka.model.utils.ValidationHelper;


public class TestBeanPropertiesValidation {

	private RootClass root;
	
	@Before public void setUp() {
		root = new RootClass();
		root.startLifeCycle();
	}
	
	@Test
	public void testValidationScenarioOne() {
		root.validate();
		// Should return a single invalid property - root class has
		// the ChildOfRoot as mandatory
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
	@Test
	public void testValidationScenarioTwo() {
		// root now has all mandatory properties set
		root.setChildOfRoot(new ChildOfRoot());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 0);
	}
	
	@Test
	public void testValidationScenarioThree() {
		// ChildOfChildOfRoot has a single mandatory property
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
	@Test
	public void testValidationScenarioFour() {
		// ChildOfChildOfRoot has a single mandatory property, but Another Class has
		// a single mandatory property - should be two invalid properties
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 2);
	}
	
	@Test
	public void testValidationScenarioFive() {
		// ChildOfChildOfRoot has a single mandatory property, and it has a list that
		// cannot contain more than 1 element - therefore, 2 invalid properties
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		AnotherClass ac = new AnotherClass();
		ac.setFirstProperty("somevalue");
		root.getChildOfRoot().getChild().getListOfObjects().add(ac);
		AnotherClass ac2 = new AnotherClass();
		ac2.setFirstProperty("somevalue");
		root.getChildOfRoot().getChild().getListOfObjects().add(ac2);
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 2);
	}
	
	@Test
	public void testValidationScenarioSix() {
		// ChildOfChildOfRoot has a single mandatory property, and it has a list that
		// cannot contain more than 1 element - and then both AnotherClass instances
		// have a single mandatory property - 4 invalid properties in total
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 4);
	}
	
	@Test
	public void testValidationScenarioSeven() {
		// Root.someProperty has conditional mandatoriness - 
		// is root.dummyproperty is not null, and the child reference
		// on the ChildOfRoot instance is not null, then someProperty becomes
		// mandatory - hence there should be a single validation error
		root.setDummyProperty("anything");
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().setFirstProperty(new Date());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
	@Test
	public void testValidationScenarioEight() {
		// Root.someProperty has conditional mandatoriness - 
		// is root.dummyproperty is not null, and the child reference
		// on the ChildOfRoot instance is not null, then someProperty becomes
		// mandatory - so this should produce no validation errors
		root.setDummyProperty("anything");
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().setFirstProperty(new Date());
		root.setSomeProperty("value");
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 0);
	}
	
	@Test
	public void testValidationScenarioNine() {
		// ChildOfChildOfRoot has a single mandatory property, and it has a list that
		// cannot contain more than 1 element - and then both AnotherClass instances
		// have a single mandatory property - all fulfilled - but AnotherClass.AndAnotherClass
		// has a list with a minimum required element count of 1. So, 1 invalid property should result
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().setFirstProperty(new Date());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.getChildOfRoot().getChild().getListOfObjects().get(0).setFirstProperty("gggggg");
		root.getChildOfRoot().getChild().getListOfObjects().get(0).setAndAnotherClass(new AndAnotherClass());
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
	@Test
	public void testValidationScenarioTen() {
		root.setChildOfRoot(new ChildOfRoot());
		root.setRootsAnotherClass(new AnotherClass());
		root.getRootsAnotherClass().setFirstProperty("somevalue");
		// Should return a single invalid property - AnotherClass has a
		// non-allowable field, notAllowedValue
		root.getRootsAnotherClass().setNotAllowedValue("invalid");
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
	@Test
	public void testValidationScenarioEleven() {
		// ChildOfChildOfRoot has a single mandatory property, but Another Class has
		// a single mandatory property - should be two invalid properties
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.validate();
		assertTrue(root.getInvalidProperties(true).size() == 2);
		// Now removing the AnotherClass reference should reduce the validation error
		// count by one
		root.getChildOfRoot().getChild().getListOfObjects().remove(0);
		root.startLifeCycle();
		root.validate();
		ValidationHelper.validationCheck(root.getInvalidProperties(true), 1);
	}
	
}
