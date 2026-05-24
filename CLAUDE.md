# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project purpose

This is a demo for a talk on **Spec-Driven Development with the AI Unified Process (AIUP)**. It re-implements the
classic Spring PetClinic by writing the specs first (`docs/`) and generating code against them.

**`docs/` is the source of truth, not the code.** When asked to implement something, read the relevant spec first:

- `docs/entity_model.md` — ER diagram and attribute tables with validation rules. The schema in Flyway migrations must
  match this.
- `docs/use_cases.puml` — PlantUML actor/use-case diagram.
- `docs/use_cases/UC-NNN-*.md` — one file per use case with preconditions, main success scenario, alternative flows,
  postconditions, business rules. UI flows, field labels, and navigation come from these.

If a use case and the code disagree, the use case wins unless the user says otherwise.

## Stack

- **Java 25**, **Quarkus 3.32.3**, **Vaadin 25.1**
- **jOOQ** for type-safe SQL — generated sources live in `target/generated-sources/jooq` under package
  `pe.joedayz.petclinic.database`
- **Flyway** migrations in `src/main/resources/db/migration` (currently empty — migrations are added as features are
  implemented)
- **PostgreSQL** in prod; **Testcontainers** (`postgres:17-alpine`) for tests *and* for jOOQ code generation at build
  time

## Commands

```bash
# Run the app locally (Quarkus Dev Services starts Postgres when Docker is running)
./mvnw quarkus:dev

# Full build — this also runs jOOQ codegen against a throwaway Testcontainers Postgres
./mvnw verify

# Run all tests (Docker must be running)
./mvnw test

# Run a single test class / method
./mvnw test -Dtest=PetclinicApplicationTest
./mvnw test -Dtest=OwnerViewTest#findsOwnersByLastName

# Regenerate jOOQ sources after changing a Flyway migration
./mvnw generate-sources
```

Docker must be running for `test`, `verify`, and `generate-sources` — the `testcontainers-jooq-codegen-maven-plugin`
spins up Postgres, applies the Flyway scripts from `src/main/resources/db/migration`, and generates jOOQ classes from
the resulting schema. **If you add or change a migration, jOOQ classes won't update until you
re-run `generate-sources` (or any phase after it).**

## Architecture

Single Maven module, **package-by-feature** under `pe.joedayz.petclinic` — each feature (e.g. `owner`, `pet`,
`visit`, `vet`) is its own package containing two sub-packages:

- **`ui`** — Vaadin views, forms, and other UI components for that feature. One view per use case / screen.
- **`domain`** — domain types and jOOQ query logic for that feature. Queries are written against the generated
  `database.*` tables/records. No JPA, no Spring Data repositories.

So a feature looks like `pe.joedayz.petclinic.<feature>.ui.*` and `pe.joedayz.petclinic.<feature>.domain.*`.
Cross-feature reach-in should go through the other feature's `domain` package, not its `ui`.

**Flyway migrations** define the schema declaratively; jOOQ codegen consumes them, so the migrations effectively *are*
the schema DSL.

The project is intentionally thin on layers — there is no separate service/repository/DTO layering beyond `ui` +
`domain` unless a use case demands it. Prefer putting jOOQ query logic close to where it's used until duplication
justifies extraction.

## Testing conventions

- **Vaadin Browserless Testing** for Quarkus: add `browserless-test-quarkus` and `quarkus-junit` (test scope). See
  https://vaadin.com/docs/latest/flow/testing/browserless/quarkus.
- View tests extend `QuarkusBrowserlessTest` and are annotated `@QuarkusTest`. Quarkus boots the application for the
  test JVM (CDI, `QuarkusInstantiator`, etc.); browserless tests still drive the UI server-side without a real browser.
- Core API (all inherited from `QuarkusBrowserlessTest`):
    - `navigate(MyView.class)` — routes to the view *and returns the instantiated view instance*. This is how you get
      hold of the view under test.
    - `test(component).setValue(...)` / `test(component).click()` — wrap a component to simulate user interaction.
      Prefer this over calling setters/listeners directly.
    - `$(Type.class).single()` / `$(Type.class).all()` — query the current UI tree for components by type. Use this for
      `Notification`, `Dialog`, and anything not directly reachable from the view.
    - `fireShortcut(Key.ENTER)` / `fireShortcut(Key.KEY_S, KeyModifier.CONTROL)` — simulate keyboard shortcuts.
- **Component field access:** place tests in the **same package** as the view and access component fields that are *
  *package-private** directly (`view.lastNameField`, `view.resultsGrid`). This is the idiomatic browserless pattern —
  fields are not a "test backdoor", they're the view's structure. **Do not add public getters just for tests.**
- For custom form components (e.g. `OwnerForm`, `PetForm`), the form's package-private fields are part of its contract —
  `test(view.ownerForm.firstName).setValue(...)` is fine.
- For navigation assertions, check `UI.getCurrent().getInternals().getActiveViewLocation().getPath()` rather than
  asserting on domain state pulled back out of the view.
- For rendered-state assertions (owner name shown, pet listed, etc.), find the actual `Paragraph`/`H3`/etc. via
  `$(Paragraph.class)` and assert on `.getText()` so the render path is exercised end-to-end.
- **Database in tests:** use **Quarkus Dev Services** — Postgres starts automatically in `%dev` and `%test` when Docker
  is running (`quarkus.datasource.devservices.image-name=postgres:17-alpine` in `application.properties`). No Spring
  `TestcontainersConfiguration` bean is required.
- **Jackson:** Vaadin 25.1 requires Jackson **3.1+**. If a test dependency pulls 3.0.x, pin `tools.jackson.core`
  `jackson-core` / `jackson-databind` to `3.1.2` in `dependencyManagement` (see `pom.xml`).
- **Security scenarios:** with Quarkus Security on the classpath, use `@TestSecurity` on browserless test methods (see
  Vaadin Quarkus browserless docs).

## AIUP skills (optional)

The [AI Unified Process](https://unifiedprocess.ai/) defines optional Cursor/Claude **skills** (separate packages, not
shipped inside this repo). If you have them installed in your editor, prefer them over ad-hoc prompts:

| Skill | Use when |
| --- | --- |
| `aiup-core:entity-model` | Create or update `docs/entity_model.md` |
| `aiup-core:use-case-spec` | Write or refine `docs/use_cases/UC-*.md` |
| `aiup-core:use-case-diagram` | Update `docs/use_cases.puml` |
| `aiup-core:requirements` | Broader requirements work in `docs/` |
| `aiup-vaadin-jooq:flyway-migration` | Generate `src/main/resources/db/migration/V*.sql` from the entity model |
| `aiup-vaadin-jooq:implement` | Implement a UC end-to-end (Vaadin view + jOOQ); adapt for **Quarkus** + `QuarkusBrowserlessTest` |
| `aiup-vaadin-jooq:playwright-test` | Full browser E2E tests (optional; UC view tests stay browserless per *Testing conventions*) |

**If those skills are not installed**, follow the same workflow manually: read the UC spec → implement under
`pe.joedayz.petclinic.<feature>.ui` / `.domain` → add a `QuarkusBrowserlessTest` in the view's package → `./mvnw test`.
The `karibu-test` / UI Unit Testing skill names are obsolete; do not use them.
