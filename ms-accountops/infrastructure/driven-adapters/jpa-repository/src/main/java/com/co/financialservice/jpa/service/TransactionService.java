package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.repository.TransactionRepositoryAdapter;
import com.co.financialservice.model.transactionmodel.TransactionModel;
import com.co.financialservice.model.transactionmodel.gateways.TransactionModelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionModelRepository {

    public final TransactionRepositoryAdapter transactionRepositoryAdapter;
    @Override
    public Mono<TransactionModel> getTransactionById(Long transactionId) {
        return Mono.justOrEmpty(transactionRepositoryAdapter.findByTransaccionId(transactionId))
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found with id: " + transactionId)))
                .onErrorResume(e -> Mono.error(new RuntimeException("error obteniendo la transacción")));

    }

    @Transactional
    @Override
    public Mono<TransactionModel> saveTransaction(TransactionModel transaction) {
        return Mono.just(transactionRepositoryAdapter.save(transaction))
                .onErrorResume(e -> Mono.error(new RuntimeException("error guardando la transacción")));
    }

    @Transactional
    @Override
    public Mono<Void> deleteTransaction(Long transactionId) {
        return Mono.defer(() -> {
            try {
            transactionRepositoryAdapter.deleteByTransactionId(transactionId);
            return Mono.empty();
            } catch (Exception e) {
                return Mono.error(new RuntimeException("error eliminando la transacción " + transactionId));
            }
        }).then();
    }

    @Transactional
    @Override
    public Mono<TransactionModel> updateTransaction(Long transactionId, TransactionModel transaction) {
        return Mono.just(transactionRepositoryAdapter.updateByTransactionId(transactionId, transaction))
                .onErrorResume(e -> Mono.error(new RuntimeException("error actualizando la transacción")));
    }

    @Override
    public Flux<TransactionModel> getAllTransactionsByAccountId(Long accountId) {
        return Flux.fromIterable(transactionRepositoryAdapter.findAllTransactionByAccountId(accountId))
                .switchIfEmpty(Flux.error(new RuntimeException("No hay transacciones disponibles")))
                .onErrorResume(e -> Flux.error(new RuntimeException("error obteniendo las transacciones")));
    }
}
