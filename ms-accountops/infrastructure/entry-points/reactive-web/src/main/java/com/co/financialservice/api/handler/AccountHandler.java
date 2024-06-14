package com.co.financialservice.api.handler;

import com.co.financialservice.api.dto.AccountDto;
import com.co.financialservice.model.accountmodel.AccountModel;
import com.co.financialservice.usecase.account.AccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountUseCase accountUseCase;

        public Mono<ServerResponse> getAccount(ServerRequest serverRequest) {
            Long accountId;
            try {
                accountId =getAccountId(serverRequest);
            } catch (RuntimeException e) {
                return ServerResponse.badRequest().bodyValue(e.getMessage());
            }

            return accountUseCase.getAccountById(accountId)
                    .flatMap(this::accountBuildResponse)
                    .flatMap(account -> ServerResponse.ok().body(Mono.just(account), AccountDto.class))
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
        }

        public Mono<ServerResponse> createAccount(ServerRequest serverRequest) {
            return serverRequest.bodyToMono(AccountDto.class)
                    .flatMap(accountDto -> accountUseCase.saveAccount(accountBuildRequest(accountDto).block()))
                    .flatMap(this::accountBuildResponse)
                    .flatMap(account -> ServerResponse.ok().body(Mono.just(account), AccountDto.class))
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
        }

        public Mono<ServerResponse> updateAccount(ServerRequest serverRequest) {
            Long accountId;
            try {
                accountId = getAccountId(serverRequest);
            } catch (RuntimeException e) {
                return ServerResponse.badRequest().bodyValue(e.getMessage());
            }

            return serverRequest.bodyToMono(AccountDto.class)
                    .flatMap(accountDto -> accountUseCase.updateAccount(accountId, accountBuildRequest(accountDto).block()))
                    .flatMap(this::accountBuildResponse)
                    .flatMap(account -> ServerResponse.ok().body(Mono.just(account), AccountDto.class))
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
        }

        public Mono<ServerResponse> deleteAccount(ServerRequest serverRequest) {
            Long accountId;
            try {
                accountId = getAccountId(serverRequest);
            } catch (RuntimeException e) {
                return ServerResponse.badRequest().bodyValue(e.getMessage());
            }

            return accountUseCase.deleteAccount(accountId)
                    .flatMap(msg -> ServerResponse.ok().bodyValue(msg))
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
        }

        private Long getAccountId(ServerRequest serverRequest) {
            try {
                return Long.valueOf(serverRequest.pathVariable("id"));
            } catch (NumberFormatException e) {
                throw new RuntimeException("El formato del id no es correcto");
            }
        }

        private Mono<AccountDto> accountBuildResponse(AccountModel accountDto) {
            return Mono.just(AccountDto.builder()
                    .accountId(accountDto.getAccountId())
                            .clientId(accountDto.getClientId())
                            .nombreCliente(accountDto.getNombreCliente())
                            .estado(accountDto.getEstado())
                            .tipoCuenta(accountDto.getTipoCuenta())
                            .saldoInicial(accountDto.getSaldoInicial())
                            .saldoDisponible(accountDto.getSaldoDisponible())
                            .numeroCuenta(accountDto.getNumeroCuenta())
                    .build());
        }

        private Mono<AccountModel> accountBuildRequest(AccountDto accountDto) {
            return Mono.just(AccountModel.builder()
                            .estado(accountDto.estado())
                            .tipoCuenta(accountDto.tipoCuenta())
                            .saldoInicial(accountDto.saldoInicial())
                            .saldoDisponible(accountDto.saldoDisponible())
                            .numeroCuenta(accountDto.numeroCuenta())
                    .clientId(accountDto.clientId())
                    .build());
        }


}
