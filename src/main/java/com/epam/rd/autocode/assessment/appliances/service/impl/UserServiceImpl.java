package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.model.CustomUser;
import com.epam.rd.autocode.assessment.appliances.repository.AdminRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;

    public UserServiceImpl(ClientRepository clientRepository,
                           EmployeeRepository employeeRepository,
                           AdminRepository adminRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public CustomUser findUserByEmail(String email) {
        return clientRepository.findByEmail(email).map(c -> (CustomUser) c)
                .or(() -> employeeRepository.findByEmail(email))
                .or(() -> adminRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email)
                || employeeRepository.existsByEmail(email)
                || adminRepository.existsByEmail(email);
    }
}
