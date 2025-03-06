# Task Management Application
A simple task management application for the customer service operations department. This application allows users to add new tasks, view all tasks, and mark tasks as completed.
## Technologies Used

### Backend:

- Spring Boot 3.4.x
- Spring Data JPA
- H2 Database
- Java 17

### Frontend:

- Vue.js 3
- Vue Router
- Axios for API requests

## Features

- Create, view, update, and delete tasks
- Mark tasks as completed

## Project Structure
```
task-management/
├── backend/                # Spring Boot application
│   ├── src/                # Java source code
│   └── pom.xml             # Maven dependencies
├── frontend/               # Vue.js application
│   ├── public/             # Static assets
│   ├── src/                # Vue source code
│   └── package.json        # NPM dependencies
└── README.md               # Documentation
```
## Setup and Installation
### Prerequisites

- Java 17 or higher
- Maven
- Node.js (v16+) and npm
- H2 Database (embedded)

## Backend Setup

### Clone the repository:
```bash
    git clone https://github.com/yisheng316/Task-Manager.git
```

### Run the backend:
```
cd backend/taskmanager
./mvnw spring-boot:run
```
- The backend will start on http://localhost:8080
- H2 console will be available at http://localhost:8080/h2-console

### Accessing H2 Database Console
You can view and interact with the database directly at http://localhost:8080/h2-console with these settings:

- JDBC URL: jdbc:h2:mem:taskmanager
- Username: sa
- Password: (leave empty)

### Run the tests
```
cd backend/taskmanager
./mvnw test
```

## Frontend Setup

### Install dependencies:
```
cd frontend
npm install
```

### Run the frontend:
```
npm run serve
```
The frontend will start on http://localhost:8081

## API Endpoints
All API endpoints are prefixed with /api/tasks.

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get a task by ID
- `POST /api/tasks` - Create a new task
- `PUT /api/tasks/{id}` - Update a task
- `PATCH /api/tasks/complete/{id}` - Mark a task as completed
- `DELETE /api/tasks/{id}` - Delete a task
