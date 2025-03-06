<template>
  <div class="task-list">
    <h2>Task List</h2>

    <div class="task-actions">
      <button class="btn btn-primary" @click="$router.push('/create')">
        Add New Task
      </button>
    </div>

    <div v-if="loading" class="loading">
      Loading tasks...
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else-if="tasks.length === 0" class="no-tasks">
      No tasks found. Create a new task to get started.
    </div>

    <div v-else class="task-cards">
      <div v-for="task in tasks" :key="task.id" class="task-card" :class="{ 'completed': task.completed }">
        <div class="task-card-header">
          <h3>{{ task.title }}</h3>
          <div class="task-status">
            <span v-if="task.completed" class="status completed">Completed</span>
            <span v-else class="status pending">Pending</span>
          </div>
        </div>

        <div class="task-card-content">
          <p>{{ task.description }}</p>
        </div>

        <div class="task-card-footer">
          <div class="task-dates">
            <small>Created: {{ formatDate(task.createdAt) }}</small>
            <small v-if="task.updatedAt">Last updated: {{ formatDate(task.updatedAt) }}</small>
          </div>

          <div class="task-actions">
            <button v-if="!task.completed" @click="completeTask(task.id)" class="btn btn-success">
              Mark Complete
            </button>
            <button @click="$router.push(`/edit/${task.id}`)" class="btn btn-info">
              Edit
            </button>
            <button @click="deleteTask(task.id)" class="btn btn-danger">
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TaskService from '../services/TaskService';

export default {
  name: 'TaskList',
  data() {
    return {
      tasks: [],
      loading: true,
      error: null
    };
  },
  created() {
    this.fetchTasks();
  },
  methods: {
    fetchTasks() {
      this.loading = true;
      TaskService.getAllTasks()
        .then(response => {
          this.tasks = response.data;
          this.loading = false;
        })
        .catch(error => {
          this.error = 'Error fetching tasks: ' + (error.response?.data?.message || error.message);
          this.loading = false;
        });
    },
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleString();
    },
    completeTask(id) {
      TaskService.markTaskCompleted(id)
        .then(() => {
          this.fetchTasks();
        })
        .catch(error => {
          this.error = 'Error completing task: ' + (error.response?.data?.message || error.message);
        });
    },
    deleteTask(id) {
      if (confirm('Are you sure you want to delete this task?')) {
        TaskService.deleteTask(id)
          .then(() => {
            this.fetchTasks();
          })
          .catch(error => {
            this.error = 'Error deleting task: ' + (error.response?.data?.message || error.message);
          });
      }
    }
  }
};
</script>

<style scoped>
.task-list {
  max-width: 100%;
}

.task-actions {
  margin-bottom: 1rem;
  display: flex;
  justify-content: flex-end;
}

.task-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
}

.task-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  transition: all 0.3s ease;
}

.task-card.completed {
  background-color: #f8f9fa;
  border-left: 4px solid var(--success-color);
}

.task-card:not(.completed) {
  border-left: 4px solid var(--primary-color);
}

.task-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.task-status .status {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: bold;
}

.status.completed {
  background-color: var(--success-color);
  color: white;
}

.status.pending {
  background-color: var(--primary-color);
  color: white;
}

.task-card-content {
  margin-bottom: 1rem;
}

.task-card-footer {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.task-dates {
  display: flex;
  flex-direction: column;
  font-size: 0.8rem;
  color: #6c757d;
}

.task-actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.375rem 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-success {
  background-color: var(--success-color);
  color: white;
}

.btn-info {
  background-color: #17a2b8;
  color: white;
}

.btn-danger {
  background-color: var(--danger-color);
  color: white;
}

.btn:hover {
  opacity: 0.9;
}

.loading, .error, .no-tasks {
  text-align: center;
  margin: 2rem 0;
}

.error {
  color: var(--danger-color);
}

@media (max-width: 768px) {
  .task-cards {
    grid-template-columns: 1fr;
  }

  .task-card-header {
    flex-direction: column;
  }

  .task-status {
    margin-top: 0.5rem;
  }
}
</style>