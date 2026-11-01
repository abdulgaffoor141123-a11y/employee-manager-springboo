package com.example.employeemanager.controller;

import com.example.employeemanager.model.Employee;
import com.example.employeemanager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // 1. Show list of employees and registration form (GET /employees)
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("employee", new Employee());
        return "employees";
    }

    // 2. Save a new employee with validation (POST /employees)
    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, 
                               BindingResult result, 
                               Model model) {
        
        // Check if email already exists
        Optional<Employee> existingEmployee = employeeService.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent() && (employee.getId() == null || !employee.getId().equals(existingEmployee.get().getId()))) {
            result.rejectValue("email", "error.employee", "This email is already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // 3. Show edit form filled with employee data (GET /employees/{id}/edit)
    @GetMapping("/{id}/edit")
    public String editEmployeeForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    // 4. Update the changes (POST /employees/{id}/update)
    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable("id") Long id, 
                                 @Valid @ModelAttribute("employee") Employee employee, 
                                 BindingResult result, 
                                 Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "edit-employee";
        }

        employee.setId(id);
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // 5. Remove the employee (POST /employees/{id}/delete)
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}