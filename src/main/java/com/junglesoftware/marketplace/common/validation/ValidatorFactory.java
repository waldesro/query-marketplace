package com.junglesoftware.marketplace.common.validation;

import com.erac.services.restframework.validation.Severity.WARNING;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class ValidatorFactory {

  public static <T> Set<ConstraintViolation<T>> extractWarnings(Set<ConstraintViolation<T>> violations) {
    // When we are done we need two separate and distinct sets of constraint violations based on
    // the severity of the violation within the current context.  The severity is part of the
    // violation so we can easily parse the list by filtering.

    if (CollectionUtils.isEmpty(violations)) {
      return null;
    }

    Set<ConstraintViolation<T>> warnings = violations.stream()
        .filter(ValidatorFactory::hasWarningPayload)
        .collect(Collectors.toSet());

    violations.removeAll(warnings);

    return warnings;
  }

  private static boolean hasWarningPayload(ConstraintViolation<?> constraint) {
    Set<?> payloadSet = null;
    ConstraintDescriptor<?> descriptor = constraint.getConstraintDescriptor();
    if (descriptor != null) {
      payloadSet = descriptor.getPayload();
    }
    if (CollectionUtils.isNotEmpty(payloadSet)) {
      return payloadSet.contains(WARNING.class);
    }
    return false;
  }
}
