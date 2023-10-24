package com.junglesoftware.marketplace.repository;

import com.junglesoftware.marketplace.model.AccountModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountRepository extends CrudRepository<AccountModel, UUID> {
    int countAccountModelByuuid(UUID id);
}
