# Estágio de build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
# Baixa as dependências para cache em camada separada
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Estágio de execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/pedido-design-patterns-1.0.0.jar app.jar

# Expõe a porta que a aplicação utiliza
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]