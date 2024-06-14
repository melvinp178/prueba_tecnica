package com.co.financialservice.usecase.account;

import com.co.financialservice.model.accountmodel.AccountModel;
import com.co.financialservice.model.accountmodel.gateways.AccountModelRepository;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.clientmodel.gateways.ClientModelRepository;
import com.co.financialservice.model.transactionmodel.TransactionModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountModelRepository accountRepository;
    private final ClientModelRepository clientRepository;

    public Mono<AccountModel> getAccountById(Long accountId) {
        return accountRepository.getAccountById(accountId)
                .flatMap(accountModel ->clientRepository.getClient(accountModel.getClientId())
                        .flatMap(clientModel -> {
                            accountModel.setNombreCliente(clientModel.getPersona().getNombre());
                            return Mono.just(accountModel);
                        })
                .onErrorResume(error -> Mono.error(new RuntimeException("la cuentano existe " + accountId))));
    }

    public Mono<AccountModel> saveAccount(AccountModel account) {
        return validateAccount(account)
                .flatMap(accountModel -> Mono.<AccountModel>error(new RuntimeException("La cuenta ya existe")))
                .switchIfEmpty(Mono.defer(() -> accountRepository.saveAccount(account)))
                .flatMap(accountModel ->clientRepository.getClient(accountModel.getClientId())
                .flatMap(clientModel -> {
                    accountModel.setNombreCliente(clientModel.getPersona().getNombre());
                    return Mono.just(accountModel);
                })
                .onErrorResume(error -> Mono.error(new RuntimeException("la cuentano existe " + account.getAccountId()))));
    }

    public Mono<String> deleteAccount(Long accountId) {
        return getAccountById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("La cuenta con el ID " + accountId + " no existe")))
                .then(accountRepository.deleteAccount(accountId))
                .then(Mono.just("Cuenta eliminada con éxito"));
    }

    public Mono<AccountModel> updateAccount(Long accountId, AccountModel account) {
        return getAccountById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("La cuenta con el ID " + accountId + " no existe")))
                .then(accountRepository.updateAccount(accountId, account))
                .flatMap(accountModel ->clientRepository.getClient(accountModel.getClientId())
                        .flatMap(clientModel -> {
                            accountModel.setNombreCliente(clientModel.getPersona().getNombre());
                            return Mono.just(accountModel);
                        })
                        .onErrorResume(error -> Mono.error(new RuntimeException("la cuentano existe " + accountId))));
    }

    public Mono<Void>UpdateAccountBalance(Mono<TransactionModel> transaction) {
        return transaction.flatMap(transactionModel -> {
            return accountRepository.getAccountById(transactionModel.getCuentaId())
                    .flatMap(account -> {
                        if (transactionModel.getTipoMovimiento().toUpperCase().equals("DEPOSITO")) {
                            account.setSaldoDisponible(account.getSaldoDisponible()+ transactionModel.getValor()) ;
                        } else if (transactionModel.getTipoMovimiento().toUpperCase().equals("RETIRO")){
                            if(account.getSaldoDisponible()+transactionModel.getValor() < 0){
                                return Mono.error(new RuntimeException("Saldo no disponible"));
                            }account.setSaldoDisponible(account.getSaldoDisponible()+ transactionModel.getValor());
                        }
                        return accountRepository.updateAccount(account.getAccountId(), account);
                    }).then();
        });
    }

    Mono<AccountModel> validateAccount(AccountModel account) {
        return accountRepository.getAccountByNumeroCuenta(account.getNumeroCuenta());
    }
}
