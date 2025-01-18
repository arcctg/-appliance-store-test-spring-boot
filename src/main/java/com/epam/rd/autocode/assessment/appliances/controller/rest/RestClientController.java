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
    public ResponseEntity<Page<Client>> getAllClients(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(clientService.getAllClients(pageable));
    }

    @Loggable
    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody @Valid Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @Loggable
    @PutMapping("/{id}")
    public ResponseEntity<Client> editClient(@PathVariable @Min(1) Long id, @RequestBody @Valid Client client) {
        client.setId(id);
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @Loggable
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleClient(@PathVariable @Min(1) Long id) {
        clientService.toggleClientBlockById(id);
        return ResponseEntity.noContent().build();
    }

    @Loggable
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable @Min(1) Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}
