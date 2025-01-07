package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class RestClientController {
    private final ClientService clientService;

    public RestClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Loggable
    @GetMapping
    public Page<Client> getAllClients(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return clientService.getAllClients(pageable);
    }

    @Loggable
    @PostMapping
    public Client addClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @Loggable
    @PutMapping("/{id}")
    public Client editClient(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        return clientService.saveClient(client);
    }

    @Loggable
    @PatchMapping("/{id}/toggle")
    public void toggleClient(@PathVariable Long id) {
        clientService.toggleClientBlockById(id);
    }

    @Loggable
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
    }
}
