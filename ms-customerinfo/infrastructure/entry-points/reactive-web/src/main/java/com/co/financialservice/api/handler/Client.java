package com.co.financialservice.api.handler;

import com.co.financialservice.api.dto.ClientDto;
import com.co.financialservice.api.dto.PersonDto;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.personmodel.PersonModel;
import com.co.financialservice.usecase.client.ClientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Client {

    private final ClientUseCase clientUseCase;

    public Mono<ServerResponse> getClient(ServerRequest request) {
        Long clientId;
        try {
            clientId =getClientId(request);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return clientUseCase.getClientById(clientId)
                .flatMap(this::clientBuildResponse)
                .flatMap(client -> ServerResponse.ok().body(Mono.just(client), ClientDto.class))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> createClient(ServerRequest request) {
        return request.bodyToMono(ClientDto.class)
                .flatMap(clientDto -> clientUseCase.saveClient(clientBuildRequest(clientDto).block()))
                .flatMap(this::clientBuildResponse)
                        .flatMap(client -> ServerResponse.ok().body(Mono.just(client), ClientDto.class))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> deleteClient(ServerRequest request) {
        Long clientId;
        try {
            clientId =getClientId(request);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return clientUseCase.deleteClient(clientId)
                .flatMap(msg -> ServerResponse.ok().bodyValue(msg));
                //.onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> updateClient(ServerRequest request) {
        Long clientId;
        try {
            clientId =getClientId(request);
        } catch (RuntimeException e) {
            return ServerResponse.badRequest().bodyValue(e.getMessage());
        }
        return request.bodyToMono(ClientModel.class)
                .flatMap(client -> clientUseCase.updateClient(clientId, client))
                .flatMap(this::clientBuildResponse)
                .flatMap(client -> ServerResponse.ok().body(Mono.just(client), ClientDto.class))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    private Long getClientId(ServerRequest serverRequest) {
        try {
            return Long.valueOf(serverRequest.pathVariable("id"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("El formato del id no es correcto");
        }
    }

    private Mono<ClientDto> clientBuildResponse(ClientModel clientModel) {

        return Mono.just(ClientDto.builder()
                .estado(clientModel.getEstado())
                .clienteId(clientModel.getClienteId())
                .contrasena(clientModel.getContrasena())
                .persona(PersonDto.builder()
                        .nombre(clientModel.getPersona().getNombre())
                        .direccion(clientModel.getPersona().getDireccion())
                        .edad(clientModel.getPersona().getEdad())
                        .genero(clientModel.getPersona().getGenero())
                        .identificacion(clientModel.getPersona().getIdentificacion())
                        .personId(clientModel.getPersona().getPersonId())
                        .telefono(clientModel.getPersona().getTelefono())
                        .build()).build());
    }

    private Mono<ClientModel> clientBuildRequest(ClientDto clientDto) {
        return Mono.just(ClientModel.builder()
                .estado(clientDto.estado())
                .contrasena(clientDto.contrasena())
                .persona(PersonModel.builder()
                        .nombre(clientDto.persona().nombre())
                        .direccion(clientDto.persona().direccion())
                        .edad(clientDto.persona().edad())
                        .genero(clientDto.persona().genero())
                        .identificacion(clientDto.persona().identificacion())
                        .telefono(clientDto.persona().telefono())
                        .build()).build());
    }
}
