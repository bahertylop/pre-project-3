# Dockerfile
FROM ubuntu:22.04

EXPOSE 6000 80

# читаем переменные среды из .env
ARG DOMAIN_EMAIL
ARG DOMAIN_URL

# устанавливаем переменные среды в переменные
ENV DOMAIN_EMAIL=levka288@gmail.com
ENV DOMAIN_URL=bahertylop

WORKDIR /certbot
COPY . /certbot
WORKDIR /certbot

RUN apt-get update
RUN apt-get -y install certbot

# запускаем скрипт генерации
CMD ["sh", "generate-certificate.sh"]