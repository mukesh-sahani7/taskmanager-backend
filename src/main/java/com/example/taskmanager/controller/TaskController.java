package com.example.taskmanager.controller;

import com.example.taskmanager.model.task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.taskmanager.model.task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Taaki aapka React/Frontend bina CORS error ke ise access kar sake
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // 1. Saare tasks ko get karne ke liye (Read)

    @GetMapping
    public List<com.example.taskmanager.model.task> getAllTasks(@RequestParam Long userId) {
        // Ab yeh findAll() nahi karega, balki sirf us userId ke tasks laayega
        return taskRepository.findByUserId(userId);
    }
    // 2. Naya task create karne ke liye (Create)

    @Autowired
    private com.example.taskmanager.repository.UserRepository userRepository; // UserRepository ko inject karein

    // Naya task create karne ke liye (Updated with User mapping)
    @PostMapping
    public task createTask(@RequestBody task task, @RequestParam Long userId) {
        // Database se user ko dhoodhenge aur task ke sath jodenge
        com.example.taskmanager.model.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    // 3. Kisi task ko delete karne ke liye (Delete)
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "Task with ID " + id + " has been deleted successfully.";
    }

    // 4. Task ko update karne ke liye (Update status, title, etc.)
    @PutMapping("/{id}")
    public task updateTask(@PathVariable Long id, @RequestBody task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setStatus(updatedTask.getStatus());
                    task.setPriority(updatedTask.getPriority());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }
}
