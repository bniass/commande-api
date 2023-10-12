FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8087
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} commande-api.jar
ENTRYPOINT ["java", "-jar", "commande-api.jar"]