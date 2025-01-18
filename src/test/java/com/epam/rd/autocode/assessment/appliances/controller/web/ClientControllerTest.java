package com.epam.rd.autocode.assessment.appliances.controller.web;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import java.util.Collections;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private static final String BASE_URL = "/clients";
    private static final Long CLIENT_ID = 1L;

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllClients() throws Exception {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by("id"));
        Page<Client> mockPage = new PageImpl<>(Collections.emptyList());
        when(clientService.getAllClients(pageable)).thenReturn(mockPage);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("client/clients"))
                .andExpect(model().attributeExists("clients"));

        verify(clientService).getAllClients(any(Pageable.class));
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void testAddClientForm() throws Exception {
        mockMvc.perform(get(BASE_URL + "/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/newClient"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void testEditClientForm() throws Exception {
        Client mockClient = new Client();
        when(clientService.getClientById(CLIENT_ID)).thenReturn(mockClient);

        mockMvc.perform(get(BASE_URL + "/edit").param("id", CLIENT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("client/editClient"))
                .andExpect(model().attribute("client", mockClient));

        verify(clientService).getClientById(CLIENT_ID);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void testUpdateClient() throws Exception {
        Client client = createTestClient();
        when(clientService.updateClient(client)).thenReturn(client);

        mockMvc.perform(put(BASE_URL + "/edit-client").flashAttr("client", client))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL));

        verify(clientService).updateClient(client);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void testAddClient() throws Exception {
        Client client = createTestClient();
        when(clientService.addClient(client)).thenReturn(client);

        mockMvc.perform(post(BASE_URL + "/add-client").flashAttr("client", client))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL));

        verify(clientService).addClient(client);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void testToggleClient() throws Exception {
        doNothing().when(clientService).toggleClientBlockById(CLIENT_ID);

        mockMvc.perform(patch(BASE_URL + "/toggle").param("id", CLIENT_ID.toString()))
                .andExpect(status().is3xxRedirection());

        verify(clientService).toggleClientBlockById(CLIENT_ID);
        verifyNoMoreInteractions(clientService);
    }

    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClientById(CLIENT_ID);

        mockMvc.perform(delete(BASE_URL + "/delete").param("id", CLIENT_ID.toString()))
                .andExpect(status().is3xxRedirection());

        verify(clientService).deleteClientById(CLIENT_ID);
        verifyNoMoreInteractions(clientService);
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setId(CLIENT_ID);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setPassword("password");
        client.setShippingAddress("123 Main St");

        return client;
    }
}
