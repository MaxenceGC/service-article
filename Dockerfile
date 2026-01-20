FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

RUN useradd -r -u 1001 quarkus && \
    mkdir -p /opt/app && \
    chown -R quarkus:quarkus /opt/app

WORKDIR /opt/app
USER quarkus

COPY --from=builder /workspace/target/quarkus-app/lib/ ./lib/
COPY --from=builder /workspace/target/quarkus-app/*.jar ./
COPY --from=builder /workspace/target/quarkus-app/app/ ./app/
COPY --from=builder /workspace/target/quarkus-app/quarkus/ ./quarkus/

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 \
               -Djava.util.logging.manager=org.jboss.logmanager.LogManager \
               -XX:+UseContainerSupport"

EXPOSE 8080

ENTRYPOINT ["java","-jar","quarkus-run.jar"]
