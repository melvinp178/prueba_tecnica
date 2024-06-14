package com.co.financialservice.api.dto;

import lombok.Builder;

@Builder
public record PersonDto (
     Long personId,

     String nombre,
     String genero,
     int edad,
     String identificacion,
     String direccion,
     String telefono
){
}
