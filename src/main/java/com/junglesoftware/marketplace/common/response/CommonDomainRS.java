package com.junglesoftware.marketplace.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junglesoftware.marketplace.common.enumeration.ServiceStatusEnumeration;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class CommonDomainRS {
  @JsonIgnore
  private MessageFactory messageFactory;
  @JsonIgnore
  private boolean inBody;
  @JsonFormat
  @JsonIgnore
  private List<Message> businessMessages;

  @ApiModelProperty(
      dataType = "com.junglesoftware.marketplace.common.response.Message[]",
      required = false,
      name = "messages",
      value = "A Json encoded list of errors or messages encountered while processing the request.  This will be returned if the errors encountered are severe enough to prohibit servicing the request."
  )
  private List<Message> messages;
  @JsonFormat
  private Locale locale;
  @JsonIgnore
  private ServiceStatusEnumeration statusCode;

  protected CommonDomainRS() {
    this((List)null);
  }

  protected CommonDomainRS(List<Message> messages) {
    this.inBody = false;
    this.statusCode = ServiceStatusEnumeration.OK;
    this.messages = (null == messages ? new ArrayList<>() : messages);
  }

  protected CommonDomainRS(Message message) {
    this((List)null);
    this.addError(message);
  }

  public void addBusinessMessage(Message message) {
    if (null == this.getBusinessMessages()) {
      this.businessMessages = new ArrayList<>();
    }

    this.businessMessages.add(message);
    this.setStatusCode(ServiceStatusEnumeration.BUSINESS_ERROR);
  }

  public void addBusinessMessage(String errorId) {
    this.addBusinessMessage(this.messageFactory.forId(errorId));
  }

  public void addBusinessMessages(List<Message> messages) {
    if (null == this.getBusinessMessages()) {
      this.businessMessages = new ArrayList<>();
    }

    if (CollectionUtils.isNotEmpty(messages)) {
      this.businessMessages.addAll(messages);
      this.setStatusCode(ServiceStatusEnumeration.BUSINESS_ERROR);
    }

  }

  public void addError(String errorId) {
    this.addError(this.messageFactory.forId(errorId));
  }

  public void addError(Message message) {
    if (null != message) {
      if (null == this.getMessages()) {
        this.messages = new ArrayList<>();
      }

      this.messages.add(message);
      this.setStatusCode(ServiceStatusEnumeration.TECHNICAL_ERROR);
    }

  }

  public void addErrors(List<Message> errors) {
    if (null == this.messages) {
      this.messages = new ArrayList<>();
    }

    if (CollectionUtils.isNotEmpty(errors)) {
      this.messages.addAll(errors);
      this.setStatusCode(ServiceStatusEnumeration.TECHNICAL_ERROR);
    }

  }

  @JsonIgnore
  public boolean isValid() {
    return ServiceStatusEnumeration.BUSINESS_ERROR.equals(this.statusCode) || ServiceStatusEnumeration.OK.equals(this.statusCode);
  }

  public void setMessages(List<Message> messages) {
    if (null == this.getMessages()) {
      this.messages = messages;
    } else if (messages == null) {
      this.messages = null;
    } else {
      this.messages.addAll(messages);
    }

  }

  public boolean containsMessageId(String messageId) {
    if (this.messages != null) {
      Iterator<Message> var2 = this.messages.iterator();

      while(var2.hasNext()) {
        Message message = var2.next();
        if (message.getMessageId() != null && message.getMessageId().equals(messageId)) {
          return true;
        }
      }
    }

    return false;
  }

}

