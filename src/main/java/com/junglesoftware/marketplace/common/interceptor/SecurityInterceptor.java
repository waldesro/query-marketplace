package com.junglesoftware.marketplace.common.interceptor;

import com.erac.arch.inf.security.Token;
import com.erac.arch.inf.security.ssf.AppSecCredential;
import com.erac.arch.inf.security.ssf.AppSecServicePermission;
import com.erac.arch.inf.security.ssf.ServiceAccessDeniedException;
import com.erac.arch.inf.security.ssf.ServiceSecurityValidator;
import com.erac.arch.inf.security.ssf.ServiceSecurityValidatorFactory;
import com.erac.services.restframework.authorization.AuthService;
import com.erac.services.restframework.domain.CommonErrors;
import com.erac.services.restframework.domain.exception.AuthorizationFailureException;
import com.erac.services.restframework.domain.exception.InvalidAuthorizationRequestException;
import com.erac.services.restframework.response.MessageFactory;
import com.erac.services.restframework.validation.annotation.SecuredObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.erac.services.restframework.response.RequestResponseHeaders.AUTHORIZATION;

/**
 * Intercepts an HTTP request to determine if AppSec OR JWT authorization
 * exists, based on Authentication header, to allow request to proceed
 */
@Component
@Slf4j
public class SecurityInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private MessageFactory messageFactory;

  @Autowired
  private AuthService authService;

  @Value("${service.application.static.id}")
  private int authServiceAppStaticId;

  @Value("${service.application.regex}")
  private String urlRegex;

  @Value("${jwt.enable:false}")
  private boolean jwtEnabled;

  private static final Set<String> ALLOWED_VERBS = new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
  private static final String INVALID_AUTHORIZATION_REQUEST = "Verb %s at path %s is not configured in the security interceptor.";
  private static final String BEARER = "Bearer ";
  private static final String AOSTOKEN = "AOSTOKEN";
  private static final String NULL_SECURED_OBJECT = "No secured object present, unable to authorize JWT.";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

    // Confirm request method is allowed
    validateRequestType(request);

    // Validate request with App-Sec or JWT
    if (handler instanceof HandlerMethod) {
      if (request.getHeader(AUTHORIZATION).startsWith(AOSTOKEN)) {
        return validateSecurityDetailsWithAppSec(request, handler);
      } else if (jwtEnabled) {
        return validateSecurityDetailsWithJWT(request, handler);
      } else {
        throw new AuthorizationFailureException(messageFactory.forId(CommonErrors.SECURITY_CREDENTIALS_INVALID));
      }
    } else {
      return true;
    }
  }

  private boolean validateSecurityDetailsWithJWT(HttpServletRequest request, Object handler) {
    SecuredObject so = ((HandlerMethod) handler).getMethodAnnotation(SecuredObject.class);

    // JWT tokens validation requires a SecuredObject.
    // If client is not sending an AOSTOKEN and there is no SO annotation on the
    // controller, the token is invalid.
    if (so == null) {
      log.warn(NULL_SECURED_OBJECT);
      throw new AuthorizationFailureException(messageFactory.forId(CommonErrors.SECURITY_CREDENTIALS_INVALID));
    }

    return authService.validateSecurityDetailsWithJWT(getJwtStringFromHeader(request), so.name());
  }

  private String getJwtStringFromHeader(HttpServletRequest request) {
    String jwtString = request.getHeader(AUTHORIZATION);
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(BEARER)) {
      jwtString = bearerToken.substring(BEARER.length());
    }
    return jwtString;
  }

  private boolean validateSecurityDetailsWithAppSec(HttpServletRequest request, Object handler) {
    ServiceSecurityValidator ssf = ServiceSecurityValidatorFactory.getInstance().getServiceSecurityValidator();
    AppSecCredential serviceAccountCredential = new AppSecCredential();
    Token token = new Token(request.getHeader(AUTHORIZATION));
    serviceAccountCredential.setToken(token);
    serviceAccountCredential.setAppStaticId(authServiceAppStaticId);
    serviceAccountCredential.setCallingApplicationStaticId(authServiceAppStaticId);

    try {
      HandlerMethod hm = (HandlerMethod) handler;
      SecuredObject so = hm.getMethodAnnotation(SecuredObject.class);

      // If the method handling this request has the SecuredObject
      // annotation validate with that object, otherwise just validate access to the
      // application
      if (so != null) {
        AppSecServicePermission serviceAccessPermission = new AppSecServicePermission();
        serviceAccessPermission.setSecuredObjectStaticId(so.value());
        ssf.validateServiceSecurity(serviceAccountCredential, serviceAccessPermission);
      } else {
        ssf.validateServiceSecurity(serviceAccountCredential);
      }
      return true;
    } catch (ServiceAccessDeniedException sade) {
      log.warn(AuthService.AUTHORIZATION_FAILURE);
      throw new AuthorizationFailureException(messageFactory.forId(CommonErrors.SECURITY_CREDENTIALS_INVALID), sade);
    }
  }

  private void validateRequestType(HttpServletRequest request) {
    String method = request.getMethod();
    if (!ALLOWED_VERBS.contains(method)) {
      String errorDetails = String.format(INVALID_AUTHORIZATION_REQUEST, method, request.getServletPath());
      log.warn(errorDetails);

      throw new InvalidAuthorizationRequestException(
          messageFactory.forId(CommonErrors.SECURITY_CREDENTIALS_INVALID).withSupportInfo(errorDetails));
    }
  }

}