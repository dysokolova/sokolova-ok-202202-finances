# sokolova-ok-202202-finances

Учебный проект Соколовой Д.Ю. курса [Kotlin Backend Developer](https://otus.ru/lessons/kotlin/?int_source=courses_catalog&int_term=programming).
Системы учета финансов:
возможность хранения актуального состояния и истории изменения счетов, возможность получения отчетов по изменениям счетов за период.



## Учебный маркетинг приложения

Считаем, что целевая аудитория учебного сервиса -- это физические лица, которые стремятся выровнять собственный бюджет и начать копить деньги на важные покупки.

### Гипотетический портрет пользователя для MVP системы учета финансов

Лица от 25 до 35 лет с достатком до 150 т.р. / месяц

1. без детей, вне брака, или в браке но без детей, ведущие отдельный личный бюджет.

2. с маленьким ребенком, в браке обеспечивающие семью в одиночку (предположительно, мужчины). 

## Описание MVP

1. Пользователь может управлять своими счетами в системе (создать, изменить название/описание, безопасно удалить, найти)
2. Пользователь может управлять операциями со своими счетами вида (датавремя + счет -> счет + сумма) (создать, изменить датавремя/счет из/счет в/сумма, найти, прочитать)
3. Пользователь может просмотреть текущее состояние своего счета, историю операций по своему счету

### Функции (эндпониты)

1. CRUDS (create, read, update, delete, search) для счёта (account)
2. CRUD (create, read, update, delete) для операции (operation)
3. account.history

### Описание сущности account
* UserId
* Name
* Description
* AccountId

### Описание сущности operation
* UserId
* Description
* Amount
* FromAccountId
* ToAccountId
* DateTime
* OperationId


# Структура проекта

## Транспортные модели, API

1. [specs](specs) - описание API в форме OpenAPI-спецификаций
2. [finances-transport-main-openapi](finances-transport-main-openapi) - Генерация первой версии транспортных моделей с
   Jackson
3. [finances-common](finances-common) - Модуль с общими классами для модулей проекта. В частности,
   там располагаются внутренние модели и контекст.
4. [finances-mappers](finances-mappers) - Мапер между внутренними моделями и моделями API

## Фреймворки и транспорты

5. [finances-services](finances-services) - Сервис. Служит оберткой для модуля бизнес-логики. Подключается всеми фреймворками (модулями *-app-*)
6. [finances-app-ktor](finances-app-ktor) - Приложение на Ktor JVM для запуска API (Rest, Websocket)
7. [finances-app-rabbit](finances-app-rabbit) - Микросервис на RabbitMQ

## Модули бизнес-логики

8. [finances-stubs](finances-stubs) - Стабы для ответов сервиса
9. [finances-biz](finances-biz) - Модуль бизнес-логики приложения

## Хранение, репозитории, базы данных

10. [finances-repo-test](finances-repo-test) - Базовые тесты для репозиториев всех баз данных
11. [finances-repo-inmemory](finances-repo-inmemory)  - Репозиторий на базе EhCache для тестирования
12. [finances-storage-postgresql](finances-storage-postgresql) - Репозиторий на базе PostgreSQL
