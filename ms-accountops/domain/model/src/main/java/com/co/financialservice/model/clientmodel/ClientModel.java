package com.co.financialservice.model.clientmodel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientModel {
    private Long clienteId;
    private String contrasena;
    private String estado;
    private PersonModel persona;
}
