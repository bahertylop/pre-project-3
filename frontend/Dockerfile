#FROM node:18-alpine
#WORKDIR /app
#COPY ../../../.. .
#RUN npm install -g http-server
#EXPOSE 3000
#CMD ["http-server", "-p", "3000", "--cors", "*"]

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY ./pages /usr/share/nginx/html

COPY ./mydomain.key /etc/nginx/ssl.key

COPY ./mydomain.crt /etc/nginx/ssl.crt

EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]
