package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.repository.PersonRepositoryAdapter;
import com.co.financialservice.model.personmodel.PersonModel;
import com.co.financialservice.model.personmodel.gateways.PersonModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService implements PersonModelRepository {

    private final PersonRepositoryAdapter personRepositoryAdapter;
    @Override
    public Mono<PersonModel> getPersonByDocument(String document) {
        return Mono.justOrEmpty(personRepositoryAdapter.findByDocument(document));
    }

    @Override
    public Mono<PersonModel> getPersonById(Long personId) {
        return Mono.justOrEmpty(personRepositoryAdapter.findById(personId));
    }

    @Override
    public Flux<PersonModel> getAllPersons() {
        return Flux.fromIterable(personRepositoryAdapter.findAll());
    }

    @Transactional
    @Override
    public Mono<PersonModel> savePerson(PersonModel personModel) {
        return Mono.just(personRepositoryAdapter.save(personModel));
    }

    @Transactional
    @Override
    public Mono<Void> deletePerson(Long personId) {
        return Mono.fromRunnable(() -> personRepositoryAdapter.deleteByPersonId(personId));
    }
//    @Transactional
//    @Override
//    public Mono<PersonModel> updatePerson(Long personId, PersonModel personModel) {
//        return Mono.just(personRepositoryAdapter.updateByPersonId(personId, personModel));
//    }
}
