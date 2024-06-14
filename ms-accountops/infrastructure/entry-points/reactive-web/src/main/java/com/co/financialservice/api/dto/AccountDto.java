package com.co.financialservice.api.dto;

import lombok.Builder;

@Builder
public record AccountDto(Long accountId,String nombreCliente, String tipoCuenta, String numeroCuenta, String estado , double saldoInicial, Long clientId, double saldoDisponible) {
}
