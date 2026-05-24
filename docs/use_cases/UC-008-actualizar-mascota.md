# Caso de uso: Actualizar mascota

## Resumen

**ID del caso de uso:** UC-008   
**Nombre del caso de uso:** Actualizar mascota   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Modificar los datos de una mascota existente (nombre, fecha de nacimiento o tipo).   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- El dueño y la mascota existen.
- El usuario de la clínica ha navegado a la vista Detalle del dueño del dueño de la mascota.

## Escenario principal de éxito

1. El usuario de la clínica hace clic en «Editar mascota» junto a la mascota en la vista Detalle del dueño.
2. El sistema muestra el formulario de edición de mascota rellenado con el nombre, la fecha de nacimiento y el tipo actuales.
3. El usuario de la clínica modifica uno o más campos y envía el formulario.
4. El sistema valida que:
    - el nombre no esté en blanco,
    - la fecha de nacimiento esté indicada y no sea futura,
    - ninguna otra mascota del mismo dueño (con distinto id) tenga ya el mismo nombre.
5. El sistema actualiza las propiedades de la mascota (nombre, fecha de nacimiento, tipo) en la colección en memoria del dueño y persiste el dueño.
6. El sistema vuelve a la vista Detalle del dueño y muestra la notificación «Se han editado los datos de la mascota».

## Flujos alternativos

### A1: Nombre de mascota duplicado

**Disparador:** El nombre enviado coincide con otra mascota distinta que ya pertenece a este dueño (paso 4).
**Flujo:**

1. El sistema rechaza el campo `name` con el error «ya existe».
2. El sistema vuelve a renderizar el formulario de edición con el error.
3. El usuario de la clínica ajusta el nombre.
4. El caso de uso continúa en el paso 3.

### A2: Fecha de nacimiento en el futuro

**Disparador:** La fecha de nacimiento enviada es posterior a hoy (paso 4).
**Flujo:**

1. El sistema rechaza el campo `birthDate` con un error de tipo incompatible.
2. El sistema vuelve a renderizar el formulario de edición con el error.
3. El usuario de la clínica corrige la fecha.
4. El caso de uso continúa en el paso 3.

### A3: Falta un campo obligatorio

**Disparador:** Falta el nombre o la fecha de nacimiento (paso 4).
**Flujo:**

1. El sistema rechaza el campo afectado con un error «obligatorio».
2. El sistema vuelve a renderizar el formulario de edición con los mensajes de error.
3. El usuario de la clínica proporciona el valor o valores faltantes.
4. El caso de uso continúa en el paso 3.

## Postcondiciones

### Postcondiciones de éxito

- El registro de la mascota refleja el nombre, la fecha de nacimiento y el tipo actualizados.
- El usuario está viendo la vista Detalle del dueño con la mascota actualizada.

### Postcondiciones de fallo

- El registro de la mascota no cambia.
- El formulario de edición se vuelve a mostrar con errores de validación.

## Reglas de negocio

### BR-001: Nombre de mascota único por dueño

Dos mascotas distintas del mismo dueño no pueden compartir nombre (sin distinguir mayúsculas/minúsculas).

### BR-002: Fecha de nacimiento no futura

La fecha de nacimiento de una mascota no puede ser posterior a hoy.

### BR-003: Tipo de mascota en la actualización

El tipo puede dejarse sin cambios al actualizar; el validador de mascota solo exige un tipo cuando la mascota es nueva.
