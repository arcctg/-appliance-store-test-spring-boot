package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientService {
    Client saveClient(Client client);

    Client getClientById(Long id);

    Optional<Client> getClientByEmail(String email);

    void deleteClientById(Long id);

    void toggleClientBlockById(Long id);

    Page<Client> getAllClients(Pageable pageable);
}
