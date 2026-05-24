# Caso de uso: Reservar consulta para mascota

## Resumen

**ID del caso de uso:** UC-009   
**Nombre del caso de uso:** Reservar consulta para mascota   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Registrar una consulta veterinaria para una mascota existente, documentando la fecha y el motivo de la cita.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- El dueño existe.
- La mascota existe y pertenece al dueño indicado.

## Escenario principal de éxito

1. El usuario de la clínica hace clic en «Añadir consulta» junto a la mascota en la vista Detalle del dueño.
2. El sistema carga el dueño y la mascota, y muestra el formulario de consulta. El campo fecha se precarga con la fecha de hoy; se muestran el nombre de la mascota y las consultas anteriores como contexto.
3. El usuario de la clínica puede ajustar la fecha, introduce una descripción de la consulta y envía el formulario.
4. El sistema valida que la descripción no esté en blanco.
5. El sistema añade la consulta a la mascota y persiste el dueño (insertando la consulta en cascada mediante `pet_id`).
6. El sistema vuelve a la vista Detalle del dueño y muestra la notificación «Su consulta ha sido reservada».

## Flujos alternativos

### A1: Falta la descripción

**Disparador:** El campo descripción está en blanco en el paso 4.
**Flujo:**

1. El sistema vuelve a renderizar el formulario de consulta con un error de validación en `description`.
2. El usuario de la clínica introduce una descripción.
3. El caso de uso continúa en el paso 3.

### A2: La mascota no pertenece al dueño indicado

**Disparador:** En el paso 2, el id de mascota enviado no corresponde a ninguna mascota del dueño.
**Flujo:**

1. El sistema no puede resolver la mascota para ese dueño y muestra la vista de error de la aplicación.
2. Fin del caso de uso.

### A3: Dueño no encontrado

**Disparador:** En el paso 2, no existe ningún dueño con el id de dueño enviado.
**Flujo:**

1. El sistema no puede resolver el dueño y muestra la vista de error de la aplicación.
2. Fin del caso de uso.

## Postcondiciones

### Postcondiciones de éxito

- Existe un nuevo registro `Visit` vinculado a la mascota mediante `pet_id`.
- La consulta aparece en el historial de consultas de la mascota en la vista Detalle del dueño.

### Postcondiciones de fallo

- No se persiste ninguna consulta.
- El formulario de consulta se vuelve a mostrar con errores de validación, o se muestra la vista de error cuando no se puede resolver el dueño o la mascota.

## Reglas de negocio

### BR-001: Descripción obligatoria

Toda consulta debe tener una descripción no vacía.

### BR-002: Fecha por defecto

Si el usuario no cambia el campo fecha, la consulta se registra con la fecha de hoy.

### BR-003: Coherencia dueño/mascota

Solo se puede reservar una consulta a través del dueño que posee la mascota; el controlador rechaza la solicitud si el id de mascota no pertenece al id de dueño de la URL.
