package com.junglesoftware.marketplace.common.interceptor;

import com.erac.services.restframework.validation.annotation.HeaderValidation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties
@Data
public class HeaderValidationProperties {
  private Map<HeaderValidation, Boolean> headerValidation = new HashMap<>();
}
