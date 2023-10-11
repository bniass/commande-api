FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} commande-api.jar
ENTRYPOINT ["java", "-jar", "commande-api.jar"]