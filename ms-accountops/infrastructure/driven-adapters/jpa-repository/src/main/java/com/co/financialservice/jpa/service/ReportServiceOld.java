package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.jpa.entity.TransactionEntity;
import com.co.financialservice.jpa.repository.AccountRepository;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.report.Report;
import com.co.financialservice.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportServiceOld implements ReportRepository {
    private final AccountRepository accountRepository;
    private final EventsService eventsService;

    @Override
    public List<Report> generarReporteEstadoCuenta(LocalDate fechaInicio, LocalDate fechaFin, Long clienteId) {

        ClientModel cliente = eventsService.getClient(clienteId).block();
        List<AccountEntity> cuentas = accountRepository.findByClientId(clienteId);
        List<TransactionEntity> transacciones = accountRepository.findTransactionsByDateRangeAndClient(fechaInicio, fechaFin, clienteId);

        List<Report> reportes = new ArrayList<>();

        for (AccountEntity cuenta : cuentas) {

            List<TransactionEntity> transaccionesCuenta = transacciones.stream()
                    .filter(transaccion -> transaccion.getCuenta().getAccountId().equals(cuenta.getAccountId()))
                    .collect(Collectors.toList());

            for (TransactionEntity transaccion : transaccionesCuenta) {

                Report reporte = Report.builder()
                        .fecha(transaccion.getFecha())
                        .cliente(cliente.getPersona().getNombre())
                        .numeroCuenta(cuenta.getNumeroCuenta())
                        .tipo(cuenta.getTipoCuenta())
                        .saldoInicial(cuenta.getSaldoInicial())
                        .estado("true".equals(cuenta.getEstado()))
                        .movimiento(transaccion.getValor())
                        .saldoDisponible(transaccion.getSaldo())
                        .build();

                reportes.add(reporte);
            }
        }

        return reportes;
    }

}
