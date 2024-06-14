package com.co.financialservice.api.handler;

import com.co.financialservice.api.dto.ReportDto;
import com.co.financialservice.model.report.Report;
import com.co.financialservice.usecase.report.ReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportHandler {
    private final ReportUseCase reportUseCase;



    public Mono<ServerResponse> getReport(ServerRequest serverRequest) {
        Long clientId = Long.valueOf(serverRequest.queryParam("clientId").orElse("0"));
        LocalDate fechaInicio = LocalDate.parse(serverRequest.queryParam("fechaInicio").orElse(LocalDate.now().toString()));
        LocalDate fechaFin = LocalDate.parse(serverRequest.queryParam("fechaFin").orElse(LocalDate.now().toString()));
        return reportUseCase.generarReporteEstadoCuenta(fechaInicio, fechaFin, clientId)
                .collectList()
                .flatMap(reports -> ServerResponse.ok().bodyValue(reports.stream()
                        .map(report -> reportDtoMono(report).block())
                        .collect(Collectors.toList()))
                ).onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    private Mono<ReportDto> reportDtoMono(Report report) {
        return Mono.just(ReportDto.builder()
                .fecha(report.getFecha())
                .cliente(report.getCliente())
                .numeroCuenta(report.getNumeroCuenta())
                .tipo(report.getTipo())
                .saldoInicial(report.getSaldoInicial())
                .estado(report.isEstado())
                .movimiento(report.getMovimiento())
                .saldoDisponible(report.getSaldoDisponible())
                .build());
    }
}
