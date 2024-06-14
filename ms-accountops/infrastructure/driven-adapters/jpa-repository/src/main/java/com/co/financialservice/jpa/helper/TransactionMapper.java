package com.co.financialservice.jpa.helper;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.jpa.entity.TransactionEntity;
import com.co.financialservice.model.accountmodel.AccountModel;
import com.co.financialservice.model.transactionmodel.TransactionModel;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapper {

    public TransactionModel mapToModel(TransactionEntity transactionEntity) {
        return TransactionModel.builder()
                .transaccionId(transactionEntity.getTransaccionId())
                .fecha(transactionEntity.getFecha())
                .tipoMovimiento(transactionEntity.getTipoMovimiento())
                .valor(transactionEntity.getValor())
                .saldo(transactionEntity.getSaldo())
                .cuentaId(transactionEntity.getCuenta().getAccountId())
                .build();
    }
    public TransactionEntity mapToEntity(TransactionModel transactionModel) {
        return TransactionEntity.builder()
                .transaccionId(transactionModel.getTransaccionId())
                .fecha(transactionModel.getFecha())
                .tipoMovimiento(transactionModel.getTipoMovimiento())
                .valor(transactionModel.getValor())
                .saldo(transactionModel.getSaldo())
                .cuenta(AccountEntity.builder().accountId(transactionModel.getCuentaId()).build())
                .build();
    }
}
