package com.junglesoftware.marketplace.common.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rest.message")
public class MessageConfiguration {
  private MessageFormat defaultMessageFormat;
  
  private static final String WARNING_MSG = "\n" +
      "==============================================================\n" + 
      "|                          WARNING!                          |\n" +
      "| No value for rest.message.default-message-format has been  |\n" +
      "| declared! This value is required for proper message format |\n" +
      "| support as of version 2.0.0 of the VS REST Framework. The  |\n" +
      "| Framework will now default to the Legacy message format,   |\n" +
      "| which is NOT compliant with the EHI API Standards!         |\n" +
      "==============================================================";
  
  @PostConstruct
  public void messageFormatProvidedCheck() {
    if (defaultMessageFormat == null) {
      log.warn(WARNING_MSG);
      defaultMessageFormat = MessageFormat.LEGACY;
    }
  }
}
