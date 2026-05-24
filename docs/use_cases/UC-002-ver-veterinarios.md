# Caso de uso: Ver veterinarios

## Resumen

**ID del caso de uso:** UC-002   
**Nombre del caso de uso:** Ver veterinarios   
**Actor principal:** Visitante   
**Objetivo:** Consultar la lista de veterinarios de la clínica junto con sus especialidades.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- Existe al menos un veterinario en la base de datos (de lo contrario, la lista queda vacía).

## Escenario principal de éxito

1. El visitante hace clic en el enlace «Veterinarios» de la barra de navegación.
2. El sistema recupera el primer bloque de veterinarios del repositorio mediante un proveedor de datos perezoso.
3. El sistema renderiza la cuadrícula de veterinarios mostrando, para cada uno, nombre, apellido y una lista separada por comas de especialidades (o «ninguna» si no tiene especialidades).
4. Cuando el visitante se desplaza hacia el final de la cuadrícula, el sistema obtiene y añade el siguiente bloque de veterinarios hasta cargar todas las entradas.

## Flujos alternativos

### A1: Solicitar veterinarios como JSON/XML

**Disparador:** Un cliente solicita `/vets` (sin `.html`) esperando una representación legible por máquina.
**Flujo:**

1. El sistema carga todos los veterinarios del repositorio.
2. El sistema los envuelve en un objeto contenedor `Vets`.
3. El sistema devuelve la colección serializada como JSON o XML (negociación de contenido).
4. Fin del caso de uso.

## Postcondiciones

### Postcondiciones de éxito

- La página solicitada (o la lista completa) de veterinarios se muestra o devuelve al solicitante.
- No se modifican datos.

### Postcondiciones de fallo

- Ante errores de acceso a datos, se muestra la vista de error de la aplicación y no se muestra la lista de veterinarios.

## Reglas de negocio

### BR-001: Carga perezosa

La cuadrícula de veterinarios usa desplazamiento infinito: las filas se obtienen de forma perezosa del backend conforme el usuario se desplaza. No hay controles de paginación visibles ni tamaño de página fijo.

### BR-002: Orden de especialidades

Dentro de cada veterinario, las especialidades se listan en orden alfabético por nombre.

### BR-003: Acceso anónimo

Consultar veterinarios no requiere autenticación.
