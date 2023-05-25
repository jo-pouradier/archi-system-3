FROM openjdk:8
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/ProxyServer-0.0.1-SNAPSHOT.jar ProxyServer-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "ProxyServer-0.0.1-SNAPSHOT.jar"]