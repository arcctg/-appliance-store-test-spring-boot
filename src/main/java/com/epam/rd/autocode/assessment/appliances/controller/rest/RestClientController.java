package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing clients.
 */
@RestController
@RequestMapping("/api/clients")
public class RestClientController {

    /**
     * The client service for managing client operations.
     */
    private final ClientService clientService;

    /**
     * Constructs a new RestClientController with the specified ClientService.
     *
     * @param clientService the client service
     */
    public RestClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Retrieves all clients with pagination.
     *
     * @param pageable the pagination information
     * @return the response entity containing the page of clients
     */
    @Loggable
    @GetMapping
    public ResponseEntity<Page<Client>> getAllClients(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(clientService.getAllClients(pageable));
    }

    /**
     * Adds a new client.
     *
     * @param client the new client
     * @return the response entity containing the added client
     */
    @Loggable
    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody @Valid Client client) {
        return ResponseEntity.ok(clientService.addClient(client));
    }

    /**
     * Updates an existing client.
     *
     * @param id the ID of the client to update
     * @param client the updated client
     * @return the response entity containing the updated client
     */
    @Loggable
    @PutMapping("/{id}")
    public ResponseEntity<Client> editClient(@PathVariable @Min(1) Long id, @RequestBody @Valid Client client) {
        client.setId(id);
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    /**
     * Toggles the enabled status of a client.
     *
     * @param id the ID of the client to toggle
     * @return the response entity with no content
     */
    @Loggable
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleClient(@PathVariable @Min(1) Long id) {
        clientService.toggleClientBlockById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a client.
     *
     * @param id the ID of the client to delete
     * @return the response entity with no content
     */
    @Loggable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable @Min(1) Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}
