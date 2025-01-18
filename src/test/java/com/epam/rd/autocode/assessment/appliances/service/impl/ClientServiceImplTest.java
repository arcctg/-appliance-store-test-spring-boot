package com.epam.rd.autocode.assessment.appliances.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    private static final Long CLIENT_ID = 1L;
    private static final String CLIENT_EMAIL = "test@example.com";
    private static final String CLIENT_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CartService cartService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = createTestClient();
    }

    @Test
    void testUpdateClient_Found() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(CLIENT_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.updateClient(client);

        assertEquals(client, updatedClient);
        verify(clientRepository).save(client);
    }

    @Test
    void testUpdateClient_NotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(client));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void testGetClientById_Found() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));

        Client foundClient = clientService.getClientById(CLIENT_ID);

        assertEquals(client, foundClient);
        verify(clientRepository).findById(CLIENT_ID);
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(CLIENT_ID));
        verify(clientRepository).findById(CLIENT_ID);
    }

    @Test
    void testGetClientByEmail_Found() {
        when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientByEmail(CLIENT_EMAIL);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
    }

    @Test
    void testGetClientByEmail_NotFound() {
        when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.getClientByEmail(CLIENT_EMAIL);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteClientById_Found() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));

        clientService.deleteClientById(CLIENT_ID);

        verify(clientRepository).delete(client);
    }

    @Test
    void testDeleteClientById_NotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(CLIENT_ID));
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    void testToggleClientBlockById_ClientFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));

        clientService.toggleClientBlockById(CLIENT_ID);

        assertFalse(client.getEnabled());
        verify(clientRepository).save(client);
    }

    @Test
    void testToggleClientBlockById_ClientNotFound() {
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        clientService.toggleClientBlockById(CLIENT_ID);

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void testGetAllClients() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(new Client(), new Client()));

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);

        Page<Client> result = clientService.getAllClients(pageable);

        assertEquals(2, result.getContent().size());
        verify(clientRepository).findAll(pageable);
    }

    private Client createTestClient() {
        Client client1 = new Client();
        client1.setId(CLIENT_ID);
        client1.setEmail(CLIENT_EMAIL);
        client1.setPassword(CLIENT_PASSWORD);
        return client1;
    }
}
