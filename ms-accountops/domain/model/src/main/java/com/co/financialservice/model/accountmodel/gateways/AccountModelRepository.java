package com.co.financialservice.model.accountmodel.gateways;

import com.co.financialservice.model.accountmodel.AccountModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountModelRepository {
    Mono<AccountModel> getAccountById(Long accountId);
    Flux<AccountModel> getAllAccounts();
    Mono<AccountModel> saveAccount(AccountModel account);
    Mono<Void> deleteAccount(Long accountId);
    Mono<AccountModel> updateAccount(Long accountId, AccountModel account);
    Mono<AccountModel> getAccountByNumeroCuenta(String accountNumber);


}
