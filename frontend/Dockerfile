#FROM node:18-alpine
#WORKDIR /app
#COPY ../../../.. .
#RUN npm install -g http-server
#EXPOSE 3000
#CMD ["http-server", "-p", "3000", "--cors", "*"]

FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf

COPY ./pages /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
