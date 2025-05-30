FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
# Set the working directory in the container
WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean install

FROM eclipse-temurin:21-jre-alpine
RUN apk --update --no-cache add curl brotli brotli-libs libstdc++
WORKDIR /app
COPY --from=build /app/target/ .
RUN mv /app/pdf-merger-*.jar /app/app.jar
EXPOSE 6001
ENTRYPOINT ["java","-XX:+UseParallelGC","-Xms512m","-Xss4m","-Dproduction=true","-Dsentry.stacktrace.app.packages=net.equiptal","--enable-preview","-jar","/app/app.jar"]
