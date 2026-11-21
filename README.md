# A programar se aprende programando

Repositorio de práctica de desarrollo backend y frontend. Contiene proyectos independientes, ordenados por nivel (principiante, intermedio y avanzado), y cada uno se diseña, documenta y construye como una aplicación real.

## Proyectos

| Proyecto | Nivel | Frontend | Backend | Estado |
|----------|-------|----------|---------|--------|
| [Bin2Dec](./Bin2Dec/README.md) — conversor de binario a decimal de hasta 8 bits | Principiante | React 19 + Vite | Java 21 + Spring Boot 4 | Completado |
| [Border Radius Previewer](./BorderRadiusPreviewer/README.md) — vista previa y generador de `border-radius` | Principiante | React 19 + Vite | — | En especificación |

## Niveles

| Nivel | Qué se practica |
|-------|-----------------|
| Principiante | Una funcionalidad acotada, validación de entrada, pruebas unitarias y, si el problema lo requiere, una API REST simple sin persistencia. |
| Intermedio | Persistencia en base de datos, varias entidades relacionadas, manejo de estado más complejo en el cliente y pruebas de integración. |
| Avanzado | Autenticación y autorización, integración con servicios externos, despliegue y decisiones de arquitectura documentadas. |

## Estructura de cada proyecto

Cada proyecto vive en su propia carpeta en la raíz del repositorio:

```text
<Proyecto>/
├── README.md    # Especificación de requisitos (SRS) del proyecto
├── backend/     # Servidor y API (solo si el proyecto lo requiere)
└── frontend/    # Cliente web
```

Un proyecto solo tiene backend cuando el problema lo justifica; si todo se resuelve en el navegador, la columna Backend de la tabla queda con «—».

El README de cada proyecto es la fuente de verdad sobre su alcance, requisitos, API y criterios de aceptación.

## Convenciones

- **Commits:** [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/) con el nombre del proyecto como scope. Ejemplo: `feat(bin2dec): agregar visor de bits sincronizado con el formulario`.
- **Cambios que afectan a todo el repositorio:** sin scope. Ejemplo: `chore: agregar .gitignore en la raíz del repositorio`.
