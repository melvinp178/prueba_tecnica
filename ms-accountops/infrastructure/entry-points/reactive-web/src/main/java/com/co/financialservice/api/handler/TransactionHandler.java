package com.co.financialservice.api.handler;

import com.co.financialservice.api.dto.TransactionDto;
import com.co.financialservice.model.accountmodel.AccountModel;
import com.co.financialservice.model.transactionmodel.TransactionModel;
import com.co.financialservice.usecase.transaction.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

    private final TransactionUseCase transactionUseCase;

    public Mono<ServerResponse> getTransaction (ServerRequest serverRequest) {
        Long transactionId;
        try {
            transactionId = getTransactionId(serverRequest);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return transactionUseCase.getTransactionById(transactionId)
                .flatMap(transaction -> ServerResponse.ok().body(Mono.just(transaction), TransactionDto.class))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> createTransaction(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TransactionDto.class)
                .flatMap(transactionDto -> transactionUseCase.saveTransaction(transactionBuildRequest(transactionDto).block()))
                .flatMap(transaction -> ServerResponse.ok().body(Mono.just(transaction), TransactionDto.class))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> updateTransaction(ServerRequest serverRequest) {
        Long transactionId;
        try {
            transactionId = getTransactionId(serverRequest);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return serverRequest.bodyToMono(TransactionDto.class)
                .flatMap(transactionDto -> transactionUseCase.updateTransaction(transactionId, transactionBuildRequest(transactionDto).block()))
                .flatMap(transaction -> ServerResponse.ok().body(Mono.just(transaction), TransactionDto.class))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> deleteTransaction(ServerRequest serverRequest) {
        Long transactionId;
        try {
            transactionId = getTransactionId(serverRequest);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return transactionUseCase.deleteTransaction(transactionId)
                .flatMap(transaction -> ServerResponse.ok().body(Mono.just(transaction), TransactionDto.class))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getAllTransactionsByAccountId(ServerRequest serverRequest) {
        Long accountId;
        try {
            accountId = getTransactionId(serverRequest);
        } catch (RuntimeException e) {
            return Mono.just(ServerResponse.badRequest().bodyValue(e.getMessage()).block());
        }
        return transactionUseCase.getAllTransactionsByAccountId(accountId)
                .collectList()
                .flatMap(transactions -> ServerResponse.ok().bodyValue(transactions.stream()
                        .map(transaction -> transactionBuildResponse(transaction).block())
                        .collect(Collectors.toList()))
                ).onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }



    private Mono<TransactionModel> transactionBuildRequest(TransactionDto transactionDto) {
        return Mono.just(TransactionModel.builder()
                .tipoMovimiento(transactionDto.tipoMovimiento())
                        .cuentaId(transactionDto.cuentaId())
                .valor(transactionDto.valor())
                .fecha(transactionDto.fecha())
                .saldo(transactionDto.saldo())
                .build());
    }

    private Mono<TransactionDto> transactionBuildResponse(TransactionModel transactionModel) {
        return Mono.just(TransactionDto.builder()
                .transaccionId(transactionModel.getTransaccionId())
                .tipoMovimiento(transactionModel.getTipoMovimiento())
                .cuentaId(transactionModel.getCuentaId())
                .valor(transactionModel.getValor())
                .fecha(transactionModel.getFecha())
                .saldo(transactionModel.getSaldo())
                .build());
    }

    private Long getTransactionId(ServerRequest serverRequest) {
        try {
            return Long.valueOf(serverRequest.pathVariable("id"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("El formato del id no es correcto");
        }
    }
}
