package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private TaskDto taskDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        task1 = Task.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Test Description 1")
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        task2 = Task.builder()
                .id(2L)
                .title("Test Task 2")
                .description("Test Description 2")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        taskDto = TaskDto.builder()
                .id(null)
                .title("New Task")
                .description("New Description")
                .completed(false)
                .build();
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<TaskDto> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        assertEquals("Test Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_WithValidId_ShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        TaskDto result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task 1", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_WithInvalidId_ShouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.getTaskById(99L);
        });
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() {
        Task savedTask = Task.builder()
                .id(3L)
                .title("New Task")
                .description("New Description")
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto result = taskService.createTask(taskDto);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertFalse(result.isCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_WithValidId_ShouldReturnUpdatedTask() {
        TaskDto updateDto = TaskDto.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .build();

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskDto result = taskService.updateTask(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.isCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_WithInvalidId_ShouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.updateTask(99L, taskDto);
        });
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void markTaskAsCompleted_WithValidId_ShouldMarkTaskCompleted() {
        Task completedTask = Task.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Test Description 1")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(completedTask);

        TaskDto result = taskService.markTaskAsCompleted(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.isCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsCompleted_WithInvalidId_ShouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.markTaskAsCompleted(99L);
        });
        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WithValidId_ShouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_WithInvalidId_ShouldThrowException() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.deleteTask(99L);
        });
        verify(taskRepository, times(1)).existsById(99L);
        verify(taskRepository, never()).deleteById(any());
    }
}