package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.ClientEntity;
import com.co.financialservice.jpa.entity.PersonEntity;
import com.co.financialservice.jpa.helper.AdapterOperations;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.personmodel.PersonModel;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import jakarta.transaction.Transactional;

@Repository
public class PersonRepositoryAdapter extends AdapterOperations<PersonModel, PersonEntity, Long, PersonRepository> {

    public PersonRepositoryAdapter(PersonRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, PersonModel.class));
    }
    public PersonModel findByDocument(String document) {
        PersonEntity personEntity= repository.findByIdentificacion(document);
        return personEntity==null?null:mapper.map(personEntity, PersonModel.class);
    }

    @Transactional
    public void deleteByPersonId(Long personId) {
         repository.deleteByPersonId(personId);
    }

//    @Transactional
//    public PersonModel updateByPersonId(Long personId, PersonModel personModel) {
//        return mapper.map(repository.updateByPersonId(personId, mapper.map(personModel, PersonEntity.class)), PersonModel.class);
//    }
}