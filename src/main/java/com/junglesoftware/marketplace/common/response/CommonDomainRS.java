package com.junglesoftware.marketplace.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junglesoftware.marketplace.common.enumeration.ServiceStatusEnumeration;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class CommonDomainRS {
  @JsonIgnore
  private MessageFactory messageFactory;
  @JsonIgnore
  private boolean inBody;
  public static final String BUSINESS_MESSAGES_HEADER_FIELD = "Ehi-Messages";
  public static final String RESPONSE_HEADER_ETAG = "Etag";
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

  public CommonDomainRS() {
    this((List)null);
  }

  public CommonDomainRS(List<Message> messages) {
    this.inBody = false;
    this.statusCode = ServiceStatusEnumeration.OK;
    this.messages = (List)(null == messages ? new ArrayList() : messages);
  }

  public CommonDomainRS(Message message) {
    this((List)null);
    this.addError(message);
  }

  public void addBusinessMessage(Message message) {
    if (null == this.getBusinessMessages()) {
      this.businessMessages = new ArrayList();
    }

    this.businessMessages.add(message);
    this.setStatusCode(ServiceStatusEnumeration.BUSINESS_ERROR);
  }

  public void addBusinessMessage(String errorId) {
    this.addBusinessMessage(this.messageFactory.forId(errorId));
  }

  public void addBusinessMessages(List<Message> messages) {
    if (null == this.getBusinessMessages()) {
      this.businessMessages = new ArrayList();
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
        this.messages = new ArrayList();
      }

      this.messages.add(message);
      this.setStatusCode(ServiceStatusEnumeration.TECHNICAL_ERROR);
    }

  }

  public void addErrors(List<Message> errors) {
    if (null == this.messages) {
      this.messages = new ArrayList();
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
      Iterator var2 = this.messages.iterator();

      while(var2.hasNext()) {
        Message message = (Message)var2.next();
        if (message.getMessageId() != null && message.getMessageId().equals(messageId)) {
          return true;
        }
      }
    }

    return false;
  }


  protected CommonDomainRS(final CommonDomainRSBuilder<?, ?> b) {
    this.inBody = false;
    this.messageFactory = b.messageFactory;
    this.inBody = b.inBody;
    this.businessMessages = b.businessMessages;
    this.messages = b.messages;
    this.locale = b.locale;
    this.statusCode = b.statusCode;
  }

  public MessageFactory getMessageFactory() {
    return this.messageFactory;
  }

  public boolean isInBody() {
    return this.inBody;
  }

  public List<Message> getBusinessMessages() {
    return this.businessMessages;
  }

  public List<Message> getMessages() {
    return this.messages;
  }

  public Locale getLocale() {
    return this.locale;
  }

  public ServiceStatusEnumeration getStatusCode() {
    return this.statusCode;
  }

  public void setMessageFactory(final MessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  public void setInBody(final boolean inBody) {
    this.inBody = inBody;
  }

  public void setBusinessMessages(final List<Message> businessMessages) {
    this.businessMessages = businessMessages;
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public void setStatusCode(final ServiceStatusEnumeration statusCode) {
    this.statusCode = statusCode;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof CommonDomainRS)) {
      return false;
    } else {
      CommonDomainRS other = (CommonDomainRS)o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label87: {
          Object this$messageFactory = this.getMessageFactory();
          Object other$messageFactory = other.getMessageFactory();
          if (this$messageFactory == null) {
            if (other$messageFactory == null) {
              break label87;
            }
          } else if (this$messageFactory.equals(other$messageFactory)) {
            break label87;
          }

          return false;
        }

        if (this.isInBody() != other.isInBody()) {
          return false;
        } else {
          Object this$businessMessages = this.getBusinessMessages();
          Object other$businessMessages = other.getBusinessMessages();
          if (this$businessMessages == null) {
            if (other$businessMessages != null) {
              return false;
            }
          } else if (!this$businessMessages.equals(other$businessMessages)) {
            return false;
          }

          label65: {
            Object this$messages = this.getMessages();
            Object other$messages = other.getMessages();
            if (this$messages == null) {
              if (other$messages == null) {
                break label65;
              }
            } else if (this$messages.equals(other$messages)) {
              break label65;
            }

            return false;
          }

          Object this$locale = this.getLocale();
          Object other$locale = other.getLocale();
          if (this$locale == null) {
            if (other$locale != null) {
              return false;
            }
          } else if (!this$locale.equals(other$locale)) {
            return false;
          }

          Object this$statusCode = this.getStatusCode();
          Object other$statusCode = other.getStatusCode();
          if (this$statusCode == null) {
            if (other$statusCode != null) {
              return false;
            }
          } else if (!this$statusCode.equals(other$statusCode)) {
            return false;
          }

          return true;
        }
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof CommonDomainRS;
  }

  public int hashCode() {
    int result = 1;
    Object $messageFactory = this.getMessageFactory();
    result = result * 59 + ($messageFactory == null ? 43 : $messageFactory.hashCode());
    result = result * 59 + (this.isInBody() ? 79 : 97);
    Object $businessMessages = this.getBusinessMessages();
    result = result * 59 + ($businessMessages == null ? 43 : $businessMessages.hashCode());
    Object $messages = this.getMessages();
    result = result * 59 + ($messages == null ? 43 : $messages.hashCode());
    Object $locale = this.getLocale();
    result = result * 59 + ($locale == null ? 43 : $locale.hashCode());
    Object $statusCode = this.getStatusCode();
    result = result * 59 + ($statusCode == null ? 43 : $statusCode.hashCode());
    return result;
  }

  public String toString() {
    return "CommonDomainRS(messageFactory=" + this.getMessageFactory() + ", inBody=" + this.isInBody() + ", businessMessages=" + this.getBusinessMessages() + ", messages=" + this.getMessages() + ", locale=" + this.getLocale() + ", statusCode=" + this.getStatusCode() + ")";
  }

  public abstract static class CommonDomainRSBuilder<C extends CommonDomainRS, B extends CommonDomainRSBuilder<C, B>> {
    private MessageFactory messageFactory;
    private boolean inBody;
    private List<Message> businessMessages;
    private String etag;
    private List<Message> messages;
    private Locale locale;
    private ServiceStatusEnumeration statusCode;

    public CommonDomainRSBuilder() {
    }

    protected abstract B self();

    public abstract C build();

    public B messageFactory(final MessageFactory messageFactory) {
      this.messageFactory = messageFactory;
      return this.self();
    }

    public B inBody(final boolean inBody) {
      this.inBody = inBody;
      return this.self();
    }

    public B businessMessages(final List<Message> businessMessages) {
      this.businessMessages = businessMessages;
      return this.self();
    }

    public B etag(final String etag) {
      this.etag = etag;
      return this.self();
    }

    public B messages(final List<Message> messages) {
      this.messages = messages;
      return this.self();
    }

    public B locale(final Locale locale) {
      this.locale = locale;
      return this.self();
    }

    public B statusCode(final ServiceStatusEnumeration statusCode) {
      this.statusCode = statusCode;
      return this.self();
    }

    public String toString() {
      return "CommonDomainRS.CommonDomainRSBuilder(messageFactory=" + this.messageFactory + ", inBody=" + this.inBody + ", businessMessages=" + this.businessMessages + ", etag=" + this.etag + ", messages=" + this.messages + ", locale=" + this.locale + ", statusCode=" + this.statusCode + ")";
    }
  }
}

