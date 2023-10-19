package com.junglesoftware.marketplace.common.exception;


import java.util.ArrayList;
import java.util.List;

public abstract class MessageAwareException extends RuntimeException {

  private static final long serialVersionUID = -746380429553458643L;

  private List<Message> messages;

  public MessageAwareException() {
  }

  public MessageAwareException(List<Message> messages) {
    this.messages = messages;
  }

  public MessageAwareException(Message message) {
    this.messages = new ArrayList<>();
    this.messages.add(message);
  }

  public List<Message> addMessage(Message message) {
    if (CollectionUtils.isEmpty(this.messages)) {
      this.messages = new ArrayList<>();
    }

    this.messages.add(message);

    return messages;
  }

  public List<Message> getMessages() {
    return messages;
  }
}
