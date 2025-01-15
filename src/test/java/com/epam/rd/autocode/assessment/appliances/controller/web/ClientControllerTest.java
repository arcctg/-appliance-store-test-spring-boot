package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllClients() throws Exception {
        // Arrange: Create a mock pageable result
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Client> mockPage = new PageImpl<>(Collections.emptyList());
        when(clientService.getAllClients(pageable)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/clients"))
                .andExpect(model().attributeExists("pageable"));

        verify(clientService, times(1)).getAllClients(any(Pageable.class));
    }

    @Test
    void testAddClientForm() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/clients/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/newClient"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void testEditClientForm() throws Exception {
        // Arrange
        Long clientId = 1L;
        Client mockClient = new Client();
        when(clientService.getClientById(clientId)).thenReturn(mockClient);

        // Act & Assert
        mockMvc.perform(get("/clients/edit")
                        .param("id", String.valueOf(clientId)))
                .andExpect(status().isOk())
                .andExpect(view().name("client/editClient"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("client", mockClient));

        verify(clientService, times(1)).getClientById(clientId);
    }

    @Test
    void testSaveClient() throws Exception {
        // Arrange
        Client client = new Client();
        when(clientService.saveClient(client)).thenReturn(client); // Mock return value if saveClient returns something

        // Act & Assert
        mockMvc.perform(post("/clients/add-client")
                        .flashAttr("client", client))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        // Verify that saveClient was called once
        verify(clientService, times(1)).saveClient(client);
    }


    @Test
    void testToggleClient() throws Exception {
        // Arrange
        Long clientId = 1L;
        doNothing().when(clientService).toggleClientBlockById(clientId);


        // Act & Assert
        mockMvc.perform(get("/clients/toggle")
                        .param("id", String.valueOf(clientId)))
                .andExpect(status().is3xxRedirection());

        verify(clientService, times(1)).toggleClientBlockById(clientId);
    }

    @Test
    void testDeleteClient() throws Exception {
        // Arrange
        Long clientId = 1L;
        doNothing().when(clientService).deleteClientById(clientId);

        // Act & Assert
        mockMvc.perform(get("/clients/delete")
                        .param("id", String.valueOf(clientId)))
                .andExpect(status().is3xxRedirection());

        verify(clientService, times(1)).deleteClientById(clientId);
    }
}
