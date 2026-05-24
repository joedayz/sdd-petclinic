# Caso de uso: Ver detalle del dueño

## Resumen

**ID del caso de uso:** UC-005   
**Nombre del caso de uso:** Ver detalle del dueño   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Consultar los datos de contacto de un dueño junto con la lista de sus mascotas y el historial de consultas de cada una.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.
- Existe en la base de datos un dueño con el identificador solicitado.

## Escenario principal de éxito

1. El usuario de la clínica abre la vista Detalle del dueño para un id de dueño dado (p. ej., desde un resultado de búsqueda, un dueño recién creado o un marcador).
2. El sistema carga el dueño por identificador junto con sus mascotas (de forma anticipada) y las consultas de cada mascota (ordenadas por fecha ascendente).
3. El sistema renderiza la vista Detalle del dueño mostrando:
    - Dueño: nombre, dirección, ciudad, teléfono.
    - Mascotas: nombre, fecha de nacimiento, tipo y consultas (fecha y descripción) de cada mascota.
4. El sistema ofrece enlaces de acción: «Editar dueño», «Añadir nueva mascota» y, para cada mascota, «Editar mascota» y «Añadir consulta».
5. El usuario de la clínica puede seguir uno de los enlaces para activar UC-006, UC-007, UC-008 o UC-009.

## Flujos alternativos

### A1: Dueño no encontrado

**Disparador:** No existe ningún dueño con el identificador solicitado en el paso 2.
**Flujo:**

1. El sistema no puede resolver el dueño y muestra la vista de error de la aplicación.
2. Fin del caso de uso.

## Postcondiciones

### Postcondiciones de éxito

- Se muestran el detalle del dueño, las mascotas y las consultas.
- No se modifican datos.

### Postcondiciones de fallo

- No se renderiza la vista Detalle del dueño; se muestra la vista de error de la aplicación.

## Reglas de negocio

### BR-001: Orden de consultas

Las consultas de cada mascota se listan en orden cronológico (fecha de consulta ascendente).

### BR-002: Orden de mascotas

Las mascotas de un dueño se listan en orden alfabético por nombre.
