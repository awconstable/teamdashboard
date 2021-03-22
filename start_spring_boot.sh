#!/bin/sh

#../mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.cloud.discovery.enabled=true --spring.cloud.service-registry.auto-registration.enabled=true --spring.cloud.consul.config.enabled=true --spring.cloud.consul.discovery.health-check-tls-skip-verify=true --logging.level.sun.net.www=TRACE --spring.cloud.consul.discovery.scheme=https --server.ssl.key-store-type=PKCS12 --server.ssl.key-store=happiness.p12 --server.ssl.key-store-password=changeit --server.ssl.key-alias=happiness"

../mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.cloud.discovery.enabled=true --spring.cloud.service-registry.auto-registration.enabled=true --logging.level.sun.net.www=TRACE --spring.cloud.consul.discovery.scheme=http"
