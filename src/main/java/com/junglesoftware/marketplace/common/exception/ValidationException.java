package com.junglesoftware.marketplace.common.exception;

import com.erac.services.restframework.response.Message;

import java.util.List;


/**
 * Holds the list of error message created by the validations.
 */
public class ValidationException extends MessageAwareException {

  private static final long serialVersionUID = -1717265908507405593L;

  public ValidationException() {
  }

  /**
   * Instantiates a new Validation exception.
   *
   * @param messages the messages
   */
  public ValidationException(List<Message> messages) {
    super(messages);
  }

  /**
   * Instantiates a new Validation exception.
   *
   * @param message the messages
   */
  public ValidationException(Message message) {
    super(message);
  }
}
