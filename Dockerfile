############################
# Builder: Maven + JDK 21  #
############################
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Cache dépendances
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

# Build
COPY src ./src
RUN mvn -B -DskipTests package


############################
# Runtime: Temurin JRE 21  #
############################
FROM eclipse-temurin:21-jre
WORKDIR /opt/app

# Copier le layout Quarkus
COPY --from=builder /workspace/target/quarkus-app/lib/ ./lib/
COPY --from=builder /workspace/target/quarkus-app/*.jar ./
COPY --from=builder /workspace/target/quarkus-app/app/ ./app/
COPY --from=builder /workspace/target/quarkus-app/quarkus/ ./quarkus/

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

EXPOSE 8080

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS $JAVA_OPTS_APPEND -jar quarkus-run.jar"]
