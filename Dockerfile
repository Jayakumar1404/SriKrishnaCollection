
FROM maven:3.9-eclipse-temurin-26

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java -jar target/*.jar"]
=======
# FROM eclipse-temurin:26-jdk

# WORKDIR /app

# COPY . .

# RUN chmod +x mvnw

# RUN ./mvnw clean package -DskipTests

# EXPOSE 8080

# CMD ["java", "-jar", "target/srikrishna-0.0.1-SNAPSHOT.jar"]

