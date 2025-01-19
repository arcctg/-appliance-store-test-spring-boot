package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.exception.EmployeeNotFoundException;
import com.epam.rd.autocode.assessment.appliances.exception.UserAlreadyExistsException;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.enums.Role;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            UserService userService,
            BCryptPasswordEncoder passwordEncoder,
            OrderRepository orderRepository) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Employee existingEmployee = employeeRepository
                .findById(employee.getId())
                .orElseThrow(() -> new EmployeeNotFoundException(employee.getId()));

        if (!existingEmployee.getEmail().equals(employee.getEmail())
                && userService.existsByEmail(employee.getEmail())) {
            throw new UserAlreadyExistsException(employee.getEmail());
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    @Override
    public Employee addEmployee(Employee employee) {
        if (userService.existsByEmail(employee.getEmail())) {
            throw new UserAlreadyExistsException(employee.getEmail());
        }

        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public void deleteEmployeeById(Long id) {
        orderRepository.findByEmployee(getEmployeeById(id)).ifPresent(order -> order.setEmployee(null));
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}
