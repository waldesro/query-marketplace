package com.junglesoftware.marketplace.common.enumeration;

/**
 * The status of a response.
 */
public enum ServiceStatusEnumeration {
  OK(0),
  BUSINESS_ERROR(1),
  TECHNICAL_ERROR(2);

  private int value;

  ServiceStatusEnumeration(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
