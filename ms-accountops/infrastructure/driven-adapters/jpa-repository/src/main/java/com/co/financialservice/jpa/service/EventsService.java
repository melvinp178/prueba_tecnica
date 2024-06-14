package com.co.financialservice.jpa.service;

import com.co.financialservice.jpa.kafka.config.event.Event;
import com.co.financialservice.model.clientmodel.ClientModel;
import com.co.financialservice.model.clientmodel.gateways.ClientModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
@RequiredArgsConstructor
public class EventsService implements ClientModelRepository {
    private final KafkaTemplate<String, Event<?>> kafkaTemplate;
    private final String clientVerificationTopic = "client-verification-topic";
    private final String clientConfirmationTopic = "client-confirmation-topic";
    private final ObjectMapper mapper;
    private final ConcurrentMap<String, MonoSink<ClientModel>> pendingRequests = new ConcurrentHashMap<>();
    Mono<ClientModel> clientModelMono;
    CompletableFuture<Void> future = new CompletableFuture<>();



    @Override
    public Mono<ClientModel> getClient(Long clientId) {
            String requestId = UUID.randomUUID().toString();
            Event<Long> solicitud = new Event<>();
            solicitud.setData(clientId);
            solicitud.setId(requestId);
            solicitud.setType("CREATED");

       future.runAsync(() -> {
            try {
                kafkaTemplate.send(clientVerificationTopic, solicitud);
                TimeUnit.SECONDS.sleep(3);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
       return clientModelMono;
    }

    @KafkaListener(topics = clientConfirmationTopic, groupId = "ms-accountops")
    public void listenClientConfirmation(Event<?> record) {

        if (record.getClass().isAssignableFrom(Event.class)){
            clientModelMono=Mono.just(mapper.map(record.getData(), ClientModel.class));
        }
        future.cancel(true);
    }
}
