package com.junglesoftware.marketplace.common.exception;

import com.erac.services.restframework.response.Message;

import java.util.List;

public class AuthorizationFailureException extends MessageAwareException {

	private static final long serialVersionUID = 8001369742210538152L;

	public AuthorizationFailureException() {
	}

	/**
	 * Instantiates a new AuthorizationFailureException.
	 *
	 * @param messages
	 *            the messages
	 */
	public AuthorizationFailureException(List<Message> messages) {
		super(messages);
	}

	/**
	 * Instantiates a new AuthorizationFailureException.
	 *
	 * @param message
	 *            the messages
	 */
	public AuthorizationFailureException(Message message) {
		super(message);
	}

	public AuthorizationFailureException(Message message, Exception cause) {
		super(message);
		initCause(cause);
	}
}
