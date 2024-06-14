package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.jpa.entity.TransactionEntity;
import com.co.financialservice.model.report.gateways.ReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Object[]> generarReporteEstadoCuenta_(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<AccountEntity> accountRoot = cq.from(AccountEntity.class);
        Join<AccountEntity, TransactionEntity> transactionJoin = accountRoot.join("transacciones", JoinType.LEFT);


        cq.multiselect(
                accountRoot.get("numeroCuenta"),
                accountRoot.get("tipoCuenta"),
                accountRoot.get("saldoInicial"),
                accountRoot.get("saldoDisponible"),
                transactionJoin.get("fecha"),
                transactionJoin.get("tipoMovimiento"),
                transactionJoin.get("valor"),
                transactionJoin.get("saldo")
        );

        Predicate clientePredicate = cb.equal(accountRoot.get("clientId"), clienteId);
        Predicate fechaPredicate = cb.between(transactionJoin.get("fecha"), fechaInicio, fechaFin);
        cq.where(clientePredicate, fechaPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

}
