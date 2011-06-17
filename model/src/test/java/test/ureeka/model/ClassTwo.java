package test.ureeka.model;

import java.io.Serializable;

public class ClassTwo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ClassOne csOne = new ClassOne(this);

	/**
	 * @return the csOne
	 */
	public ClassOne getCsOne() {
		return csOne;
	}

	/**
	 * @param csOne the csOne to set
	 */
	public void setCsOne(ClassOne csOne) {
		this.csOne = csOne;
	}
	
}
