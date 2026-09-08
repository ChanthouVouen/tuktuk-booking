# tuktuk-booking

Multi-module Spring Boot application for a tuk-tuk ride booking platform.

## Modules

| Module | Package | Purpose |
|---|---|---|
| `tuktuk-common` | `com.tuktuk.common` | Shared DTOs, exception types, and utility classes |
| `tuktuk-domain` | `com.tuktuk.domain` | JPA entities and Spring Data repositories |
| `tuktuk-core` | `com.tuktuk.core` | Business logic and service implementations |
| `tuktuk-api-passenger` | `com.tuktuk.passenger` | Passenger-facing REST API (runnable Spring Boot app) |
| `tuktuk-api-driver` | `com.tuktuk.driver` | Driver-facing REST API (runnable Spring Boot app) |

`tuktuk-common` -> `tuktuk-domain` -> `tuktuk-core` -> {`tuktuk-api-passenger`, `tuktuk-api-driver`}

`tuktuk-common`, `tuktuk-domain`, and `tuktuk-core` are libraries only; they have no
`@SpringBootApplication` class or `main` method. Each API app's
`@SpringBootApplication(scanBasePackages = "com.tuktuk")` (plus `@EntityScan` /
`@EnableJpaRepositories` on `com.tuktuk.domain`) pulls in the components, entities,
and repositories defined in the other modules at runtime.

## Build

```bash
./mvnw clean install
```

## Run

```bash
./mvnw -pl tuktuk-api-passenger spring-boot:run
./mvnw -pl tuktuk-api-driver spring-boot:run
```

## API docs (Swagger)

Each API module exposes its own Swagger UI and OpenAPI JSON via `springdoc-openapi`:

| Module | Port | Swagger UI | OpenAPI JSON |
|---|---|---|---|
| `tuktuk-api-passenger` | 8081 | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| `tuktuk-api-driver` | 8082 | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |


