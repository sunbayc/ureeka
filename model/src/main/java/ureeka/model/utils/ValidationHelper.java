/*
 * ureeka - https://github.com/sunbayc/ureeka
 *
 * Copyright (C) 2011 Sunbay Consulting
 * Copyright (C) 2011 Simon Bronner
 *
 * ureeka is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * ureeka is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ureeka; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package ureeka.model.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ureeka.model.AbstractPropertiesOwner;
import ureeka.model.EmptyPropertyEventListener;
import ureeka.model.Property;
import ureeka.model.PropertyEventListener;
import ureeka.model.ValidationMessage;

public class ValidationHelper {

	public static void validationCheck(List<Property<?>> invalidProperties, int nbrOfInvalidProperties) {
		assertNotNull(invalidProperties);
		if (nbrOfInvalidProperties != invalidProperties.size()) {
			System.out.printf("Validation Check : Expected %s invalid properties, but found %s invalid properties\n",
					nbrOfInvalidProperties, invalidProperties.size());
			outputInvalidProperties(invalidProperties);	
		}
		assertEquals(nbrOfInvalidProperties, invalidProperties.size());
	}
	
	public static void doValidationAndCheck(AbstractPropertiesOwner propertiesOwner, int nbrOfInvalidProperties) {
		if (propertiesOwner == null) throw new NullPointerException("Passed property was null");
		propertiesOwner.startLifeCycle();
		propertiesOwner.validate();
		validationCheck(propertiesOwner.getInvalidProperties(true), nbrOfInvalidProperties);
	}
	
	public static void outputInvalidProperties(List<Property<?>> properties) {
		for (Property<?> property : properties) {
			System.err.printf(
					"[%s.%s] has validation errors, see the following:\n",
					property.getContainer().getClass().getSimpleName(),
					property.getPropertyName());
			Map<ValidationMessage, Object[]> messageAndParameters = property.getValidationErrors();
			for (ValidationMessage message : messageAndParameters.keySet()) {
				System.err.printf("\tMessage : %s\n", message.formatLabel(messageAndParameters.get(message)));
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void validateAndTestExpectedMessage(Property rootProperty, Property propertyToTest,
			ValidationMessage message) {
		final List<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();
		PropertyEventListener listener = new EmptyPropertyEventListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void validationErrorOccurred(Property property) {
				validationMessages.addAll(property.getValidationErrors().keySet());
			}
		};
		propertyToTest.registerListener(listener);
		rootProperty.startLifeCycle();
		rootProperty.validate();
		assertTrue("Was expected to fail...", validationMessages.contains(message));
	}
	
	public static void assertIsInvalid(Property<?> p) {
		assertTrue(
			String.format("Property (%s) should be invalid", p.getPropertyName()),
			p.getValidationErrors().size() > 0);
	}
	
	public static void assertIsValid(Property<?> p) {
		assertTrue(
			String.format("Property (%s) should be valid", p.getPropertyName()),
			p.getValidationErrors().size() == 0);
	}
	
	public static void assertContainsMessage(Property<?> p, ValidationMessage message) {
		assertTrue(p.getValidationErrors().containsKey(message));
	}
	
	public static void validateAndEnsurePropertiesAreValid(Property<?> rootProperty, Property<?>... mustBeValidProperties) {
		rootProperty.startLifeCycle();
		rootProperty.validate();
		for (Property<?> property : mustBeValidProperties)
			assertTrue(
				String.format("Property (%s) is not valid:\n %s",
						property.getPropertyName(), generatePropertyValidationMessages(property)),
				property.getValidationErrors().size() == 0);
	}
	
	public static String generatePropertyValidationMessages(Property<?> property) {
		Map<ValidationMessage, Object[]> errors = property.getValidationErrors();
		if (errors.size() == 0) return null;
		StringBuffer buf = new StringBuffer();
		for (ValidationMessage message : errors.keySet()) {
			buf.append(message.formatLabel(errors.get(message)) + "\n");
		}
		return buf.toString();
	}
	
}
