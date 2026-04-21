# ⟨/⟩ OpenTrack

**Open Source Contribution Tracker** — Track GitHub activity, score contributors, and celebrate open source work.

## What is OpenTrack?

OpenTrack is a Spring Boot backend + HTML/JS frontend that:
- Fetches GitHub events (commits, PRs, issues, reviews) via the GitHub REST API
- Scores contributors using a weighted algorithm
- Displays real-time leaderboards with pagination and filtering
- Generates monthly and weekly contribution reports
- JWT-based authentication with role-based access

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA |
| Database | MySQL 8.0 + Flyway migrations |
| Cache | Redis (API rate limits + leaderboard caching) |
| Frontend | HTML5, CSS3, Vanilla JS, Chart.js |
| Auth | JWT (JSON Web Tokens) |
| Dev Tools | Docker, Maven |

## Quick Start

### Prerequisites
- Java 17+, Docker + Docker Compose
- GitHub Personal Access Token

### Run Locally

```bash
cp .env.example .env
# Edit .env and set GITHUB_TOKEN and JWT_SECRET

docker-compose up mysql redis -d
./mvnw spring-boot:run
```

API: `http://localhost:8080/api/v1`

### Frontend
Open `frontend/index.html` in your browser.

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /auth/register | No | Register user |
| POST | /auth/login | No | Login, returns JWT |
| GET | /contributors | No | Get leaderboard |
| GET | /contributors/{username}/stats | No | Get contributor stats |
| POST | /sync/contributor/{username} | Yes | Trigger GitHub sync |
| GET | /reports/monthly | Yes | Monthly report |
| GET | /reports/weekly/{username} | Yes | Weekly report |

Swagger UI: `http://localhost:8080/api/v1/swagger-ui/index.html`

## Scoring Algorithm

| Event | Points |
|-------|--------|
| Commit | 3 |
| PR Opened | 5 |
| PR Merged | 10 |
| Issue Opened | 2 |
| Issue Closed | 4 |
| Code Review | 6 |
| Release | 15 |

## License
MIT