package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.AuthenticationRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    @Loggable
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationRequest form,
                                        HttpServletRequest request) {
        try {
            request.login(form.getUsername(), form.getPassword());
        } catch (ServletException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        return ResponseEntity.ok("Login successful!");
    }

    @Loggable
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        if (request.getUserPrincipal() == null) {
            return ResponseEntity.status(400).body("No user is currently logged in!");
        } else {
            request.logout();
        }

        return ResponseEntity.ok("Logout successful!");
    }

    @Loggable
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status(@AuthenticationPrincipal User user) {
        if (user != null) {
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", user.getUsername(),
                    "roles", user.getAuthorities().toString()
            ));
        }
        return ResponseEntity.ok(Map.of("authenticated", false));
    }
}
