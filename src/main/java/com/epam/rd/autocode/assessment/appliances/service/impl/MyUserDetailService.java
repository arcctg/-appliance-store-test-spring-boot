package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    public MyUserDetailService(ClientRepository clientRepository, EmployeeRepository employeeRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> myClientOptional = clientRepository.findByEmail(email);
        Optional<Employee> myEmployeeOptional = employeeRepository.findByEmail(email);
        if (myClientOptional.isPresent()) {
            Client client = myClientOptional.get();
            return User.builder()
                    .username(client.getEmail())
                    .password(client.getPassword())
                    .roles("USER")
                    .build();
        } else if (myEmployeeOptional.isPresent()) {
            Employee employee = myEmployeeOptional.get();
            return User.builder()
                    .username(employee.getEmail())
                    .password(employee.getPassword())
                    .roles("EMPLOYEE")
                    .build();

        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
