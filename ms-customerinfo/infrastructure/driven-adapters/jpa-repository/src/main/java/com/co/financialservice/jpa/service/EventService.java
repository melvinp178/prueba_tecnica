package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.kafka.config.event.Event;
import com.co.financialservice.model.clientmodel.ClientModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@AllArgsConstructor
@Component
public class EventService {

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;
    private final ClientService clientService;

    @KafkaListener(topics = "client-verification-topic", groupId = "ms-customerinfo")
    public void verificarCliente(Event<?> event) {
        int clientId = (int) event.getData();
        ClientModel cliente = clientService.getClientById(Long.valueOf(clientId)).block();
            Event<ClientModel> solicitud = new Event<>();
            solicitud.setData(cliente);
            solicitud.setId(UUID.randomUUID().toString());
            solicitud.setType("CREATED");
            kafkaTemplate.send("client-confirmation-topic", solicitud);
    }
}
