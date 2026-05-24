# Caso de uso: Registrar nuevo dueño

## Resumen

**ID del caso de uso:** UC-003   
**Nombre del caso de uso:** Registrar nuevo dueño   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Añadir un nuevo dueño de mascotas a la clínica para poder registrar después sus mascotas y consultas.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- El usuario ha llegado a la pantalla «Añadir dueño» (p. ej., desde la página «Buscar dueños»).

## Escenario principal de éxito

1. El usuario de la clínica elige «Añadir dueño» en la vista Buscar dueños.
2. El sistema muestra un formulario de dueño vacío con campos para nombre, apellido, dirección, ciudad y teléfono.
3. El usuario de la clínica completa todos los campos y envía el formulario.
4. El sistema valida que nombre, apellido, dirección, ciudad y teléfono no estén en blanco y que el teléfono cumpla el patrón de 10 dígitos.
5. El sistema persiste el nuevo dueño, asignándole un identificador nuevo.
6. El sistema navega a la vista Detalle del dueño del dueño recién creado y muestra la notificación «Dueño creado».

## Flujos alternativos

### A1: Errores de validación

**Disparador:** Uno o más campos fallan la validación en el paso 4 (campo en blanco o teléfono que no coincide con `\d{10}`).
**Flujo:**

1. El sistema vuelve a renderizar el formulario de creación de dueño con mensajes de error por campo.
2. El sistema muestra la notificación «Hubo un error al crear el dueño.»
3. El usuario de la clínica corrige la entrada.
4. El caso de uso continúa en el paso 3.

## Postcondiciones

### Postcondiciones de éxito

- Existe un nuevo registro `Owner` en la base de datos con los valores enviados.
- El usuario está viendo la página de detalle del nuevo dueño.

### Postcondiciones de fallo

- No se persiste ningún dueño.
- El formulario se vuelve a mostrar con retroalimentación de validación para que el usuario corrija la entrada.

## Reglas de negocio

### BR-001: Campos obligatorios

Nombre, apellido, dirección, ciudad y teléfono son obligatorios.

### BR-002: Formato del teléfono

El teléfono debe tener exactamente 10 dígitos (regex `\d{10}`).

### BR-003: Identificador asignado por el servidor

El dueño no puede indicar su propio id; la base de datos lo genera al insertar.
