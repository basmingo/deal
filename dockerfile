FROM openjdk:20
COPY target/scala-2.13/deal-assembly-0.1.0-SNAPSHOT.jar deal.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deal.jar"]