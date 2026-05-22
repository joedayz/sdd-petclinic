# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Quarkus guides](https://quarkus.io/guides/)
* [Using Quarkus with Vaadin](https://vaadin.com/docs/latest/flow/integrations/quarkus)
* [Quarkus Dev Services](https://quarkus.io/guides/dev-services)
* [Quarkus Flyway extension](https://quarkus.io/guides/flyway)
* [Quarkus jOOQ extension](https://quarkiverse.github.io/quarkiverse-docs/quarkus-jooq/dev/index.html)
* [Testcontainers jOOQ codegen Maven plugin](https://github.com/testcontainers/testcontainers-jooq-codegen-maven-plugin)

### Guides

The following guides illustrate how to use some features concretely:

* [Creating CRUD UI with Vaadin](https://spring.io/guides/gs/crud-with-vaadin/)
* [Building a RESTful Web Service](https://quarkus.io/guides/getting-started-reactive)

### Dev Services

This project uses [Quarkus Dev Services](https://quarkus.io/guides/dev-services) to start a PostgreSQL container automatically in dev and test mode.

The configured image is `postgres:17-alpine`. Please review the tag and set it to match what you run in production.

### jOOQ code generation

jOOQ sources are generated at build time by the `testcontainers-jooq-codegen-maven-plugin`, which spins up a temporary PostgreSQL container, applies Flyway migrations from `src/main/resources/db/migration`, and generates type-safe SQL classes into `target/generated-sources/jooq`.
