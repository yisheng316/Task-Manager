package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private TaskDto task1;
    private TaskDto task2;
    private TaskDto newTaskDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        task1 = TaskDto.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Test Description 1")
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        task2 = TaskDto.builder()
                .id(2L)
                .title("Test Task 2")
                .description("Test Description 2")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        newTaskDto = TaskDto.builder()
                .title("New Task")
                .description("New Description")
                .completed(false)
                .build();
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() throws Exception {
        List<TaskDto> tasks = Arrays.asList(task1, task2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Task 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test Task 2")));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_WithValidId_ShouldReturnTask() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task1);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Task 1")));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new EntityNotFoundException("Task not found with id: 99"));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(99L);
    }

    @Test
    void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskDto createdTask = TaskDto.builder()
                .id(3L)
                .title("New Task")
                .description("New Description")
                .completed(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskService.createTask(any(TaskDto.class))).thenReturn(createdTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTaskDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(taskService, times(1)).createTask(any(TaskDto.class));
    }

    @Test
    void updateTask_WithValidId_ShouldReturnUpdatedTask() throws Exception {
        TaskDto updateDto = TaskDto.builder()
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .build();

        TaskDto updatedTask = TaskDto.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDto.class));
    }

    @Test
    void updateTask_WithInvalidId_ShouldReturnNotFound() throws Exception {
        TaskDto updateDto = TaskDto.builder()
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .build();

        when(taskService.updateTask(eq(99L), any(TaskDto.class)))
                .thenThrow(new EntityNotFoundException("Task not found with id: 99"));

        mockMvc.perform(put("/api/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(99L), any(TaskDto.class));
    }

    @Test
    void markTaskAsCompleted_WithValidId_ShouldReturnCompletedTask() throws Exception {
        TaskDto completedTask = TaskDto.builder()
                .id(1L)
                .title("Test Task 1")
                .description("Test Description 1")
                .completed(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(taskService.markTaskAsCompleted(1L)).thenReturn(completedTask);

        mockMvc.perform(patch("/api/tasks/complete/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(taskService, times(1)).markTaskAsCompleted(1L);
    }

    @Test
    void markTaskAsCompleted_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(taskService.markTaskAsCompleted(99L))
                .thenThrow(new EntityNotFoundException("Task not found with id: 99"));

        mockMvc.perform(patch("/api/tasks/complete/99"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).markTaskAsCompleted(99L);
    }

    @Test
    void deleteTask_WithValidId_ShouldReturnNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_WithInvalidId_ShouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Task not found with id: 99"))
                .when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(99L);
    }
}