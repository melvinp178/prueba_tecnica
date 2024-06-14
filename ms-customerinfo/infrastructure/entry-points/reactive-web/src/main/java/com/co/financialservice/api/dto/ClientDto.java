package com.co.financialservice.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ClientDto (
         Long clienteId,
         String contrasena,
         String estado,
         PersonDto persona

){
}
