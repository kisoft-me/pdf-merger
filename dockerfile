FROM --platform=linux/amd64  eclipse-temurin:21-jre-alpine
RUN apk --update --no-cache add curl brotli brotli-libs libstdc++
COPY ./target/ /app/
WORKDIR /app
RUN mv /app/pdf-merger-*.jar /app/app.jar
EXPOSE 6001
ENTRYPOINT ["java","-XX:+UseParallelGC","-Xms512m","-Xss4m","-Dproduction=true","-Dsentry.stacktrace.app.packages=net.equiptal","--enable-preview","-jar","/app/app.jar"]
