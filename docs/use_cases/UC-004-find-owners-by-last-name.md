# Caso de uso: Buscar dueños por apellido

## Resumen

**ID del caso de uso:** UC-004   
**Nombre del caso de uso:** Buscar dueños por apellido   
**Actor principal:** Usuario de la clínica   
**Objetivo:** Localizar uno o más dueños por su apellido para revisar o editar sus datos.   
**Estado:** Aprobado

## Precondiciones

- La aplicación PetClinic está en ejecución.

## Escenario principal de éxito

1. El usuario de la clínica hace clic en «Buscar dueños» en la barra de navegación.
2. El sistema muestra el formulario Buscar dueños con un único campo de entrada «Apellido».
3. El usuario de la clínica introduce todo o el inicio del apellido de un dueño y envía el formulario.
4. El sistema consulta el repositorio de dueños usando coincidencia «empieza por» sobre el apellido.
5. El sistema encuentra más de un dueño coincidente y renderiza la Lista de dueños con desplazamiento infinito, mostrando para cada uno nombre, dirección, ciudad, teléfono y mascotas.
6. El usuario de la clínica selecciona un dueño de la lista para navegar a la vista Detalle del dueño (UC-005).

## Flujos alternativos

### A1: Búsqueda con apellido vacío

**Disparador:** El usuario de la clínica envía el formulario con el campo apellido vacío en el paso 3.
**Flujo:**

1. El sistema trata la cadena vacía como la búsqueda más amplia posible y devuelve todos los dueños, cargados de forma perezosa conforme el usuario se desplaza.
2. El caso de uso continúa en el paso 5.

### A2: Exactamente una coincidencia

**Disparador:** La consulta «empieza por» devuelve exactamente un dueño en el paso 4.
**Flujo:**

1. El sistema navega directamente a la vista Detalle del dueño de ese dueño (UC-005).
2. Fin del caso de uso.

### A3: Sin coincidencias

**Disparador:** La consulta no devuelve dueños en el paso 4.
**Flujo:**

1. El sistema vuelve a renderizar el formulario Buscar dueños.
2. El sistema adjunta el error «no encontrado» al campo apellido.
3. El usuario de la clínica ajusta el término de búsqueda.
4. El caso de uso continúa en el paso 3.

### A4: Desplazarse por los resultados

**Disparador:** El conjunto de resultados es mayor de lo que cabe en pantalla (paso 5).
**Flujo:**

1. El usuario de la clínica se desplaza hacia el final de la Lista de dueños.
2. El sistema obtiene el siguiente bloque de dueños del repositorio y lo añade a la lista.
3. El caso de uso continúa en el paso 5 o 6.

## Postcondiciones

### Postcondiciones de éxito

- Se muestran los dueños coincidentes, o el usuario es redirigido a la página de detalle de un único dueño.
- No se modifican datos.

### Postcondiciones de fallo

- El formulario Buscar dueños se vuelve a mostrar con el mensaje «no encontrado».

## Reglas de negocio

### BR-001: Coincidencia por prefijo

Las búsquedas usan coincidencia sensible a mayúsculas «empieza por» sobre el apellido; no se exige coincidencia de cadena completa.

### BR-002: Carga perezosa

La Lista de dueños usa desplazamiento infinito: las filas se obtienen de forma perezosa del backend conforme el usuario se desplaza. No hay controles de paginación visibles ni tamaño de página fijo.

### BR-003: Búsqueda vacía devuelve todos

Un campo apellido vacío devuelve todos los dueños en lugar de generar un error de validación.
