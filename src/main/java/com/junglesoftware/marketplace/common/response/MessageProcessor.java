package com.junglesoftware.marketplace.common.response;

import com.junglesoftware.marketplace.common.exception.ValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageProcessor {

  @Autowired
  private MessageFactory messageFactory;

  public <T> List<Message> process(Set<ConstraintViolation<T>> violations,
      Set<ConstraintViolation<T>> warnings) throws ValidationException {

    List<Message> messages = processConstraintSet(warnings);

    if (CollectionUtils.isNotEmpty(violations)) {
      messages.addAll(processConstraintSet(violations));
      throw new ValidationException(messages);
    }

    return messages;
  }

  private <T> List<Message> processConstraintSet(Set<ConstraintViolation<T>> constraints) {
    if (constraints == null) {
      return new ArrayList<>();
    }

    return constraints.stream()
        .filter(Objects::nonNull)
        .map(this::buildMessage)
        .collect(Collectors.toList());
  }

  private <T> Message buildMessage(ConstraintViolation<T> violation) {
    Message message = messageFactory.forIdAndText(ExceptionUtils.removeMessageKeyBrackets(violation.getMessageTemplate()), violation.getMessage());
    if(violation.getPropertyPath() != null) {
      message.setPath(violation.getPropertyPath().toString());
    }
    if (message instanceof MessageV1_1) {
      message.setSeverity(getViolationSeverity(violation));
    }
    return message;
  }
  
  private <T> String getViolationSeverity(ConstraintViolation<T> violation) {
    ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor();
    if (descriptor == null) {
      return null;
    }
    Set<Class<? extends Payload>> payloadSet = descriptor.getPayload();
    if (CollectionUtils.isNotEmpty(payloadSet)) {
      if (payloadSet.contains(Severity.ERROR.class)) {
        return Severity.ERROR.toString();
      } else if (payloadSet.contains(Severity.WARNING.class)) {
        return Severity.WARNING.toString();
      } else if (payloadSet.contains(Severity.OK.class)) {
        return Severity.OK.toString();
      }
    }
    return null;
  }
  
}
