package com.example.TaskControl.service;

import com.example.TaskControl.domain.Task;
import com.example.TaskControl.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public void saveOrUpdateTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> getTasks() { return taskRepository.findAll(); }

    public Task getTaskById(Integer id) { return taskRepository.getById(id); }

    public void deleteTask(Integer id) { taskRepository.deleteById(id); }
}
