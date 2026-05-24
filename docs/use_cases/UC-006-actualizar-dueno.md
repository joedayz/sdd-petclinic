# Caso de uso: Actualizar dueño

## Resumen

**ID del caso de uso:** UC-006   
**Nombre del caso de uso:** Actualizar dueño   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Modificar los datos de contacto de un dueño existente.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- El dueño a actualizar existe.
- El usuario de la clínica ha navegado a la vista Detalle del dueño de ese dueño (UC-005).

## Escenario principal de éxito

1. El usuario de la clínica hace clic en «Editar dueño» en la vista Detalle del dueño.
2. El sistema carga el dueño existente y muestra el formulario de edición, rellenado con el nombre, apellido, dirección, ciudad y teléfono actuales.
3. El usuario de la clínica modifica uno o más campos y envía el formulario.
4. El sistema valida que todos los campos obligatorios estén presentes y que el teléfono cumpla el patrón de 10 dígitos.
5. El sistema persiste el dueño actualizado en la base de datos.
6. El sistema vuelve a la vista Detalle del dueño y muestra la notificación «Datos del dueño actualizados».

## Flujos alternativos

### A1: Errores de validación

**Disparador:** Uno o más campos fallan la validación en el paso 4.
**Flujo:**

1. El sistema vuelve a renderizar el formulario de edición con mensajes de error por campo.
2. El sistema muestra la notificación «Hubo un error al actualizar el dueño.»
3. El usuario de la clínica corrige la entrada.
4. El caso de uso continúa en el paso 3.

## Postcondiciones

### Postcondiciones de éxito

- El registro del dueño refleja los valores enviados.
- El usuario ve la vista Detalle del dueño actualizada.

### Postcondiciones de fallo

- El registro del dueño no cambia.
- El formulario de edición se vuelve a mostrar con retroalimentación de validación.

## Reglas de negocio

### BR-001: Campos obligatorios

Nombre, apellido, dirección, ciudad y teléfono siguen siendo obligatorios en la actualización.

### BR-002: Formato del teléfono

El teléfono debe tener exactamente 10 dígitos (regex `\d{10}`).
