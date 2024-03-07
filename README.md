<h1>CLEVERTEC (final task)</h1>

<p>CLEVERTEC final task - News App (main):</p>

<p>
Разработать RESTfulweb-service, реализующей функционал для работы с системой управления новостями.
Основныесущности:
<p>-	news (новость) содержит поля: id, time, title, text и comments (list).</p>
<p>-	comment содержит поля: id, time, text, username и news_id.</p>
</p>

<p>Требования:</p>

<ol>

<li>
Использовать SpringBoot 3.x, Java 17, GradleиPostgreSQL. 
</li>

<li>
Разработать APIсогласно подходам REST (UI не надо):
<p>-	CRUD для работы с новостью</p>
<p>-	CRUD для работы с комментарием</p>
<p>-	просмотр списка новостей (с пагинацией)</p>
<p>-	просмотр новости с комментариями относящимися к ней (с пагинацией)</p>
<p>-    /news/{newsId}/comments/{commentsId}</p>
<p>-	полнотекстовый поиск по различным параметрам (для новостей и комментариев)</p>
Для потенциально объемных запросов реализовать постраничность.
</li>

<li>
Разместить проект в любом из публичных git-репозиториев (Bitbucket, github, gitlab).
</li>

<li>
Код должен быть легко читаемый и понятный, с использованием паттернов проектирования.
</li>

<li>
Реализовать на основе Spring @Profile (e.g. test&prod) подключение к базам данных. 
</li>

<li>
Подключить liquibase:
<p>- при запуске сервиса накатываются скрипты на рабочую БД (генерируются необходимые таблицы из одного файла и наполняются таблицы данными 
из другого файла, 20 новостей и 10 комментариев, связанных с каждой новостью</p>
<p>- при запуске тестов должен подхватываться скрипт по генерации необходимых таблиц + накатить данные по заполнению таблиц (третий файл).</p>
</li>

<li>
Создать реализацию кэша, для хранения сущностей. Реализовать два алгоритмаLRU и LFU. 
Алгоритм и максимальный размер коллекции должны читаться из файла application.yml. 
Алгоритм работы с кешем:
<p>•	GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем</p>
<p>•	POST - сохраняем в dao и потом сохраняем в кеше</p>
<p>•	DELETE - удаляем из dao и потом удаляем в кеша</p>
<p>•	PUT - обновление/вставка в dao и потом обновление/вставка в кеше.</p>
</li>

<li>
Весь код должен быть покрыт юнит-тестами (80%) (сервисный слой – 100%).
</li>

<li>
Реализовать логирование запрос-ответ в аспектном стиле (для слоя Controlles), а также логирование по уровням в отдельных 
слоях приложения, используя logback.
</li>

<li>
Предусмотреть обработку исключений и интерпретацию их согласно REST (см. https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
</li>

<li>
Все настройки должны быть вынесены в *.yml
</li>

<li>
Код должен быть документирован @JavaDoc, а назначение приложения и его интерфейс 
и настройки должны быть описаны в README.md файле
</li>

<li>
Использовать SpringRESTDocs или другие средства автоматического документирования 
(например asciidoctorhttps://asciidoctor.org/docs/asciidoctor-gradle-plugin/ и т.д) 
и/или Swagger (OpenAPI 3.0)
</li>

<li>
использовать testcontainers в тестах на persistence layer (для БД).
</li>

<li>
Написать интеграционные тесты.
</li>

<li>
Использовать WireMock в тестах для слоя clients.
</li>

<li>
Использовать Docker (написать Dockerfile–для springboot приложения, 
docker-compose.yml для поднятия БД и приложения в контейнерах и настроить взаимодействие между ними).
</li>

<li>
* Подключить кэш провайдер Redis (в docker) 
(в случае реализации, использовать @Profile для переключения между LRU/LFU и Redis)
</li>

<li>
* Spring Security:
<p>-	API для регистрации пользователей с ролями admin/journalist/subscriber</p>
<p>-	Администратор (roleadmin) может производить CRUD-операции со всеми сущностями</p>
<p>-	Журналист (rolejournalist) может добавлять и изменять/удалять только свои новости </p>
<p>-	Подписчик (rolesubscriber) может добавлять и изменять/удалять только свои комментарии</p>
<p>-	Незарегистрированные пользователи могут только просматривать новости и комментарии</p>
Создать отдельный микросервис с реляционной базой (postgreSQL) хранящей
информацию о пользователях/ролях. Из главного микросервиса (отвечающего за
новости) запрашивать эту информацию по  REST с использованием spring-cloud-
feign-client.
</li>

<li>
* Настроить SpringCloudConfig (вынести в отдельный сервис и настроить разрабатываемый сервис 
на получение их в зависимости от профиля)
</li>

<li>
* Реализацию логирования п.10 и обработку исключений вынести в отдельные
spring-boot-starter-ы.
</li>

<li>
* Сущности веб интерфейса (DTO) должны генерироваться при сборке проекта из 
.proto файлов (см. https://github.com/google/protobuf-gradle-plugin)
</li>

</ol>


<h2>Что сделано:</h2>

<p>
Разработан RESTfulweb-service, реализующий функционал для работы с системой управления новостями.
Основныесущности:
- news (новость) содержит поля: id, uuid, created, updated, title, text, comments (list), journalist_id.
- comment содержит поля: id, uuid, created, text, subscriber_id, news_id, updated.
</p>

<ol>

<li>
Использован SpringBoot 3.x, Java 17, Gradle, PostgreSQL и др. 
</li>

<li>
Разработан API согласно подходам REST (UI не разрабатывался):
<p>- CRUD для работы с новостью</p>
<p>- CRUD для работы с комментарием</p>
<p>- просмотр списка новостей (с пагинацией)</p>
<p>- просмотр новости с комментариями относящимися к ней (с пагинацией)</p>
<p>- полнотекстовый поиск по различным параметрам (для новостей и комментариев)</p>
</li>

<li>
Проект размещен в публичном git-репозитории github.
</li>

<li>
Код сделаy легко читаемым и понятным, с использованием паттернов проектирования.
</li>

<li>
Реализовано на основе Spring @Profile (test, dev, prod) подключение к базам данных в файле 
настроек application.yaml. 
</li>

<li>
Подключен liquibase:
<p>
- при запуске сервиса накатываются скрипты на рабочую БД (генерируются необходимые таблицы 
из одного файла и наполняются таблицы данными из другого файла, 20 новостей и 10 комментариев, 
связанных с каждой новостью.
</p>

<p>
- при запуске тестов подхватывается скрипт по генерации необходимых таблиц + накатываются данные 
по заполнению таблиц.
</p>
</li>

<li>
Создана реализация кэша, для хранения сущностей. Реализовано два алгоритма LRU и LFU. 
Алгоритм и максимальный размер коллекции читаються из файла application.yml. 
Алгоритм работы с кешем:
<p>
• GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем.
</p>

<p>
• POST - сохраняем в dao и потом сохраняем в кеше
</p>

<p>
• DELETE - удаляем из dao и потом удаляем в кеша
</p>

<p>
• PUT - обновление/вставка в dao и потом обновление/вставка в кеше.
</p>
</li>

<li>
Код покрыт  юнит-тестами.
</li>

<li>
Реализовано логирование запрос-ответ в аспектном стиле (для слоя Controlles), включить / отключить, 
которое можно в файле настроек для dev | prod профиля. 
а также логирование по уровням в отдельных слоях приложения, используя logback.
</li>

<li>
Предусмотрена обработка исключений и интерпретация их согласно REST.
</li>

<li>
Все настройки вынесены в *.yaml
</li>

<li>
Код документирован @JavaDoc, а назначение приложения и его интерфейс и настройки 
описаны в README.md файле.
</li>

<li>
Использован SpringRESTDocs как средство автоматического документирования.
</li>

<li>
Использован testcontainers в интеграционных тестах.
</li>

<li>
Написаны интеграционные тесты.
</li>

<li>
Использован WireMock в интеграционных тестах для тестирования клиента, возвращающего данные 
с другого микросервиса.
</li>

<li>
Использовать Docker (написан Dockerfile–для springboot приложения, 
docker-compose.yml для поднятия БД и приложения в контейнерах и настроено взаимодействие между ними).
</li>

<li>
* Подключен кэш провайдер Redis (в docker) (использован @Conditional, а для переключения между LRU/LFU и Redis - application.yaml)
</li>

<li>
* Spring Security:
<p>
- API для регистрации пользователей с ролями admin/journalist/subscriber.
</p>

<p>
- Администратор (roleadmin) может производить CRUD-операции со всеми сущностями.
</p>

<p>
- Журналист (rolejournalist) может добавлять и изменять/удалять только свои новости.
</p>

<p>
- Подписчик (rolesubscriber) может добавлять и изменять/удалять только свои комментарии.
</p>

<p>
- Незарегистрированные пользователи могут только просматривать новости и комментарии.
</p>

<p>
Создан отдельный микросервис с реляционной базой (keycloak c postgreSQL) хранящей информацию 
о пользователях/ролях (см. users). Из главного микросервиса (отвечающего за новости) запрашивается 
информация по REST с использованием spring-cloud-feign-client о пользователях и ролях.
</p>

<p>
После аутентификации пользователю возвращается сгенерированный Bearer токен, который необходимо
подставлять в Authoreazation header для обращения к защищенным ендпоинтам.
</p>

</li>

<li>
* Добавлены стартеры
</li>

<li>

</li>

<li>

</li>

</ol>


<h3>Запуск и использование:</h3>

<ol>
<li>Билдим:.\gradlew publish .\gradlew clean build</li>
<li>Запускаем news app в docker: docker-compose up -d</li>
</ol>
