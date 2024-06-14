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
public class ClientRepositoryAdapter extends AdapterOperations<ClientModel, ClientEntity, Long, ClientRepository>

{

    public ClientRepositoryAdapter(ClientRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, ClientModel.class));
    }

    @Transactional
    public void deleteByClienteId(Long clientId) {
         repository.deleteByClienteId(clientId);
    }

    public ClientModel updateByClienteId(Long clientId, ClientModel clientModel) {
        ClientEntity existingClient = repository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("cliente no encontrado"));

        updateClientEntity(clientModel, existingClient);
        return mapper.map(repository.save(existingClient), ClientModel.class);

    }
    private void updateClientEntity(ClientModel clientModel, ClientEntity existingClient) {
        existingClient.setEstado(clientModel.getEstado());
        existingClient.setContrasena(clientModel.getContrasena());
        existingClient.setPersona(mapper.map(clientModel.getPersona(), PersonEntity.class));
    }



}
