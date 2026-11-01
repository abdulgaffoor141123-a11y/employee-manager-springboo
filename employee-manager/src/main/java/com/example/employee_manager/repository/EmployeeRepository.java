package com.example.employee_manager.repository;

import com.example.employee_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Custom query to find an employee by email (useful for validation/duplicates)
    Optional<Employee> findByEmail(String email);
}