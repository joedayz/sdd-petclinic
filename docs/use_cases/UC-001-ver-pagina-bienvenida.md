# Caso de uso: Ver página de bienvenida

## Resumen

**ID del caso de uso:** UC-001   
**Nombre del caso de uso:** Ver página de bienvenida   
**Actor principal:** Visitante   
**Objetivo:** Mostrar la página de inicio de la aplicación para que el usuario se oriente y navegue a las áreas funcionales principales.   
**Estado:** Aprobado

## Precondiciones

- La aplicación web PetClinic está en ejecución y accesible por HTTP.

## Escenario principal de éxito

1. El visitante navega a la URL raíz (`/`) de la aplicación PetClinic.
2. El sistema renderiza la página de bienvenida con el logo de la clínica, una imagen decorativa y la barra de navegación principal.
3. El visitante ve enlaces de navegación para Inicio, Buscar dueños, Veterinarios y Error.

## Flujos alternativos

_Ninguno — la página de bienvenida es estática y no recibe entrada del usuario._

## Postcondiciones

### Postcondiciones de éxito

- La página de bienvenida se muestra en el navegador del visitante.
- No se modifica el estado de la aplicación.

### Postcondiciones de fallo

- Si el servidor no está disponible, el navegador del visitante muestra un error de transporte (gestionado fuera de la aplicación).

## Reglas de negocio

### BR-001: Acceso anónimo

La página de bienvenida es accesible sin autenticación.
