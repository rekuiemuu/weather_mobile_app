
# Weather App — Курсовой проект

**Weather App** — Android-приложение для просмотра прогноза погоды с возможностью авторизации, локального хранения данных, просмотра истории и удобным интерфейсом.  
Реализовано в рамках курсовой работы по дисциплине **"Разработка мобильных приложений"**.

---

##  Функциональность

- Поиск прогноза по городу
- Получение прогноза по текущей геолокации
- Актуальная информация о температуре, влажности, скорости ветра и УФ-индексе
- Прогноз на несколько дней
- История запросов
- Авторизация (Firebase Authentication)
- Настройки

---

## Технологии и библиотеки

| Категория        | Использовано                              |
|------------------|--------------------------------------------|
| Язык             | Java                                       |
| UI               | ViewBinding, Material Design 3             |
| Архитектура      | MVVM, Repository                           |
| Сеть             | Retrofit, Gson                             |
| БД               | Room (SQLite)                              |
| DI               | Dagger Hilt                                |
| Кэш/Prefs        | SharedPreferences                          |
| Картинки         | Glide                                      |
| Геолокация       | FusedLocationProviderClient                |
| Авторизация      | Firebase Auth                              |
| API              | [weatherapi.com](https://openweathermap.org/api) |
| Навигация        | Jetpack Navigation                         |
| Тестирование     | JUnit, Espresso, Mockito                   |

---

## Сборка и запуск

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/yourname/weather_app.git
   cd weather_app
   ```

2. В `local.properties` добавьте ключ:
   ```properties
   weatherApiKey=ВАШ_КЛЮЧ
   ```

3. Соберите и запустите проект в Android Studio.

