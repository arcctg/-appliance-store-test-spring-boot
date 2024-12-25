package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    void addClient(Client client);
    void updateClient(Client client);
    Client getClientById(Long id);
    void deleteClientById(Long id);
    List<Client> getAllClients(Pageable pageable);
    List<Client> getAllClients();
    int getClientsSize();
}
