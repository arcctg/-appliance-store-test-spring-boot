package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for managing clients.
 */
public interface ClientService {
    /**
     * Updates an existing client.
     *
     * @param client the client to update
     * @return the updated client
     */
    Client updateClient(Client client);

    /**
     * Adds a new client.
     *
     * @param client the client to add
     * @return the added client
     */
    Client addClient(Client client);

    /**
     * Retrieves a client by its ID.
     *
     * @param id the ID of the client
     * @return the client with the specified ID
     */
    Client getClientById(Long id);

    /**
     * Retrieves a client by its email.
     *
     * @param email the email of the client
     * @return an {@link Optional} containing the client if found, or empty if not
     */
    Optional<Client> getClientByEmail(String email);

    /**
     * Deletes a client by its ID.
     *
     * @param id the ID of the client to delete
     */
    void deleteClientById(Long id);

    /**
     * Toggles the block status of a client by its ID.
     *
     * @param id the ID of the client to toggle
     */
    void toggleClientBlockById(Long id);

    /**
     * Retrieves all clients with pagination.
     *
     * @param pageable the pagination information
     * @return a page of clients
     */
    Page<Client> getAllClients(Pageable pageable);
}
