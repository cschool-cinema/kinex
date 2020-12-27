FROM openjdk:8-jre
RUN mkdir /app
WORKDIR /app
COPY target/*.jar kinex.jar
RUN useradd --system kinex
RUN chown -R kinex:kinex /app
USER kinex
EXPOSE 8080
ENTRYPOINT ["java", "-Djasypt.encryptor.password=@x8HcZsUlfdE", "-jar","kinex.jar"]