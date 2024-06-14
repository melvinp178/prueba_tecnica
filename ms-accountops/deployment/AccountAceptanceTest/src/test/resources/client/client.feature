Feature: Client API tests

  Background:
    * url baseUrl

  Scenario: Create a client
    Given url '/clients'
    And request { clienteId: 1, contrasena: 'password', estado: 'activo', persona: { personId: 1, nombre: 'Nombre', genero: 'Femenino', edad: 25, identificacion: '123456789', direccion: 'Dirección', telefono: '0987654321' } }
    When method post
    Then status 200
    And match response.clienteId == 1
    And match response.estado == 'activo'
    And match response.persona.personId == 1

  Scenario: Get client by ID
    Given url '/clients/1'
    When method get
    Then status 200
    And match response.clienteId == 1
    And match response.estado == 'activo'
    And match response.persona.personId == 1

  Scenario: Update a client
    Given url '/clients/1'
    And request { clienteId: 1, contrasena: 'password', estado: 'inactivo', persona: { personId: 1, nombre: 'Nombre', genero: 'Femenino', edad: 25, identificacion: '123456789', direccion: 'Dirección', telefono: '0987654321' } }
    When method put
    Then status 200
    And match response.estado == 'inactivo'

  Scenario: Delete a client
    Given url '/clients/1'
    When method delete
    Then status 200
    And match response == 'Cliente eliminado con éxito'
