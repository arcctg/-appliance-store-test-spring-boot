package com.epam.rd.autocode.assessment.appliances.controller.web;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Loggable
    @GetMapping
    public String getAllEmployees(@PageableDefault(size = 5, sort = "id") Pageable pageable, Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees(pageable));

        return "employee/employees";
    }

    @Loggable
    @GetMapping("/add")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee/newEmployee";
    }

    @Loggable
    @GetMapping("/edit")
    public String editEmployeeForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee/editEmployee";
    }

    @Loggable
    @PostMapping({"/add-employee", "/edit-employee"})
    public String saveEmployee(@ModelAttribute @Valid Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @Loggable
    @DeleteMapping("/delete")
    public String deleteEmployee(@RequestParam("id") Long id, HttpServletRequest request) {
        employeeService.deleteEmployeeById(id);
        return "redirect:" + request.getHeader("Referer");
    }
}
