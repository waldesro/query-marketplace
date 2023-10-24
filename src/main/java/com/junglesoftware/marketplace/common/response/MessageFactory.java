package com.junglesoftware.marketplace.common.response;


import com.junglesoftware.marketplace.common.request.RequestContext;
import com.junglesoftware.marketplace.common.validation.Severity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static com.junglesoftware.marketplace.common.CommonErrors.UNKNOWN_ERROR;


@Component
@Data
public class MessageFactory {

  @Autowired
  private RequestContext requestContext;
  @Autowired
  private MessageBundle messageBundle;

  public Message unknown() {
    return forId(UNKNOWN_ERROR);
  }

  public Message forId(String messageId) {
    Message message = new Message(requestContext, messageBundle);
    message.setSeverity(Severity.ERROR.toString());
    message.setMessageTextById(messageId);
    return message;
  }

  public Message forIdAndText(String messageId, String messageText) {
    Message message = new Message(messageId, messageText);
    message.setSeverity(Severity.ERROR.toString());
    message.setLocale(requestContext.getLocale());
    return message;
  }

  public Message forIdWithTextReplacement(String messageId, String... messageText) {
    Message message = new Message(requestContext, messageBundle);
    message.setSeverity(Severity.ERROR.toString());
    message.setMessageTextByIdWithTextReplacement(messageId, messageText);
    return message;
  }
}
