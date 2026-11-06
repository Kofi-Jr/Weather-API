# Weather API

A Spring Boot REST API that stores and retrieves temperature data for cities over three days — yesterday, today and tomorrow — in a MySQL database. It also serves a simple Thymeleaf web page that lists the stored records and lets you add new ones.

## Tech stack

- Java 17, Spring Boot 3.4.1 (Maven)
- Spring Web, Spring Data JPA (Hibernate), Bean Validation
- Thymeleaf (server-rendered view at `/`)
- MySQL 8 (runtime) via MySQL Connector/J
- JUnit 5, Mockito, MockMvc and H2 (tests only)

## Endpoints

| Method | Path | Description |
| ------ | ---- | ----------- |
| `GET` | `/` | Thymeleaf page listing all records, with a form to add one |
| `GET` | `/test` | Health check — returns `Test successful!` |
| `POST` | `/create` | Create a record (201) |
| `GET` | `/weather` | List all records (200) |
| `GET` | `/weather/{id}` | Get one record (200, or 404 if missing) |
| `PUT` | `/weather/{id}` | Update a record (200, or 404 if missing) |
| `DELETE` | `/weather/{id}` | Delete a record (204, or 404 if missing) |

A record looks like this:

```json
{
  "id": 1,
  "cityName": "New York",
  "yesterdayTemp": 18.5,
  "todayTemp": 21.0,
  "tomorrowTemp": 19.5
}
```

The `id` is supplied by the client when a record is created. `cityName` must not be blank — invalid input returns `400` with an `error` message instead of a `500`. Unknown ids return `404`, and a non-numeric id (e.g. `/weather/abc`) returns `400`.

### Examples

```bash
# Create a record
curl -X POST http://localhost:8080/create \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "cityName": "New York", "yesterdayTemp": 18.5, "todayTemp": 21.0, "tomorrowTemp": 19.5}'

# List all records
curl http://localhost:8080/weather

# Get one record
curl http://localhost:8080/weather/1

# Update a record
curl -X PUT http://localhost:8080/weather/1 \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "cityName": "New York", "yesterdayTemp": 19.0, "todayTemp": 25.0, "tomorrowTemp": 22.0}'

# Delete a record
curl -X DELETE http://localhost:8080/weather/1
```

## Setup

### 1. Create the database

```sql
CREATE DATABASE weather_db;
```

Hibernate creates/updates the `weather_info` table automatically (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Configure the connection

The app reads its database settings from environment variables — no password is stored in the code or in `application.properties`:

| Variable | Default | Meaning |
| -------- | ------- | ------- |
| `DB_URL` | `jdbc:mysql://localhost:3306/weather_db` | JDBC URL |
| `DB_USER` | `root` | Database user |
| `DB_PASSWORD` | (empty) | Database password |

```bash
export DB_URL=jdbc:mysql://localhost:3306/weather_db
export DB_USER=root
export DB_PASSWORD=yourpassword
```

### 3. Run it

```bash
./mvnw spring-boot:run
```

The API is then available at `http://localhost:8080`, and the web view at `http://localhost:8080/`.

## Tests

Tests run against an in-memory H2 database, so **no MySQL server is needed** to run them:

```bash
./mvnw test
```

Coverage:

- `WeatherInfoServiceTest` — unit tests for the service layer (create, read, update, delete, missing-record cases), with the repository mocked.
- `WeatherInfoControllerTest` — MockMvc tests for every REST endpoint, including 404s and 400s for blank city names, malformed JSON and non-numeric ids.
- `WeatherViewControllerTest` — MockMvc tests for the Thymeleaf page (records table, empty state, form submit redirect).
- `WeatherInfoRepositoryTest` — `@DataJpaTest` round-trip tests (save, find, delete) against H2.
- `WeatherApiApplicationTests` — application context loads.

## Project structure

```
src/main/java/com/weather/
├── WeatherApiApplication.java   # Entry point
├── controller/
│   ├── WeatherInfoController.java   # JSON REST API
│   ├── WeatherViewController.java   # Thymeleaf web view (GET /)
│   └── GlobalExceptionHandler.java  # 400/500 error responses
├── model/
│   └── WeatherInfo.java             # JPA entity: id, cityName, 3 temperatures
├── repository/
│   └── WeatherInfoRepo.java         # Spring Data JPA repository
└── service/
    └── WeatherInfoService.java      # Business logic
src/main/resources/
├── application.properties           # DB config via DB_URL / DB_USER / DB_PASSWORD
└── templates/index.html             # Thymeleaf page
```
