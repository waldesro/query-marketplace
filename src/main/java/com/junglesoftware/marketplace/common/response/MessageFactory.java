package com.junglesoftware.marketplace.common.response;


import com.junglesoftware.marketplace.common.request.RequestContext;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.erac.services.restframework.domain.CommonErrors.UNKNOWN_ERROR;

@Component
@Data
public class MessageFactory {

  @Autowired
  private RequestContext requestContext;
  @Autowired
  private MessageBundle messageBundle;
  @Autowired
  private MessageConfiguration messageConfig;

  public Message unknown() {
    return forId(UNKNOWN_ERROR);
  }
  
  private MessageFormat getMessageFormat() {
    MessageFormat format = requestContext.getResponseMessageFormat();
    if (format == null) {
      format = messageConfig.getDefaultMessageFormat();
    }
    return format;
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
