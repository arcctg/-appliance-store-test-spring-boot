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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private static final String REDIRECT_EMPLOYEES = "redirect:/employees";
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
    @PutMapping("/edit-employee")
    public String updateEmployee(@ModelAttribute @Valid Employee employee) {
        employeeService.updateEmployee(employee);

        return REDIRECT_EMPLOYEES;
    }

    @Loggable
    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute @Valid Employee employee) {
        employeeService.addEmployee(employee);

        return REDIRECT_EMPLOYEES;
    }

    @Loggable
    @DeleteMapping("/delete")
    public String deleteEmployee(@RequestParam("id") Long id, HttpServletRequest request) {
        employeeService.deleteEmployeeById(id);

        return redirectToReferer(request);
    }

    private String redirectToReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        return referer != null ? "redirect:" + referer : REDIRECT_EMPLOYEES;
    }
}
