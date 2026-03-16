# Use lightweight Maven image with JDK 17
FROM maven:3.8.5-openjdk-17-slim

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first — Docker caches this layer
# If pom.xml doesn't change, dependencies won't be re-downloaded
COPY pom.xml .

# Download all dependencies — cached as separate layer
RUN mvn dependency:go-offline -q

# Copy source code
COPY src ./src

# Copy testng.xml
COPY testng.xml .

# Copy WireMock mappings
COPY wiremock ./wiremock

# Default command — run all tests
ENTRYPOINT ["mvn", "test"]
