package com.junglesoftware.marketplace.common.interceptor;

import com.erac.services.restframework.interceptor.annotation.BusinessMessageInResponseBody;
import com.erac.services.restframework.request.RequestContext;
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
public class BusinessMessageInResponseBodyInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RequestContext requestContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        requestContext.setInBody(isInbody(handler));
        return true;
    }

    private boolean isInbody(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            Method callingMethod = hm.getMethod();
            BusinessMessageInResponseBody businessMessageInResponseBody = getBusinessMessageInResponseBodyAnnotation(callingMethod);

            if (businessMessageInResponseBody != null) {
                return businessMessageInResponseBody.inBody();
            }
            log.info("BusinessMessageInResponseBody not found for method {}", callingMethod.getName());
        }
        return false;
    }

    private BusinessMessageInResponseBody getBusinessMessageInResponseBodyAnnotation(Method callingMethod) {
        if (callingMethod.isAnnotationPresent(BusinessMessageInResponseBody.class)) {
            return callingMethod.getAnnotation(BusinessMessageInResponseBody.class);
        }
        return callingMethod.getDeclaringClass().getAnnotation(BusinessMessageInResponseBody.class);
    }
}
