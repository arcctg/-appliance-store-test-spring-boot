package com.epam.rd.autocode.assessment.appliances.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.epam.rd.autocode.assessment.appliances.model.Admin;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.CustomUser;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String CLIENT_EMAIL = "client@example.com";
    private static final String EMPLOYEE_EMAIL = "employee@example.com";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String NON_EXISTENT_EMAIL = "notfound@example.com";

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testFindUserByEmail_ClientFound() {
        Client client = createTestClient();
        when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(Optional.of(client));

        CustomUser result = userService.findUserByEmail(CLIENT_EMAIL);

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals(CLIENT_EMAIL, result.getEmail());
        verify(clientRepository).findByEmail(CLIENT_EMAIL);
        verifyNoInteractions(employeeRepository, adminRepository);
    }

    @Test
    void testFindUserByEmail_EmployeeFound() {
        Employee employee = createTestEmployee();
        when(clientRepository.findByEmail(EMPLOYEE_EMAIL)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(EMPLOYEE_EMAIL)).thenReturn(Optional.of(employee));

        CustomUser result = userService.findUserByEmail(EMPLOYEE_EMAIL);

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals(EMPLOYEE_EMAIL, result.getEmail());
        verify(clientRepository).findByEmail(EMPLOYEE_EMAIL);
        verify(employeeRepository).findByEmail(EMPLOYEE_EMAIL);
        verifyNoInteractions(adminRepository);
    }

    @Test
    void testFindUserByEmail_AdminFound() {
        Admin admin = createTestAdmin();
        when(clientRepository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.of(admin));

        CustomUser result = userService.findUserByEmail(ADMIN_EMAIL);

        assertNotNull(result, "Expected a non-null CustomUser");
        assertEquals(ADMIN_EMAIL, result.getEmail());
        verify(clientRepository).findByEmail(ADMIN_EMAIL);
        verify(employeeRepository).findByEmail(ADMIN_EMAIL);
        verify(adminRepository).findByEmail(ADMIN_EMAIL);
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        when(clientRepository.findByEmail(NON_EXISTENT_EMAIL)).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(NON_EXISTENT_EMAIL)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(NON_EXISTENT_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
            () -> userService.findUserByEmail(NON_EXISTENT_EMAIL),
            "Expected UsernameNotFoundException for non-existent user"
        );

        verify(clientRepository).findByEmail(NON_EXISTENT_EMAIL);
        verify(employeeRepository).findByEmail(NON_EXISTENT_EMAIL);
        verify(adminRepository).findByEmail(NON_EXISTENT_EMAIL);
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setEmail(CLIENT_EMAIL);
        return client;
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        employee.setEmail(EMPLOYEE_EMAIL);
        return employee;
    }

    private Admin createTestAdmin() {
        Admin admin = new Admin();
        admin.setEmail(ADMIN_EMAIL);
        return admin;
    }
}
