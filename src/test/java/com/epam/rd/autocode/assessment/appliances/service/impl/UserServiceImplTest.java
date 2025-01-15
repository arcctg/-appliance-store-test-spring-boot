package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.CustomUser;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserByEmail_ClientFound() {
        Client client = new Client();
        client.setEmail("client@example.com");

        when(clientRepository.findByEmail("client@example.com")).thenReturn(Optional.of(client));

        CustomUser result = userService.findUserByEmail("client@example.com");

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals("client@example.com", result.getEmail());
        verify(clientRepository).findByEmail("client@example.com");
        verifyNoInteractions(employeeRepository, adminRepository);
    }

    @Test
    void testFindUserByEmail_EmployeeFound() {
        Employee employee = new Employee();
        employee.setEmail("employee@example.com");

        when(clientRepository.findByEmail("employee@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail("employee@example.com")).thenReturn(Optional.of(employee));

        CustomUser result = userService.findUserByEmail("employee@example.com");

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals("employee@example.com", result.getEmail());
        verify(clientRepository).findByEmail("employee@example.com");
        verify(employeeRepository).findByEmail("employee@example.com");
        verifyNoInteractions(adminRepository);
    }

    @Test
    void testFindUserByEmail_AdminFound() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");

        when(clientRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        CustomUser result = userService.findUserByEmail("admin@example.com");

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals("admin@example.com", result.getEmail());
        verify(clientRepository).findByEmail("admin@example.com");
        verify(employeeRepository).findByEmail("admin@example.com");
        verify(adminRepository).findByEmail("admin@example.com");
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        when(clientRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.findUserByEmail("notfound@example.com"),
                "Expected UsernameNotFoundException for non-existent user"
        );

        verify(clientRepository).findByEmail("notfound@example.com");
        verify(employeeRepository).findByEmail("notfound@example.com");
        verify(adminRepository).findByEmail("notfound@example.com");
    }
}
