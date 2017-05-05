FROM openjdk:8-jre

COPY [ "pkg/docker/bootstrap.sh", "application.yml", "/" ]
COPY build/libs/service-virtualidentity*.jar /app.jar
RUN chmod +x /bootstrap.sh
ENTRYPOINT ["/bootstrap.sh"]
EXPOSE 46015