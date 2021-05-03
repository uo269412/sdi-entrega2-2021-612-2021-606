module.exports = function(app, gestorBD) {

    /**
     * Este controlador se encarga de responder a la petición POST de api/mensaje/:id, la cual recibirá
     * como parámetro id la oferta a la cual se quiere mandar el mensaje. Si este procedimiento falla, se verán
     * los errores.
     * Funciona tal que:
     *  - Obtenemos la oferta que se corresponde con el id
     *  - Obtenemos la conversación que tiene como campo oferta, el object id de la oferta que obtuvimos previamente
     *  - Si existe esa conversación, obtenemos el id de esta y creamos un nuevo mensaje que referenciará a la
     *  conversación que ya existe.
     *  - En caso contrario, se creará una nueva conversación que tendrá como parámetro el object id de la oferta,
     *  además del usuario interesado y el propietario. Tras insertarlo en la base de datos, crearemos el mensaje
     *  haciendo referencia al nuevo id de la conversación que acabamos de crear.
     */
    app.post("/api/mensaje/:id", function (req, res) {
        let errors = [];
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)}
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                let criterioConversaciones = {"oferta": gestorBD.mongo.ObjectID(req.params.id)}
                gestorBD.obtenerConversaciones(criterioConversaciones, function (conversaciones) {
                    if (conversaciones == null || conversaciones.length === 0) {
                        let conversacion = {
                            interesado: res.usuario,
                            propietario: ofertas[0].vendedor,
                            oferta: gestorBD.mongo.ObjectID(req.params.id)
                        }
                        if (res.usuario === ofertas[0].vendedor) {
                            res.status(500);
                            errors.push("El propietario de la oferta no puede comenzar la conversación");
                            res.json({
                                errores: errors
                            })
                        } else {
                            gestorBD.insertarConversacion(conversacion, function (id) {
                                if (id == null) {
                                    res.status(500);
                                    errors.push("se ha producido un error")
                                    res.json({
                                        errores: errors
                                    })
                                } else {
                                    insertarMensaje(id, errors, req, res);
                                    res.status(201);
                                    res.json({
                                        conversacion: conversacion
                                    })
                                }
                            });
                        }
                    } else {
                        console.log(conversaciones[0]);
                        console.log(conversaciones[0]._id);
                        insertarMensaje(gestorBD.mongo.ObjectID(conversaciones[0]._id), errors, req, res);
                        res.status(201);
                    }
                });
            }
        });
    });

    /**
     * Este controlador se encarga de responder a la petición PUT de api/mensaje/:id, la cual se encargará de actualizar
     * el mensaje a leído que se corresponda con el parámetro :id. Se comprobará que el usuario es el interesado antes
     * de realizar esta operación.
     */
    app.put("/api/mensaje/:id", function(req, res) {
        let mensaje = {
            leido: true
        }; // Solo los atributos a modificar
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) };
        let usuarioSesion = res.usuario;
        let errors = [];
        gestorBD.obtenerMensajes(criterio,function(mensajes){
            if (mensajes == null || mensajes.length === 0){
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                usuarioInteresadoPropietarioYNoEsAutor(gestorBD.mongo.ObjectID(req.params.id), usuarioSesion, mensajes[0], function(sePermiteLeer) {
                    if (sePermiteLeer) {
                        gestorBD.modificarMensaje(criterio, mensaje, function(result) {
                            if (result == null) {
                                res.status(500);
                                errors.push("Se ha producido un error marcando el mensaje como leído");
                                res.json({
                                    errores: errors
                                })
                            } else {
                                res.status(200);
                                res.json({
                                    mensaje : "mensaje leído",
                                    _id : req.params.id
                                })
                            }
                        });
                    } else {
                        res.status(413);
                        errors.push("El usuario que está en sesión no es el autor de la canción");
                        res.json({
                            errores: errors
                        })
                    }
                })
            }
        });
    });

    /**
     * Función que se creo para extraer código repetido del controlador que inserta mensajes. Es el encargado de
     * insertar el mensaje según el criterio en la base de datos.
     * @param id de la conversación que utilizará el mensaje
     * @param errors que luego se mostrarán si hay algún error
     * @param req proveniente del controlador
     * @param res proveniente del controlador
     */
    function insertarMensaje(id, errors, req, res) {
        let mensaje = {
            autor: res.usuario,
            fecha: new Date(),
            texto: req.body.texto,
            leido: false,
            conversacion: id
        }
        gestorBD.insertarMensaje(mensaje, function (id) {
            if (id == null) {
                res.status(500);
                errors.push("se ha producido un error")
                res.json({
                    errores: errors
                })
            } else {
                res.status(201);
                res.json({
                    texto: mensaje.texto,
                    autor: mensaje.autor,
                    _id: id
                })
            }
        });
    }

    /**
     * Función que emplea el controlador de la petición PUT de api/mensaje/:id. Se encarga de comprobar que
     * el usuario en sesión sea el propietario o el interesado de la conversación, además de que no es el autor del mensaje,
     * para así marcarlo como léido
     * @param conversacionID que se utilizará como criterio para obtener la conversación de la base de datos
     * @param usuarioSesion que es el usuario que se encuentra en sesión
     * @param mensaje que se analizará para saber si el usuario es autor o no
     * @param funcionCallback devolverá falso o verdadero dependiendo de si el usuario cumple esa condición o no
     */
    function usuarioInteresadoPropietarioYNoEsAutor(conversacionID, usuarioSesion, mensaje, funcionCallback) {
        let criterio = { "_id" : conversacionID}
        gestorBD.obtenerConversaciones(criterio,function(conversaciones){
            if ( conversaciones == null || conversaciones.length === 0){
            } else {
                if ((conversaciones[0]).propietario === usuarioSesion|| (conversaciones[0]).interesado === usuarioSesion) {
                    if (mensaje.autor !== usuarioSesion) {
                        funcionCallback(true)
                    } else {
                        funcionCallback(false);
                    }
                } else {
                    funcionCallback(false)
                }
            }
        });
    }
}