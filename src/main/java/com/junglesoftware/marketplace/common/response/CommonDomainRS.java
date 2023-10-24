package com.junglesoftware.marketplace.common.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(
    callSuper = false
)
@SuperBuilder
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
      value =
          "A Json encoded list of errors or messages encountered while processing the request.  This will be returned if the errors encountered are severe enough to prohibit servicing the request."
  )
  @Setter(AccessLevel.NONE)
  private List<Message> messages;

  @JsonFormat
  private Locale locale;

  @JsonIgnore
  private ServiceStatusEnumeration statusCode;

  /**
   * Instantiates a new Rental Authorization Response with all defaults.
   */
  public CommonDomainRS() {
    this((List<Message>) null);
  }

  /**
   * Instantiates a new Response.
   *
   * @param messages the messages
   */
  public CommonDomainRS(List<Message> messages) {
    statusCode = ServiceStatusEnumeration.OK;
    this.messages = (null == messages) ? new ArrayList<>() : messages;
  }

  /**
   * Instantiates a new Response.
   *
   * @param message the message
   */
  public CommonDomainRS(Message message) {
    this((List<Message>) null);
    addError(message);
  }

  /**
   * Add a business message to this response.  If there are no messages, this will ensure the
   * messages list is properly initialized.
   */
  public void addBusinessMessage(Message message) {
    if (null == getBusinessMessages()) {
      businessMessages = new ArrayList<>();
    }

    businessMessages.add(message);
    setStatusCode(ServiceStatusEnumeration.BUSINESS_ERROR);
  }

  public void addBusinessMessage(String errorId) {
    addBusinessMessage(messageFactory.forId(errorId));
  }


  /**
   * Add a list of business messages to this response.  If there are no messages, this will ensure
   * the messages list is properly initialized.
   */
  public void addBusinessMessages(List<Message> messages) {
    if (null == getBusinessMessages()) {
      businessMessages = new ArrayList<>();
    }

    if (CollectionUtils.isNotEmpty(messages)) {
      businessMessages.addAll(messages);
      setStatusCode(ServiceStatusEnumeration.BUSINESS_ERROR);
    }
  }

  public void addError(String errorId) {
    addError(messageFactory.forId(errorId));
  }

  /**
   * Add a message to this response.  If there are no messages, this will ensure the messages list
   * is properly initialized.
   */
  public void addError(Message message) {
    if (null != message) {
      if (null == getMessages()) {
        messages = new ArrayList<>();
      }

      messages.add(message);
      setStatusCode(ServiceStatusEnumeration.TECHNICAL_ERROR);
    }
  }

  public void addErrors(List<Message> errors) {
    if (null == messages) {
      messages = new ArrayList<>();
    }
    if (CollectionUtils.isNotEmpty(errors)) {
      messages.addAll(errors);
      setStatusCode(ServiceStatusEnumeration.TECHNICAL_ERROR);
    }
  }

  /**
   * verifies if status is OK.
   *
   * @return boolean
   */
  @JsonIgnore
  public boolean isValid() {
    return ServiceStatusEnumeration.BUSINESS_ERROR.equals(statusCode) ||
        ServiceStatusEnumeration.OK.equals(statusCode);
  }

  /**
   * Set the list of messages for this response, If this response already has messages, the new
   * messages will be added.
   */
  public void setMessages(List<Message> messages) {
    if (null == getMessages()) {
      this.messages = messages;
    } else {
      if (messages == null) {
        this.messages = null;
      } else {
        this.messages.addAll(messages);
      }
    }
  }

  public boolean containsMessageId(String messageId) {

    if (messages != null) {
      for (Message message : messages) {
        if (message.getMessageId() != null && message.getMessageId().equals(messageId)) {
          return true;
        }
      }

    }
    return false;
  }

}
