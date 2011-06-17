package test.ureeka.model;

import java.io.Serializable;

import ureeka.model.WeakReferenceSerialisable;

// Test Class
public class ClassOne implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClassTwo classTwo;
	
	private WeakReferenceSerialisable<ClassTwo> classTwoWeakReference;

	public ClassOne(ClassTwo classTwo) {
		super();
		this.classTwo = classTwo;
		this.classTwoWeakReference = new WeakReferenceSerialisable<ClassTwo>(classTwo);
	}

	/**
	 * @return the classTwo
	 */
	public ClassTwo getClassTwo() {
		return classTwo;
	}

	/**
	 * @param classTwo the classTwo to set
	 */
	public void setClassTwo(ClassTwo classTwo) {
		this.classTwo = classTwo;
	}

	/**
	 * @return the classTwoWeakReference
	 */
	public WeakReferenceSerialisable<ClassTwo> getClassTwoWeakReference() {
		return classTwoWeakReference;
	}

	/**
	 * @param classTwoWeakReference the classTwoWeakReference to set
	 */
	public void setClassTwoWeakReference(
			WeakReferenceSerialisable<ClassTwo> classTwoWeakReference) {
		this.classTwoWeakReference = classTwoWeakReference;
	}
	
}
