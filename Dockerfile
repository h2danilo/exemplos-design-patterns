# Estágio 1: Compila a aplicação com Maven
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Estágio 2: Cria a imagem final e otimizada
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia apenas o JAR executável do estágio de compilação
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]