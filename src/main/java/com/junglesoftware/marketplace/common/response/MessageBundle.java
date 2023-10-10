package com.junglesoftware.marketplace.common.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
public class MessageBundle {

  private MessageSource messageSource;

  MessageSourceAccessor accessor;

  public String get(String code, Locale locale) {
    try {
      return accessor.getMessage(code, locale);
    } catch (NoSuchMessageException nsme) {
      log.warn(code, nsme);
      return "";
    }
  }
  

}
