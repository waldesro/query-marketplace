package com.junglesoftware.marketplace.common.interceptor;

import com.erac.services.restframework.domain.exception.ValidationException;
import com.erac.services.restframework.request.RequestContext;
import com.erac.services.restframework.response.Message;
import com.erac.services.restframework.response.MessageFactory;
import com.erac.services.restframework.utils.LocaleUtils;
import com.erac.services.restframework.validation.annotation.HeaderValidation;
import com.erac.services.restframework.validation.annotation.RequiredHeaders;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import static com.erac.services.restframework.domain.CommonErrors.ACCEPT_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.AUTHORIZATION_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.CONTENT_TYPE_MISSING_OR_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_API_KEY_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_CALLER_ID_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_CALLER_ID_HEADER_LENGTH_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_CALLING_APP_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_DEVICE_LOCATION_ID_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_LOCALE_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_LOCALE_HEADER_MISSING;
import static com.erac.services.restframework.domain.CommonErrors.EHI_ORIGIN_IDENTITY_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_SPAN_ID_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_TRACE_ID_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.EHI_WORKFLOW_ID_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.IF_MATCH_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.IF_MODIFIED_SINCE_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.IF_NONE_MATCH_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.IF_RANGE_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.IF_UNMODIFIED_SINCE_HEADER_INVALID;
import static com.erac.services.restframework.domain.CommonErrors.UNKNOWN_ERROR;
import static com.erac.services.restframework.response.RequestResponseHeaders.ACCEPT;
import static com.erac.services.restframework.response.RequestResponseHeaders.AUTHORIZATION;
import static com.erac.services.restframework.response.RequestResponseHeaders.CALLER_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.CALLER_ID_MAX_LENGTH;
import static com.erac.services.restframework.response.RequestResponseHeaders.CALLING_APPLICATION;
import static com.erac.services.restframework.response.RequestResponseHeaders.CONTENT_TYPE;
import static com.erac.services.restframework.response.RequestResponseHeaders.DEVICE_LOCATION_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.EHI_API_KEY;
import static com.erac.services.restframework.response.RequestResponseHeaders.EHI_SPAN_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.EHI_TRACE_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.IF_MATCH;
import static com.erac.services.restframework.response.RequestResponseHeaders.IF_MODIFIED_SINCE;
import static com.erac.services.restframework.response.RequestResponseHeaders.IF_NONE_MATCH;
import static com.erac.services.restframework.response.RequestResponseHeaders.IF_RANGE;
import static com.erac.services.restframework.response.RequestResponseHeaders.IF_UNMODIFIED_SINCE;
import static com.erac.services.restframework.response.RequestResponseHeaders.LOCALE;
import static com.erac.services.restframework.response.RequestResponseHeaders.ORIGIN_IDENTITY;
import static com.erac.services.restframework.response.RequestResponseHeaders.SPAN_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.TRACE_ID;
import static com.erac.services.restframework.response.RequestResponseHeaders.WORKFLOW_ID;
import static com.erac.services.restframework.utils.LocaleUtils.getLocaleFromString;
import static com.erac.services.restframework.utils.LocaleUtils.getValidLocale;
import static com.erac.services.restframework.utils.LocaleUtils.normalizeLocaleString;

@Component
public class HeaderValidationInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private RequestContext requestContext;

  @Autowired
  private MessageFactory messageFactory;

  @Autowired
  private HeaderValidationProperties headerValidationProperties;

  private static final Set<String> METHODS_REQUIRE_CONTENT_TYPE = new HashSet<>(Arrays.asList("POST", "PUT", "PATCH"));

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    List<Message> messages = new ArrayList<>();
    if (handler instanceof HandlerMethod) {
      validateSuperPreHandle(request, response, handler, messages);

      Set<HeaderValidation> optionalHeaders = getOptionalHeadersToValidate(handler);
      Locale locale = validateEhiLocale(request, messages, optionalHeaders);
      requestContext.setLocale(locale);

      validateRequiredHeaders(request, messages);
      validateOptionalHeaders(handler, request, messages, optionalHeaders);

      populateRequestContext(request);
    }
    if (!messages.isEmpty()) {
      throw new ValidationException(messages);
    }

    return true;
  }

  private void validateRequiredHeaders(HttpServletRequest request, List<Message> messages) {
    validateAccept(request, messages);
    validateRequiredContentType(request, messages);
    validateAuthorization(request, messages);
    validateEhiCallingApplication(request, messages);
  }

  private void validateOptionalHeaders(Object handler, HttpServletRequest request, List<Message> messages,
      Set<HeaderValidation> optionalHeaders) {
    if (CollectionUtils.isNotEmpty(optionalHeaders)) {
      for (HeaderValidation header : optionalHeaders) {
        switch (header) {
          case CONTENT_TYPE:
            validateOptionalContentType(request, messages);
            break;
          case EHI_API_KEY:
            validateEhiApiKey(request, messages);
            break;
          case EHI_CALLER_ID:
            validateEhiCallerId(request, messages);
            break;
          case EHI_DEVICE_LOCATION_ID:
            validateEhiDeviceLocationId(request, messages);
            break;
          case IF_MATCH:
            validateIfMatch(request, messages);
            break;
          case IF_NONE_MATCH:
            validateIfNoneMatch(request, messages);
            break;
          case IF_MODIFIED_SINCE:
            validateIfModifiedSince(request, messages);
            break;
          case IF_UNMODIFIED_SINCE:
            validateIfUnmodifiedSince(request, messages);
            break;
          case IF_RANGE:
            validateIfRange(request, messages);
            break;
          case EHI_ORIGIN_IDENTITY:
            validateEhiOriginIdentity(request, messages);
            break;
          case EHI_WORKFLOW_ID:
            validateEhiWorkflowId(request, messages);
            break;
          case X_B3_TRACE_ID:
            validateTraceId(request, messages);
            break;
          case X_B3_SPAN_ID:
            validateSpanId(request, messages);
            break;
          default:
            break;
        }
      }
    }
  }

  private Set<HeaderValidation> getOptionalHeadersToValidate(Object handler) {
    Set<HeaderValidation> headerValidations = new HashSet<>();

    addHeadersToValidateFromProperties(headerValidations);
    addHeadersToValidateFromAnnotations(headerValidations, handler);

    return headerValidations;
  }

  private void addHeadersToValidateFromProperties(Set<HeaderValidation> headerValidations) {
    headerValidations.addAll(
        headerValidationProperties.getHeaderValidation().entrySet().stream()
            .filter(Entry::getValue)
            .map(Entry::getKey)
            .collect(Collectors.toSet()));
  }

  private static void addHeadersToValidateFromAnnotations(Set<HeaderValidation> headerValidations, Object handler) {
    HandlerMethod hm = (HandlerMethod) handler;
    Method callingMethod = hm.getMethod();

    addHeadersToValidateFromMethodAnnotation(headerValidations, callingMethod);
    addHeadersToValidateFromClassAnnotation(headerValidations, callingMethod);
  }

  private static void addHeadersToValidateFromMethodAnnotation(Set<HeaderValidation> headerValidations,
      Method callingMethod) {
    RequiredHeaders requiredHeadersAnnotation = callingMethod.getAnnotation(RequiredHeaders.class);
    if (requiredHeadersAnnotation != null) {
      Collections.addAll(headerValidations, requiredHeadersAnnotation.value());
    }
  }

  private static void addHeadersToValidateFromClassAnnotation(Set<HeaderValidation> headerValidations,
      Method callingMethod) {
    RequiredHeaders requiredHeadersAnnotation = callingMethod.getDeclaringClass().getAnnotation(RequiredHeaders.class);
    if (requiredHeadersAnnotation != null) {
      Collections.addAll(headerValidations, requiredHeadersAnnotation.value());
    }
  }

  /**
   * This method gets the service path to use in security logging which is "${contextPath} + ${servletPath}".
   *
   * @param request The incoming HttpServletRequest that this data is to be retireved from
   * @return The service path from the given request.
   */
  private static String getServicePath(HttpServletRequest request) {
    String path = "";
    if (StringUtils.isNotBlank(request.getContextPath())) {
      path += request.getContextPath();
    }

    if (StringUtils.isNotBlank(request.getServletPath())) {
      if (path.endsWith("/") && request.getServletPath().startsWith("/")) {
        path = path.substring(0, path.length() - 1);
      }
      path += request.getServletPath();
    }

    return path;
  }

  private void populateRequestContext(HttpServletRequest request) {
    requestContext.setCallingApplication(request.getHeader(CALLING_APPLICATION));
    requestContext.setSpanId(getSpanId(request));
    requestContext.setTraceId(getTraceId(request));
    requestContext.setAccept(request.getHeader(ACCEPT));
    requestContext.setWorkflowId(request.getHeader(WORKFLOW_ID));

    requestContext.setContentType(request.getHeader(CONTENT_TYPE));
    requestContext.setEhiApiKey(request.getHeader(EHI_API_KEY));
    requestContext.setEhiCallerId(request.getHeader(CALLER_ID));
    requestContext.setEhiOriginIdentity(request.getHeader(ORIGIN_IDENTITY));
    requestContext.setEhiDeviceLocationId(request.getHeader(DEVICE_LOCATION_ID));

    requestContext.setIfMatch(request.getHeader(IF_MATCH));
    requestContext.setIfNoneMatch(request.getHeader(IF_NONE_MATCH));
    requestContext.setIfModifiedSince(request.getHeader(IF_MODIFIED_SINCE));
    requestContext.setIfUnmodifiedSince(request.getHeader(IF_UNMODIFIED_SINCE));
    requestContext.setIfRange(request.getHeader(IF_RANGE));

    requestContext.setServicePath(getServicePath(request));
    requestContext.setAuthorization(request.getHeader(AUTHORIZATION));
  }

  private void validateSuperPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      List<Message> messages) {
    try {
      if (!super.preHandle(request, response, handler)) {
        messages.add(messageFactory.forId(UNKNOWN_ERROR));
      }
    } catch (Exception e) {
      messages.add(messageFactory.forId(UNKNOWN_ERROR));
    }
  }

  private void validateAccept(HttpServletRequest request, List<Message> messages) {
    String headerValue = request.getHeader(ACCEPT);
    validateStringNotBlank(headerValue, ACCEPT_HEADER_INVALID, messages);
  }

  private void validateRequiredContentType(HttpServletRequest request, List<Message> messages) {
    String method = request.getMethod();
    if (METHODS_REQUIRE_CONTENT_TYPE.contains(method)) {
      validateStringNotBlank(request.getHeader(CONTENT_TYPE), CONTENT_TYPE_MISSING_OR_INVALID, messages);
    }
  }

  private void validateAuthorization(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(AUTHORIZATION), AUTHORIZATION_HEADER_INVALID, messages);
  }

  private void validateEhiCallerId(HttpServletRequest request, List<Message> messages) {
    String value = request.getHeader(CALLER_ID);

    if (validateStringNotBlank(value, EHI_CALLER_ID_HEADER_INVALID, messages)) {
      validateLength(value, CALLER_ID_MAX_LENGTH, EHI_CALLER_ID_HEADER_LENGTH_INVALID, messages);
    }
  }

  private void validateEhiOriginIdentity(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(ORIGIN_IDENTITY), EHI_ORIGIN_IDENTITY_HEADER_INVALID, messages);
  }

  private void validateEhiDeviceLocationId(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(DEVICE_LOCATION_ID), EHI_DEVICE_LOCATION_ID_HEADER_INVALID, messages);
  }

  private void validateEhiApiKey(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(EHI_API_KEY), EHI_API_KEY_HEADER_INVALID, messages);
  }

  private void validateOptionalContentType(HttpServletRequest request, List<Message> messages) {
    String method = request.getMethod();
    if (!METHODS_REQUIRE_CONTENT_TYPE.contains(method)) {
      validateStringNotBlank(request.getHeader(CONTENT_TYPE), CONTENT_TYPE_MISSING_OR_INVALID, messages);
    }
  }

  private void validateEhiWorkflowId(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(WORKFLOW_ID), EHI_WORKFLOW_ID_HEADER_INVALID, messages);
  }

  private void validateIfMatch(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(IF_MATCH), IF_MATCH_HEADER_INVALID, messages);
  }

  private void validateIfNoneMatch(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(IF_NONE_MATCH), IF_NONE_MATCH_HEADER_INVALID, messages);
  }

  private void validateIfModifiedSince(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(IF_MODIFIED_SINCE), IF_MODIFIED_SINCE_HEADER_INVALID, messages);
  }

  private void validateIfUnmodifiedSince(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(IF_UNMODIFIED_SINCE), IF_UNMODIFIED_SINCE_HEADER_INVALID, messages);
  }

  private void validateIfRange(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(IF_RANGE), IF_RANGE_INVALID, messages);
  }

  private void validateEhiCallingApplication(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(request.getHeader(CALLING_APPLICATION), EHI_CALLING_APP_HEADER_INVALID, messages);
  }

  private Locale validateEhiLocale(HttpServletRequest request, List<Message> messages,
      Set<HeaderValidation> optionalHeaders) {
    String localeString = request.getHeader(LOCALE);
    if (optionalHeaders.stream().anyMatch(header -> header == HeaderValidation.EHI_LOCALE)) {
      validateStringNotBlank(localeString, EHI_LOCALE_HEADER_MISSING, messages);
    }

    if (StringUtils.isNotBlank(localeString)) {
      return validateLocale(localeString, messages);
    }

    return LocaleUtils.DEFAULT_LOCALE;
  }

  private void validateSpanId(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(getSpanId(request), EHI_SPAN_ID_HEADER_INVALID, messages);
  }

  private void validateTraceId(HttpServletRequest request, List<Message> messages) {
    validateStringNotBlank(getTraceId(request), EHI_TRACE_ID_HEADER_INVALID, messages);
  }

  private void validateLength(String value, Integer limit, String errorCode, List<Message> messages) {
    if (value.length() > limit) {
      messages.add(messageFactory.forId(errorCode));
    }
  }

  private Locale validateLocale(String value, List<Message> messages) {
    String normalizedLocale = normalizeLocaleString(value);
    Locale unresolvedLocale = getLocaleFromString(value);
    Locale locale = getValidLocale(unresolvedLocale.getLanguage(), unresolvedLocale.getCountry());
    if (null != locale) {
      if (!normalizedLocale.equals(locale.getISO3Language() + "_" + locale.getISO3Country())
          && !normalizedLocale.equals(locale.getLanguage() + "_" + locale.getCountry())) {
        messages.add(messageFactory.forId(EHI_LOCALE_HEADER_INVALID));
      }
      return locale;
    } else {
      messages.add(messageFactory.forId(EHI_LOCALE_HEADER_INVALID));
      return LocaleUtils.DEFAULT_LOCALE;
    }
  }

  private boolean validateStringNotBlank(String value, String errorCode, List<Message> messages) {
    if (StringUtils.isBlank(value)) {
      messages.add(messageFactory.forId(errorCode));
      return false;
    }
    return true;
  }

  /*
   * Helpers to transparently allow old EHI-SPAN-ID and EHI-TRACE-ID until we can remove them from
   * active clients. Currently only VOS and CCS
   */
  private static String getSpanId(HttpServletRequest request) {
    return StringUtils.isNotBlank(request.getHeader(SPAN_ID)) ? request.getHeader(SPAN_ID)
        : request.getHeader(EHI_SPAN_ID);
  }

  private static String getTraceId(HttpServletRequest request) {
    return StringUtils.isNotBlank(request.getHeader(TRACE_ID)) ? request.getHeader(TRACE_ID)
        : request.getHeader(EHI_TRACE_ID);
  }
}
