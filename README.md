# URL Shortener API

REST API para acortar URLs construida con Java 21 y Spring Boot 3. Incluye autenticación JWT, caché con Redis, estadísticas de clicks y documentación interactiva con Swagger.

## Tech Stack

- **Java 21** + **Spring Boot 3.5**
- **PostgreSQL** — persistencia de datos
- **Redis** — caché de URLs frecuentes
- **Spring Security** + **JWT** — autenticación
- **Flyway** — migraciones de base de datos
- **Docker** + **Docker Compose** — contenerización
- **Swagger / OpenAPI** — documentación interactiva

## Requisitos

- Docker Desktop

## Levantar el proyecto

```bash
docker compose up --build
```

La API estará disponible en `http://localhost:8080`  
La documentación Swagger en `http://localhost:8080/swagger-ui`

## Endpoints

### Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Iniciar sesión |

### URLs (requiere JWT)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/shorten` | Acortar una URL |
| GET | `/api/stats/{code}` | Ver estadísticas de una URL |

### Redirección (público)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/{code}` | Redirigir a la URL original |

## Ejemplo de uso

**Registrarse:**
```json
POST /api/auth/register
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

**Acortar una URL:**
```json
POST /api/shorten
Authorization: Bearer {token}

{
  "url": "https://www.ejemplo.com/articulo-muy-largo"
}
```

**Respuesta:**
```json
{
  "code": "aB3kZx",
  "shortUrl": "http://localhost:8080/aB3kZx",
  "originalUrl": "https://www.ejemplo.com/articulo-muy-largo",
  "createdAt": "2026-07-07T10:00:00",
  "expiresAt": "2026-08-06T10:00:00"
}
```

## Características

- URLs con expiración automática a 30 días
- Caché con Redis — redirecciones en microsegundos tras el primer acceso
- Registro de clicks con IP y timestamp
- Validación de URLs malformadas
- Errores con mensajes descriptivos en español