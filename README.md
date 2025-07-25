# Card Transfer Project

Приложение для перевода средств с одной банковской карты на другую.

## О проекте
- Backend — REST-сервис на Spring Boot, реализующий логику переводов.
- Frontend — React-приложение для взаимодействия с пользователем.
- Логи операций записываются в файл `transfers.log`.
- Запуск через Docker/

## Запуск проекта
### Запуск FRONT
- открыть корневую папку проекта FRONT
- открыть cmd из этой папки
- ввести: `set NODE_OPTIONS=--openssl-legacy-provider`
- ввести: `npm run start`
- FRONT запустится по адресу http://localhost:3000/card-transfer

### Запуск BACK
- собрать Docker-образ: `docker build -t spring-demo-app .`
- запустить контейнер из образа: `docker run -p 5500:5500 spring-demo-app`
- после запуска приложение будет доступно по адресу http://localhost:5500
