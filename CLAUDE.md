# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & run

System Maven is at `D:\Maven\apache-maven-3.9.9-bin\apache-maven-3.9.9\bin\mvn.cmd`. The bundled `mvnw.cmd` fails on this machine because `JAVA_HOME` (`C:\Users\Ari Gunawan.JAKARTA\...`) contains a space — always invoke the system mvn directly:

```bash
& "D:\Maven\apache-maven-3.9.9-bin\apache-maven-3.9.9\bin\mvn.cmd" clean compile -DskipTests
& "D:\Maven\apache-maven-3.9.9-bin\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
```

App boots on port **9090**. Swagger UI at `/docs.html`.

Test single file: `mvn -Dtest=ClassName test`. No tests currently exist beyond Spring Boot's default placeholder.

## Architecture

### Single Oracle datasource, JPA-as-query-runner

`config/database/OracleDBConfiguration.java` is the only datasource. Every "JPA repository" extends `JpaRepository<NativeEntity, String>` where `NativeEntity` is a **dummy stub** (`@Table(name = "dummy")`, single id field). The repos exist purely to host `@Query(nativeQuery = true)` methods — there is no real entity mapping. Native queries return **interface projections** (e.g. `HaircutResponse`) where camelCase getters auto-bind to UPPERCASE column names.

When adding a new module, follow the existing per-module package split:
```
controller/{Module}/{Module}Controller.java
service/{Module}/{Module}Service.java
database/oracle/repository/jpa/{Module}/{Module}JpaRepository.java
database/oracle/repository/projection/{Module}/{Response}.java   ← interface projections
database/oracle/repository/dto/{Module}/{Request}.java           ← Lombok @Getter/@Setter request DTOs
```

`@RequestMapping("api/mkbd/")` is the convention; Lombok `@RequiredArgsConstructor` for DI.

### MKBD all-data endpoints (heavy-fetch pattern)

Two endpoints load 96+ aggregation queries used to render the MKBD report. They share `genDate` (string `YYYY-MM-DD`) as the only parameter, but differ in implementation:

- **`GET /api/mkbd/all-data`** — kept as a kept-for-comparison baseline. 3 hardcoded queries via `MkbdAllDataJpaRepository` (`@Query` + interface projections), parallel via `CompletableFuture`, typed nested response.

- **`GET /api/mkbd/all-data-full`** — the production path. 98 `.sql` files in `src/main/resources/sql/{detail,summary}/{vd}/baris*.sql` are scanned at startup by `SqlScriptRegistry` (`@PostConstruct`, classpath glob), trailing `;` stripped, cached in memory. `MkbdAllDataFullService` runs all 98 in parallel via `NamedParameterJdbcTemplate` and assembles a nested `Map<String, Object>` shaped `{detail: {coa, vd51:{baris30,…}, vd52:{…}}, summary: {…}}`. JDBC column names are converted snake_case → camelCase before serialization.

Both endpoints are wrapped with `@Cacheable` (Caffeine, 2 min TTL, declared in `config/CacheConfig.java`) keyed on `genDate`. Cache TTL is short on purpose: the underlying ledger data can change intra-day, but multi-user concurrent hits within the window dedupe to one DB execution.

### Date parameter convention

Every SQL file uses **`:genDate` as a string bind**, wrapped in Oracle's `TO_DATE(:genDate, 'yyyy-mm-dd')` at the SQL level — never pass a `java.sql.Date` from Java. Original SQL in `docs/Detail/` and `docs/Summary/` had `'2026-04-21'` hardcoded; the copies under `src/main/resources/sql/` are the parameterized versions and are the source of truth at runtime. Do not edit `docs/` — those are reference originals from the SQL author.

### Parallel execution constraints

`config/AsyncConfig.java` defines `mkbdQueryExecutor` (fixed pool size 6). HikariCP max pool is 10 (`application.yml`). Pool 6 leaves 4 connections free for other endpoints — do not bump executor without bumping HikariCP first.

Spring Boot virtual threads are enabled (`spring.threads.virtual.enable=true`). Do **not** use the implicit virtual thread executor for DB-bound work — connection pool exhaustion happens silently. Always submit DB tasks to `mkbdQueryExecutor`.

### Known SQL anomalies

Some original SQL files in `docs/` have orphan trailing statements (debug `SELECT * FROM MKBD.MKBD_COA_MAPPER WHERE VD_LINE_NO = N` left by the author). When copied to `resources/sql/`, these are cleaned manually — Oracle JDBC rejects multi-statement strings with `ORA-00933`. UNION ALL queries with blank-line formatting are valid single statements; do not "fix" those.

## Adding queries to all-data-full

Drop a `.sql` file under `src/main/resources/sql/detail/{vd}/baris{N}.sql` (or `summary/`). Use `:genDate` for the date bind. The registry picks it up on next startup; no Java changes needed. Filename pattern `baris{N}.sql` becomes the response key (`baris65_69` is allowed — match the original file's range notation).

## Logging

Hibernate SQL/parameter logging is **on by default** (`org.hibernate.SQL: DEBUG`, `BasicBinder: TRACE`, `oracle.jdbc: TRACE` in `application.yml`). Output is verbose — when debugging the all-data-full endpoint, look for `MkbdAllDataFullService` log lines (per-query timing) and the startup `Loaded N SQL scripts` line from `SqlScriptRegistry`.
