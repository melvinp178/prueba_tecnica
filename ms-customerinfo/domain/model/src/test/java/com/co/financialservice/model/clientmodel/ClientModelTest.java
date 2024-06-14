package com.co.financialservice.model.clientmodel;

import com.co.financialservice.model.personmodel.PersonModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientModelTest {
    private ClientModel clientModel;
    private PersonModel personModel;


    @BeforeEach
    public void setUp() {
        personModel = new PersonModel(1L, "Nombre", "Femenino", 25, "123456789", "Dirección", "0987654321");
        clientModel = new ClientModel(1L, "password", "activo", personModel);
    }

    @Test
    public void testClientModelCreation() {
        assertNotNull(clientModel);
    }

    @Test
    public void testClientModelAttributes() {
        assertEquals(1L, clientModel.getClienteId());
        assertEquals("password", clientModel.getContrasena());
        assertEquals("activo", clientModel.getEstado());
        assertEquals(personModel, clientModel.getPersona());
    }

    @Test
    public void testClientModelSetters() {
        clientModel.setContrasena("newpassword");
        assertEquals("newpassword", clientModel.getContrasena());

        clientModel.setEstado("inactivo");
        assertEquals("inactivo", clientModel.getEstado());
    }

}