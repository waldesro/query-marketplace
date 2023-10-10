package com.junglesoftware.marketplace.common.utils;


import com.junglesoftware.marketplace.common.response.Message;
import com.junglesoftware.marketplace.common.response.MessageFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class ExceptionUtils {

  @Autowired
  private MessageFactory messageFactory;

  public static Throwable checkExceptionIsOfType(Throwable cause, Class<?> clazz) {
    if (cause == null) {
      return null;
    } else if (clazz.isInstance(cause)) {
      return cause;
    } else {
      return checkExceptionIsOfType(cause.getCause(), clazz);
    }
  }
  
  /**
   * This method converts a list of messageId strings to a list of Message objects.
   * @param messageIds
   * @return
   */
  public List<Message> convertMessageIdsToMessages(List<String> messageIds) {
    List<Message> results = new ArrayList<>();
    
    if (messageIds != null) {
      for (String messageId : messageIds) {
        Message message = convertMessageIdToMessage(messageId);
        results.add(message);
      }
    }
    return results;
  }
  
  public Message convertMessageIdToMessage(String messageId) {
    return messageFactory.forId(removeMessageKeyBrackets(messageId));
  }

  /**
   * This function removes any brace pairs from the input parameter.
   * 
   * @param messageKey
   * @return messageKey without brace pairs
   */
  public static String removeMessageKeyBrackets(String messageKey) {
    return messageKey.replaceAll("\\{(.*)\\}", "$1");
  }

  /**
   * This function grabs the element not matching the regex in the message for use in a custom error message
   *
   * @param message the message to parse
   * @return the element failing the validation, or the original message if it doesn't meet the regex
   */
  public static String getElementFromConstraintMessage(String message) {
    if (StringUtils.isNotEmpty(message) && message.matches(".+\\.(?<element>.+): must match \".+\"")) {
      Pattern pattern = Pattern.compile(".+\\.(?<element>.+): must match \".+\"");
      Matcher matcher = pattern.matcher(message);
      if (matcher.find()) {
        return matcher.group("element");
      }
    }
    return message;
  }
}
