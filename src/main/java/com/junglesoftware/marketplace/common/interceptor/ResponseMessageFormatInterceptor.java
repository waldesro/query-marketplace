package com.junglesoftware.marketplace.common.interceptor;

import com.erac.services.restframework.interceptor.annotation.ResponseMessageFormat;
import com.erac.services.restframework.request.RequestContext;
import com.erac.services.restframework.response.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@Slf4j
public class ResponseMessageFormatInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private RequestContext requestContext;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    requestContext.setResponseMessageFormat(getResponseMessageFormat(handler));
    return true;
  }
  
  private MessageFormat getResponseMessageFormat(Object handler) {
    if (handler instanceof HandlerMethod) {
      HandlerMethod hm = (HandlerMethod) handler;
      Method callingMethod = hm.getMethod();
      ResponseMessageFormat responseMessageFormat = getMessageFormatAnnotation(callingMethod);
      
      if (responseMessageFormat != null) {
        return responseMessageFormat.value();
      }
      log.info("ResponseMessageFormat not found for method {}", callingMethod.getName());
    }
    return null;
  }
  
  private ResponseMessageFormat getMessageFormatAnnotation(Method callingMethod) {
    if (callingMethod.isAnnotationPresent(ResponseMessageFormat.class)) {
      return callingMethod.getAnnotation(ResponseMessageFormat.class);
    }
    return callingMethod.getDeclaringClass().getAnnotation(ResponseMessageFormat.class);
  }

}
