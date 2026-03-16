FROM maven:3.8.5-openjdk-17-slim

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
COPY testng.xml .
COPY testng-mock.xml .
COPY wiremock ./wiremock

# Use ENTRYPOINT + CMD kombinaciju
# ENTRYPOINT — ne menja se
# CMD — može da se override iz docker-compose
ENTRYPOINT ["mvn", "test"]
CMD ["-Pintegration"]