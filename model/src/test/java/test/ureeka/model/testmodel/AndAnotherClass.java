package test.ureeka.model.testmodel;

import java.util.List;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.PropertyList;


public class AndAnotherClass extends AbstractPropertiesOwner {

	private static final long serialVersionUID = -3802332077896510588L;
	
	public final PropertyList<String> list = new PropertyList<String>(this, "list");

	{
		list.setMinimumSize(1);
	}
	
	public List<String> getList() {
		return list;
	}
	
}
