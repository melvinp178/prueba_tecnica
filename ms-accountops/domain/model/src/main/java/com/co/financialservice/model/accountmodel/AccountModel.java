package com.co.financialservice.model.accountmodel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountModel {
    private Long accountId;
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private double saldoDisponible;
    private String estado;
    private Long clientId;
    private String nombreCliente;
    private List<Long> transacciones;
}
