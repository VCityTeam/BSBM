FROM eclipse-temurin:26
LABEL maintainer="vcity"
LABEL description="This is a docker image for the BSBM project"

WORKDIR /app
COPY . /app

ENV DATA_DESTINATION=data

ENTRYPOINT ["/app/entrypoint.sh"]
