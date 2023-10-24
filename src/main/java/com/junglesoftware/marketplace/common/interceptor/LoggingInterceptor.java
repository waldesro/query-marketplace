package com.junglesoftware.marketplace.common.interceptor;

import com.erac.services.restframework.logging.PerformanceLogger;
import com.erac.services.restframework.logging.SlaBreachLogger;
import com.erac.services.restframework.logging.SoaLogger;
import com.erac.services.restframework.logging.TransactionLogger;
import com.erac.services.restframework.request.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private TransactionLogger requestResponseLogger;

	@Autowired
	private SoaLogger soaLogger;

	@Autowired
	@Qualifier("perfLogger")
	private PerformanceLogger perfLogger;

	@Autowired
	private SlaBreachLogger slaBreachLogger;

	@Autowired
	private RequestContext requestContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			requestResponseLogger.logRequest(request);

			soaLogger.logRequest(request);
		}
		/* We should always proceed from here to the next intercepter handler. */
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, final HttpServletResponse response, Object handler,
			Exception ex) throws Exception {
		if (handler instanceof HandlerMethod) {
			soaLogger.logResponse(request, response, handler);

			if (requestContext.isLogRqRs()) {
				requestResponseLogger.logRequest(request);
			}

			requestResponseLogger.logResponse(request, response, handler);

			perfLogger.logResponse(request, response, handler);

			slaBreachLogger.logResponse(request, response, handler);
		}
	}

}
