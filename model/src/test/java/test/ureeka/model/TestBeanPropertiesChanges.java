package test.ureeka.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import test.ureeka.model.testmodel.AnotherClass;
import test.ureeka.model.testmodel.AnotherClassReferringToClassContainingInvalidProperties;
import test.ureeka.model.testmodel.ChildOfChildOfRoot;
import test.ureeka.model.testmodel.ChildOfRoot;
import test.ureeka.model.testmodel.ClassContainingInvalidProperties;
import test.ureeka.model.testmodel.ClassReferringToClassContainingInvalidProperties;
import test.ureeka.model.testmodel.RootClass;


public class TestBeanPropertiesChanges {

	private RootClass root;
	
	@Before public void setUp() {
		root = new RootClass();
		root.startLifeCycle();
	}
	
	@Test
	public void testChangesScenarioOne() {
		// Should return no changes 
		assertTrue(root.getChangedProperties(true).size() == 0);
	}
	
	@Test
	public void testChangesScenarioTwo() {
		// Should detect one change
		root.setChildOfRoot(new ChildOfRoot());
		assertTrue(root.getChangedProperties(true).size() == 1);
	}
	
	@Test
	public void testChangesScenarioThree() {
		// Should detect no changes
		root.setChildOfRoot(new ChildOfRoot());
		root.setChildOfRoot(null);
		assertTrue(root.getChangedProperties(true).size() == 0);
	}
	
	@Test
	public void testChangesScenarioFour() {
		// Should detect 3 changes
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		assertTrue(root.getChangedProperties(true).size() == 3);
	}
	
	@Test
	public void testChangesScenarioFive() {
		// Should detect 3 changes (list of objects is detected as changed once - 2 items added)
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		assertTrue(root.getChangedProperties(true).size() == 3);
	}
	
	@Test
	public void testChangesScenarioSix() {
		// Should detect 2 changes - anotherClass is changed because we've set a value 
		// on it and firstProperty on anotherClass is changed
		root.setRootsAnotherClass(new AnotherClass());
		root.getRootsAnotherClass().setFirstProperty("yes, ok");
		assertTrue(root.getChangedProperties(true).size() == 2);
	}
	
	@Test
	public void testChangesScenarioSeven() {
		// Should detect 1 changes - someProperty on the root object has changed
		// Not that root is not detected as a changed property because it's not
		// a property of any other object 
		root.setSomeProperty("dummyvalue");
		assertTrue(root.getChangedProperties(true).size() == 1);
	}
	
	@Test
	public void testChangesScenarioEight() {
		// Should detect 5 changes
		root.setChildOfRoot(new ChildOfRoot());
		root.getChildOfRoot().setChild(new ChildOfChildOfRoot());
		root.getChildOfRoot().getChild().getListOfObjects().add(new AnotherClass());
		// This is only one change - firstProperty is marked as changed, but AnotherClass is new, so it's not changed
		// Arguably, PropertyList should be marked as changed because an item has been added to it - that is not the
		// current behaviour
		root.getChildOfRoot().getChild().getListOfObjects().get(0).setFirstProperty("yesirree");
		assertTrue(root.getChangedProperties(true).size() == 4);
	}
	
	@Test public void testGetChangedPropertiesInDirectedAcyclicGraph() {
		ClassReferringToClassContainingInvalidProperties o = new ClassReferringToClassContainingInvalidProperties();
		o.invalidPropertyContainer.set(new ClassContainingInvalidProperties());
		o.referenceToAnotherClass.set(new AnotherClassReferringToClassContainingInvalidProperties());
		o.referenceToAnotherClass.get().invalidPropertyContainer.set(o.invalidPropertyContainer.get());
		o.startLifeCycle();
		o.invalidPropertyContainer.get().firstProperty.set("bobo");
		assertTrue(o.getChangedProperties(true).size() == 1);
	}
	
}
