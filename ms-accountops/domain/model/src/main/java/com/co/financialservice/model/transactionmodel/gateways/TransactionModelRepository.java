package com.co.financialservice.model.transactionmodel.gateways;

import com.co.financialservice.model.transactionmodel.TransactionModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionModelRepository {

    Mono<TransactionModel> getTransactionById(Long transactionId);
    Mono<TransactionModel> saveTransaction(TransactionModel transaction);
    Mono<Void> deleteTransaction(Long transactionId);
    Mono<TransactionModel> updateTransaction(Long transactionId, TransactionModel transaction);
    Flux<TransactionModel> getAllTransactionsByAccountId(Long accountId);
}
