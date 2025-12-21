# Comanda

Comanda is a Kotlin/Ktor-based backend for managing restaurant orders and tables.

## Latest changes (from last commit)

- Added PostgreSQL support with a Docker Compose service (server/Comanda/docker-compose.yml).
- Integrated Exposed (JetBrains SQL framework) and HikariCP for database access (Gradle, Exposed modules and HikariCP dependency).
- Added a database plugin (server/Comanda/src/main/kotlin/dev/giuseppedarro/comanda/plugins/Database.kt) which:
  - Connects to PostgreSQL via HikariCP
  - Creates a `users` table (Users schema) and inserts a default user if none exists
- Updated authentication to read JWT settings from `application.yaml` and to look up users from the database instead of using hardcoded credentials.
- Added a `Users` schema (server/Comanda/src/main/kotlin/dev/giuseppedarro/comanda/features/users/data/UserSchema.kt).
- Wired Koin modules to accept `ApplicationConfig` so configuration values can be injected.
- Updated `application.yaml` formatting and added JWT configuration entries.
- Brought database initialization into the application startup (configureDatabase called from Application module).

## Requirements

- JDK 17+ (or compatible with the Gradle Kotlin/JS plugin and Ktor version used)
- Docker and Docker Compose (to run PostgreSQL locally)
- Gradle (recommended to use the Gradle wrapper)

## Quick start (development)

1. Start PostgreSQL using Docker Compose:

   cd server/Comanda
   docker-compose up -d

   This starts PostgreSQL on localhost:5432 with username `postgres`, password `postgres` and database `comanda`.

2. Run the application (from repo root):

   ./gradlew :server:Comanda:run

   The application will connect to the local PostgreSQL database using the default JDBC URL `jdbc:postgresql://localhost:5432/comanda`.

3. Default credentials

   The database plugin will create a default user if none exists:
   - employeeId: `1234`
   - password: `password`
   - role: `WAITER`

   Note: Passwords are stored in plaintext in the database for now; replace this with a proper hashing strategy (bcrypt/argon2) before production use.

## Configuration

- `server/Comanda/src/main/resources/application.yaml` contains these JWT settings:
  - `jwt.secret` — secret used to sign tokens
  - `jwt.issuer` — issuer (e.g. http://0.0.0.0:8080)
  - `jwt.audience` — expected audience for tokens
  - `jwt.realm` — authentication realm

- The database plugin currently uses the following hard-coded JDBC parameters inside `configureDatabase()`:
  - jdbcUrl: `jdbc:postgresql://localhost:5432/comanda`
  - username: `postgres`
  - password: `postgres`

  If you want to inject these from environment variables or `application.yaml`, modify `configureDatabase()` to read the values from `environment.config`.

## Notes & next steps

- Hash passwords before storing them.
- Consider moving DB and JWT secrets into environment variables or a secrets manager.
- Add migrations (Flyway or Liquibase) instead of creating tables directly on startup.

---

This README was updated to reflect the changes in the most recent commit (added PostgreSQL, Exposed, HikariCP, Users schema, database initialization, and JWT configuration). For the full commit details see:
https://github.com/Giuseppedarro/Comanda/commit/57c90394246fb61d02a502348cea1239123c92f8

More commits: https://github.com/Giuseppedarro/Comanda/commits
