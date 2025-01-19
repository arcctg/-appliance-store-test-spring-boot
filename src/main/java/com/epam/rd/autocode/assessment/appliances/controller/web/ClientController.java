package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for managing clients.
 */
@Controller
@RequestMapping("/clients")
public class ClientController {
    /**
     * The redirect URL for clients.
     */
    private static final String REDIRECT_CLIENTS = "redirect:/clients";
    /**
     * The client service for managing client operations.
     */
    private final ClientService clientService;

    /**
     * Constructs a new ClientController with the specified ClientService.
     *
     * @param clientService the client service
     */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Retrieves all clients with pagination.
     *
     * @param pageable the pagination information
     * @param model the model to hold the clients
     * @return the view name for displaying clients
     */
    @Loggable
    @GetMapping
    public String getAllClients(@PageableDefault(size = 5, sort = "id") Pageable pageable, Model model) {
        model.addAttribute("clients", clientService.getAllClients(pageable));

        return "client/clients";
    }

    /**
     * Displays the form for adding a new client.
     *
     * @param model the model to hold the new client
     * @return the view name for the new client form
     */
    @Loggable
    @GetMapping("/add")
    public String addClientForm(Model model) {
        model.addAttribute("client", new Client());

        return "client/newClient";
    }

    /**
     * Displays the form for editing an existing client.
     *
     * @param id the ID of the client to edit
     * @param model the model to hold the client
     * @return the view name for the edit client form
     */
    @Loggable
    @GetMapping("/edit")
    public String editClientForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));

        return "client/editClient";
    }

    /**
     * Updates an existing client.
     *
     * @param client the updated client
     * @return the redirect URL after updating the client
     */
    @Loggable
    @PutMapping("/edit-client")
    public String updateClient(@ModelAttribute @Valid Client client) {
        clientService.updateClient(client);

        return REDIRECT_CLIENTS;
    }

    /**
     * Adds a new client.
     *
     * @param client the new client
     * @return the redirect URL after adding the client
     */
    @Loggable
    @PostMapping("/add-client")
    public String addClient(@ModelAttribute @Valid Client client) {
        clientService.addClient(client);

        return REDIRECT_CLIENTS;
    }

    /**
     * Toggles the enabled status of a client.
     *
     * @param id the ID of the client to toggle
     * @param request the HTTP request
     * @return the redirect URL after toggling the client enabled status
     */
    @Loggable
    @PatchMapping("/toggle")
    public String editClientForm(@RequestParam("id") Long id, HttpServletRequest request) {
        clientService.toggleClientBlockById(id);

        return redirectToReferer(request);
    }

    /**
     * Deletes a client.
     *
     * @param id the ID of the client to delete
     * @param request the HTTP request
     * @return the redirect URL after deleting the client
     */
    @Loggable
    @DeleteMapping("/delete")
    public String deleteClient(@RequestParam("id") Long id, HttpServletRequest request) {
        clientService.deleteClientById(id);

        return redirectToReferer(request);
    }

    /**
     * Redirects to the referer URL.
     *
     * @param request the HTTP request
     * @return the redirect URL to the referer
     */
    private String redirectToReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        return referer != null ? "redirect:" + referer : REDIRECT_CLIENTS;
    }
}
