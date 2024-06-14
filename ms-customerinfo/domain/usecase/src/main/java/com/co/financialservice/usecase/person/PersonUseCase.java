package com.co.financialservice.usecase.person;

import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.personmodel.PersonModel;
import com.co.financialservice.model.personmodel.gateways.PersonModelRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonUseCase {
    private final PersonModelRepository personModelRepository;

    public Mono<PersonModel> getPersonByDocument(String document) {
        return personModelRepository.getPersonByDocument(document);
    }


    public Mono<PersonModel> savePerson(PersonModel person) {
        return personModelRepository.savePerson(person);
    }

    public Mono<Void> deletePerson(Long personId) {
        return personModelRepository.deletePerson(personId);
    }

//    public Mono<PersonModel> updatePerson(Long personId, PersonModel person) {
//        return personModelRepository.(personId, person);
//    }
//public Mono<ClientModel> updateClient(Long clientId, ClientModel updatedClient) {
//    return getClientById(clientId)
//            .switchIfEmpty(Mono.error(new RuntimeException("El cliente con el ID " + clientId + " no existe")))
//            .then(clientModelRepository.updateClient(clientId, updatedClient));
//}
}
