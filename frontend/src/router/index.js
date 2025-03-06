import { createRouter, createWebHistory } from 'vue-router'
import TaskList from '../views/TaskList.vue'
import TaskForm from '../views/TaskForm.vue'

const routes = [
  {
    path: '/',
    name: 'tasks',
    component: TaskList
  },
  {
    path: '/create',
    name: 'create-task',
    component: TaskForm
  },
  {
    path: '/edit/:id',
    name: 'edit-task',
    component: TaskForm,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router