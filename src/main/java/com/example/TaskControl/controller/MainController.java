package com.example.TaskControl.controller;

import com.example.TaskControl.domain.Employee;
import com.example.TaskControl.domain.Task;
import com.example.TaskControl.service.EmployeeService;
import com.example.TaskControl.service.TaskService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Date;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    @Value("${upload.path}")
    private String upPath;
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
    public String addEmployee(@RequestParam String role,
                              @RequestParam String name,
                              @RequestParam String login,
                              @RequestParam String password,
                              @RequestParam String surName,
                              @RequestParam String middleName,
                              @RequestParam String phone,
                              Map<String, Object> model) {
        Employee employee = new Employee(name, surName, middleName, phone, login, password, role);
        employeeService.saveOrUpdateEmployee(employee);
        reloadUsers(model);
        reloadTasks(model);
        return getMainPage(model);
    }

    @PostMapping("employees/addEmployee")
    public String addEmployee(@RequestParam Long empId,
                              @RequestParam String role,
                              @RequestParam String name,
                              @RequestParam String login,
                              @RequestParam String password,
                              @RequestParam String surName,
                              @RequestParam String middleName,
                              @RequestParam String phone,
                              Map<String, Object> model) {
        try {
            Employee employee = employeeService.getEmployeeById(empId);
            employee.setLogin(login);
            employee.setPassword(password);
            employee.setMiddleName(middleName);
            employee.setPhone(phone);
            employee.setSurName(surName);
            employee.setRole(role);
            employee.setName(name);
            employeeService.saveOrUpdateEmployee(employee);
        } catch (Exception ex) {
            return "errorPage";
        }
        reloadUsers(model);
        reloadTasks(model);
        return "redirect:/" + getMainPage(model);
    }

    @PostMapping("addTask")
    public String addTask(@RequestParam String userId,
                          @RequestParam String description,
                          @RequestParam Date endDate,
                          @RequestParam Date beginDate,
                          @RequestParam String priority,
                          @RequestParam String status,
                          @RequestParam String report,
                          Map<String, Object> model) {
        String id = userId.substring(0, userId.indexOf(' '));
        Employee employee = employeeService.getEmployeeById(Long.parseLong(id));
        Task task = new Task(description, beginDate, endDate, priority, status, employee);
        taskService.saveOrUpdateTask(task);
        return getMainPage(model);
    }

    @GetMapping("updateTaskByUser/{id}")
    public String updateTaskByUser(@PathVariable int id, Map<String, Object> model) {
        model.put("toEditTask", taskService.getTaskById(id));
        reloadUsers(model);
        reloadTasks(model);
        return "editTaskByUser";
    }

    @PostMapping("updateTaskByUser/addTask")
    public String addTask(@RequestParam Integer taskId,
                          @RequestParam String description,
                          @RequestParam String status,
                          @RequestParam MultipartFile report,
                          Map<String, Object> model) {
        try {
            Task task = taskService.getTaskById(taskId);
            task.setDescription(description);
            task.setStatus(status);
            task.setReport(report.getOriginalFilename());
            taskService.saveOrUpdateTask(task);

            report.transferTo(new File(upPath + "/" + report.getOriginalFilename() + ".test"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/" + getMainPage(model);
    }

    @PostMapping("updateTask/addTask")
    public String addTask(@RequestParam Integer taskId,
                          @RequestParam String userId,
                          @RequestParam String description,
                          @RequestParam Date endDate,
                          @RequestParam Date beginDate,
                          @RequestParam String priority,
                          @RequestParam String status,
                          Map<String, Object> model) {
        String id = userId.substring(0, userId.indexOf(' '));
        Employee employee = employeeService.getEmployeeById(Long.parseLong(id));
        Task task;
        try {
            task = taskService.getTaskById(taskId);
            task.setDescription(description);
            task.setEndDate(endDate);
            task.setBeginDate(beginDate);
            task.setPriority(priority);
            task.setStatus(status);
            task.setExecutor(employee);
        } catch (Exception ex) {
            task = new Task(description, beginDate, endDate, priority, status, employee);
        }
        taskService.saveOrUpdateTask(task);
        return "redirect:/" + getMainPage(model);
    }

    //showing

    @GetMapping("employees")
    public String listOfEmployees(Map<String, Object> model) {
        reloadUsers(model);
        reloadTasks(model);
        return "listOfEmployees";
    }

    //updating
    @GetMapping("updateTask/{id}")
    public String updateTask(@PathVariable int id, Map<String, Object> model) {
        model.put("toEditTask", taskService.getTaskById(id));
        reloadUsers(model);
        reloadTasks(model);
        return "editTask";
    }

    @GetMapping("employees/{id}")
    public String updateEmployee(@PathVariable long id, Map<String, Object> model) {
        model.put("toEditUser", employeeService.getEmployeeById(id));
        reloadUsers(model);
        reloadTasks(model);
        return "editEmployee";
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

    @GetMapping("hrPage")
    public String hrPage(Map<String, Object> model){
        reloadUsers(model);
        reloadTasks(model);
        return "hrPage";
    }

    @GetMapping("userPage")
    public String userPage(Map<String, Object> model){
        reloadUsers(model);
        reloadTasks(model);
        Employee employee = employeeService.getEmployeeById(id);

        if (employee != null) {
            model.put("currentUser", employee);
            return "userPage";
        }
        model.put("errorMessage", "can't login by user");
        return "errorPage";
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
