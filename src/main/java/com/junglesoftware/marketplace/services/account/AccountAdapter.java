package com.junglesoftware.marketplace.services.account;

import com.junglesoftware.marketplace.dto.account.AccountDTO;
import com.junglesoftware.marketplace.dto.account.CreateAccountRequest;
import com.junglesoftware.marketplace.model.AccountModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountAdapter {
    public AccountModel fromCreateAccountRequest(CreateAccountRequest request) {
        if (request.getAccountInfo() != null) {
            return fromDTO(request.getAccountInfo());
        }
        return null;
    }
    public AccountModel fromDTO(AccountDTO accountDTO) {
        return AccountModel
                .builder()
                .email(accountDTO.getUser())
                .name(accountDTO.getName() != null? accountDTO.getName() : null)
                .countryISO3(accountDTO.getCountryISO() != null? accountDTO.getCountryISO() : null)
                .build();
    }
}
