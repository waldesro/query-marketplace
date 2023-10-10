package com.junglesoftware.marketplace.common.enumeration;

import com.junglesoftware.marketplace.common.CommonErrors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum FieldErrorCodes {

  NOTNULL (CommonErrors.NOT_NULL, false),
  TYPEMISMATCH(CommonErrors.INVALID_PARAM_TYPE, false),
  MIN(CommonErrors.MINIMUM_CONSTRAINT_VIOLATED, true),
  MAX(CommonErrors.MAXIMUM_CONSTRAINT_VIOLATED, true),
  SIZE(CommonErrors.INVALID_LENGTH, true),
  DEFAULT_CODE(CommonErrors.INVALID_VALUE, true);

  private String messageId;
  private boolean addDetail;

  private static final String MIN_VALUE = "min";
  private static final String MAX_VALUE = "max";
  private static final String LIMIT_VALUE = "value";


  public String[] getAdditionalDetails(FieldError fe) {
    String[] additionalDetails = null;
    switch (this) {
      case MIN:
      case MAX:
        additionalDetails = getAdditionalDetailsForMinMax(fe);
        break;
      case SIZE:
        additionalDetails = getAdditionalDetailsForSize(fe);
        break;
      default:
        additionalDetails = getDefaultMessage(fe);
    }
    if (additionalDetails == null) {
      additionalDetails = getDefaultMessage(fe);
    }
    return additionalDetails;
  }

  private String[] getDefaultMessage(FieldError fe) {
    String[] additionalDetails;
    additionalDetails = new String[1];
    additionalDetails[0] = fe.getDefaultMessage();
    return additionalDetails;
  }

  private String[] getAdditionalDetailsForSize(FieldError fe) {
    String[] additionalDetails = null;
    if (fe.getArguments() != null && fe.getArguments().length > 0) {
      Optional<Object> argMap = Arrays.stream(fe.getArguments())
          .filter(arg -> arg instanceof Map).findFirst();
      if (argMap.isPresent()) {
        additionalDetails = new String[2];
        additionalDetails[0] = (((Map) argMap.get()).get(MIN_VALUE) != null ? ((Map) argMap.get())
            .get(MIN_VALUE).toString() : null);
        additionalDetails[1] = (((Map) argMap.get()).get(MAX_VALUE) != null ? ((Map) argMap.get())
            .get(MAX_VALUE).toString() : null);
      }
    }
    return additionalDetails;
  }

  private String[] getAdditionalDetailsForMinMax(FieldError fe) {
    String[] additionalDetails = null;
    if (fe.getArguments() != null && fe.getArguments().length > 0) {
      Optional<Object> argMap = Arrays.stream(fe.getArguments())
          .filter(arg -> arg instanceof Map).findFirst();
      if (argMap.isPresent()) {
        additionalDetails = new String[1];
        additionalDetails[0] = (((Map) argMap.get()).get(LIMIT_VALUE) != null ? ((Map) argMap.get())
            .get(LIMIT_VALUE).toString() : null);
      }
    }
    return additionalDetails;
  }

}
