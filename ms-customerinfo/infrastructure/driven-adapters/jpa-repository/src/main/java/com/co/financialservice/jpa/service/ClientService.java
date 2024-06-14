package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.repository.ClientRepositoryAdapter;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.clientmodel.gateways.ClientModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientModelRepository {

    private final ClientRepositoryAdapter clientRepositoryAdapter;

    @Override
    public Mono<ClientModel> getClientById(Long clientId) {
        return Mono.justOrEmpty(clientRepositoryAdapter.findById(clientId))
                .switchIfEmpty(Mono.error(new RuntimeException("Client not found with id: " + clientId)));
    }

    @Override
    public Flux<ClientModel> getAllClients() {
        return Flux.fromIterable(clientRepositoryAdapter.findAll());
    }
    @Transactional
    @Override
    public Mono<ClientModel> saveClient(ClientModel client) {
        return Mono.just(clientRepositoryAdapter.save(client))
                .flatMap(clientModel -> {
                    return Mono.just(clientModel);})
                            .onErrorResume(e -> Mono.error(new RuntimeException("Error saving client: " + e.getMessage())));

    }


    @Transactional
    @Override
    public Mono<Void> deleteClient(Long clientId) {
        return Mono.defer(() -> {
            clientRepositoryAdapter.deleteByClienteId(clientId);
            return Mono.empty();
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
    @Transactional
    @Override
    public Mono<ClientModel> updateClient(Long clientId, ClientModel client) {
        return Mono.just(clientRepositoryAdapter.updateByClienteId(clientId, client));
    }
}