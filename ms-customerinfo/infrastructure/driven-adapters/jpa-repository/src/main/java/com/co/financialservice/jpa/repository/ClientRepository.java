package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.ClientEntity;
import com.co.financialservice.jpa.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface ClientRepository extends CrudRepository<ClientEntity,Long >, QueryByExampleExecutor<ClientEntity> {


    void deleteByClienteId(Long clientId);

}
