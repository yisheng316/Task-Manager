<template>
  <div class="app">
    <header class="app-header">
      <h1>Task Management</h1>
      <nav>
        <router-link to="/">Task List</router-link> |
        <router-link to="/create">Add New Task</router-link>
      </nav>
    </header>
    <main class="app-main">
      <router-view />
    </main>
    <footer class="app-footer">
      <p>&copy; 2025 LHN Group - Customer Service Operations</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
:root {
  --primary-color: #3498db;
  --secondary-color: #2c3e50;
  --success-color: #2ecc71;
  --danger-color: #e74c3c;
  --light-color: #ecf0f1;
  --dark-color: #34495e;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  color: var(--secondary-color);
  background-color: var(--light-color);
}

.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  background-color: var(--secondary-color);
  color: white;
  padding: 1rem;
  text-align: center;
}

.app-header h1 {
  margin-bottom: 1rem;
}

.app-header nav {
  margin-bottom: 0.5rem;
}

.app-header a {
  color: white;
  text-decoration: none;
  margin: 0 0.5rem;
  padding: 0.25rem 0.5rem;
}

.app-header a:hover,
.app-header a.router-link-active {
  color: var(--primary-color);
}

.app-main {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.app-footer {
  background-color: var(--secondary-color);
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}

@media (max-width: 768px) {
  .app-main {
    padding: 1rem;
  }
}
</style>