# Flavorful Book — Вкусный букварь

Веб-приложение для создания, поиска и обмена кулинарными рецептами с элементами социальной сети.

## Функциональность

**Без авторизации:** просмотр и поиск рецептов, фильтрация по категориям и времени приготовления, сортировка.

**Авторизованным пользователям:** создание и редактирование своих рецептов, добавление в избранное, написание отзывов,
управление профилем.

## Запуск

Требования: Docker + Docker Compose.

```bash
docker compose up --build
```

Приложение: http://localhost:8080/flavorful_book  
Swagger UI: http://localhost:8080/flavorful_book/swagger-ui.html

## Стек

| Слой             | Технология                     |
|------------------|--------------------------------|
| Framework        | Spring Boot                    |
| Web              | Spring MVC                     |
| Templates        | JSP                            |
| Security         | Spring Security                |
| Persistence      | Spring Data JPA                |
| Database         | PostgreSQL                     |
| Cache            | Redis                          |
| HTTP Client      | OkHttp (внешний API TheMealDB) |
| Build            | Maven                          |
| Containerization | Docker Compose                 |

## Конфигурация Docker Compose

| Сервис  | Образ              | Порт        |
|---------|--------------------|-------------|
| `db`    | postgres:17-alpine | 5433 → 5432 |
| `redis` | redis:7-alpine     | 6379 → 6379 |
| `app`   | (локальная сборка) | 8080 → 8080 |
