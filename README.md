# Pack free ebook - slack bot

Slack bot for sending daily free random ebook from Packt Publishing.

![Slack preview](img/slack_preview.png)

#### Description

1. Getting and parsing data from Packt Publishing website.
2. Mapping ebook data to Slack message format.
3. Executing POST request to Slack webhook url.

#### Technology

* Java Spring Boot
* REST
* Jsoup / Xsoup
* Lombok
* Maven

#### Build and run

```sh
$ mvn clean install
```
```sh
$ java -jar target/ebook-0.0.1-SNAPSHOT.jar
```

#### Features

- [x] Configurable parameters in application.properties
- [ ] Create external config.properties file outside jar for dynamic parameters
- [ ] Web admin dashboard with configurable parameters
- [ ] Multiple Slack webhooks handling
- [ ] Configurable cron scheduler with Quartz support

#### To do

- [ ] Enums for styles, types, colors etc.
- [ ] Disable sending with null fields -> exceptions 
- [ ] Move cron expression to properties