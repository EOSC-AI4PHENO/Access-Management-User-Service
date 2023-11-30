FROM openjdk:11

# Install curl and git
RUN apt-get update && apt-get install -y curl git

# Update git to fix CVE-2022-41903
RUN apt-get upgrade -y git

COPY *.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]