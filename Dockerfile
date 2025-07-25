FROM openjdk:17-alpine
EXPOSE 5500
ADD target/springBootDemo-0.0.1-SNAPSHOT.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]