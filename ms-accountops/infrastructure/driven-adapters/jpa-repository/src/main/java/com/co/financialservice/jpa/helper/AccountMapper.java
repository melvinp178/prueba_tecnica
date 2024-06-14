package com.co.financialservice.jpa.helper;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.model.accountmodel.AccountModel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AccountMapper {
    public AccountModel mapToModel(AccountEntity accountEntity) {
        return AccountModel.builder()
                .accountId(accountEntity.getAccountId())
                .clientId(accountEntity.getClientId())
                .estado(accountEntity.getEstado())
                .tipoCuenta(accountEntity.getTipoCuenta())
                .saldoInicial(accountEntity.getSaldoInicial())
                .saldoDisponible(accountEntity.getSaldoDisponible())
                .numeroCuenta(accountEntity.getNumeroCuenta())
                .transacciones(accountEntity.getTransacciones().stream()
                        .map(transaccion -> transaccion.getTransaccionId())
                        .collect(Collectors.toList()))
                .build();
    }
}
