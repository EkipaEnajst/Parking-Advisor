FROM eclipse-temurin:17
WORKDIR /opt/parkingadvisor/
RUN mkdir api
COPY ./api ./api
CMD ["java", "-jar", "./api/target/parkingadvisor-1.0-SNAPSHOT.jar"]