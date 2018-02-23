FROM openjdk:8-jre-alpine
VOLUME postman
ADD target/postman-restful-api-exporter-1.0-SNAPSHOT.jar /postman/exporter.jar
ADD /opt/monitor/postman/postman_globals.json /postman/postman_globals.json
ADD /opt/monitor/postman/postman_environment.json /postman/postman_environment.json
ADD /opt/monitor/postman/postman_collection.json /postman/postman_collection.json
ENTRYPOINT ["java", "-jar", "/postman/exporter.jar"]