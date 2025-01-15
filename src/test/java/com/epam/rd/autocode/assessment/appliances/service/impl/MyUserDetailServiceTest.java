package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.CustomUser;
import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserDetailServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    private CustomUser user;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Prepare mock user for testing
        user = new CustomUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
    }

    @Test
    void testLoadUserByUsername_UserFoundAndNotLocked() {
        // Mock userService to return a user
        when(userService.findUserByEmail("test@example.com")).thenReturn(user);

        // Call the service method
        UserDetails userDetails = myUserDetailService.loadUserByUsername("test@example.com");

        // Verify results
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonLocked()); // User is not locked
        verify(userService, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserFoundAndLocked() {
        // Mock userService to return a client with enabled = false
        Client client = new Client();
        client.setEmail("test@example.com");
        client.setPassword("password123");
        client.setRole(Role.CLIENT);
        client.setEnabled(false); // Account is locked
        when(userService.findUserByEmail("test@example.com")).thenReturn(client);

        // Call the service method
        UserDetails userDetails = myUserDetailService.loadUserByUsername("test@example.com");

        // Verify results
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertFalse(userDetails.isAccountNonLocked()); // Account is locked
        verify(userService, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock userService to return null for non-existent user
        when(userService.findUserByEmail("nonexistent@example.com")).thenReturn(null);

        // Call the service method and assert exception
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailService.loadUserByUsername("nonexistent@example.com"));
        verify(userService, times(1)).findUserByEmail("nonexistent@example.com");
    }

}
