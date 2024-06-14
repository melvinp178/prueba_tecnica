package com.co.financialservice.api.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TransactionDto(Long transaccionId, LocalDate fecha, String tipoMovimiento, double valor, double saldo, Long cuentaId) {
}