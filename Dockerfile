FROM ubuntu:latest

MAINTAINER Ramakrishnan Sundaramoorthy "ramki149@gmail.com"

RUN apt-get update && apt-get install -y openjdk-8-jdk

WORKDIR /usr/local/bin/

ADD cargointerview-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "cargointerview-0.0.1-SNAPSHOT"]