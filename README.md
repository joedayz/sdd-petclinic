# Quarkus PetClinic

Proyecto de demostración que acompaña una charla sobre **Desarrollo Guiado por Especificaciones** (*Spec-Driven Development*).

Retoma el clásico ejemplo Spring PetClinic, pero construido desde cero usando primero las especificaciones — casos de uso, modelo de entidades y flujos de UI — y dejando que los asistentes de IA implementen el código a partir de esas especificaciones.

## AI Unified Process

El Desarrollo Guiado por Especificaciones (SDD) invierte el flujo habitual de «escribir un prompt y rezar». En lugar de pedirle a una IA que produzca código a partir de una solicitud de una línea, inviertes tiempo de antemano en una especificación precisa y legible por máquina de lo que el sistema debe hacer. La IA trabaja entonces *contra* esa especificación — generando código, pruebas y documentación que pueden verificarse contra una fuente de verdad estable.

El [**AI Unified Process (AIUP)**](https://unifiedprocess.ai/) es una adaptación ligera del Proceso Unificado para el desarrollo asistido por IA. Conserva los artefactos que importan — casos de uso, modelos de dominio, decisiones arquitectónicas — y elimina la ceremonia que no aporta. El resultado es un flujo de trabajo en el que las personas mantienen el control de la *intención* y la IA se encarga de la traducción mecánica al código.

Este repositorio es el ejemplo en ejecución utilizado en la charla.

## Stack

- **Java 25**
- **Quarkus 3.32**
- **Vaadin 25** — UI
- **jOOQ** — SQL con tipos seguros
- **Flyway** — migraciones de base de datos
- **PostgreSQL** (Dev Services en dev/test; Testcontainers para la generación de código jOOQ)

## Especificaciones

Las especificaciones que guían la implementación están en [`docs/`](docs/):

- [`docs/entity_model.md`](docs/entity_model.md) — el modelo de dominio
- [`docs/use_cases.puml`](docs/use_cases.puml) — diagrama de casos de uso en PlantUML
- [`docs/use_cases/`](docs/use_cases) — especificaciones individuales de casos de uso

## Ejecución local

```bash
./mvnw quarkus:dev
```

Quarkus Dev Services inicia automáticamente un contenedor PostgreSQL en modo desarrollo (requiere Docker).

Para producción, configura el datasource de forma explícita:

```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/petclinic
quarkus.datasource.username=petclinic
quarkus.datasource.password=petclinic
```

Las pruebas usan Quarkus Dev Services y requieren Docker en ejecución:

```bash
./mvnw test
```

Compilación para producción:

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## Estructura

```
docs/        — especificaciones (la fuente de verdad)
src/main/   — implementación derivada de las especificaciones
src/test/   — pruebas que verifican la implementación contra las especificaciones
```
