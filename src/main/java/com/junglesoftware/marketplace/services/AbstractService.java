package com.junglesoftware.marketplace.services;

import com.junglesoftware.marketplace.common.request.CommonDomainRQ;
import com.junglesoftware.marketplace.common.request.RequestContext;
import com.junglesoftware.marketplace.common.response.CommonDomainRS;
import com.junglesoftware.marketplace.common.response.MessageFactory;
import com.junglesoftware.marketplace.common.response.MessageProcessor;
import com.junglesoftware.marketplace.common.validation.ValidatorFactory;
import jakarta.validation.groups.Default;
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
    private MessageProcessor messageProcessor;

    public final T_OUTPUT process(T_INPUT request) throws Exception {
        populateRequestContext(request);
        T_OUTPUT response = initializeResponse(request);
        if (response.isValid()) {
            response = preProcessRequest(request, response);
            if (response.isValid()) {
                response = performBusinessLogic(request, response);
                if (response.isValid()) {
                    response = postProcessResponse(request, response);
                }
            }
        }
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
        response.addBusinessMessages(messageProcessor.process(violations, warnings));
        return response;
    }

    public T_OUTPUT validateResponse(T_OUTPUT response, Class<?>... validationGroups) {
        Set<ConstraintViolation<T_OUTPUT>> violations = validator.validate(response, validationGroups);
        Set<ConstraintViolation<T_OUTPUT>> warnings = ValidatorFactory.extractWarnings(violations);
        response.addBusinessMessages(messageProcessor.process(violations, warnings));
        return response;
    }

    protected T_OUTPUT preProcessRequest(T_INPUT request, T_OUTPUT response) throws Exception {
        validateRequest(request, response, Default.class);
        return response;
    }

    protected T_OUTPUT performBusinessLogic(T_INPUT request, T_OUTPUT response) throws Exception {
        return response;
    }

    protected T_OUTPUT postProcessResponse(T_INPUT request, T_OUTPUT response) throws Exception {
        validateResponse(response, Default.class);
        return response;
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
