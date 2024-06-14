package com.co.financialservice.model.clientmodel.gateways;

import com.co.financialservice.model.clientmodel.ClientModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientModelRepository {
    Mono<ClientModel> getClientById(Long clientId);
    Flux<ClientModel> getAllClients();
    Mono<ClientModel> saveClient(ClientModel client);
    Mono<Void> deleteClient(Long clientId);
    Mono<ClientModel> updateClient(Long clientId, ClientModel client);
}
