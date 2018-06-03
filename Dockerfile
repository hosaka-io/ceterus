FROM openjdk:8-jre-alpine
COPY ./target/uberjar/ceterus.jar /srv/ceterus.jar
WORKDIR /srv

EXPOSE 8080 8079

ENTRYPOINT /usr/bin/java -Xms128m -Xmx512m -Dconfig=`ls /run/secrets/*ceterus_secret.edn` -jar /srv/ceterus.jar