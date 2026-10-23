# Employee Service (Java 11 + Oracle)

Employee API built with Spring Boot 2.7.18, which is the last Spring Boot line that supports Java 11. The build refuses every other JDK. The runtime image is Eclipse Temurin 11.

## Layout

```
src/main/java/com/example/employee
  config          application, JPA auditing, Oracle dialect, CORS
  controller      REST API and error handling
  dto             request, response, and error bodies
  entity          JPA employee mapped to Oracle
  exception       not-found and duplicate-email errors
  mapper          request/entity/response conversion
  repository      Spring Data JPA repository
  service         employee use cases
```

## Run

Docker is the supported way to run the stack. Oracle Database 21c XE and the application both start from Compose. The application container is built with JDK 11.

```bash
docker compose up --build
```

Oracle listens on `localhost:1521` (service `XEPDB1`). The API listens on `http://localhost:8080`.

| Setting | Value |
| --- | --- |
| JDBC URL | `jdbc:oracle:thin:@//localhost:1521/XEPDB1` |
| Username | `appuser` |
| Password | `AppUser#123` |
| SYS/SYSTEM password | `Oracle#Pwd123` |

The first Oracle start can take a few minutes. Wait until Compose reports the database as healthy; the app starts after that.

## API

- `GET /api/v1/info`
- `GET /api/v1/employees`
- `GET /api/v1/employees/{id}`
- `POST /api/v1/employees`
- `PUT /api/v1/employees/{id}`
- `DELETE /api/v1/employees/{id}`
- `GET /actuator/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

Create an employee:

```bash
curl -X POST http://localhost:8080/api/v1/employees ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Ada\",\"lastName\":\"Lovelace\",\"email\":\"ada@example.com\",\"department\":\"Engineering\",\"jobTitle\":\"Developer\",\"salary\":90000.00,\"hireDate\":\"2020-01-15\"}"
```

## Build on the host

Install JDK 11, then:

```bash
mvn test
mvn spring-boot:run
```

`mvn` fails on JDK 17, 21, or 25. Point `JAVA_HOME` at a JDK 11 install, or build through Docker:

```bash
docker compose build app
```

Oracle must already be running before `spring-boot:run`. Hibernate creates `EMPLOYEES` and `EMPLOYEE_SEQ` on startup (`ddl-auto: update`).
