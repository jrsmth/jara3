var express = require('express');
var bodyParser = require('body-parser');
const port = process.env.PORT || 8702;

var app = express();
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

// ------------------ METHODS --------------------------------------------------

app.all('/', function (req, res) {
  var port = server.address().port;
  var msg = 'Hello from the Frontend, on port ' + port;
  res.status(200);
  res.end(msg);
})

// ------------------ Eureka Config --------------------------------------------

const Eureka = require('eureka-js-client').Eureka;

const eureka = new Eureka({
  instance: {
    app: 'jara3-frontend',
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    statusPageUrl: 'http://localhost:'+port,
    port: {
      '$': port,
      '@enabled': 'true',
    },
    vipAddress: 'localhost',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    }
  },
  eureka: {
    host: 'localhost',
    port: 8761,
    servicePath: '/eureka/apps/'
  }
});
eureka.logger.level('debug');
eureka.start(function(error){
  console.log(error || 'complete');
});

// ------------------ Server Config --------------------------------------------
var server = app.listen(port, function () {
  var host = server.address().address;
  var port = server.address().port;
  console.log('Listening at http://%s:%s', host, port);
});