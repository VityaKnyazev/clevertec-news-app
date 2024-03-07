FROM openjdk:17
COPY . /usr/src/appNews
WORKDIR /usr/src/appNews

EXPOSE 8080

RUN microdnf install findutils

CMD ["/bin/bash", "-c", "./gradlew publishToMavenLocal;./gradlew bootRun"]
