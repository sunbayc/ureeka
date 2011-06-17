package test.ureeka.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import test.ureeka.model.testmodel.RootClass;
import ureeka.model.ModelHelper;


public class TestAbstractBeanEquality {

	private RootClass root;

	@Before
	public void setUp() {
		root = new RootClass();
		root.startLifeCycle();
	}

	@Test
	public void testScenarioOne() {
		try {
			RootClass rc2 = (RootClass) ModelHelper.copy(root);
			assertFalse(root == rc2);
			assertTrue(root.equals(rc2));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Test
	public void testScenarioTwo() {
		ClassTwo class2 = new ClassTwo();
		ClassTwo copyclass2 = (ClassTwo) ModelHelper.copy(class2);
		assertFalse(class2 == copyclass2);
		assertTrue(copyclass2.getCsOne().getClassTwo() == copyclass2.getCsOne().getClassTwoWeakReference().getValue());
	}

}
