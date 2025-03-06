package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class TaskController {

    private final TaskService taskService;

    // Create a new task
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        TaskDto createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // Update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        TaskDto updatedTask = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    // Mark a task as completed
    @PatchMapping("/complete/{id}")
    public ResponseEntity<TaskDto> markTaskAsCompleted(@PathVariable Long id) {
        TaskDto completedTask = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(completedTask);
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}