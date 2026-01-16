############################
# Builder: Maven + JDK 21  #
############################
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Précharger les dépendances pour profiter du cache Docker
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Compiler les sources
COPY src ./src
RUN mvn -B -DskipTests package

############################
# Runner: UBI9 OpenJDK 21   #
############################
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23 AS runner
ENV LANGUAGE="en_US:en"

# Copier le résultat Quarkus (4 couches) dans /deployments
COPY --chown=185 --from=builder /workspace/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=builder /workspace/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=builder /workspace/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=builder /workspace/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
