package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.repository.AccountRepositoryAdapter;
import com.co.financialservice.model.accountmodel.AccountModel;
import com.co.financialservice.model.accountmodel.gateways.AccountModelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountModelRepository {

    public final AccountRepositoryAdapter accountRepositoryAdapter;
    public final EventsService eventsService;
    @Override
    public Mono<AccountModel> getAccountById(Long accountId) {
        return Mono.justOrEmpty(accountRepositoryAdapter.findByAccountId(accountId))
                .switchIfEmpty(Mono.error(new RuntimeException("Cuenta no existe " + accountId)))
                .flatMap(accountModel -> accountModel.getEstado().equals("true") ? Mono.just(accountModel) : Mono.error(new RuntimeException("Account is inactive")))
                .onErrorResume(e -> Mono.error(new RuntimeException("error obteniendo la cuenta")));
    }

    @Override
    public Flux<AccountModel> getAllAccounts() {
        return Flux.fromIterable(accountRepositoryAdapter.findAll())
                .onErrorResume(e -> Flux.error(new RuntimeException("error obteniendo las cuentas")));
    }

    @Transactional
    @Override
    public Mono<AccountModel> saveAccount(AccountModel account) {
        eventsService.getClient(account.getClientId());
        return Mono.just(accountRepositoryAdapter.save(account))
                .onErrorResume(e -> Mono.error(new RuntimeException("error guardando la cuenta")));
    }

    @Transactional
    @Override
    public Mono<Void> deleteAccount(Long accountId) {
        return Mono.defer(() -> {
            try {
            accountRepositoryAdapter.deleteByAccountId(accountId);
            return Mono.empty();
            } catch (Exception e) {
                return Mono.error(new RuntimeException("error eliminando la cuenta " + accountId));
            }
        }).then();
    }

    @Transactional
    @Override
    public Mono<AccountModel> updateAccount(Long accountId, AccountModel account) {
        return Mono.just(accountRepositoryAdapter.updateByAccountId(accountId, account))
                .onErrorResume(e -> Mono.error(new RuntimeException("error actualizando la cuenta")));
    }

    @Override
    public Mono<AccountModel> getAccountByNumeroCuenta(String accountNumber) {
        return Mono.justOrEmpty(accountRepositoryAdapter.findByAccountNumber(accountNumber))
                .flatMap(accountModel -> accountModel.getEstado().equals("true") ? Mono.just(accountModel) : Mono.error(new RuntimeException("Account is inactive")))
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())));
    }
}
