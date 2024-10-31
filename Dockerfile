# Use uma imagem base do Java
FROM openjdk:17-jdk-alpine AS build


WORKDIR /app/notificacao-app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app/notificacao-app

COPY --from=build /app/notificacao-app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]


EXPOSE 8080
