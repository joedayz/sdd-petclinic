# Caso de uso: Añadir mascota al dueño

## Resumen

**ID del caso de uso:** UC-007   
**Nombre del caso de uso:** Añadir mascota al dueño   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Registrar una nueva mascota bajo un dueño existente para poder registrar después sus consultas e información médica.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- El dueño al que se añadirá la mascota existe.
- Hay al menos un tipo de mascota (p. ej., gato, perro, hámster) configurado en la base de datos.

## Escenario principal de éxito

1. El usuario de la clínica hace clic en «Añadir nueva mascota» en la vista Detalle del dueño del dueño seleccionado.
2. El sistema muestra el formulario de creación de mascota, precargado con el nombre del dueño y un desplegable de tipos de mascota disponibles.
3. El usuario de la clínica introduce el nombre y la fecha de nacimiento de la mascota, selecciona un tipo y envía el formulario.
4. El sistema valida que:
    - el nombre no esté en blanco,
    - la fecha de nacimiento esté indicada y no sea futura,
    - se haya seleccionado un tipo,
    - ninguna otra mascota del mismo dueño tenga ya el mismo nombre.
5. El sistema asocia la nueva mascota al dueño y persiste el dueño (insertando la mascota en cascada).
6. El sistema vuelve a la vista Detalle del dueño y muestra la notificación «Se ha añadido una nueva mascota».

## Flujos alternativos

### A1: Nombre de mascota duplicado para el dueño

**Disparador:** El nombre enviado coincide con una mascota que ya pertenece a este dueño (paso 4).
**Flujo:**

1. El sistema rechaza el campo `name` con el error «ya existe».
2. El sistema vuelve a renderizar el formulario de mascota con el mensaje de error.
3. El usuario de la clínica ajusta el nombre.
4. El caso de uso continúa en el paso 3.

### A2: Fecha de nacimiento en el futuro

**Disparador:** La fecha de nacimiento enviada es posterior a hoy (paso 4).
**Flujo:**

1. El sistema rechaza el campo `birthDate` con un error de tipo incompatible.
2. El sistema vuelve a renderizar el formulario de mascota con el mensaje de error.
3. El usuario de la clínica corrige la fecha.
4. El caso de uso continúa en el paso 3.

### A3: Falta un campo obligatorio

**Disparador:** Falta el nombre, la fecha de nacimiento o el tipo (paso 4).
**Flujo:**

1. El sistema rechaza el campo afectado con un error «obligatorio».
2. El sistema vuelve a renderizar el formulario de mascota con los mensajes de error.
3. El usuario de la clínica proporciona el valor o valores faltantes.
4. El caso de uso continúa en el paso 3.

## Postcondiciones

### Postcondiciones de éxito

- Existe un nuevo registro `Pet` vinculado al dueño mediante `owner_id`.
- El usuario está viendo la vista Detalle del dueño con la nueva mascota.

### Postcondiciones de fallo

- No se persiste ninguna mascota.
- El formulario de mascota se vuelve a mostrar con errores de validación.

## Reglas de negocio

### BR-001: Nombre de mascota único por dueño

Un dueño no puede tener dos mascotas con el mismo nombre (sin distinguir mayúsculas/minúsculas).

### BR-002: Fecha de nacimiento no futura

La fecha de nacimiento de una mascota debe ser hoy o anterior.

### BR-003: Tipo de mascota obligatorio al crear

Debe elegirse un tipo de mascota cuando la mascota se crea por primera vez.
