package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import com.epam.rd.autocode.assessment.appliances.exception.UserAlreadyExistsException;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.CartService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link ClientService} interface for managing clients.
 */
@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final UserService userService;
    private final CartService cartService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a new ClientServiceImpl with the specified dependencies.
     *
     * @param clientRepository the client repository
     * @param userService the user service
     * @param cartService the cart service
     * @param passwordEncoder the password encoder
     */
    public ClientServiceImpl(
            ClientRepository clientRepository,
            UserService userService,
            CartService cartService,
            BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client updateClient(Client client) {
        Client oldClient = clientRepository
                .findById(client.getId())
                .orElseThrow(() -> new ClientNotFoundException(client.getId()));

        if (!oldClient.getEmail().equals(client.getEmail()) && userService.existsByEmail(client.getEmail())) {
            throw new UserAlreadyExistsException(client.getEmail());
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        return clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client addClient(Client client) {
        if (userService.existsByEmail(client.getEmail())) {
            throw new UserAlreadyExistsException(client.getEmail());
        }

        client.setRole(Role.CLIENT);
        client.setPassword(passwordEncoder.encode(client.getPassword()));

        return clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        cartService.deleteCartByClient(client);
        clientRepository.delete(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleClientBlockById(Long id) {
        clientRepository.findById(id).ifPresent(client -> {
            client.setEnabled(!client.getEnabled());
            clientRepository.save(client);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }
}
