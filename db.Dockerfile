FROM postgres:latest
RUN localedef -i pl_PL -c -f UTF-8 -A /usr/share/locale/locale.alias pl_PL.UTF-8
ENV LANG pl_PL.utf8
ENV POSTGRES_USER=kinex_user
ENV POSTGRES_DB=kinex
ENV POSTGRES_PASSWORD=termos2137
COPY ./sql/functions.sql /docker-entrypoint-initdb.d/
EXPOSE 5432