package com.example.TaskControl.controller;

import com.example.TaskControl.domain.Employee;
import com.example.TaskControl.domain.Task;
import com.example.TaskControl.service.EmployeeService;
import com.example.TaskControl.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    TaskService taskService;

    @PostMapping
    public String login(@RequestParam String login, @RequestParam String password, Map<String, Object> model) {
        if (login != null && password != null && !login.trim().equals("") && !password.trim().equals("")) {
            Employee employee = employeeService.getEmployeeByLogPass(login, password);
            reloadUsers(model);
            if (employee != null) {
                if (employee.getRole().equals("admin")) {
                    return "adminPage";
                } else {
                    return "list";
                }
            }
        }
        model.put("errorMessage", "Incorrect login or password");
        return "errorPage";
    }

    @PostMapping("addEmployee")
    public String addEmployee(@RequestParam String role,@RequestParam String name, @RequestParam String login, @RequestParam String password,  Map<String, Object> model) {
        Employee employee = new Employee(name, login, password, role);
        employeeService.saveEmployee(employee);
        reloadUsers(model);
        return "list";
    }

    @PostMapping("addTask")
    public String addEmployee(@RequestParam String userId, @RequestParam String description, @RequestParam Date endDate, @RequestParam String priority, Map<String, Object> model) {
        Task task = new Task(description, new Date(System.currentTimeMillis()), endDate, priority, "todo");
        taskService.saveTask(task);
        System.out.println("usrId: " + userId);
        List<Task> tasks = taskService.getTasks();
        model.put("tasks", tasks);
        reloadUsers(model);
        return "list";
    }

    @GetMapping
    public String start(Map<String, Object> model) {
        reloadUsers(model);
        return "start";
    }

    private void reloadUsers(Map<String, Object> model) {
        List<Employee> users = employeeService.getEmployees();
        model.put("users", users);
    }
}
