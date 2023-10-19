package com.junglesoftware.marketplace.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junglesoftware.marketplace.common.CommonErrors;
import com.junglesoftware.marketplace.common.request.RequestContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Locale;

@Data
@NoArgsConstructor
public class Message {

  Message(String messageId, String messageText) {
    this.messageId = messageId;
    this.messageText = messageText;
  }

  Message(RequestContext requestContext, MessageBundle messageBundle) {
    this.requestContext = requestContext;
    this.messageBundle = messageBundle;
    this.locale = requestContext.getLocale();
  }

  @JsonIgnore
  protected RequestContext requestContext;

  @JsonIgnore
  protected MessageBundle messageBundle;

  @Setter(AccessLevel.NONE)
  private String messageId;

  @Setter(AccessLevel.NONE)
  @JsonProperty("message")
  private String messageText;

  private String path;

  @JsonProperty("support_info")
  private String supportInfo;

  protected Locale locale;
  
  @JsonIgnore
  protected String severity;

  public Message withSupportInfo(String supportInfo) {
    this.supportInfo = supportInfo;
    return this;
  }

  public void setMessageTextById(String messageId) {
    this.messageId = messageId;
    this.messageText = resolveMessageId(messageId);
    if (StringUtils.isEmpty(this.messageText)) {
      this.messageId = CommonErrors.UNKNOWN_ERROR;
      this.messageText = resolveMessageId(CommonErrors.UNKNOWN_ERROR);
    }
  }

  public void setMessageTextByIdWithTextReplacement(String messageId, String... arguments) {
    setMessageTextById(messageId);
    this.messageText = (StringUtils.isNotBlank(this.messageText) && arguments != null)
        ? MessageFormat.format(messageText, (Object[]) arguments)
        : messageText;
  }
  
  private String resolveMessageId(String messageId) {
    if (messageBundle == null) {
      return StringUtils.EMPTY;
    }
    return messageBundle.get(messageId, this.locale);
  }
}
