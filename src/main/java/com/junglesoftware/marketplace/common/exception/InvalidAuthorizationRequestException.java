package com.junglesoftware.marketplace.common.exception;

import com.junglesoftware.marketplace.common.response.Message;

import java.util.List;

public class InvalidAuthorizationRequestException extends MessageAwareException {


  private static final long serialVersionUID = 5232352960442508358L;

  public InvalidAuthorizationRequestException() {
  }

  /**
   * Instantiates a new InvalidAuthorizationRequestException.
   *
   * @param messages the messages
   */
  public InvalidAuthorizationRequestException(List<Message> messages) {
    super(messages);
  }

  /**
   * Instantiates a new InvalidAuthorizationRequestException.
   *
   * @param message the messages
   */
  public InvalidAuthorizationRequestException(Message message) {
    super(message);
  }
}
