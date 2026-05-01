# Team Task Manager

A practical full-stack student project using React + Spring Boot + MySQL + JWT authentication.

## Backend Structure

backend/
- pom.xml
- src/main/resources/application.properties
- src/main/java/com/teamtaskmanager/TeamTaskManagerApplication.java
- src/main/java/com/teamtaskmanager/config/SecurityConfig.java
- src/main/java/com/teamtaskmanager/controller/
  - AuthController.java
  - UserController.java
  - ProjectController.java
  - TaskController.java
  - DashboardController.java
- src/main/java/com/teamtaskmanager/service/
  - CustomUserDetailsService.java
  - AuthService.java
  - UserService.java
  - ProjectService.java
  - TaskService.java
  - DashboardService.java
- src/main/java/com/teamtaskmanager/security/
  - JwtUtil.java
  - JwtAuthFilter.java
- src/main/java/com/teamtaskmanager/entity/
  - User.java
  - Project.java
  - Task.java
  - Role.java
  - TaskStatus.java
  - Priority.java
- src/main/java/com/teamtaskmanager/repository/
  - UserRepository.java
  - ProjectRepository.java
  - TaskRepository.java
- src/main/java/com/teamtaskmanager/dto/
  - LoginRequest.java
  - SignupRequest.java
  - AuthResponse.java
  - ProjectRequest.java
  - TaskRequest.java
  - TaskStatusUpdateRequest.java
- src/main/java/com/teamtaskmanager/exception/
  - BadRequestException.java
  - NotFoundException.java
  - GlobalExceptionHandler.java

## Frontend Structure

frontend/
- package.json
- .env.example
- index.html
- vite.config.js
- src/main.jsx
- src/App.jsx
- src/api.js
- src/auth.js
- src/styles.css
- src/components/
  - ProtectedRoute.jsx
  - NavBar.jsx
- src/pages/
  - LoginPage.jsx
  - SignupPage.jsx
  - DashboardPage.jsx
  - ProjectsPage.jsx
  - TasksPage.jsx

## Setup

### 1) Backend

1. Create MySQL database (or rely on auto create): `team_task_manager`
2. Set env vars (optional, for production/Railway):
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `JWT_SECRET`
   - `JWT_EXPIRATION_MS`
   - `CORS_ALLOWED_ORIGIN`
3. Run backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### 2) Frontend

1. Copy env file:
   ```bash
   cd frontend
   copy .env.example .env
   ```
2. Install and run:
   ```bash
   npm install
   npm run dev
   ```

## Sample API Endpoints

Auth:
- `POST /api/auth/signup`
- `POST /api/auth/login`

Users (ADMIN):
- `GET /api/users`

Projects:
- `POST /api/projects` (ADMIN)
- `GET /api/projects`
- `POST /api/projects/{projectId}/members/{userId}` (ADMIN)
- `DELETE /api/projects/{projectId}/members/{userId}` (ADMIN)

Tasks:
- `POST /api/tasks/project/{projectId}` (ADMIN)
- `GET /api/tasks/project/{projectId}`
- `GET /api/tasks/me`
- `PATCH /api/tasks/{taskId}/status` (assigned MEMBER only)

Dashboard:
- `GET /api/dashboard`

## Railway Notes

Backend is Railway-friendly:
- Uses `server.port=${PORT:8080}`
- Uses env-driven DB and JWT config
- No server-side session dependency (JWT only)

## Quick Test Flow

1. Signup an ADMIN user.
2. Create project.
3. Signup MEMBER user.
4. Add MEMBER to project.
5. Create task and assign to MEMBER.
6. Login as MEMBER and update task status.
7. Check dashboard stats.
