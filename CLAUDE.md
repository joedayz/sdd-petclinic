# CLAUDE.md

Este archivo orienta a Claude Code (claude.ai/code) al trabajar con el código de este repositorio.

## Propósito del proyecto

Este es un demo para una charla sobre **Spec-Driven Development con el AI Unified Process (AIUP)**. Reimplementa el
PetClinic clásico escribiendo primero las especificaciones (`docs/`) y generando el código a partir de ellas.

**`docs/` es la fuente de verdad, no el código.** Cuando pidas implementar algo, lee primero la spec relevante:

- `docs/entity_model.md` — diagrama ER y tablas de atributos con reglas de validación. El esquema en las migraciones
  Flyway debe coincidir con esto.
- `docs/use_cases.puml` — diagrama PlantUML de actores y casos de uso.
- `docs/use_cases/UC-NNN-*.md` — un archivo por caso de uso con precondiciones, escenario principal de éxito, flujos
  alternativos, postcondiciones y reglas de negocio. Los flujos de UI, etiquetas de campos y la navegación salen de
  aquí.

Si un caso de uso y el código no coinciden, gana el caso de uso salvo que el usuario indique lo contrario.

## Stack

- **Java 25**, **Quarkus 3.32.3**, **Vaadin 25.1**
- **jOOQ** para SQL type-safe — las fuentes generadas viven en `target/generated-sources/jooq` bajo el paquete
  `pe.joedayz.petclinic.database`
- **Flyway** en `src/main/resources/db/migration` (vacío por ahora — se añaden migraciones al implementar features)
- **PostgreSQL** en prod; **Testcontainers** (`postgres:17-alpine`) para tests *y* para la generación de código jOOQ
  en el build

## Comandos

```bash
# Ejecutar la app en local (Quarkus Dev Services levanta Postgres si Docker está corriendo)
./mvnw quarkus:dev

# Build completo — también ejecuta codegen jOOQ contra un Postgres efímero de Testcontainers
./mvnw verify

# Ejecutar todos los tests (Docker debe estar corriendo)
./mvnw test

# Ejecutar una clase o método de test concreto
./mvnw test -Dtest=PetclinicApplicationTest
./mvnw test -Dtest=UC001VerPaginaBienvenidaTest
./mvnw test -Dtest=OwnerViewTest#findsOwnersByLastName

# Regenerar fuentes jOOQ tras cambiar una migración Flyway
./mvnw generate-sources
```

Docker debe estar corriendo para `test`, `verify` y `generate-sources` — el plugin `testcontainers-jooq-codegen-maven-plugin`
levanta Postgres, aplica los scripts Flyway de `src/main/resources/db/migration` y genera las clases jOOQ a partir del
esquema resultante. **Si añades o cambias una migración, las clases jOOQ no se actualizarán hasta que vuelvas a ejecutar
`generate-sources` (o cualquier fase posterior).**

## Arquitectura

Módulo Maven único, **package-by-feature** bajo `pe.joedayz.petclinic` — cada feature (p. ej. `owner`, `pet`,
`visit`, `vet`) es su propio paquete con dos subpaquetes:

- **`ui`** — vistas Vaadin, formularios y demás componentes de UI de esa feature. Una vista por caso de uso / pantalla.
- **`domain`** — tipos de dominio y lógica de consultas jOOQ de esa feature. Las consultas se escriben contra las
  tablas/registros generados `database.*`. Sin JPA, sin repositorios Spring Data.

Así, una feature queda como `pe.joedayz.petclinic.<feature>.ui.*` y `pe.joedayz.petclinic.<feature>.domain.*`.
El acceso cruzado entre features debe ir por el paquete `domain` del otro feature, no por su `ui`.

Las **migraciones Flyway** definen el esquema de forma declarativa; el codegen jOOQ las consume, así que las migraciones
*son* efectivamente el DSL del esquema.

El proyecto es deliberadamente fino en capas — no hay service/repository/DTO separados más allá de `ui` + `domain`
salvo que un caso de uso lo exija. Prefiere dejar la lógica jOOQ cerca de donde se usa hasta que la duplicación
justifique extraerla.

## Convenciones de testing

Los tests de vista son **tests de caso de uso**. Sigue el skill `aiup-vaadin-jooq:browserless-test` (adaptado a Quarkus
abajo) en lugar de inventar otra estructura.

- **Nombre de clase:** `UC<id><NombrePascalDelCasoDeUso>Test` — p. ej. `UC001VerPaginaBienvenidaTest` para UC-001
  *Ver página de bienvenida*.
- **`@UseCase` en cada método de test** — enlaza spec ↔ test para el plugin AIUP IntelliJ Navigator. Si no existe
  `@interface UseCase` en el proyecto, créala en `pe.joedayz.petclinic` con la forma canónica del skill
  (`id`, `scenario` por defecto `"Main Success Scenario"`, `businessRules`).
- **Un método `@Test` por escenario** del UC (escenario principal, cada flujo alternativo `A1`, `A2`, …) y por regla de
  negocio relevante (`BR-XXX` en `businessRules` cuando aplique).
- **Vaadin Browserless Testing** para Quarkus: añade `browserless-test-quarkus` y `quarkus-junit` (scope test). Ver
  https://vaadin.com/docs/latest/flow/testing/browserless/quarkus.
- Los tests de vista extienden `QuarkusBrowserlessTest` y llevan `@QuarkusTest`. Quarkus arranca la aplicación en la
  JVM de test (CDI, `QuarkusInstantiator`, etc.); los tests browserless siguen manejando la UI en el servidor sin
  navegador real.
- API principal (heredada de `QuarkusBrowserlessTest`):
    - `navigate(MyView.class)` — navega a la vista *y devuelve la instancia creada*. Así obtienes la vista bajo test.
    - `test(component).setValue(...)` / `test(component).click()` — envuelve un componente para simular interacción.
      Prefiere esto a llamar setters/listeners directamente.
    - `$(Type.class).single()` / `$(Type.class).all()` — consulta el árbol de UI actual por tipo de componente. Úsalo
      para `Notification`, `Dialog` y lo que no sea accesible directamente desde la vista.
    - `fireShortcut(Key.ENTER)` / `fireShortcut(Key.KEY_S, KeyModifier.CONTROL)` — simula atajos de teclado.
- **Acceso a campos de componentes:** coloca los tests en el **mismo paquete** que la vista y accede a campos
  *package-private* directamente (`view.lastNameField`, `view.resultsGrid`). Es el patrón browserless idiomático —
  los campos no son un "backdoor" de test, son la estructura de la vista. **No añadas getters públicos solo para tests.**
- En formularios propios (p. ej. `OwnerForm`, `PetForm`), los campos package-private del formulario forman parte de
  su contrato — `test(view.ownerForm.firstName).setValue(...)` está bien.
- Para aserciones de navegación, comprueba `UI.getCurrent().getInternals().getActiveViewLocation().getPath()` en lugar
  de asertar sobre estado de dominio sacado de la vista.
- Para aserciones de renderizado (nombre del dueño mostrado, mascota listada, etc.), localiza el `Paragraph`/`H3`/etc.
  real vía `$(Paragraph.class)` y aserta sobre `.getText()` para ejercitar el camino de renderizado de punta a punta.
- **Base de datos en tests:** usa **Quarkus Dev Services** — Postgres arranca automáticamente en `%dev` y `%test` con
  Docker corriendo (`quarkus.datasource.devservices.image-name=postgres:17-alpine` en `application.properties`). No hace
  falta un bean Spring `TestcontainersConfiguration`.
- **Jackson:** Vaadin 25.1 requiere Jackson **3.1+**. Si una dependencia de test trae 3.0.x, fija `tools.jackson.core`
  `jackson-core` / `jackson-databind` en `3.1.2` en `dependencyManagement` (ver `pom.xml`).
- **Escenarios de seguridad:** con Quarkus Security en el classpath, usa `@TestSecurity` en métodos browserless (ver
  docs de Vaadin Quarkus browserless).

## Skills AIUP

Este repo está pensado para usarse con los skills del [AI Unified Process](https://unifiedprocess.ai/) instalados en
Cursor/Claude. **Prefiérelos siempre** a generación ad hoc; adapta solo lo que el skill asuma de Spring Boot (ver abajo).

| Skill | Usar cuando |
| --- | --- |
| `aiup-core:entity-model` | Crear o actualizar `docs/entity_model.md` |
| `aiup-core:use-case-spec` | Escribir o refinar `docs/use_cases/UC-*.md` |
| `aiup-core:use-case-diagram` | Actualizar `docs/use_cases.puml` |
| `aiup-core:requirements` | Trabajo más amplio de requisitos en `docs/` |
| `aiup-vaadin-jooq:flyway-migration` | Generar `src/main/resources/db/migration/V*.sql` desde el modelo de entidades |
| `aiup-vaadin-jooq:implement` | **Solo** vista Vaadin + jOOQ del UC (el skill **no** crea tests) |
| `aiup-vaadin-jooq:browserless-test` | Tests browserless del UC: `UC00N…Test`, `@UseCase`, un test por escenario/BR |
| `aiup-vaadin-jooq:playwright-test` | Tests E2E en navegador (opcional; los UC de vista siguen con browserless) |

### Flujo por caso de uso

1. Leer `docs/use_cases/UC-NNN-*.md` (y `entity_model.md` si hay persistencia).
2. Skill **`implement`** → código en `pe.joedayz.petclinic.<feature>.ui` / `.domain`.
3. Skill **`browserless-test`** → clase `UC00N…Test` en el **mismo paquete** que la vista, con `@UseCase` en cada
   método; base class **`QuarkusBrowserlessTest`** + **`@QuarkusTest`** (no `SpringBrowserlessTest` ni
   `@SpringBootTest`).
4. `./mvnw test`.

**Adaptación Quarkus del skill browserless-test:** sustituir `SpringBrowserlessTest` / `@SpringBootTest` /
`TestcontainersConfiguration` por `QuarkusBrowserlessTest` / `@QuarkusTest` / Quarkus Dev Services. Mantener acceso a
campos package-private de la vista cuando la spec lo permita; usar `$()` para overlays no alcanzables desde la vista.

**Si los skills no están instalados**, replica el mismo flujo manualmente según *Convenciones de testing*.

Los nombres `karibu-test` / UI Unit Testing están obsoletos; no los uses.
