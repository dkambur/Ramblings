FROM postgres:latest
RUN apt update
RUN apt install -y curl wget vim
RUN apt install -y postgresql-contrib-14

RUN echo "\
shared_preload_libraries = 'pg_stat_statements' \n\
\n\
track_activity_query_size = 2048 \n\
track_io_timing = on \n\
\n\
pg_stat_statements.track = all" >>/usr/share/postgresql/postgresql.conf.sample


RUN echo "CREATE EXTENSION IF NOT EXISTS pg_stat_statements" >/docker-entrypoint-initdb.d/EnablePGStat.sql