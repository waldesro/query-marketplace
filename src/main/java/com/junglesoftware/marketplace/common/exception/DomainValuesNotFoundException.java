package com.junglesoftware.marketplace.common.exception;

public class DomainValuesNotFoundException extends InternalServiceException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public DomainValuesNotFoundException(String errorCode, String supportInformation) {
    super(errorCode, supportInformation);
  }
}
