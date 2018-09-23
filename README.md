# Team Dashboard
A proof of concept team dashboard

Visibility over team health is important, this application provides a simple method of capturing and visualising team metrics over time

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* Metrics can be submitted multiple times per person within a period allowing the results to be intentionally or unintentionally skewed
* No security whatsoever - anonymous users can easily delete or alter all data

## Dependencies

1. MongoDB

## Quick start guide

### Add a team

```
 curl -H "Content-Type: application/json" -X POST -d '{"teamId": "esrp", "teamName": "Team Name", "platformName": "Platform Name", "domainName": "Domain Name"}' http://localhost:8080/team
```


### View team dashboard

<http://localhost:8080/dashboard/{teamId}>


---

## Developer guide

### Compile

```
mvn clean install
```

### Run in development

*Might be **-Drun.arguments** - see: https://stackoverflow.com/questions/23316843/get-command-line-arguments-from-spring-bootrun*

```
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.host=<mogo host>,--spring.data.mongodb.port=<mongo port>,--spring-data.mongodb.database=<mongo db>"
```

### Create Docker image

```
mvn dockerfile:build dockerfile:tag
```

### Run app as a Docker container

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop dashboard_app
docker rm dashboard_app
docker run --name dashboard_app -d -p 8080:8080 --network <mongo network> -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mondo db> team/dashboard:0.0.1-SNAPSHOT
```

