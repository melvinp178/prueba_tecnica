package com.co.financialservice.model.clientmodel.gateways;

import com.co.financialservice.model.clientmodel.ClientModel;
import reactor.core.publisher.Mono;

public interface ClientModelRepository {
    Mono<ClientModel> getClient(Long clientId);
}
