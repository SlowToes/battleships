# Battleships Online

A real-time, multiplayer Battleships game where two players place ships on a 10x10 grid and take turns firing at each other's fleet. Built with a **Spring Boot** backend, a **Next.js** frontend, and **WebSocket (STOMP)** for live game updates.

> **Play now:** [battleships-lime-beta.vercel.app](https://battleships-lime-beta.vercel.app/)

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [API Reference](#api-reference)
- [Running Tests](#running-tests)
- [Deployment](#deployment)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Authors](#authors)
- [Project Status](#project-status)

---

## Features

- **Guest & registered accounts** — jump straight in as a guest or sign up for a persistent identity.
- **Game lobby** — create a new game and share the game ID, or join an existing one.
- **Interactive ship placement** — drag-and-drop placement for Carrier (5), Battleship (4), Destroyer (3), Submarine (3), and Patrol Boat (2) with validation for overlaps and out-of-bounds.
- **Real-time gameplay** — turns, hits, misses, sinks, and game-over events are pushed instantly over WebSocket.
- **Responsive UI** — built with Tailwind CSS and Radix UI primitives for a clean experience on desktop and mobile.

---

## Tech Stack

| Layer | Technology |
|------------|---------------------------------------------|
| Frontend | Next.js 16, React 19, TypeScript 5, Tailwind CSS 4 |
| Backend | Spring Boot 4, Java 21, Spring Security, Spring WebSocket |
| Database | PostgreSQL with Flyway migrations |
| Real-time | STOMP over WebSocket (SockJS fallback) |
| Deployment | Vercel (frontend), Render (backend + database) |

---

## Architecture

```
┌──────────────┐  REST / WebSocket   ┌──────────────────┐       ┌────────────┐
│   Next.js    │ ◄──────────────────► │   Spring Boot    │ ◄───► │ PostgreSQL │
│   (Vercel)   │                      │   (Render)       │       │  (Render)  │
└──────────────┘                      └──────────────────┘       └────────────┘
```

The project is a **monorepo** with two independent applications:

- `frontend/battleship-game` — Next.js App Router SPA
- `backend/battleships-online` — Spring Boot REST + WebSocket API

REST is used for authentication, game creation, ship placement, and firing. WebSocket (STOMP) delivers real-time events such as opponent joining, both players ready, shot results, and game over.

---

## Installation

### Prerequisites

| Requirement | Version |
|-------------|---------|
| Java | 21+ |
| Maven | 3.9+ (wrapper included) |
| Node.js | 18+ |
| npm | 9+ |
| PostgreSQL | 15+ |

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/battleships.git
cd battleships
```

### 2. Set up the database

Create a local PostgreSQL database. The default dev credentials are `root` / `root`:

```sql
CREATE DATABASE battleships;
CREATE USER root WITH PASSWORD 'root';
GRANT ALL PRIVILEGES ON DATABASE battleships TO root;
```

### 3. Start the backend

```bash
cd backend/battleships-online
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at `http://localhost:8080`.

### 4. Start the frontend

Create an environment file:

```bash
cd frontend/battleship-game
echo NEXT_PUBLIC_API_URL=http://localhost:8080 > .env.local
```

Install dependencies and run:

```bash
npm install
npm run dev
```

The app will be available at `http://localhost:3000`.

---

## Usage

1. Open `http://localhost:3000` and log in as a guest or create an account.
2. Click **Create Game** to start a new match — you will receive a game ID.
3. Share the game ID with a friend. They click **Join Game** and enter the ID.
4. Both players place their ships on the grid, then mark themselves as ready.
5. Take turns firing at your opponent's grid. Hits, misses, and sinks are shown in real time.
6. The first player to sink all five of the opponent's ships wins.

### Ship roster

| Ship | Size |
|--------------|------|
| Carrier | 5 |
| Battleship | 4 |
| Destroyer | 3 |
| Submarine | 3 |
| Patrol Boat | 2 |

---

## API Reference

### Authentication

| Method | Endpoint | Description |
|--------|------------------------|----------------------|
| POST | `/api/players/guest` | Create a guest session |
| POST | `/api/players/signup` | Register a new account |
| POST | `/api/players/login` | Log in |

### Games

| Method | Endpoint | Description |
|--------|-------------------------------|-------------------------------|
| POST | `/api/games/create` | Create a new game |
| POST | `/api/games/{id}/join` | Join an existing game |
| POST | `/api/games/{id}/ships/place` | Place a ship on the board |
| POST | `/api/games/{id}/ships/reset` | Reset all placed ships |
| POST | `/api/games/{id}/ships/ready` | Mark ready to play |

### Gameplay

| Method | Endpoint | Description |
|--------|--------------------------------|-------------------------------|
| GET | `/api/play/me` | Current player username |
| GET | `/api/play/{id}/opponent` | Opponent username |
| GET | `/api/play/{id}/initial` | Who goes first |
| GET | `/api/play/{id}/coordinates` | Own ship positions |
| POST | `/api/play/{id}/fire` | Fire at a coordinate |

### WebSocket (STOMP)

Connect to `/game` (SockJS). Subscribe to the following topics:

| Topic | Event |
|--------------------------------------|-------------------------------|
| `/topic/game/{id}/ready` | Second player joined |
| `/topic/game/{id}/place-ships` | Both players ready to place |
| `/topic/game/{id}/update` | Shot result / game over |

---

## Running Tests

### Backend

The backend includes JUnit 5 + Mockito tests for ship placement validation:

```bash
cd backend/battleships-online
./mvnw test
```

---

## Deployment

The application is deployed on two platforms:

| Component | Platform | URL |
|-----------|----------|-----|
| Frontend | Vercel | [battleships-lime-beta.vercel.app](https://battleships-lime-beta.vercel.app/) |
| Backend + DB | Render | [battleships-v2y0.onrender.com](https://battleships-v2y0.onrender.com) |

### Environment variables (production)

| Variable | Component | Purpose |
|------------------------|-----------|----------------------------------|
| `NEXT_PUBLIC_API_URL` | Frontend | Backend base URL |
| `DB_URL` | Backend | PostgreSQL JDBC connection string |
| `DB_USERNAME` | Backend | Database user |
| `DB_PASSWORD` | Backend | Database password |
| `PORT` | Backend | Server port (default `8080`) |

---

## Roadmap

- [ ] Spectator mode
- [ ] Match history and player statistics
- [ ] Matchmaking / random opponent queue
- [ ] Mobile-optimized touch controls for ship placement
- [ ] Rematch button after a game ends

---

## Authors

Built by the repository maintainers. Contributions and feedback are appreciated — feel free to open an issue or pull request.

---

## Project Status

On **hiatus**. Core gameplay is fully functional and deployed. See the [Roadmap](#roadmap) for planned improvements.
