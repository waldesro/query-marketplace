package com.junglesoftware.marketplace.common.validation.validator;

import com.junglesoftware.marketplace.common.validation.annotation.EnumerationValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnumerationLabelValidator
    implements ConstraintValidator<EnumerationValidation, String> {

  private Set<String> validValues;

  public static Set<String> getEnumLabels(Class<? extends Enum<?>> e) {
    Enum<?> enums[] = e.getEnumConstants();
    String names[] = new String[enums.length];

    for (int i = 0; i < enums.length; i++) {
      names[i] = enums[i].name();
    }

    return new HashSet<String>(Arrays.asList(names));
  }

  @Override
  public void initialize(EnumerationValidation enumeration) {
    Class<? extends Enum<?>> enumSelected = enumeration.enumClass();
    validValues = getEnumLabels(enumSelected);
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext context) {
    boolean result = true;

    if (s != null) {
      result = validValues.contains(s);
    }

    return result;
  }
}
