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
// routerUsuarioToken
let routerUsuarioToken = express.Router();
routerUsuarioToken.use(function(req, res, next) {
    // obtener el token, vía headers (opcionalmente GET y/o POST).
    let token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        // verificar el token
        jwt.verify(token, 'secreto', function(err, infoToken) {
            if (err || (Date.now()/1000 - infoToken.tiempo) > 240 ){
                res.status(403); // Forbidden
                res.json({
                    acceso : false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
                return;

            } else {
                // dejamos correr la petición
                res.usuario = infoToken.usuario;
                next();
            }
        });

    } else {
        res.status(403); // Forbidden
        res.json({
            acceso : false,
            mensaje: 'No hay Token'
        });
    }
});
// Aplicar routerUsuarioToken
app.use('/api/cancion', routerUsuarioToken);

// routerUsuarioSession
var routerUsuarioSession = express.Router();
routerUsuarioSession.use(function(req, res, next) {
    console.log("routerUsuarioSession");
    if ( req.session.usuario ) {
        // dejamos correr la petición
        next();
    } else {
        console.log("va a : "+req.session.destino);
        res.redirect("/identificarse");
    }
});

//Aplicar routerUsuarioSession
app.use("/ofertas/agregar",routerUsuarioSession);
app.use("/propias",routerUsuarioSession);
app.use("/cancion/comprar",routerUsuarioSession);
app.use("/compras",routerUsuarioSession);
app.use("/ofertas",routerUsuarioSession);
app.use("/conversaciones/list",routerUsuarioSession);

//routerUsuarioAutor

let routerUsuarioAutor = express.Router();
routerUsuarioAutor.use(function(req, res, next) {
    console.log("routerUsuarioAutor");
    let path = require('path');
    let id = path.basename(req.originalUrl);
    gestorBD.obtenerOfertas(
        {_id: mongo.ObjectID(id) }, function (ofertas) {
            console.log(ofertas[0]);
            if(ofertas[0].vendedor == req.session.usuario ){
                next();
            } else {
                res.redirect("/ofertas");
            }
        })
});

//Aplicar routerUsuarioAutor
app.use("/oferta/eliminar",routerUsuarioAutor);

//RUTAS
require("./routes/rusuarios.js")(app, swig, gestorBD); // (app, param1, param2, etc.)
require("./routes/rofertas.js")(app, swig, gestorBD); // (app, param1, param2, etc.)
require("./routes/rconversaciones.js")(app, swig, gestorBD); // (app, param1, param2, etc.)
//LANZAR EL SERVIDOR
https.createServer({}, app).listen(app.get('port'), function() {
    console.log("Servidor activo");
});