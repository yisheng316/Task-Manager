package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // Convert Task entity to TaskDto
    private TaskDto convertToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    // Convert TaskDto to Task entity
    private Task convertToEntity(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .completed(taskDto.isCompleted())
                .build();
    }

    // Create a new task
    public TaskDto createTask(TaskDto taskDto) {
        Task task = convertToEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    // Get all tasks
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get task by ID
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    // Update a task
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setCompleted(taskDto.isCompleted());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDto(updatedTask);
    }

    // Mark a task as completed
    public TaskDto markTaskAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        task.setCompleted(true);

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    // Delete a task
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}