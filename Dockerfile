From openjdk:17
VOLUME /tmp
EXPOSE 3032
COPY target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar authentication.jar
ENTRYPOINT ["java", "-jar", "/authentication.jar"]