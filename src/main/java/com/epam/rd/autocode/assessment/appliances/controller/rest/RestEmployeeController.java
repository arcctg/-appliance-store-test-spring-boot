package com.epam.rd.autocode.assessment.appliances.controller.rest;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class RestEmployeeController {
    private final EmployeeService employeeService;

    public RestEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Loggable
    @GetMapping
    public Page<Employee> getAllEmployees(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return employeeService.getAllEmployees(pageable);
    }

    @Loggable
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @Loggable
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @Loggable
    @PutMapping("/{id}")
    public Employee editEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return employeeService.saveEmployee(employee);
    }

    @Loggable
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
    }
}
