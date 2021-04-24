module.exports = function(app, swig, gestorBD) {

    //items/add
    app.get('/ofertas/agregar', function (req, res) {
        let respuesta = swig.renderFile('views/offers/add.html', {});
        res.send(respuesta);
    });

    function validaDatosRegistroOferta(oferta, errors, funcionCallback) {
        if (oferta.titulo === null || typeof oferta.titulo === 'undefined' ||
            oferta.titulo === "") {
            errors.push("Se tiene que añadir un título")
        }
        if (oferta.descripcion === null || typeof oferta.descripcion === 'undefined' ||
            oferta.descripcion === "") {
            errors.push("Se tiene que añadir una descripción")
        }
        if (oferta.precio === null || typeof oferta.precio === 'undefined' ||
            oferta.precio === "") {
            errors.push("Se tiene que añadir un precio")
        }
        try {
            Number(oferta.precio);
        } catch (Exception) {
            errors.push("El precio no es un número")
        }
        if (errors.length <= 0) {
            funcionCallback(null)
        } else {
            funcionCallback(errors)
        }
    }

    //items/add
    app.post("/oferta", function(req, res) {
        let errors = new Array();
        let oferta = {
            titulo : req.body.titulo,
            descripcion : req.body.descripcion,
            fechaSubida : new Date(),
            precio : parseFloat(req.body.precio),
            vendedor: req.session.usuario,
            comprador: null,
            destacada: false
        }
        validaDatosRegistroOferta(oferta, errors, function (errors) {
            if (errors != null && errors.length > 0) {
                let respuesta = swig.renderFile("views/offers/add.html", {
                    errores: errors
                })
                res.send(respuesta);
            } else {
                gestorBD.insertarOferta(oferta, function(id){
                    if (id == null) {
                        res.send("Error al insertar oferta");
                    } else {
                        res.redirect("/home");
                    }
                });
            }
        })
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
                        saldo: req.session.saldo,
                        paginas: paginas,
                        actual: pg,
                        email: req.session.usuario
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
        let criterioUsuarios = {"email": req.session.usuario};
        console.log(req.session.usuario);
        let usuario = req.session.usuario;

        let oferta = {
            comprador: usuario
        }
        gestorBD.añadirCompra(criterio, oferta, function (idCompra) {
            if (idCompra == null) {
                res.send(respuesta);
            } else {
                gestorBD.obtenerOfertas(criterio,function(ofertas){
                    if ( ofertas == null ){
                        res.send(respuesta);
                    } else {
                        let nuevoSaldo = Number(req.session.saldo) - Number(ofertas[0].precio);
                        req.session.saldo = nuevoSaldo;
                        let usuario = {
                            saldo: nuevoSaldo
                        }
                        gestorBD.modificarUsuario(criterioUsuarios, usuario, function (idCompra) {
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
    });

    app.get('/oferta/nodestacar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        res.redirect('/propias');
    });

    app.get('/oferta/destacar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        res.redirect('/propias');
    });

}