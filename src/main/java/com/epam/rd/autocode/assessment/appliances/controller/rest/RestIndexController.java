package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> home(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.ok("Welcome, anonymous user!");
        }

        String message;
        String authorities = user.getAuthorities().toString();
        if (authorities.contains("ROLE_CLIENT")) {
            message = "Welcome, client!";
        } else if (authorities.contains("ROLE_EMPLOYEE")) {
            message = "Welcome, employee!";
        } else if (authorities.contains("ROLE_ADMIN")) {
            message = "Welcome, admin!";
        } else {
            message = "Welcome!";
        }

        return ResponseEntity.ok(message);
    }
}
