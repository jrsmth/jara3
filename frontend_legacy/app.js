require('dotenv').config();
const Server = require('./models/server');
const server = new Server();

server.listen();

const PORT = process.env.PORT || 3000; // should this be 8702 instead? or does the server run on a different port to the client here?
const eurekaHelper = require('./models/eureka-helper');

eurekaHelper.registerWithEureka('jara3-frontend', PORT);
