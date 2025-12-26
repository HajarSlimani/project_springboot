# Plant Manager Backend (Spring Boot 3, Java 17)

Features:
- CRUD: Plants, Tasks, Logs with DTO mapping
- Auth: Register/Login (email+password), JWT, Google Sign-In (GSI credential) and optional OAuth2 code callback
- Security: Spring Security + JWT filter, CORS
- MySQL persistence (Hibernate)

## Configure
Set environment variables or edit `src/main/resources/application.yml`:
- `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS` (default `http://localhost:4200`)
- `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` (for code callback), `redirect-uri`

## Run
```powershell
# From backend folder
mvn spring-boot:run
```

## REST Endpoints
- Auth: `POST /auth/register`, `POST /auth/login`, `POST /auth/google`, `GET /auth/google/callback?code=...`, `GET /auth/me`
- Plants: `GET /plants`, `GET /plants/{id}`, `POST /plants`, `PUT /plants/{id}`, `DELETE /plants/{id}`
- Tasks: `GET /tasks/plant/{plantId}`, `POST /tasks`, `PUT /tasks/{id}`, `DELETE /tasks/{id}`
- Logs: `GET /logs/plant/{plantId}`, `POST /logs`, `PUT /logs/{id}`, `DELETE /logs/{id}`

## Google Sign-In (Frontend)
Use Google Identity Services (GSI) to get an ID token credential and POST to `/auth/google`.

Example (Angular): see `../frontend/README.md` and blueprints.
