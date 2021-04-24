module.exports = function(app, swig, gestorBD) {

    //items/add
    app.get('/ofertas/agregar', function (req, res) {
        let respuesta = swig.renderFile('views/offers/add.html', {});
        res.send(respuesta);
    });

    //items/add
    app.post("/oferta", function(req, res) {
        var oferta = {
            titulo : req.body.titulo,
            descripcion : req.body.descripcion,
            fechaSubida : new Date(),
            precio : req.body.precio,
            vendedor: req.session.usuario,
            comprador: null
        }
        // Conectarse
        gestorBD.insertarOferta(oferta, function(id){
            if (id == null) {
                res.send("Error al insertar oferta");
            } else {
                res.redirect("/home");
            }
        });
    });

    //items/listAll
    app.get("/ofertas", function(req, res) {
        let criterio = {};
        if (req.query.busqueda != null) {
            criterio = {"nombre": req.query.busqueda};
        }

        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas == null) {
                res.redirect("/error" + "?mensaje=Error al listar." + "&tipoMensaje=alert-danger");
            } else {
                let ultimaPg = total / 4;
                if (total % 4 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                let paginas = []; // paginas mostrar
                for (let i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                let respuesta = swig.renderFile('views/offers/listAll.html',
                    {
                        ofertas: ofertas,
                        paginas: paginas,
                        actual: pg
                    });
                res.send(respuesta);
            }
        });
    });


    //items/delete/{id}
    app.get("/oferta/eliminar/:id", function(req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (oferta) {
            if (oferta == null) {
                res.send("Error");
            } else {
                res.redirect("/propias");
            }
        });
    });

    //items/comprar/{id}
    app.get("/oferta/comprar/:id", function(req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        let oferta = {
            comprador: req.session.usuario
        }
        gestorBD.añadirCompra(criterio, oferta, function (idCompra) {
            if (idCompra == null) {
                res.send(respuesta);
            } else {
                gestorBD.obtenerOfertas(criterio,function(ofertas){
                    if ( ofertas == null ){
                        res.send(respuesta);
                    } else {
                        let criterioUsuario = {"email": req.session.usuario};
                        gestorBD.obtenerUsuarios(criterioUsuario, function(usuarios) {
                            if (usuarios == null || usuarios.length == 0) {
                                req.session.usuario = null;
                                res.redirect("/identificarse" +
                                    "?mensaje=Email o password incorrecto"+
                                    "&tipoMensaje=alert-danger ");
                            } else {
                                console.log(Number(usuarios[0].saldo) - Number(ofertas[0].precio))
                                let usuarioNuevo = {
                                    saldo: Number(usuarios[0].saldo) - Number(oferta.precio)
                                }
                                gestorBD.modificarUsuario(criterioUsuario, usuarioNuevo, function (idCompra) {
                                    if (idCompra == null) {
                                        res.send(respuesta);
                                    } else {
                                        res.redirect("/compras");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    });


    app.get('/home', function (req, res) {
        let respuesta = swig.renderFile('views/home.html', {});
        res.send(respuesta);
    });

    app.get('/', function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

}