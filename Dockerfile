FROM maven:3.8.5-openjdk-17-slim

WORKDIR /app

# Copy pom.xml first - better Docker layer caching
COPY pom.xml .

# Download dependencies - cached as separate layer
RUN mvn dependency:go-offline -q

# Copy source code
COPY src ./src

# Copy all TestNG files
COPY testng.xml .
COPY testng-mock.xml .
COPY testng-keycloak.xml .

# Copy WireMock mappings
COPY wiremock ./wiremock

# Copy Keycloak config
COPY keycloak ./keycloak

# ENTRYPOINT - always "mvn test"
# CMD - default profile, can be overridden from docker-compose
ENTRYPOINT ["mvn", "test"]
CMD ["-Pintegration"]