package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findById_ShouldReturnTask() {
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        entityManager.persistAndFlush(task);

        Optional<Task> found = taskRepository.findById(task.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
    }

    @Test
    void findAll_ShouldReturnAllTasks() {
        Task task1 = Task.builder()
                .title("Test Task 1")
                .description("Test Description 1")
                .completed(false)
                .build();

        Task task2 = Task.builder()
                .title("Test Task 2")
                .description("Test Description 2")
                .completed(true)
                .build();

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.flush();

        List<Task> tasks = taskRepository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    void save_ShouldPersistTask() {
        Task task = Task.builder()
                .title("New Task")
                .description("New Description")
                .completed(false)
                .build();

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());

        Task persistedTask = entityManager.find(Task.class, savedTask.getId());
        assertNotNull(persistedTask);
        assertEquals("New Task", persistedTask.getTitle());
    }

    @Test
    void delete_ShouldRemoveTask() {
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        entityManager.persistAndFlush(task);

        taskRepository.deleteById(task.getId());

        // Assert
        Task deletedTask = entityManager.find(Task.class, task.getId());
        assertNull(deletedTask);
    }

    @Test
    void lifecycleHooks_ShouldSetTimestamps() {
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getCreatedAt());
        assertNotNull(savedTask.getUpdatedAt());

        savedTask.setTitle("Updated Title");
        Task updatedTask = taskRepository.save(savedTask);

        assertNotNull(updatedTask.getUpdatedAt());
    }
}