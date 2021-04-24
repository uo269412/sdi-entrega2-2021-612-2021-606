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
                res.redirect("/ofertas/propias");
            }
        });
    });

    //items/comprar/{id}
    app.get("/oferta/comprar/:id", function(req, res) {
        let ofertaId = gestorBD.mongo.ObjectID(req.params.id);
        let usuario = req.session.usuario;

        usuarioPuedeComprarOferta(usuario, ofertaId, function (comprar) {
            if (comprar) {
                let compra = {
                    comprador: usuario,
                    ofertaId: ofertaId
                }
                gestorBD.insertarCompra(compra, function (idCompra) {
                    if (idCompra == null) {
                        res.send(respuesta);
                    } else {
                        res.redirect("/compras");
                    }
                });
            } else {
                res.redirect("/error" + "?mensaje=Error al comprar la canción" + "&tipoMensaje=alert-danger");
            }
        });
    });

    function usuarioPuedeComprarOferta(usuario, ofertaId, funcionCallback) {
        let criterio_oferta_vendedor = {$and: [{"_id": ofertaId}, {"vendedor": usuario}]};
        let criterio_comprada = {$and: [{"cancionId": ofertaId}, {"comprador": usuario}]};
        gestorBD.obtenerOfertas(criterio_oferta_vendedor, function (ofertas) {
            if (ofertas == null || ofertas.length > 0) {
                funcionCallback(false);
            } else {
                gestorBD.obtenerCompras(criterio_comprada, function (compras) {
                    if (compras == null || compras.length > 0) {
                        funcionCallback(false);
                    } else {
                        funcionCallback(true);
                    }
                });
            }
        });
    }

    app.get('/home', function (req, res) {
        let respuesta = swig.renderFile('views/home.html', {});
        res.send(respuesta);
    });

    app.get('/', function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

    app.get('/ofertas/propias', function (req, res) {
        let criterio = {vendedor: req.session.usuario};
        gestorBD.obtenerOfertas(criterio,function (ofertas) {
            let respuesta = swig.renderFile('views/offers/listMine.html',
                {
                    ofertas: ofertas
                });
            res.send(respuesta);
        });
    });

    app.get('/ofertas/compradas', function (req, res) {
        let respuesta = swig.renderFile('views/offers/listPurchased.html', {});
        res.send(respuesta);
    });
}