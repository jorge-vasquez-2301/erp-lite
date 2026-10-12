# Repository instructions

Guidance for coding agents working in **erp-lite**. Keep this file short and actionable; prefer code, tests, and formatters over long prose rules.

## Stack

- Build: Gradle multi-module Wrapper (`./gradlew`, Gradle **9.7.1**) — do not introduce Maven
- Runtime: Java **25** (toolchain in root `build.gradle`)
- Framework: Spring Boot **4.1.1**
- Web: `spring-boot-starter-web` only in `:erp-api`
- Utilities (all subprojects): Lombok + MapStruct + `lombok-mapstruct-binding` (`compileOnly` / `annotationProcessor` / `implementation` as in root `build.gradle`)
- Persistence (in `:erp-infrastructure`): Spring Data JPA + PostgreSQL, Spring Data MongoDB, Spring Data Redis + cache
- Local data stores: `docker-compose.yaml` (Postgres 17, MongoDB 8, Redis 7, Redis Commander)
- Base package: `com.debuggeandoideas.erplite`
- Config: `erp-api/src/main/resources/application.yaml`

## Modules

| Module | Role | Depends on |
| --- | --- | --- |
| `:erp-common` | Shared utilities / cross-cutting types | — |
| `:erp-domain` | Domain model | `:erp-common` |
| `:erp-application` | Use cases / ports | `:erp-domain` |
| `:erp-infrastructure` | Adapters (JPA/Postgres, MongoDB, Redis) | `:erp-common`, `:erp-domain`, `:erp-application` |
| `:erp-api` | REST + Spring Boot entrypoint | `:erp-application`, `:erp-infrastructure` |

Only `:erp-api` applies the Spring Boot plugin and is runnable.

## Commands

Use the wrapper; do not require a global Gradle install.

| Task | Command |
| --- | --- |
| Build all | `./gradlew build` |
| Test all | `./gradlew test` |
| Run app | `./gradlew :erp-api:bootRun` |
| Clean | `./gradlew clean` |
| Start local DBs | `docker compose up -d` |
| Stop local DBs | `docker compose down` |

After behavior changes, run `./gradlew test` before considering work done.

## Repository map

```
settings.gradle                 # module includes
build.gradle                    # shared Java/Lombok/MapStruct/BOM config
docker-compose.yaml             # local Postgres, MongoDB, Redis (+ Redis UI)
db/postgresql/                  # Postgres init scripts (mounted by Compose)
db/mongodb/                     # Mongo init scripts (mounted by Compose)
erp-common/                     # ...erplite.common
erp-domain/                     # ...erplite.domain
erp-application/                # ...erplite.application
erp-infrastructure/             # ...erplite.infrastructure
erp-api/                        # controllers + ErpLiteApplication
gradle/wrapper/                 # wrapper; do not edit unless upgrading Gradle
```

Do not edit generated outputs under `build/`, `*/build/`, or `.gradle/`.

## Conventions

- Put types in the matching module package (`...common`, `...domain`, `...application`, `...infrastructure`, or root/api under `erp-api`).
- Prefer constructor injection; avoid field injection.
- Do not add Spring Web (or Boot Web) dependencies outside `:erp-api`.
- Keep persistence drivers and Spring Data starters in `:erp-infrastructure` (not in `:erp-api` / `:erp-domain`).
- Prefer YAML config in `erp-api` `application.yaml` over scattered `.properties` files.
- Keep dependency direction: api → application/infrastructure → domain → common (no reverse deps).
- Keep changes scoped to the requested task; do not refactor unrelated code.

## Safety

- Never commit secrets, credentials, or real connection strings (Compose uses local/dev-only defaults).
- Do not modify production data or run destructive DB commands without explicit approval.
- Ask before changing the Java toolchain version, Spring Boot version, or Gradle wrapper distribution.

## Definition of done

- Relevant tests pass (`./gradlew test`).
- Diff is limited to the requested change.
- New behavior has tests when practical (at least a focused unit or `@SpringBootTest` coverage for API / wiring changes).
- Report what was verified and any remaining unknowns.
