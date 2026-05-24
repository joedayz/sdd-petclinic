# Primeros pasos

### Documentación de referencia

Para más información, consulta las siguientes secciones:

* [Guías de Quarkus](https://quarkus.io/guides/)
* [Usar Quarkus con Vaadin](https://vaadin.com/docs/latest/flow/integrations/quarkus)
* [Quarkus Dev Services](https://quarkus.io/guides/dev-services)
* [Extensión Flyway para Quarkus](https://quarkus.io/guides/flyway)
* [Extensión jOOQ para Quarkus](https://quarkiverse.github.io/quarkiverse-docs/quarkus-jooq/dev/index.html)
* [Plugin Maven testcontainers-jooq-codegen](https://github.com/testcontainers/testcontainers-jooq-codegen-maven-plugin)

### Guías

Las siguientes guías muestran cómo usar algunas funcionalidades de forma concreta:

* [Crear una UI CRUD con Vaadin](https://spring.io/guides/gs/crud-with-vaadin/)
* [Construir un servicio web RESTful](https://quarkus.io/guides/getting-started-reactive)

### Dev Services

Este proyecto usa [Quarkus Dev Services](https://quarkus.io/guides/dev-services) para iniciar automáticamente un contenedor PostgreSQL en modo desarrollo y pruebas.

La imagen configurada es `postgres:17-alpine`. Revisa la etiqueta y ajústala para que coincida con la que uses en producción.

### Generación de código jOOQ

Las fuentes jOOQ se generan en tiempo de compilación mediante el plugin `testcontainers-jooq-codegen-maven-plugin`, que levanta un contenedor PostgreSQL temporal, aplica las migraciones Flyway de `src/main/resources/db/migration` y genera clases SQL con tipos seguros en `target/generated-sources/jooq`.
