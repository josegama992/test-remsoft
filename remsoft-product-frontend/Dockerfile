FROM node:22-alpine AS build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install -g @angular/cli@19 && npm install

COPY . .

RUN ng build --configuration=production

FROM nginx:alpine AS production-stage

COPY --from=build-stage /app/dist/sakai-ng/browser /usr/share/nginx/html

# Expor a porta 80
EXPOSE 80

# Comando para rodar o Nginx
CMD ["nginx", "-g", "daemon off;"]
