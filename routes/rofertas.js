module.exports = function(app, swig, gestorBD) {

    /**
     * Este controlador recibe la petición GET correspondiente a /ofertas/agregar y mediante swig manda que se renderice
     * el fichero que muestra el formulario para añadir ofertas (add.html)
     */
    app.get('/ofertas/agregar', function (req, res) {
        let respuesta = swig.renderFile('views/offers/add.html', {});
        res.send(respuesta);
    });

    /**
     * Esta función se encarga de validar el formulario mediante el cual se añaden las ofertas a vender. Comprueba que
     * los campos no estén vacíos, además de que el precio este formateado como un número.
     */
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

    /**
     * Este controlador recibe la petición POST /oferta, mediante la cual se recogen los datos de la vista (add.html) y
     * se construye una oferta la cual se validará que está de forma correcta mediante la función
     * validaDatosRegistroOferta. Si hay algún error, se mostrará en la página; mientras que en caso contrario, se
     * insertará en la base de datos.
     */
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

    /**
     * Este controlador recibe la petición GET /ofertas, mediante la cual se obtienen las ofertas de la base de datosy
     * de forma en la que se permite la paginación. Al utilizar la páginación que se encuentra en la vista, se volverá
     * a refrescar la vista mediante el número de paginación que corresponde, además de los datos que estén contenidos
     * en esa página. También se podrá utilizar una barra de búsqueda que servirá como criterio para filtrar aquellas
     * ofertas que se correspondan con el nombre.
     */
    app.get("/ofertas", function(req, res) {
        let criterio = {};
        if (req.query.busqueda != null) {
            criterio = {'titulo': {'$regex': req.query.busqueda}};
            console.log(req.query.busqueda);
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


    /**
     * Este controlador recibe la petición GET /oferta/eliminar/:id, mediante la cual se selecciona la oferta en la
     * base de datos que se corresponda con el id, para luego ser eliminada. Si la operación se realiza con éxito,
     * se volverá a las ofertas propias.
     */
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

    /**
     * Este controlador recibe la petición GET /oferta/comprar/:id, mediante la cual el usuario en sesión podrá realizar
     * la compra de una oferta que se haya seleccionado (la que se corresponda con el id). Primero para haber podido
     * realizar esta petición, el usuario tiene que tener el dinero suficiente para comprarlo y que la oferta ya no
     * esté comprada (esto se comprueba en las vistas). Si se llama a esta petición, esta actualizará al objeto,
     * haciendo que el usuario se vuelva el comprador y le resta el dinero al usuario (Para esto hay que modificar
     * el campo comprador de oferta, luego obtener la oferta de la base de datos para coger su precio y luego modificar
     * al usuario con el nuevo sueldo que tendrá)
     */
    app.get("/oferta/comprar/:id", function(req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        let criterioUsuarios = {"email": req.session.usuario};
        console.log(req.session.usuario);
        let usuario = req.session.usuario;
        let oferta = {
            comprador: usuario
        }
        gestorBD.modificarOferta(criterio, oferta, function (idCompra) {
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
    /**
     * Este controlador recibe la petición GET /home, que manda al home.html datos del email y saldo del usuario en
     * sesión
     */
    app.get('/home', function (req, res) {
        let respuesta = swig.renderFile('views/home.html', {email: req.session.usuario, saldo: req.session.saldo});
        res.send(respuesta);
    });

    /**
     * Este controlador recibe la petición GET /, que renderiza la vista index.html
     */
    app.get('/', function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

    /**
     * Este controlador recibe la petición GET /oferta/nodestacar/:id, en el cual crea dos criterios para dos método
     * del gestor de base de datos, uno encargado de buscar el id de la oferta que se quiere destacar y otro el email
     * del usuario que está en sesión (que es el vendedor de la oferta). Luego llama a la función destacarOferta
     * utilizando como parámetros los dos criterios y un false indicando que se quiere no destacar la oferta.
     */
    app.post('/oferta/nodestacar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        let criterioUsuarios = {"email": req.session.usuario};
        destacarOferta(criterio, criterioUsuarios, false, req, res);
    });

    /**
     * Este controlador recibe la petición GET /oferta/nodestacar/:id, en el cual crea dos criterios para dos método
     * del gestor de base de datos, uno encargado de buscar el id de la oferta que se quiere destacar y otro el email
     * del usuario que está en sesión (que es el vendedor de la oferta). Luego llama a la función destacarOferta
     * utilizando como parámetros los dos criterios y un true indicando que se quiere destacar la oferta.
     */
    app.post('/oferta/destacar/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        let criterioUsuarios = {"email": req.session.usuario};
        destacarOferta(criterio, criterioUsuarios, true, req, res);
    });

    /**
     * Esta función se encarga, a partir de los dos criterios recibidos y de si la oferta se puede destacar o no.
     * Primero esta función creará una oferta que tendrá de valor destacada el que le hemos pasado por parámetro, luego
     * se llamará a la base de datos que se encargará de modificar esta oferta cambiando su campo destacado. Si esta
     * gestión se realiza correctamente, se creará un nuevo saldo para el usuario que partirá del base, y dependiendo
     * de si está destacando o dejando de destacar, le bajará o aumentará el saldo en 20. Después se actualiza en la
     * base de datos y se vuelve al listado de propias.
     */
    function destacarOferta(criterio, criterioUsuarios, destacar, req, res) {
        let oferta = {
            destacada: destacar
        }
        gestorBD.modificarOferta(criterio, oferta, function (idCompra) {
            if (idCompra == null) {
                res.send(respuesta);
            } else {
                let nuevoSaldo = Number(req.session.saldo);
                if (destacar) {
                    nuevoSaldo -= 20;
                } else {
                    nuevoSaldo += 20;
                }
                req.session.saldo = nuevoSaldo;
                let usuario = {
                    saldo: nuevoSaldo
                }
                gestorBD.modificarUsuario(criterioUsuarios, usuario, function (idCompra) {
                    if (idCompra == null) {
                        res.send(respuesta);
                    } else {
                        res.redirect("/propias");
                    }
                });
            }
        });
    }

}