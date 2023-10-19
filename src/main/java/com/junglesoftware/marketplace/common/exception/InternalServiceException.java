package com.junglesoftware.marketplace.common.exception;

import com.erac.services.restframework.domain.enumeration.ServiceStatusEnumeration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class InternalServiceException extends Exception {

  private static final long serialVersionUID = 1L;
  private String errorCode;
  private boolean serviceHealthStatus;
  private ServiceStatusEnumeration severity;
  private String supportInformation;

  public InternalServiceException(Exception e) {
    super(e);
  }

  public InternalServiceException(String errorCode, String supportInformation) {
    super(errorCode);
    this.errorCode = errorCode;
    this.severity = ServiceStatusEnumeration.TECHNICAL_ERROR;
    this.supportInformation = supportInformation;
  }
}
