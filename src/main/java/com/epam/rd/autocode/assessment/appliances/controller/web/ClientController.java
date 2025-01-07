package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Loggable
    @GetMapping
    public String getAllClients(
            Model model,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        model.addAttribute("clients", clientService.getAllClients(pageable));
        model.addAttribute("pageable", pageable);

        return "client/clients";
    }

    @Loggable
    @GetMapping("/add")
    public String addClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "client/newClient";
    }

    @Loggable
    @GetMapping("/edit")
    public String editClientForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "client/editClient";
    }

    @Loggable
    @PostMapping({"/add-client", "/edit-client"})
    public String saveClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @Loggable
    @GetMapping("/toggle")
    public String editClientForm(@RequestParam("id") Long id, HttpServletRequest request) {
        clientService.toggleClientBlockById(id);
        return "redirect:" + request.getHeader("Referer");
    }

    @Loggable
    @GetMapping("/delete")
    public String deleteClient(@RequestParam("id") Long id, HttpServletRequest request) {
        clientService.deleteClientById(id);
        return "redirect:" + request.getHeader("Referer");
    }
}
