package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveClient() {
        Client client = new Client();
        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.saveClient(client);

        assertEquals(client, savedClient);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetClientById_Found() {
        Client client = new Client();
        client.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client foundClient = clientService.getClientById(1L);

        assertEquals(client, foundClient);
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClientByEmail_Found() {
        Client client = new Client();
        client.setEmail("test@example.com");

        when(clientRepository.findByEmail("test@example.com")).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClientByEmail("test@example.com");

        assertTrue(foundClient.isPresent());
        assertEquals(client, foundClient.get());
    }

    @Test
    void testGetClientByEmail_NotFound() {
        when(clientRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<Client> foundClient = clientService.getClientByEmail("nonexistent@example.com");

        assertFalse(foundClient.isPresent());
    }

    @Test
    void testDeleteClientById() {
        clientService.deleteClientById(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testToggleClientBlockById_ClientFound() {
        Client client = new Client();
        client.setId(1L);
        client.setEnabled(true);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        clientService.toggleClientBlockById(1L);

        assertFalse(client.getEnabled());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testToggleClientBlockById_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        clientService.toggleClientBlockById(1L);

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void testGetAllClients() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(new Client(), new Client()));

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);

        Page<Client> result = clientService.getAllClients(pageable);

        assertEquals(2, result.getContent().size());
        verify(clientRepository, times(1)).findAll(pageable);
    }
}
