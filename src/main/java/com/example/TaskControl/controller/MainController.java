package com.example.TaskControl.controller;

import com.example.TaskControl.domain.Employee;
import com.example.TaskControl.domain.Task;
import com.example.TaskControl.service.EmployeeService;
import com.example.TaskControl.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    TaskService taskService;

    String role;
    Long id;

    @PostMapping
    public String login(@RequestParam String login, @RequestParam String password, Map<String, Object> model) {
        if (login != null && password != null && !login.trim().equals("") && !password.trim().equals("")) {
            Employee employee = employeeService.getEmployeeByLogPass(login, password);
            if (employee != null) {
                role = employee.getRole();
                id = employee.getId();
                return getMainPage(model);
            }
        }
        model.put("errorMessage", "Incorrect login or password");
        return "errorPage";
    }

    //creating

    @GetMapping("newEmployee")
    public String newEmployee(Map<String, Object> model) {
        reloadUsers(model);
        reloadTasks(model);
        return "employee";
    }

    @GetMapping("newTask")
    public String newTask(Map<String, Object> model) {
        reloadUsers(model);
        return "task";
    }

    @PostMapping("addEmployee")
    public String addEmployee(@RequestParam String role,@RequestParam String name, @RequestParam String login, @RequestParam String password,  Map<String, Object> model) {
        Employee employee = new Employee(name, login, password, role);
        employeeService.saveOrUpdateEmployee(employee);
        reloadUsers(model);
        reloadTasks(model);
        return getMainPage(model);
    }

    @PostMapping("addTask")
    public String addTask(@RequestParam String userId,
                          @RequestParam String description,
                          @RequestParam Date endDate,
                          @RequestParam Date beginDate,
                          @RequestParam String priority,
                          @RequestParam String status,
                          Map<String, Object> model) {
        String id = userId.substring(0, userId.indexOf(' '));
        Employee employee = employeeService.getEmployeeById(Long.parseLong(id));
        Task task = new Task(description, beginDate, endDate, priority, status, employee);
        taskService.saveOrUpdateTask(task);
        return getMainPage(model);
    }

    //showing

    @GetMapping("employees")
    public String listOfEmployees(Map<String, Object> model) {
        reloadUsers(model);
        return "listOfEmployees";
    }

    //updating

    @GetMapping("tasks/new/{id}")
    public String updateTask(@PathVariable String id, Map<String, Object> model) {
        System.out.println("id is: " + id);
        if (id == null)
            return "task";
        else
            return login(model);
    }

    //deleting

    @PostMapping("deleteTask/{taskId}")
    private String deleteTask(@PathVariable("taskId") int taskId, Map<String, Object> model) {
        taskService.deleteTask(taskId);
        return "redirect:/" + getMainPage(model);
    }

    @PostMapping("employees/deleteEmployee/{empId}")
    private String deleteEmployee(@PathVariable("empId") long empId, Map<String, Object> model) {
        employeeService.deleteEmployee(empId);
        return "redirect:/" + getMainPage(model);
    }

    @GetMapping
    public String login(Map<String, Object> model) {
        reloadUsers(model);
        return "login";
    }


    private void reloadUsers(Map<String, Object> model) {
        List<Employee> users = employeeService.getEmployees();
        model.put("users", users);
    }

    private void reloadTasks(Map<String, Object> model) {
        List<Task> tasks = taskService.getTasks();
        model.put("tasks", tasks);
    }

    @GetMapping("adminPage")
    public String adminPage(Map<String, Object> model){
        reloadUsers(model);
        reloadTasks(model);

        return "adminPage";
    }

    @GetMapping("hr")
    public String hrPage(Map<String, Object> model){
        reloadUsers(model);
        reloadTasks(model);
        return "hrPage";
    }

    @GetMapping("user")
    public String userPage(Map<String, Object> model){
        reloadUsers(model);
        reloadTasks(model);
        return "userPage";
    }

    private String getMainPage(Map<String, Object> model) {
        if (role != null) {
            if (role.equals("admin")) {
                return adminPage(model);
            } else if (role.equals("hr")) {
                return hrPage(model);
            } else
                return userPage(model);
        }
        return "errorPage";
    }
}
