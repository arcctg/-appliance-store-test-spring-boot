package com.epam.rd.autocode.assessment.appliances.exception;

/**
 * Exception thrown when a client with the specified ID is not found.
 */
public class ClientNotFoundException extends NotFoundException {
    /**
     * Constructs a new ClientNotFoundException with the specified client ID.
     *
     * @param id the ID of the client that was not found
     */
    public ClientNotFoundException(Long id) {
        super("Client with id %d not found".formatted(id));
    }
}
