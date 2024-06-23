# syntax=docker/dockerfile:1

################################################################################

# Stage 1: Resolve and download dependencies
FROM eclipse-temurin:17-jdk-jammy AS deps

WORKDIR /build

# Copy the mvnw wrapper with executable permissions
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/
COPY pom.xml pom.xml

# Convert line endings of mvnw to Unix format and verify it
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix mvnw && \
    ls -l /build/ && ./mvnw --version

# Download dependencies as a separate step to take advantage of Docker's caching
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################

# Stage 2: Build the application
FROM deps AS package

WORKDIR /build

COPY ./src src/

# Debug step to verify the presence of src and pom.xml
RUN ls -l /build/ && ls -l /build/src/

RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Stage 3: Extract the application into separate layers
FROM package AS extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Stage 4: Run the application with minimal runtime dependencies
FROM eclipse-temurin:17-jre-jammy AS final

# Create a non-privileged user that the app will run under
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy the extracted layers from the extract stage
COPY --from=extract /build/target/extracted/dependencies/ ./
COPY --from=extract /build/target/extracted/spring-boot-loader/ ./
COPY --from=extract /build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract /build/target/extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
