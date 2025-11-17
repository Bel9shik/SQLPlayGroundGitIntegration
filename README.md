# SQL Playground Git Integration - OAuth2 Swagger Configuration

Современное веб-приложение для выполнения SQL запросов с интеграцией GitHub OAuth2 и полной документацией API через Swagger.

## 🚀 Возможности

- **🔐 GitHub OAuth2 авторизация** - безопасная аутентификация через GitHub
- **📊 Интерактивное выполнение SQL** - выполнение запросов в песочнице
- **🔄 Git интеграция** - сохранение запросов в GitHub репозитории
- **📚 История запросов** - отслеживание выполненных запросов
- **🛡️ Валидация запросов** - проверка синтаксиса без выполнения
- **📖 Swagger UI** - интерактивная документация API
- **🎨 Красивый интерфейс** - современный дизайн страницы входа

## 📁 Структура проекта

```
src/
├── main/
│   ├── java/org/nsu/
│   │   ├── Main.java                          # Spring Boot приложение
│   │   ├── config/
│   │   │   ├── SecurityConfig.java            # Конфигурация безопасности OAuth2
│   │   │   └── OpenApiConfig.java             # Конфигурация Swagger с OAuth2
│   │   ├── controller/
│   │   │   ├── SqlPlaygroundController.java   # API для SQL запросов
│   │   │   ├── AuthController.java            # API для авторизации и Git
│   │   │   └── WebController.java             # Веб-контроллер для страниц
│   │   ├── service/
│   │   │   └── GitHubService.java             # Сервис для работы с GitHub API
│   │   └── dto/
│   │       ├── QueryRequest.java              # DTO запроса с валидацией
│   │       ├── QueryResponse.java             # DTO ответа с Builder pattern
│   │       └── ColumnInfo.java                # DTO метаданных колонок
│   └── resources/
│       ├── application.properties             # Конфигурация OAuth2 и приложения
│       └── templates/
│           └── login.html                     # Красивая страница входа
```

## ⚙️ Настройка GitHub OAuth

### 1. Создание GitHub OAuth App

1. Перейдите в GitHub Settings → Developer settings → OAuth Apps
2. Нажмите "New OAuth App"
3. Заполните данные:
   - **Application name**: SQL Playground
   - **Homepage URL**: `http://localhost:8080`
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. Сохраните Client ID и Client Secret

### 2. Настройка переменных окружения

```bash
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

Или создайте файл `.env`:
```
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

## 🛠️ Запуск приложения

### 1. Сборка проекта

```bash
./gradlew build
```

### 2. Запуск приложения

```bash
./gradlew bootRun
```

### 3. Доступ к приложению

- **Главная страница**: http://localhost:8080
- **Страница входа**: http://localhost:8080/login
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## 🔗 API Эндпоинты

### 🔐 Авторизация и Git интеграция (`/api/v1/auth`)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/profile` | Получить профиль пользователя |
| `GET` | `/repositories` | Список GitHub репозиториев |
| `POST` | `/repositories` | Создать новый репозиторий |
| `POST` | `/repositories/{owner}/{repo}/files` | Сохранить файл в репозиторий |
| `GET` | `/status` | Статус авторизации |

### 📊 SQL Playground (`/api/v1/sql`)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `POST` | `/execute` | Выполнить SQL запрос |
| `GET` | `/history` | История выполненных запросов |
| `POST` | `/validate` | Валидация синтаксиса SQL |
| `GET` | `/schema` | Схема базы данных |
| `POST` | `/save-to-git` | Сохранить запрос в Git |

## 🔒 Безопасность

### OAuth2 конфигурация

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Настройка OAuth2 с GitHub
    // Защита API эндпоинтов
    // Разрешение доступа к Swagger UI
}
```

### Swagger Security Scheme

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList("GitHubOAuth"))
        .components(new Components()
            .addSecuritySchemes("GitHubOAuth", 
                new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl("https://github.com/login/oauth/authorize")
                            .tokenUrl("https://github.com/login/oauth/access_token")
                        )
                    )
            )
        );
}
```

## 📝 Примеры использования

### Выполнение SQL запроса

```bash
curl -X POST "http://localhost:8080/api/v1/sql/execute" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "SELECT * FROM users WHERE age > ?",
    "parameters": {"1": "25"},
    "limit": 100,
    "timeout": 30
  }'
```

### Сохранение запроса в GitHub

```bash
curl -X POST "http://localhost:8080/api/v1/sql/save-to-git" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "SELECT COUNT(*) FROM orders WHERE status = \"completed\"",
    "parameters": {}
  }' \
  -G -d "repository=my-sql-queries" \
     -d "fileName=analytics/order-count.sql"
```

## 🎨 Пользовательский интерфейс

### Страница входа

Красивая и современная страница входа с:
- Градиентным фоном
- Анимированными кнопками
- Списком возможностей
- Адаптивным дизайном

### Swagger UI

Полностью интегрированный Swagger UI с:
- OAuth2 авторизацией
- Интерактивным тестированием API
- Подробной документацией
- Примерами запросов и ответов

## 🔧 Конфигурация

### application.properties

```properties
# OAuth2 GitHub
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email,repo

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operationsSorter=method

# База данных H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

## 🏗️ Архитектура

### Слои приложения

1. **Controller Layer** - REST API контроллеры
2. **Service Layer** - Бизнес-логика и интеграция с GitHub
3. **Security Layer** - OAuth2 конфигурация и авторизация
4. **Configuration Layer** - Настройки Spring Boot и Swagger

### Паттерны проектирования

- **Builder Pattern** - для создания QueryResponse
- **Service Layer Pattern** - для бизнес-логики
- **DTO Pattern** - для передачи данных
- **Configuration Pattern** - для настроек

## 🚀 Развертывание

### Docker

```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables

```bash
GITHUB_CLIENT_ID=your_client_id
GITHUB_CLIENT_SECRET=your_client_secret
SPRING_PROFILES_ACTIVE=production
```

## 🧪 Тестирование

```bash
# Запуск тестов
./gradlew test

# Тестирование с покрытием
./gradlew test jacocoTestReport
```

## 📚 Дополнительные возможности

- **Логирование** - подробные логи для отладки
- **Валидация** - проверка входящих данных
- **Обработка ошибок** - понятные сообщения об ошибках
- **CORS** - поддержка кросс-доменных запросов
- **H2 Console** - веб-интерфейс для базы данных

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch
3. Внесите изменения
4. Добавьте тесты
5. Создайте Pull Request

## 📄 Лицензия

MIT License - см. файл LICENSE для деталей.

---

**Создано командой NSU SQLPlayground**  
*Powered by Spring Boot, GitHub OAuth2 & Swagger*