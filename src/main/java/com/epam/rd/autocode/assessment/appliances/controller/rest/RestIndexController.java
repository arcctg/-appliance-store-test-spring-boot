package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestIndexController {
    @Loggable
    @GetMapping({"", "/index"})
    public String home(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "Welcome, anonymous user!";
        }

        String authorities = user.getAuthorities().toString();
        if (authorities.contains("ROLE_CLIENT")) {
            return "Welcome, client!";
        } else if (authorities.contains("ROLE_EMPLOYEE")) {
            return "Welcome, employee!";
        } else if (authorities.contains("ROLE_ADMIN")) {
            return "Welcome, admin!";
        } else {
            return "Welcome!";
        }
    }
}
