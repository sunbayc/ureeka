package test.ureeka.model.testmodelequality;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestModelEquality {

	@Test
	public void testIsEqualToSimple() {
		ValueBucket vb = new ValueBucket();
		assertTrue(vb.isEqualTo(vb));
	}
	
	@Test
	public void testIsEqualToSimpleWithNoValue() {
		ValueBucket vb = new ValueBucket();
		ValueBucket vb2 = new ValueBucket();
		assertTrue(vb.isEqualTo(vb2));
	}
	
	@Test
	public void testIsEqualToSimpleWithSingleValue() {
		ValueBucket vb = new ValueBucket();
		vb.firstValue.set("TESTVALUE");
		ValueBucket vb2 = new ValueBucket();
		vb2.firstValue.set(vb.firstValue.get());
		assertTrue(vb.isEqualTo(vb2));
	}
	
	@Test
	public void testIsEqualToSimpleWithSingleDifferingValues() {
		ValueBucket vb = new ValueBucket();
		vb.firstValue.set("TESTVALUE");
		ValueBucket vb2 = new ValueBucket();
		vb2.firstValue.set("NOTTESTVALUE");
		assertFalse(vb.isEqualTo(vb2));
	}
	
	@Test
	public void testIsEqualToSimpleWithComplexValue() {
		ValueBucket vb = new ValueBucket();
		vb.firstValue.set("TESTVALUE");
		vb.secondValue.set(new ValueBucket());
		vb.secondValue.get().firstValue.set("TESTVALUEAGAIN");
		ValueBucket vb2 = new ValueBucket();
		vb2.firstValue.set(vb.firstValue.get());
		vb2.secondValue.set(new ValueBucket());
		vb2.secondValue.get().firstValue.set(vb.secondValue.get().firstValue.get());
		assertTrue(vb.isEqualTo(vb2));
	}
	
	@Test
	public void testIsEqualToSimpleWithComplexDifferingValues() {
		ValueBucket vb = new ValueBucket();
		vb.firstValue.set("TESTVALUE");
		vb.secondValue.set(new ValueBucket());
		vb.secondValue.get().firstValue.set("TESTVALUEAGAIN");
		ValueBucket vb2 = new ValueBucket();
		vb2.firstValue.set(vb.firstValue.get());
		vb2.secondValue.set(new ValueBucket());
		vb2.secondValue.get().firstValue.set("shnizzle");
		assertFalse(vb.isEqualTo(vb2));
	}
	
}
