#FROM node:18-alpine
#WORKDIR /app
#COPY ../../../.. .
#RUN npm install -g http-server
#EXPOSE 3000
#CMD ["http-server", "-p", "3000", "--cors", "*"]

FROM nginx:alpine

RUN apk add --no-cache certbot certbot-nginx

COPY nginx.conf /etc/nginx/nginx.conf

RUN certbot certonly --standalone --agree-tos --email levka288@gmail.com -d bahertylop.ru

COPY ./pages /usr/share/nginx/html

EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]
