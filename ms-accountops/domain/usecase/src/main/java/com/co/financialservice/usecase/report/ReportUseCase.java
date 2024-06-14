package com.co.financialservice.usecase.report;

import com.co.financialservice.model.report.Report;
import com.co.financialservice.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportRepository reportRepository;

    public Flux<Report> generarReporteEstadoCuenta(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {
        return Flux.fromIterable(reportRepository.generarReporteEstadoCuenta(fechaInicio, fechaFin, clienteId))
                .switchIfEmpty(Flux.error(new RuntimeException("No se encontraron registros")));
    }
}
