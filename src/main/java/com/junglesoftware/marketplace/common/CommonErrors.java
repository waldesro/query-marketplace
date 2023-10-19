package com.junglesoftware.marketplace.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonErrors {

  /**
   * Special error message used when an attempt to load a resource message by designator fails. This
   * is a special indicator identifying missing messages.
   */
  public static final String UNKNOWN_ERROR = "QMPAPI-UNKNOWN";

  /* Standard Error Messages */
  public static final String REQUEST_SUCCESSFUL = "QMPAPI-SUCCESSFUL";


  /* Header validation */
  public static final String HEADER_INVALID = "QMPAPI-INVALID-HEADER";

  public static final String HEADER_MISSING = "QMPAPI-MISSING-HEADER";


  /**
   * No record found
   */
  public static final String NO_RECORD_FOUND = "QMPAPI-NO-RECORD-FOUND";

  /**
   * Field contains invalid characters
   */
  public static final String UNREADABLE_REQUEST = "QMPAPI-UNREADABLE_RQ";

  /**
   * Security credentials failed validation
   */
  public static final String SECURITY_FAILED = "QMPAPI-SECURITY-FAILED";

  /**
   * Malformed request
   */
  public static final String MALFORMED_REQUEST = "QMPAPI-MALFORMED-RQ";

  /**
   * Internal error
   */
  public static final String INTERNAL_ERROR = "QMPAPI-INTERNAL-ERROR";

  /**
   * Malformed URL
   */
  public static final String MALFORMED_URL = "QMPAPI-MALFORMED-URL";


  public static final String NOT_NULL = "QMPAPI-NOT-NULL";

  /**
   * Message for value less than minimum permissible value.
   */
  public static final String MINIMUM_CONSTRAINT_VIOLATED = "QMPAPI-MIN-CONSTRAINT";

  /**
   * Message for value greater than maximum permissible value.
   */
  public static final String MAXIMUM_CONSTRAINT_VIOLATED = "QMPAPI-MAX-CONSTRAINT";

  /**
   * Message for invalid value.
   */
  public static final String INVALID_VALUE = "QMPAPI-INVALID-VALUE";

  /**
   * Message for invalid value.
   */
  public static final String INVALID_LENGTH = "QMPAPI-INVALID-LENGTH";

}