package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.CustomUser;
import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailServiceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_PASSWORD = "password123";

    @Mock
    private UserService userService;

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    @Test
    void testLoadUserByUsername_UserFoundAndNotLocked() {
        CustomUser user = createTestUser();
        when(userService.findUserByEmail(USER_EMAIL)).thenReturn(user);

        UserDetails userDetails = myUserDetailService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails);
        assertEquals(USER_EMAIL, userDetails.getUsername());
        assertEquals(USER_PASSWORD, userDetails.getPassword());
        assertTrue(userDetails.isAccountNonLocked());
        verify(userService, times(1)).findUserByEmail(USER_EMAIL);
    }

    @Test
    void testLoadUserByUsername_UserFoundAndLocked() {
        Client client = createTestClient();
        client.setEnabled(false);
        when(userService.findUserByEmail(USER_EMAIL)).thenReturn(client);

        UserDetails userDetails = myUserDetailService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails);
        assertEquals(USER_EMAIL, userDetails.getUsername());
        assertEquals(USER_PASSWORD, userDetails.getPassword());
        assertFalse(userDetails.isAccountNonLocked());
        verify(userService, times(1)).findUserByEmail(USER_EMAIL);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userService.findUserByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> myUserDetailService.loadUserByUsername("nonexistent@example.com"));
        verify(userService, times(1)).findUserByEmail("nonexistent@example.com");
    }

    private CustomUser createTestUser() {
        CustomUser user = new CustomUser();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRole(Role.CLIENT);
        return user;
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setEmail(USER_EMAIL);
        client.setPassword(USER_PASSWORD);
        client.setRole(Role.CLIENT);
        client.setEnabled(true);
        return client;
    }
}
