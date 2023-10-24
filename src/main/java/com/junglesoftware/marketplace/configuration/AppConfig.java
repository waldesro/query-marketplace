package com.junglesoftware.marketplace.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {

  @Autowired
  private HeaderValidationInterceptor headerValidationInterceptor;

  @Autowired
  private LoggingInterceptor loggingInterceptor;

  @Autowired
  private SecurityInterceptor securityInterceptor;

  @Autowired
  private TimingInterceptor timingInterceptor;
  
  @Autowired
  private ResponseMessageFormatInterceptor responseMessageFormatInterceptor;

  @Autowired
  private BusinessMessageInResponseBodyInterceptor businessMessageInResponseInterceptor;

  @Autowired
  private EHIHeaderLocaleResolver localeResolver;

  @Value("${service.interceptor.exclusions}")
  private String[] interceptorExclusionPaths;

  @Value("${service.interceptor.security.enabled:true}")
  private String securityInterceptorEnabled;

  @Value("${service.security.interceptor.exclusions:#{null}}")
  private String[] securityInterceptorExclusionPaths;

  /**
   * Add any interceptors to the message handling chain.
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // TODO: Make each of these configurable as to being on/off.
    interceptTiming(registry);
    interceptLogging(registry);
    interceptResponseMessageFormat(registry);
    interceptBusinessMessageInResponse(registry);
    interceptHeaderValidation(registry);

    interceptSecurity(registry);
  }

  /**
   * Add an interceptor for handling customized service behavior.
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptResponseMessageFormat(InterceptorRegistry registry) {
    registry.addInterceptor(responseMessageFormatInterceptor).excludePathPatterns(interceptorExclusionPaths);
  }


  /**
   * Add an interceptor for .
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptBusinessMessageInResponse(InterceptorRegistry registry) {
    registry.addInterceptor(businessMessageInResponseInterceptor).excludePathPatterns(interceptorExclusionPaths);
  }

  /**
   * Add an interceptor for recording the total duration of the transaction.
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptTiming(InterceptorRegistry registry) {
    registry.addInterceptor(timingInterceptor).excludePathPatterns(interceptorExclusionPaths);
  }

  /**
   * Add an interceptor for handling the logging of requests and responses using
   * the standard logging system.
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptHeaderValidation(InterceptorRegistry registry) {
    registry.addInterceptor(headerValidationInterceptor).excludePathPatterns(interceptorExclusionPaths);
  }

  /**
   * Add an interceptor for handling the logging of requests and responses using
   * the standard logging system.
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptLogging(InterceptorRegistry registry) {
    registry.addInterceptor(loggingInterceptor);
  }

  /**
   * Add an interceptor for handling security. This interceptor should operate for
   * all URLs handled by the service except for the URLs generating Web Pages
   * (i.e. Swagger, Maintenance).
   *
   * @param registry
   *          The InterceptorRegistry used by Spring-MVC in the message routing
   *          process.
   */
  private void interceptSecurity(InterceptorRegistry registry) {
    if (Boolean.parseBoolean(securityInterceptorEnabled)) {
      List<String> exclusionPaths = new ArrayList<>(Arrays.asList(interceptorExclusionPaths));

      if (securityInterceptorExclusionPaths != null) {
        exclusionPaths.addAll(Arrays.asList(securityInterceptorExclusionPaths));
      }
      registry.addInterceptor(securityInterceptor).excludePathPatterns(exclusionPaths);
    }
  }

  /**
   * Reads Messages.Properties file to get the messages
   *
   * @return ReloadableResourceBundleMessageSource messageBundle
   */
  @Bean
  public static MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
    messageBundle.setBasenames("classpath:messages/framework-messages", "classpath:messages/messages");
    messageBundle.setDefaultEncoding(StandardCharsets.UTF_8.name());
    messageBundle.setFallbackToSystemLocale(false);
    messageBundle.setUseCodeAsDefaultMessage(false);

    return messageBundle;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new CustomLocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Override
  public org.springframework.validation.Validator getValidator() {
    return validator();
  }

  @Bean
  public LocaleResolver localeResolver() {
    return localeResolver;
  }

  @Bean
  public HealthAggregator healthAggregator() {
    return new EHIHealthAggregator(new OrderedHealthAggregator());
  }
}
