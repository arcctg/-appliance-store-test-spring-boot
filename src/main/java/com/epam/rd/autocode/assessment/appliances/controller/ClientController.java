package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Loggable
    @GetMapping
    public String getAllClients(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String order) {
        model.addAttribute("clients", clientService.getAllClients(PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(order), sort))));
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        return "client/clients";
    }

    @Loggable
    @GetMapping("/add")
    public String addClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "client/newClient";
    }

    @Loggable
    @PostMapping("/add-client")
    public String addClient(@ModelAttribute Client client) {
        clientService.addClient(client);
        return "redirect:/clients";
    }

    @Loggable
    @GetMapping("/edit")
    public String editClientForm(@RequestParam("id") Long id, Model model) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "client/editClient";
    }

    @Loggable
    @PostMapping("/edit-client")
    public String editClient(@ModelAttribute Client client) {
        clientService.updateClient(client);
        return "redirect:/clients";
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteClient(@RequestParam("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients";
    }
}
