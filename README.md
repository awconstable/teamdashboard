# Team Dashboard
A proof of concept team dashboard

Visibility over team health is important, this application provides a simple method of capturing and visualising team metrics over time

[![CircleCI](https://circleci.com/gh/awconstable/teamdashboard.svg?style=shield)](https://circleci.com/gh/awconstable/teamdashboard)
[![codecov](https://codecov.io/gh/awconstable/teamdashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/awconstable/teamdashboard)
[![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/awconstable/teamdashboard.svg)](https://libraries.io/github/awconstable/teamdashboard)
[![dockerhub](https://img.shields.io/docker/pulls/awconstable/teamdashboard.svg)](https://cloud.docker.com/repository/docker/awconstable/teamdashboard)

![Dashboard Screenshot](https://github.com/awconstable/teamdashboard/raw/master/.github/screenshot.png?raw=true "Team Dashboard")

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* Metrics can be submitted multiple times per person within a period allowing the results to be intentionally or unintentionally skewed
* No security whatsoever - anonymous users can easily delete or alter all data

## Dependencies

1. MongoDB
2. [Team Service](https://github.com/awconstable/teamservice)

## Quick start guide

### Add a team

```
 curl -H "Content-Type: application/json" -X POST -d '{"teamId": "esrp", "teamName": "Team Name", "platformName": "Platform Name", "domainName": "Domain Name"}' http://localhost:8080/team
```

### Add some metrics

```
 curl -H "Content-Type: application/json" -X POST -d '[{"teamMetricType":"cycletime","value":23.0,"date":"2019-05-01"},{"teamMetricType":"lead_time","value":45.0,"date":"2019-05-01"}]' http://localhost:8080/metrics/team1
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

```
docker-compose up
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.host=<mogo host>,--spring.data.mongodb.port=<mongo port>,--spring-data.mongodb.database=<mongo db> --team.service.url=http://localhost:8082"
```

### Run app as a Docker container

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop dashboard_app
docker rm dashboard_app
docker pull awconstable/teamdashboard
docker run --name dashboard_app -d -p 8080:8080 --network <mongo network> -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mondo db> awconstable/teamdashboard:latest
```

