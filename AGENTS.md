# Repository instructions

Guidance for coding agents working in **erp-lite**. Keep this file short and actionable; prefer code, tests, and formatters over long prose rules.

## Stack

- Build: Gradle multi-module Wrapper (`./gradlew`, Gradle **9.7.1**) — do not introduce Maven
- Runtime: Java **25** (toolchain in root `build.gradle`)
- Framework: Spring Boot **4.1.1**
- Web / ops: `spring-boot-starter-web` + Actuator in `:erp-api` (app port **9090**)
- Utilities (all subprojects): Lombok + MapStruct + `lombok-mapstruct-binding` (`compileOnly` / `annotationProcessor` / `implementation` as in root `build.gradle`)
- Persistence / adapters (in `:erp-infrastructure`): Spring Data JPA + PostgreSQL, Spring Data MongoDB, Spring Data Redis + cache, AWS SDK S3 (`software.amazon.awssdk:s3`), validation
- Local data stores: `docker-compose.yaml` (Postgres 17, MongoDB 8, Redis 7, Redis Commander, LocalStack 3.8 S3)
- Base package: `com.debuggeandoideas.erp_lite`
- Config: `erp-api/src/main/resources/application.yaml` (+ `aws/aws.yaml` for LocalStack/S3)

## Modules

| Module | Role | Depends on |
| --- | --- | --- |
| `:erp-common` | Shared utilities / cross-cutting types (scaffold) | — |
| `:erp-domain` | Domain model (Product, Order, Catalog, shared VOs, ports) | `:erp-common` |
| `:erp-application` | Use cases / ports (scaffold) | `:erp-domain` |
| `:erp-infrastructure` | Adapters (JPA/Postgres, MongoDB, Redis deps, AWS S3) | `:erp-common`, `:erp-domain`, `:erp-application` |
| `:erp-api` | REST + Spring Boot entrypoint | `:erp-application`, `:erp-infrastructure` |

Only `:erp-api` applies the Spring Boot plugin and is runnable.

### Current status

- **Domain**: Product / Order / Catalog models, domain events, `ImageStorageService` port, `CatalogRepository` port; solid unit-test coverage under `erp-domain/src/test`
- **Infrastructure**: JPA entities/repos (products, orders), Mongo documents/repos (catalogs, product queries, audit logs), AWS S3 image-storage adapter; Redis starter is wired but no Redis adapter code yet
- **Application / common**: empty scaffolds (no use-case classes yet)
- **API**: `ErpLiteApplication` + YAML property helpers; no REST controllers yet
- **DB init**: `db/postgresql/` schema+seed, `db/mongodb/` init script (mounted by Compose)

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
docker-compose.yaml             # Postgres, MongoDB, Redis (+ UI), LocalStack S3
db/postgresql/                  # Postgres init scripts (mounted by Compose)
db/mongodb/                     # Mongo init scripts (mounted by Compose)
erp-common/                     # ...erp_lite (scaffold)
erp-domain/                     # ...erp_lite.domain (catalog, order, product, shared, ports)
erp-application/                # ...erp_lite.application (scaffold)
erp-infrastructure/             # ...erp_lite.persistence (jpa, mongo, aws)
erp-api/                        # ErpLiteApplication + configs; resources/application.yaml, aws/aws.yaml
gradle/wrapper/                 # wrapper; do not edit unless upgrading Gradle
```

Do not edit generated outputs under `build/`, `*/build/`, or `.gradle/`.

## Conventions

- Put types in the matching module package (`...erp_lite` / `...domain` / `...application` / `...persistence` / api configs under `erp-api`).
- Prefer constructor injection; avoid field injection.
- Keep REST controllers and the Boot entrypoint in `:erp-api` (do not add controllers elsewhere).
- Keep persistence drivers, Spring Data starters, and AWS SDK in `:erp-infrastructure` (not in `:erp-api` / `:erp-domain`).
- Prefer YAML config in `erp-api` (`application.yaml`, `aws/aws.yaml`) over scattered `.properties` files.
- Keep dependency direction: api → application/infrastructure → domain → common (no reverse deps).
- Keep changes scoped to the requested task; do not refactor unrelated code.

## Safety

- Never commit secrets, credentials, or real connection strings (Compose / local YAML use local/dev-only defaults).
- Do not modify production data or run destructive DB commands without explicit approval.
- Ask before changing the Java toolchain version, Spring Boot version, or Gradle wrapper distribution.

## Definition of done

- Relevant tests pass (`./gradlew test`).
- Diff is limited to the requested change.
- New behavior has tests when practical (at least a focused unit or `@SpringBootTest` coverage for API / wiring changes).
- Report what was verified and any remaining unknowns.
