FROM eclipse-temurin:11
LABEL maintainer="vcity"
LABEL description="This is a docker image for the BSBM project"
WORKDIR /app
COPY . /app
ENTRYPOINT ["/app/entrypoint.sh"]
