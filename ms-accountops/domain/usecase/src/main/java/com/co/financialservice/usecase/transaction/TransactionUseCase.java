package com.co.financialservice.usecase.transaction;

import com.co.financialservice.model.transactionmodel.TransactionModel;
import com.co.financialservice.model.transactionmodel.gateways.TransactionModelRepository;
import com.co.financialservice.usecase.account.AccountUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TransactionUseCase {

    private final TransactionModelRepository transactionRepository;
    private final AccountUseCase accountUseCase;

    public Mono<TransactionModel> getTransactionById(Long transactionId) {
        return transactionRepository.getTransactionById(transactionId)
                .onErrorResume(error -> Mono.error(new RuntimeException("la transaccion no existe " + transactionId)));
    }

    public Mono<TransactionModel> saveTransaction(TransactionModel transaction) {
        return accountUseCase.getAccountById(transaction.getCuentaId())
                .switchIfEmpty(Mono.error(new RuntimeException("La cuenta no existe")))
                .flatMap(account -> validateTransaction(Mono.just(transaction))
                        .flatMap(transactionModel -> {
                            return updateAccountWhitNewValue(transactionModel)
                                    .then(transactionRepository.saveTransaction(transactionModel));
                        }));
    }

    public Mono<String> deleteTransaction(Long transactionId) {
        return getTransactionById(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("La transaccion con el ID " + transactionId + " no existe")))
                .then(transactionRepository.deleteTransaction(transactionId))
                .then(Mono.just("Transaccion eliminada con éxito"));
    }

    public Mono<TransactionModel> updateTransaction(Long transactionId, TransactionModel transaction) {
        return getTransactionById(transactionId)
                .switchIfEmpty(Mono.error(new RuntimeException("La transaccion con el ID " + transactionId + " no existe")))
                .then(transactionRepository.updateTransaction(transactionId, transaction));
    }

    public Flux<TransactionModel> getAllTransactionsByAccountId(Long accountId) {
        return accountUseCase.getAccountById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("La cuenta no existe")))
                .flatMapMany(account -> transactionRepository.getAllTransactionsByAccountId(accountId))
                .onErrorResume(error -> Flux.error(new RuntimeException("error obteniendo las transacciones")));
    }

    public Mono<Void> updateAccountWhitNewValue( TransactionModel transaction) {
        return validateTransaction(Mono.just(transaction))
                .flatMap(transactionModel -> accountUseCase.UpdateAccountBalance(Mono.just(transactionModel)));

    }

    public Mono<TransactionModel> validateTransaction(Mono<TransactionModel> transaction) {
        return transaction.flatMap(transactionModel -> {
            if (transactionModel.getTipoMovimiento().toUpperCase().equals("DEPOSITO")) {
                if (transactionModel.getValor() <= 0) {
                    return Mono.error(new RuntimeException("El valor del deposito debe ser mayor a 0"));
                } else {
                    return Mono.just(transactionModel);
                }
            } else if (transactionModel.getTipoMovimiento().toUpperCase().equals("RETIRO")) {
                if (transactionModel.getValor() >= 0) {
                    return Mono.error(new RuntimeException("El valor del retiro debe ser un valor negativo"));
                } else {
                    return Mono.just(transactionModel);
                }

            } else {
                return Mono.error(new RuntimeException("Tipo de movimiento no valido, movimientos validos DEPOSITO, RETIRO"));
            }
        });
    }

}
