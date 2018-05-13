FROM openjdk:8
COPY ./target/uberjar/ceterus.jar /srv/ceterus.jar
WORKDIR /srv

EXPOSE 8080

ENTRYPOINT /usr/bin/java -Dconfig=`ls /run/secrets/*ceterus_secret.edn` -jar /srv/ceterus.jar