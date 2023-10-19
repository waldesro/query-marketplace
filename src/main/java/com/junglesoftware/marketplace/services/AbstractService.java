package com.junglesoftware.marketplace.services;

import com.junglesoftware.marketplace.common.request.CommonDomainRQ;
import com.junglesoftware.marketplace.common.request.RequestContext;
import com.junglesoftware.marketplace.common.response.CommonDomainRS;
import com.junglesoftware.marketplace.common.response.MessageFactory;
import com.junglesoftware.marketplace.common.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@Slf4j
public abstract class AbstractService <T_INPUT extends CommonDomainRQ, T_OUTPUT extends CommonDomainRS>{
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private MessageFactory messageFactory;
    @Autowired
    private Validator validator;
    @Autowired
    private MessageProcess

    public final T_OUTPUT process(T_INPUT request, HttpServletResponse servletResponse) throws Exception {
        populateRequestContext(request);
        T_OUTPUT response = initializeResponse(request);

        return response;
    }

    private T_OUTPUT initializeResponse(T_INPUT request) {
        T_OUTPUT response = createResponse(request);
        response.setMessageFactory(messageFactory);
        return response;
    }

    protected abstract T_OUTPUT createResponse(T_INPUT request);

    public T_OUTPUT validateRequest(T_INPUT request, T_OUTPUT response, Class<?>... validationGroups) {
        Set<ConstraintViolation<T_INPUT>> violations = validator.validate(request, validationGroups);
        Set<ConstraintViolation<T_INPUT>> warnings = ValidatorFactory.extractWarnings(violations);
        response.addBusinessMessage(me);
    }

    private void populateRequestContext(T_INPUT request) {
        if (AopUtils.isAopProxy(requestContext) && requestContext instanceof Advised advised) {
            try {
                RequestContext unwrappedRequestContext = (RequestContext) advised.getTargetSource().getTarget();
                request.setRequestContext(unwrappedRequestContext);
            } catch (Exception e) {
                log.warn("Error unwrapping RequestContext", e);
            }
        } else {
            request.setRequestContext(requestContext);
        }
    }
}
