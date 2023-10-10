package com.junglesoftware.marketplace.services.query;

import com.junglesoftware.marketplace.api.AuthorizationApiDelegate;
import com.junglesoftware.marketplace.dto.AuthorizationRequest;
import com.junglesoftware.marketplace.dto.AuthorizationResponse;

public class AuthorizationApiDelegateImpl implements AuthorizationApiDelegate {
    @Override
    public AuthorizationResponse authorizeClient(String registrationKey, AuthorizationRequest authorizationRequest) throws Exception {
        return AuthorizationApiDelegate.super.authorizeClient(registrationKey, authorizationRequest);
    }
}
