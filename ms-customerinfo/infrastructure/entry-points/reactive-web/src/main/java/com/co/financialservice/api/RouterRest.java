package com.co.financialservice.api;

import com.co.financialservice.api.handler.Client;
import lombok.Data;
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
    public RouterFunction<ServerResponse> routerFunctionGetClient(Client handler) {
        return route(GET("/client/{id}"), serverRequest -> handler.getClient(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionSaveClient(Client handler) {
        return route(POST("/client"), serverRequest -> handler.createClient(serverRequest));
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunctionRemoveClient(Client handler) {
        return route(DELETE("/client/{id}"), serverRequest -> handler.deleteClient(serverRequest));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionUpdateClient(Client handler) {
        return route(PUT("/client/{id}"), serverRequest -> handler.updateClient(serverRequest));
    }
}