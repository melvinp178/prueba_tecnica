package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.jpa.entity.TransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository extends CrudRepository<AccountEntity, Long>, QueryByExampleExecutor<AccountEntity> {

    AccountEntity findByNumeroCuenta(String accountNumber);
    List<AccountEntity> findByClientId(Long clientId);
    //AccountEntity findAccountEntitiesWithTransaccionesById(Long accountId);

    @Query("SELECT t FROM TransactionEntity t WHERE t.cuenta.clientId = :clientId AND t.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<TransactionEntity> findTransactionsByDateRangeAndClient(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, @Param("clientId") Long clientId);

}
