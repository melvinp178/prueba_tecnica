package com.co.financialservice.api;

import com.co.financialservice.api.handler.AccountHandler;
import com.co.financialservice.api.handler.ReportHandler;
import com.co.financialservice.api.handler.TransactionHandler;
import com.co.financialservice.model.report.Report;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetAccount(AccountHandler handler) {
        return route(GET("/cuentas/{id}"), serverRequest -> handler.getAccount(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionSaveAccount(AccountHandler handler) {
        return route(POST("/cuentas/"), serverRequest -> handler.createAccount(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionUpdatrAccount(AccountHandler handler) {
        return route(PUT("/cuentas/{id}"), serverRequest -> handler.updateAccount(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionDeleteAccount(AccountHandler handler) {
        return route(DELETE("/cuentas/{id}"), serverRequest -> handler.deleteAccount(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetTransaction(TransactionHandler handler) {
        return route(GET("/movimientos/{id}"), handler::getTransaction);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionSaveTransaction(TransactionHandler handler) {
        return route(POST("/movimientos/"), handler::createTransaction);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionUpdateTransaction(TransactionHandler handler) {
        return route(PUT("/movimientos/{id}"), handler::updateTransaction);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionDeleteTransaction(TransactionHandler handler) {
        return route(DELETE("/movimientos/{id}"), handler::deleteTransaction);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetAllTransaction(TransactionHandler handler) {
        return route(GET("/movimientos/all/{id}"), handler::getAllTransactionsByAccountId);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionGetReport(ReportHandler handler) {
        return route(GET("/reportes"), handler::getReport);
    }
}
