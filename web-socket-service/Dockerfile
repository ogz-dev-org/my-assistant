FROM node:16.17.0

LABEL authors="oguzugurdogan"

COPY ./build .
COPY package.json .

RUN npm install

ENTRYPOINT ["node", "./index.js"]