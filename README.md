# OpenTrack

OpenTrack is a web app that helps track open source work on GitHub. It collects activity like commits, pull requests, issues, and code reviews, then turns that activity into simple scores, reports, and leaderboards.

## What this project does

This project is built to make open source contribution data easier to understand. Instead of checking GitHub activity one page at a time, OpenTrack puts the important details in one place through a backend API, a small frontend, and a scoring system.

OpenTrack can:
- Track contributor activity from GitHub
- Save contributor data in a database
- Score different kinds of contribution events
- Show ranked leaderboards
- Display contributor profiles and stats
- Generate weekly and monthly reports
- Support login with JWT-based authentication

## Tech stack

The backend uses Java 17, Spring Boot 3, Spring Security, Spring Data JPA, MySQL, Redis, Flyway, and the GitHub REST API. The frontend uses HTML, CSS, JavaScript, and Chart.js for simple charts and profile views.

| Part | Tools used |
|------|------------|
| Backend | Java 17, Spring Boot 3, Spring Security, Spring Data JPA |
| Database | MySQL |
| Cache | Redis |
| Migrations | Flyway |
| Frontend | HTML, CSS, JavaScript, Chart.js |
| Auth | JWT |
| Local setup | Docker, Docker Compose, Maven |

## Project structure

The project has a backend in `src/` and a frontend in `frontend/`. It also includes Docker files, database migrations, tests, and common project files like `.gitignore`, `README`, and contribution guides.

```text
opentrack/
├── frontend/
│  ├── css/
│  ├── js/
│  ├── index.html
│  ├── leaderboard.html
│  ├── profile.html
│  ├── login.html
│  └── register.html
├── src/
│  ├── main/java/com/opentrack/
│  │  ├── config/
│  │  ├── controller/
│  │  ├── dto/
│  │  ├── exception/
│  │  ├── model/
│  │  ├── repository/
│  │  ├── scheduler/
│  │  ├── service/
│  │  └── util/
│  └── resources/db/migration/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Main features

### 1. Contributor tracking
OpenTrack stores contributors, organizations, and contribution events such as commits, pull requests, issues, reviews, forks, and releases. This makes it easier to measure how active a contributor has been over time.

### 2. Scoring system
The app gives points for different kinds of work so contributors can be ranked in a leaderboard. For example, commits, pull requests, reviews, and releases all have different score values in the backend scoring service.

### 3. Reports
The project includes report endpoints for weekly and monthly summaries. These reports can show contributor scores and event counts during a selected time range.

### 4. Authentication
Users can register, log in, and link their GitHub account. The backend uses Spring Security and JWT tokens to protect private endpoints.

### 5. Frontend pages
The frontend includes a home page, leaderboard page, profile page, login page, and register page. It also includes JavaScript files for API calls, charts, authentication, and leaderboard rendering.

## API overview

The backend includes endpoints for authentication, users, contributors, sync actions, and reports. These endpoints are designed so the frontend can show profiles, leaderboards, and reports without too much extra logic.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/auth/register` | Create a new account |
| POST | `/auth/login` | Log in and get a JWT token |
| GET | `/users/me` | Get current user details |
| PUT | `/users/me/github` | Link a GitHub username |
| GET | `/contributors` | Get the leaderboard |
| GET | `/contributors/{username}` | Get contributor details |
| GET | `/contributors/{username}/stats` | Get contributor stats |
| POST | `/sync/contributor/{username}` | Run a manual sync |
| GET | `/reports/monthly` | Get a monthly report |
| GET | `/reports/weekly/{username}` | Get a weekly report |

## How to run it locally

This project is set up for local development with Docker, MySQL, Redis, and Maven. A simple local setup can look like this:

1. Copy `.env.example` to `.env`.
2. Add your database password, GitHub token, and JWT secret.
3. Start MySQL and Redis with Docker Compose.
4. Run the Spring Boot app.
5. Open the frontend files in a browser.

```bash
cp .env.example .env
docker-compose up mysql redis -d
./mvnw spring-boot:run
```

Backend base path:

```text
http://localhost:8080/api/v1
```

Swagger docs path:

```text
http://localhost:8080/api/v1/swagger-ui/index.html
```

## Database and caching

The database schema is managed with Flyway migration files named `V1__init_schema.sql`, `V2__add_indexes.sql`, and `V3__seed_data.sql`. Redis is used for caching things like GitHub event data and leaderboard results so the app can respond faster and avoid extra API calls.

## Testing

The project includes tests for services and controllers such as scoring, GitHub integration, authentication, contributor endpoints, and leaderboard logic. These tests help check that the main features work as expected.

## Why this project is useful

OpenTrack is useful for student clubs, open source communities, hackathon teams, and organizations that want a simple way to track contribution activity. It can also be used as a learning project for students who want practice with Spring Boot, REST APIs, authentication, caching, database design, and frontend integration.

## Notes

This project is meant for local development and code sharing. It should not be deployed unless secrets, security settings, production database setup, and GitHub API limits are handled properly.

## License

MIT