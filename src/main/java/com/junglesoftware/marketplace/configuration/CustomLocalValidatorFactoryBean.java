package com.junglesoftware.marketplace.configuration;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides the full list of argument attributes rather than filtering it as the default implementation does.
 *
 */
public class CustomLocalValidatorFactoryBean extends LocalValidatorFactoryBean {

	private static final int ARGUMENTS_CAPACITY = 2;

	protected Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
		List<Object> arguments = new ArrayList<Object>(ARGUMENTS_CAPACITY);
		arguments.add(getResolvableField(objectName, field));
		arguments.add(descriptor.getAttributes());
		return arguments.toArray(new Object[arguments.size()]);
	}

}
