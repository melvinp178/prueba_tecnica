package com.co.financialservice.model.report.gateways;

import com.co.financialservice.model.report.Report;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository {
    List<Report> generarReporteEstadoCuenta(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId);
}
