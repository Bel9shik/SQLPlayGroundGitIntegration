# Настройка IntelliJ IDEA для SQL Playground

## 🔧 Настройка переменных окружения

### Способ 1: Через Run Configuration

1. **Откройте Run/Debug Configurations**
   - `Run` → `Edit Configurations...`
   - Или нажмите на конфигурацию запуска и выберите `Edit Configurations...`

2. **Создайте новую конфигурацию Spring Boot**
   - Нажмите `+` → `Spring Boot`
   - Name: `SQL Playground`
   - Main class: `org.nsu.Main`
   - Module: `SQLPlayGroundGitIntegration.main`

3. **Добавьте переменные окружения**
   - В разделе `Environment variables` нажмите на иконку папки
   - Добавьте следующие переменные:
     ```
     GITHUB_CLIENT_ID=your_actual_github_client_id
     GITHUB_CLIENT_SECRET=your_actual_github_client_secret
     SERVER_PORT=8081
     ```

4. **Примените настройки**
   - Нажмите `Apply` → `OK`

### Способ 2: Через .env файл (с плагином)

1. **Установите плагин EnvFile**
   - `File` → `Settings` → `Plugins`
   - Найдите и установите `EnvFile`
   - Перезапустите IDEA

2. **Настройте конфигурацию**
   - В Run Configuration включите `Enable EnvFile`
   - Добавьте путь к файлу `.env`

3. **Отредактируйте .env файл**
   ```env
   GITHUB_CLIENT_ID=your_actual_github_client_id
   GITHUB_CLIENT_SECRET=your_actual_github_client_secret
   SERVER_PORT=8081
   ```

## 🔑 Получение GitHub OAuth токенов

### 1. Создание GitHub OAuth App

1. Перейдите в [GitHub Settings](https://github.com/settings/developers)
2. Нажмите `OAuth Apps` → `New OAuth App`
3. Заполните форму:
   - **Application name**: `SQL Playground Local`
   - **Homepage URL**: `http://localhost:8081`
   - **Authorization callback URL**: `http://localhost:8081/login/oauth2/code/github`
4. Нажмите `Register application`
5. Скопируйте `Client ID` и `Client Secret`

### 2. Обновление переменных

Замените значения в конфигурации IDEA:
```
GITHUB_CLIENT_ID=ваш_реальный_client_id
GITHUB_CLIENT_SECRET=ваш_реальный_client_secret
```

## 🚀 Запуск приложения

1. **Выберите конфигурацию** `SQL Playground`
2. **Нажмите Run** (зеленая стрелка)
3. **Дождитесь запуска** (появится логотип Spring Boot)

## 🌐 Доступ к приложению

После успешного запуска:

- **Главная страница**: http://localhost:8081
- **Страница входа**: http://localhost:8081/login
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs
- **H2 Console**: http://localhost:8081/h2-console

## 🔍 Проверка работы

### 1. Тест авторизации
1. Откройте http://localhost:8081
2. Нажмите `Continue with GitHub`
3. Авторизуйтесь в GitHub
4. Вы должны попасть в Swagger UI

### 2. Тест API
1. В Swagger UI нажмите `Authorize`
2. Выполните авторизацию через GitHub OAuth
3. Попробуйте выполнить запрос к `/api/v1/auth/profile`

## 🛠️ Отладка

### Проблемы с портом
Если порт 8081 занят, измените `SERVER_PORT` на другой:
```
SERVER_PORT=8082
```

### Проблемы с OAuth
1. Проверьте правильность `Client ID` и `Client Secret`
2. Убедитесь, что callback URL в GitHub App соответствует порту
3. Проверьте логи приложения на наличие ошибок OAuth

### Логи приложения
В консоли IDEA вы увидите:
```
2025-11-18 01:15:52 - Tomcat started on port(s): 8081 (http)
2025-11-18 01:15:52 - Started Main in X.XXX seconds
```

## 📝 Дополнительные настройки

### JVM Options
Если нужны дополнительные JVM параметры:
```
-Xmx1024m -Xms512m
```

### Active Profiles
Для разных окружений:
```
SPRING_PROFILES_ACTIVE=development
```

### Database URL (если нужно)
```
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
```

## ✅ Готово!

Теперь ваше приложение SQL Playground с OAuth2 и Swagger готово к разработке!