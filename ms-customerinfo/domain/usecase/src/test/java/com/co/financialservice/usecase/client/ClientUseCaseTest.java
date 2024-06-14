package com.co.financialservice.usecase.client;


import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.clientmodel.gateways.ClientModelRepository;
import com.co.financialservice.model.personmodel.PersonModel;
import com.co.financialservice.usecase.person.PersonUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientUseCaseTest {

    @Mock
    private ClientModelRepository clientModelRepository;

    @Mock
    private PersonUseCase personUseCase;

    @InjectMocks
    private ClientUseCase clientUseCase;

    private ClientModel clientModel;
    private PersonModel personModel;

    @BeforeEach
    public void setUp() {
        personModel = new PersonModel(1L, "Nombre", "Femenino", 25, "123456789", "Dirección", "0987654321");
        clientModel = new ClientModel(1L, "password", "activo", personModel);
    }

    @Test
    public void testGetClientById() {
        when(clientModelRepository.getClientById(1L)).thenReturn(Mono.just(clientModel));

        Mono<ClientModel> result = clientUseCase.getClientById(1L);

        StepVerifier.create(result)
                .expectNext(clientModel)
                .verifyComplete();
    }

    @Test
    public void testSaveClient() {
        when(personUseCase.getPersonByDocument(anyString())).thenReturn(Mono.empty());
        when(personUseCase.savePerson(any(PersonModel.class))).thenReturn(Mono.just(personModel));
        when(clientModelRepository.saveClient(any(ClientModel.class))).thenReturn(Mono.just(clientModel));

        Mono<ClientModel> result = clientUseCase.saveClient(clientModel);

        StepVerifier.create(result)
                .expectNext(clientModel)
                .verifyComplete();
    }

    @Test
    public void testDeleteClient() {
        when(clientModelRepository.getClientById(1L)).thenReturn(Mono.just(clientModel));
        when(clientModelRepository.deleteClient(1L)).thenReturn(Mono.empty());
        when(personUseCase.deletePerson(1L)).thenReturn(Mono.empty());

        Mono<String> result = clientUseCase.deleteClient(1L);

        StepVerifier.create(result)
                .expectNext("Cliente eliminado con éxito")
                .verifyComplete();
    }

    @Test
    public void testUpdateClient() {
        when(clientModelRepository.getClientById(1L)).thenReturn(Mono.just(clientModel));
        when(clientModelRepository.updateClient(anyLong(), any(ClientModel.class))).thenReturn(Mono.just(clientModel));

        Mono<ClientModel> result = clientUseCase.updateClient(1L, clientModel);

        StepVerifier.create(result)
                .expectNext(clientModel)
                .verifyComplete();
    }
}