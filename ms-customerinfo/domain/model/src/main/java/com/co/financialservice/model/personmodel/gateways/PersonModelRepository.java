package com.co.financialservice.model.personmodel.gateways;
import com.co.financialservice.model.personmodel.PersonModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonModelRepository {
    Mono<PersonModel> getPersonByDocument(String document);

    Mono<PersonModel> getPersonById(Long personId);
    Flux<PersonModel> getAllPersons();
    Mono<PersonModel> savePerson(PersonModel personModel);
    Mono<Void> deletePerson(Long personId);
   // Mono<PersonModel> updatePerson(Long personId, PersonModel personModel);
}
