# Flavorful Book — Вкусный букварь

Веб-приложение для создания, поиска и обмена кулинарными рецептами с элементами социальной сети.

## Стек

| Слой             | Технология                              |
|------------------|-----------------------------------------|
| Framework        | Spring Boot                             |
| Web              | Spring MVC + JSP                        |
| Security         | Spring Security                         |
| Persistence      | Spring Data JPA + Hibernate             |
| Database         | PostgreSQL 17                           |
| Cache            | Redis 7                                 |
| HTTP Client      | OkHttp (внешний API TheMealDB)          |
| Build            | Maven                                   |
| Containerization | Docker Compose                          |
| API Docs         | springdoc-openapi (Swagger UI)          |

## Функциональность

**Без авторизации:** просмотр и поиск рецептов, фильтрация по категориям и времени приготовления, сортировка.

**Авторизованным пользователям:** создание и редактирование своих рецептов, добавление в избранное, написание отзывов, управление профилем.

## Запуск

Требования: Docker + Docker Compose.

```bash
docker compose up --build
```

Приложение: http://localhost:8080/flavorful_book  
Swagger UI: http://localhost:8080/flavorful_book/swagger-ui.html

## REST API

Базовый путь: `/flavorful_book/recipes`

| Метод    | Путь                      | Описание             | Доступ         |
|----------|---------------------------|----------------------|----------------|
| `POST`   | `/recipes`                | Создать рецепт       | Авторизованный |
| `PUT`    | `/recipes/{id}`           | Обновить рецепт      | Только автор   |
| `DELETE` | `/recipes/{id}`           | Удалить рецепт       | Только автор   |
| `POST`   | `/recipes/{id}/favorites` | Добавить в избранное | Авторизованный |
| `DELETE` | `/recipes/{id}/favorites` | Убрать из избранного | Авторизованный |

## Структура базы данных

```
users           — пользователи
recipes         — рецепты
ingredients     — ингредиенты
ingredient_recipe — ингредиенты рецепта (M2M с количеством)
categories      — категории
recipe_categories — рецепты ↔ категории (M2M)
reviews         — отзывы и оценки
favorites       — избранные рецепты (M2M пользователь ↔ рецепт)
recipe_views    — статистика просмотров
```

## Конфигурация Docker Compose

| Сервис  | Образ              | Порт               |
|---------|--------------------|--------------------|
| `db`    | postgres:17-alpine | 5433 → 5432        |
| `redis` | redis:7-alpine     | 6390 → 6379        |
| `app`   | (локальная сборка) | 8080 → 8080        |
