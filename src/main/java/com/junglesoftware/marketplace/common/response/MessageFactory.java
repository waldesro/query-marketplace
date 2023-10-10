package com.junglesoftware.marketplace.common.response;

import com.erac.services.restframework.request.RequestContext;
import com.erac.services.restframework.validation.Severity;
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
    Message message;
    if (getMessageFormat() == MessageFormat.V1_1) {
      message = new MessageV1_1(requestContext, messageBundle);
      message.setSeverity(Severity.ERROR.toString());
    } else {
      message = new Message(requestContext, messageBundle);
    }
    message.setMessageTextById(messageId);
    return message;
  }

  public Message forIdAndText(String messageId, String messageText) {
    Message message;
    if (getMessageFormat() == MessageFormat.V1_1) {
      message = new MessageV1_1(messageId, messageText);
      message.setSeverity(Severity.ERROR.toString());
    } else {
      message = new Message(messageId, messageText);
    }
    message.setLocale(requestContext.getLocale());
    return message;
  }

  public Message forIdWithTextReplacement(String messageId, String... messageText) {
    Message message;
    if (getMessageFormat() == MessageFormat.V1_1) {
      message = new MessageV1_1(requestContext, messageBundle);
      message.setSeverity(Severity.ERROR.toString());
    } else {
      message = new Message(requestContext, messageBundle);
    }
    message.setMessageTextByIdWithTextReplacement(messageId, messageText);
    return message;
  }
}
