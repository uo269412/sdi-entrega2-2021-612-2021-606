let express = require('express');
let app = express();
let rest = require('request');
let os = require('os');
let puerto = 3001;
app.set('rest',rest);

app.listen(puerto, function() {
    console.log("Servidor listo "+puerto);
});

//REST
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});

//CONEXIONES
let fs = require('fs');
let https = require('https');

//TOKEN
let jwt = require('jsonwebtoken');
app.set('jwt',jwt);

//SESIÓN
var expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

//VARIABLES
app.set('port', 8082);
var MongoClient = require('mongodb').MongoClient;
app.set('db',"mongodb://admin:sdi@mywallapop-shard-00-00.g25pg.mongodb.net:27017,mywallapop-shard-00-01.g25pg.mongodb.net:27017,mywallapop-shard-00-02.g25pg.mongodb.net:27017/myFirstDatabase?ssl=true&replicaSet=atlas-c18f3s-shard-0&authSource=admin&retryWrites=true&w=majority");


//ENCRIPTAR
let crypto = require('crypto');
app.set('clave','abcdefg');
app.set('crypto',crypto);
let fileUpload = require('express-fileupload');
app.use(fileUpload());

//MANEJO DATOS
let mongo = require('mongodb');
let swig = require('swig');
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static('public'));
let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app,mongo);

//ROUTERS

//RUTAS
require("./routes/rusuarios.js")(app, swig, gestorBD); // (app, param1, param2, etc.)
require("./routes/rofertas.js")(app, swig, gestorBD); // (app, param1, param2, etc.)

//LANZAR EL SERVIDOR
https.createServer({
    key: fs.readFileSync('certificates/alice.key'),
    cert: fs.readFileSync('certificates/alice.crt')
}, app).listen(app.get('port'), function() {
    console.log("Servidor activo");
});