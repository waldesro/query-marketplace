package com.junglesoftware.marketplace.common.validation.validator;

import com.erac.services.restframework.validation.ValidatableEnum;
import com.erac.services.restframework.validation.annotation.EnumerationValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnumerationValueValidator
    implements ConstraintValidator<EnumerationValidation, Integer> {

  private Set<Integer> validValues;

  public static Set<Integer> getEnumValues(Class<? extends Enum<?>> e) {
    Enum<?> enums[] = e.getEnumConstants();
    Integer values[] = new Integer[enums.length];

    for (int i = 0; i < enums.length; i++) {
      values[i] = ((ValidatableEnum) enums[i]).getValue();
    }

    return new HashSet<>(Arrays.asList(values));
  }

  @Override
  public void initialize(EnumerationValidation enumeration) {
    Class<? extends Enum<?>> enumSelected = enumeration.enumClass();
    validValues = getEnumValues(enumSelected);
  }

  @Override
  public boolean isValid(Integer i, ConstraintValidatorContext context) {
    boolean result = true;

    if (i != null) {
      result = validValues.contains(i);
    }

    return result;
  }
}
