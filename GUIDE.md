✅ ANTES DE METER A CLAUDE:
   [ ] Crear proyecto Maven/Spring Boot base
   [ ] pom.xml con todas las dependencias
   [ ] Crear carpetas src/main/java, src/test/java, src/main/resources
   [ ] Crear CLAUDE.md (guía)

✅ TAREA 1: Escribir SPECS (solo documentación)
   [ ] docs/entity_model.md (tablas, atributos, validaciones)
   [ ] docs/use_cases.puml (diagrama actor-UC)
   [ ] docs/use_cases/UC-001-*.md
   [ ] docs/use_cases/UC-002-*.md
   [ ] ... UC-009-*.md

✅ TAREA 2: Decirle a Claude "Implementa UC-001"
   [ ] Create CLADUDE.md como guía.
   [ ] Le dijiste a Claude algo como:
   "Implementa UC-001 (View Welcome Page) siguiendo CLAUDE.md. Los tests deben pasar y mapear con la spec."
   [ ] Claude crea WelcomeView.java
   [ ] Claude crea MainLayout.java
   [ ] Claude crea UC001ViewWelcomePageTest.java
   [ ] Claude crea UseCase.java (@interface)
   [ ] Claude crea TestcontainersConfiguration.java
   
✅ TAREA 3: Validar
   [ ] ./mvnw test → todos pasan
   [ ] Tests mapean 1:1 con spec
   
✅ TAREA 4: Siguiente UC (repetir con UC-002, 003, etc.)
   [ ] Crear V1__initial_schema.sql (tablas)
   [ ] ./mvnw generate-sources (jOOQ codegen)
   [ ] Claude implementa UCs 2, 3, 4...



┌─────────────────────────────────────┐
│ USUARIO: Escribir SPECS             │
│ (9 UCs, entity model, diagrama)     │
└────────────────┬────────────────────┘
                 │ (docs/ folder)
                 ├─→ entity_model.md
                 ├─→ use_cases.puml
                 └─→ UC-001.md, UC-002.md, ... UC-009.md
                 │
                 ▼
         ┌──────────────────────┐
         │ ENVIAR A CLAUDE:     │
         │ "Implementa UC-001   │
         │  según la spec"      │
         └────────┬─────────────┘
                  │
                  ▼
    ┌─────────────────────────────────────┐
    │ CLAUDE: Leer SPECS                  │
    │ - docs/use_cases/UC-001.md          │
    │ - CLAUDE.md (guía)                  │
    │ - entity_model.md                   │
    └────────┬────────────────────────────┘
             │
             ▼
    ┌─────────────────────────────────────┐
    │ CLAUDE: Generar CÓDIGO              │
    │ - WelcomeView.java                  │
    │ - MainLayout.java                   │
    │ - UC001ViewWelcomePageTest.java     │
    │ - UseCase.java (@interface)         │
    │ - TestcontainersConfiguration.java  │
    └────────┬────────────────────────────┘
             │
             ▼
         ┌──────────────┐
         │ ./mvnw test  │
         │ ✅ PASAN     │
         └──────────────┘