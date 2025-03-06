<template>
  <div class="task-form">
    <h2>{{ isEditMode ? 'Edit Task' : 'Create New Task' }}</h2>

    <div v-if="loading" class="loading">
      Loading...
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <form v-else @submit.prevent="saveTask">
      <div class="form-group">
        <label for="title">Title</label>
        <input
          type="text"
          id="title"
          v-model="task.title"
          required
          placeholder="Enter task title"
          class="form-control"
        />
      </div>

      <div class="form-group">
        <label for="description">Description</label>
        <textarea
          id="description"
          v-model="task.description"
          placeholder="Enter task description"
          class="form-control"
          rows="4"
        ></textarea>
      </div>

      <div v-if="isEditMode" class="form-group checkbox">
        <label>
          <input type="checkbox" v-model="task.completed" />
          Mark as completed
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="btn btn-secondary" @click="$router.push('/')">
          Cancel
        </button>
        <button type="submit" class="btn btn-primary">
          {{ isEditMode ? 'Update Task' : 'Create Task' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import TaskService from '../services/TaskService';

export default {
  name: 'TaskForm',
  props: {
    id: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      task: {
        title: '',
        description: '',
        completed: false
      },
      loading: false,
      error: null
    };
  },
  computed: {
    isEditMode() {
      return !!this.id;
    }
  },
  created() {
    if (this.isEditMode) {
      this.fetchTask();
    }
  },
  methods: {
    fetchTask() {
      this.loading = true;
      TaskService.getTaskById(this.id)
        .then(response => {
          this.task = response.data;
          this.loading = false;
        })
        .catch(error => {
          this.error = 'Error fetching task: ' + (error.response?.data?.message || error.message);
          this.loading = false;
        });
    },
    saveTask() {
      this.loading = true;

      const savePromise = this.isEditMode
        ? TaskService.updateTask(this.id, this.task)
        : TaskService.createTask(this.task);

      savePromise
        .then(() => {
          this.$router.push('/');
        })
        .catch(error => {
          this.error = `Error ${this.isEditMode ? 'updating' : 'creating'} task: ` +
            (error.response?.data?.message || error.message);
          this.loading = false;
        });
    }
  }
};
</script>

<style scoped>
.task-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 1rem;
}

.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
  box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
}

.checkbox {
  display: flex;
  align-items: center;
}

.checkbox input {
  margin-right: 0.5rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
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

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn:hover {
  opacity: 0.9;
}

.loading, .error {
  text-align: center;
  margin: 2rem 0;
}

.error {
  color: var(--danger-color);
}

@media (max-width: 768px) {
  .task-form {
    padding: 0 1rem;
  }
}
</style>