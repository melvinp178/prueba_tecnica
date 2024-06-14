package com.co.financialservice.usecase.client;

import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.clientmodel.gateways.ClientModelRepository;
import com.co.financialservice.model.personmodel.PersonModel;
import com.co.financialservice.usecase.person.PersonUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientModelRepository clientModelRepository;
    private final PersonUseCase personUseCase;

    public Mono<ClientModel> getClientById(Long clientId) {
        return clientModelRepository.getClientById(clientId)
                .onErrorResume(error -> Mono.error(new RuntimeException("El cliente con el ID " + clientId + " no existe")));
    }

    public Mono<ClientModel> saveClient(ClientModel client) {
        return validatePerson(client.getPersona())
                .flatMap(person ->{
                 return Mono.<ClientModel>error(new RuntimeException("La persona ya existe"));
                })
                .switchIfEmpty(personUseCase.savePerson(client.getPersona())
                        .flatMap(person ->{
                             client.getPersona().setPersonId(person.getPersonId());
                            return clientModelRepository.saveClient(client).flatMap(
                                    clientModel -> {
                                        return Mono.just(clientModel);
                                    }
                            );
                        }));
    }

    public Mono<String> deleteClient(Long clientId) {
        return getClientById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("El cliente con el ID " + clientId + " no existe")))
                .then(clientModelRepository.deleteClient(clientId))
                        .then(personUseCase.deletePerson(clientId))
                        .then(Mono.just("Cliente eliminado con éxito"));
    }
    public Mono<ClientModel> updateClient(Long clientId,ClientModel updatedClient) {
        return getClientById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("El cliente con el ID " + clientId + " no existe")))
                .then(clientModelRepository.updateClient(clientId, updatedClient));
    }

    Mono<PersonModel> validatePerson(PersonModel person) {
        return personUseCase.getPersonByDocument(person.getIdentificacion());
    }
}
