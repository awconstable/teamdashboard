# Team Dashboard
A proof of concept DevOps Performance dashboard

Awareness of team performance is important to enable data-driven continuous improvement.
[Accelerate](https://itrevolution.com/book/accelerate/), [DORA and the State of DevOps report](https://www.devops-research.com/research.html) has had a massive impact on DevOps transformations around the world providing guidance on what to measure to retain focus on the key DevOps outcomes - continuously improving Value Throughput and Stability.

[Google Cloud's The Four Keys](https://cloud.google.com/blog/products/devops-sre/using-the-four-keys-to-measure-your-devops-performance), is a fantastic tool for teams using GCP - but what if you're on-premise, or have many teams or use another cloud provider?

The Team Dashboard is here to help by providing a simple set of containerised services to enable your teams to easily measure and visualise Lead Time for Change, Deployment Frequency, Change Failure Rate and Mean Time to Recover.

---

[![CircleCI](https://circleci.com/gh/awconstable/teamdashboard.svg?style=shield)](https://circleci.com/gh/awconstable/teamdashboard)
![CodeQL](https://github.com/awconstable/teamdashboard/workflows/CodeQL/badge.svg)
[![codecov](https://codecov.io/gh/awconstable/teamdashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/awconstable/teamdashboard)
[![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/awconstable/teamdashboard.svg)](https://libraries.io/github/awconstable/teamdashboard)
[![dockerhub](https://img.shields.io/docker/pulls/awconstable/teamdashboard.svg)](https://cloud.docker.com/repository/docker/awconstable/teamdashboard)

![Dashboard Screenshot](https://github.com/awconstable/teamdashboard/raw/master/.github/metrics-screenshot.jpg?raw=true "Team Dashboard")
![Team Explorer Screenshot](https://github.com/awconstable/teamdashboard/raw/master/.github/explorer-screenshot.png?raw=true "Team Explorer")
![Deployments Screenshot](https://github.com/awconstable/teamdashboard/raw/master/.github/deployments-screenshot.png?raw=true "Deployments")

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* Metrics can be submitted multiple times within a period allowing the results to be intentionally or unintentionally skewed
* No security whatsoever - anonymous users can easily delete or alter all data

## Dependencies

1. Consul
1. MongoDB
1. [Team Service](https://github.com/awconstable/teamservice)
1. [Deployment Service](https://github.com/awconstable/deployservice)
1. [Incident Service](https://github.com/awconstable/incidentservice)
1. [Change Service](https://github.com/awconstable/changeservice)
1. [Team Happiness App](https://github.com/awconstable/happiness)

## Quick start guide

### Add some metrics

```
 curl -H "Content-Type: application/json" -X POST -d '[{"teamMetricType":"cycletime","value":23.0,"date":"2019-05-01"},{"teamMetricType":"lead_time","value":45.0,"date":"2019-05-01"}]' http://localhost:8080/metrics/team1
```

### View team dashboard

<http://localhost:8080/>


---

## Developer guide

### Compile

```
./mvnw clean install
```

### Run in development

```
docker-compose up
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.data.mongodb.host=<mogo host>,--spring.data.mongodb.port=<mongo port>,--spring-data.mongodb.database=<mongo db> --team.service.url=http://localhost:8082 --spring.cloud.discovery.enabled=true --spring.cloud.service-registry.auto-registration.enabled=true --spring.cloud.consul.config.enabled=true --spring.cloud.consul.host=<consul host> --spring.cloud.consul.port=<consul port>"
```

### Run app as a Docker container

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop dashboard_app
docker rm dashboard_app
docker pull awconstable/teamdashboard
docker run --name dashboard_app -d -p 8080:8080 --network <mongo network> -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mondo db> awconstable/teamdashboard:latest
```

