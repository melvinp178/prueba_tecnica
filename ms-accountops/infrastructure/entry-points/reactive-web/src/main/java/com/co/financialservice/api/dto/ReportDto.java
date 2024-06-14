package com.co.financialservice.api.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReportDto(
        LocalDate fecha,
        String cliente,
        String numeroCuenta,
        String tipo,
        double saldoInicial,
        boolean estado,
        double movimiento,
        double saldoDisponible) {
}
