FROM adoptopenjdk/openjdk13:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
EXPOSE 8080 8443

ENTRYPOINT ["java","-jar","/app.jar"]
