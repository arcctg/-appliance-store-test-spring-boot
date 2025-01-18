package com.epam.rd.autocode.assessment.appliances.controller.web;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private static final String BASE_URL = "/employees";
    private static final Long EMPLOYEE_ID = 1L;

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by("id"));
        Page<Employee> mockPage = new PageImpl<>(Collections.emptyList());
        when(employeeService.getAllEmployees(pageable)).thenReturn(mockPage);

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(view().name("employee/employees"))
            .andExpect(model().attributeExists("employees"));

        verify(employeeService).getAllEmployees(any(Pageable.class));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    void testAddEmployeeForm() throws Exception {
        mockMvc.perform(get(BASE_URL + "/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("employee/newEmployee"))
            .andExpect(model().attributeExists("employee"));
    }

    @Test
    void testEditEmployeeForm() throws Exception {
        Employee mockEmployee = createTestEmployee();
        when(employeeService.getEmployeeById(EMPLOYEE_ID)).thenReturn(mockEmployee);

        mockMvc.perform(get(BASE_URL + "/edit").param("id", EMPLOYEE_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("employee/editEmployee"))
            .andExpect(model().attributeExists("employee"))
            .andExpect(model().attribute("employee", mockEmployee));

        verify(employeeService).getEmployeeById(EMPLOYEE_ID);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    void testSaveEmployee() throws Exception {
        Employee mockEmployee = createTestEmployee();
        when(employeeService.saveEmployee(mockEmployee)).thenReturn(mockEmployee);

        mockMvc.perform(post(BASE_URL + "/add-employee").flashAttr("employee", mockEmployee))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(BASE_URL));

        verify(employeeService).saveEmployee(mockEmployee);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployeeById(EMPLOYEE_ID);

        mockMvc.perform(delete(BASE_URL + "/delete").param("id", EMPLOYEE_ID.toString()))
            .andExpect(status().is3xxRedirection());

        verify(employeeService).deleteEmployeeById(EMPLOYEE_ID);
        verifyNoMoreInteractions(employeeService);
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        employee.setId(EMPLOYEE_ID);
        employee.setName("Test Employee");
        employee.setEmail("test@email.com");
        employee.setPassword("password");
        employee.setDepartment("Test Department");

        return employee;
    }
}
