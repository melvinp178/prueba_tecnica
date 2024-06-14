package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long>, QueryByExampleExecutor<TransactionEntity> {

    void deleteByTransaccionId(Long transactionId);
    List<Optional<TransactionEntity>> findAllByCuenta_AccountId(Long accountId);
}
