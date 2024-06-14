package com.co.financialservice.jpa.repository;
import com.co.financialservice.jpa.entity.ClientEntity;
import com.co.financialservice.jpa.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface PersonRepository extends CrudRepository<PersonEntity,Long >, QueryByExampleExecutor<PersonEntity> {
    PersonEntity findByIdentificacion(String document);

    void deleteByPersonId(Long personId);


    //PersonEntity updateByPersonId(Long personId, PersonEntity personEntity);
}
