package com.junglesoftware.marketplace.services.account;

import com.junglesoftware.marketplace.api.AccountApiDelegate;
import com.junglesoftware.marketplace.dto.AccountInfoRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountApiDelegateImpl implements AccountApiDelegate {

    @Override
    public void createAccount(AccountInfoRequest accountInfoRequest) throws Exception {
        AccountApiDelegate.super.createAccount(accountInfoRequest);
    }
}
