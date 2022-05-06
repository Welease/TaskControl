package com.example.TaskControl.service;

import com.example.TaskControl.domain.Employee;
import com.example.TaskControl.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Employee getEmployeeByLogPass(String login, String password) {
        return employeeRepository.getEmployeeByLoginAndPassword(login, password);
    }
}
