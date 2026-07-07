# URL Shortener

Acortador de URLs full stack con autenticación JWT, caché con Redis y estadísticas de clicks.

## Estructura

```
url-shortener/
├── src/          # API REST (Java 21 + Spring Boot)
├── frontend/     # Interfaz web (Vue 3)
├── Dockerfile
└── docker-compose.yml
```

## Tech Stack

**Backend**
- Java 21 + Spring Boot 3.5
- PostgreSQL — persistencia
- Redis — caché de URLs
- Spring Security + JWT — autenticación
- Flyway — migraciones
- Docker + Docker Compose

**Frontend**
- Vue 3 + Pinia + Vue Router
- Diseño minimalista oscuro

## Levantar el proyecto

**Solo el backend (con base de datos y Redis):**

```bash
docker compose up --build
```

**Frontend en desarrollo:**

```bash
cd frontend
npm install
npm run dev
```

La API estará en `http://localhost:8080`  
El frontend en `http://localhost:5173`  
Swagger en `http://localhost:8080/swagger-ui`

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
| GET | `/api/stats/{code}` | Estadísticas de una URL |

### Redirección (público)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/{code}` | Redirigir a la URL original |

## Características

- URLs con expiración automática a 30 días
- Caché con Redis — redirecciones en microsegundos
- Registro de clicks con IP y timestamp
- Validación de URLs y errores descriptivos
- Documentación interactiva con Swagger
