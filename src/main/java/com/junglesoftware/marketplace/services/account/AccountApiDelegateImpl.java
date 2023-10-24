package com.junglesoftware.marketplace.services.account;

import com.junglesoftware.marketplace.api.account.AccountApiDelegate;
import com.junglesoftware.marketplace.dto.account.CreateAccountRequest;
import com.junglesoftware.marketplace.dto.account.CreateAccountResponse;
import com.junglesoftware.marketplace.dto.account.DeleteAccountResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class AccountApiDelegateImpl implements AccountApiDelegate {

    private final AccountCreateService accountCreateService;

    private final NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(nativeWebRequest);
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) throws Exception {
        return accountCreateService.process(createAccountRequest);
    }

    @Override
    public DeleteAccountResponse deleteAccount(UUID accountId) throws Exception {
        return AccountApiDelegate.super.deleteAccount(accountId);
    }
}
