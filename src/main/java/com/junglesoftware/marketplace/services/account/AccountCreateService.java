package com.junglesoftware.marketplace.services.account;

import com.junglesoftware.marketplace.dto.account.CreateAccountRequest;
import com.junglesoftware.marketplace.dto.account.CreateAccountResponse;
import com.junglesoftware.marketplace.model.AccountModel;
import com.junglesoftware.marketplace.repository.AccountRepository;
import com.junglesoftware.marketplace.services.AbstractService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountCreateService extends AbstractService<CreateAccountRequest, CreateAccountResponse> {

    private final AccountAdapter accountAdapter;
    private final AccountRepository accountRepository;
    @Override
    protected CreateAccountResponse createResponse(CreateAccountRequest request) {
        return new CreateAccountResponse();
    }

    @Override
    protected CreateAccountResponse preProcessRequest(CreateAccountRequest request, CreateAccountResponse response) throws Exception {
        return super.preProcessRequest(request, response);
    }

    @Override
    protected CreateAccountResponse performBusinessLogic(CreateAccountRequest request, CreateAccountResponse response) throws Exception {
        AccountModel accountModel = accountAdapter.fromCreateAccountRequest(request);
        accountRepository.save(accountModel);
        return super.performBusinessLogic(request, response);
    }

}
