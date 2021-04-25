
module.exports = function(app, gestorBD) {

    /**
     * Este controlador se encarga de responder a la petición GET de api/conversaciones/interesado, en la cual se
     * devolverán en formato json todas las conversaciones en las cuales el usuario sea el interesado. Si hubiese
     * errores, se enseñarían estos
     */
    app.get("/api/conversaciones/interesado", function (req, res) {
        let errors = [];
        let criterio = {"interesado": res.usuario}
        gestorBD.obtenerConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.json({
                    errores: errors
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(conversaciones));
            }
        });
    });

    /**
     * Este controlador se encarga de responder a la petición GET de api/conversaciones/propietario, en la cual se
     * devolverán en formato json todas las conversaciones en las cuales el usuario sea el propietario. Si hubiese
     * errores, se enseñarían estos
     */
    app.get("/api/conversaciones/propietario", function (req, res) {
        let errors = [];
        let criterio = {"propietario": res.usuario}
        gestorBD.obtenerConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.json({
                    errores: errors
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(conversaciones));
            }
        });
    });

    /**
     * Este controlador se encarga de responder a la petición GET de api/conversacion/:id, en la cual se
     * devolverán en formato json todas las mensajes de la conversación que se pasa por parámetro :id. Se comprobará
     * que para ver esta conversación es uno de los dos participantes.
     * En el caso de que en uno de estos pasos haya errores, se mostrarán.
     */
    app.get("/api/conversacion/:id", function(req, res) {
        let errors = [];
        let criterioConversaciones = {"_id": gestorBD.mongo.ObjectID(req.params.id)}
        usuarioInteresadoPropietario(gestorBD.mongo.ObjectID(req.params.id), usuarioSesion, function(tienePermiso) {
            if (tienePermiso) {
                gestorBD.obtenerConversaciones(criterioConversaciones, function (conversaciones) {
                    if (conversaciones == null) {
                        res.json({
                            errores: errors
                        })
                    } else {
                        let criterioMensajes = {"conversacion": gestorBD.mongo.ObjectID(conversaciones[0]._id)}
                        gestorBD.obtenerMensajes(criterioMensajes, function (mensajes) {
                            if (mensajes == null) {
                                res.json({
                                    errores: errors
                                })
                            } else {
                                res.status(200);
                                res.send(JSON.stringify(mensajes));
                            }
                        });
                    }
                });
            } else {
                res.status(413);
                errors.push("El usuario que está en sesión no es ni el propietario ni el interesado de la conversación");
                res.json({
                    errores: errors
                })
            }
        })
    });

    /**
     * Este controlador se encarga de responder a la petición DELETE de api/conversacion/:id, en la cual se
     * borrará aquella conversación que tenga como id el especificado por parámetro. Luego se volverán a mostrar
     * todas las conversaciones menos esa. Para que el usuario pueda borrarla, se comprobará que ese usuario en sesión
     * sea o el propietario o el interesado de la conversación. En el caso de que en uno de estos pasos
     * haya errores, se mostrarán
     */
    app.delete("/api/conversacion/:id", function(req, res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id)}
        let usuarioSesion = res.usuario;
        let errors = [];
        usuarioInteresadoPropietario(gestorBD.mongo.ObjectID(req.params.id), usuarioSesion, function(tienePermiso) {
            if (tienePermiso) {
                gestorBD.eliminarConversacion(criterio,function(conversaciones){
                    if ( conversaciones == null ){
                        res.status(500);
                        errors.push("Se ha producido un error borrando la conversación");
                        res.json({
                            errores: errors
                        })
                    } else {
                        res.status(200);
                        res.send( JSON.stringify(conversaciones) );
                    }
                });
            } else {
                res.status(413);
                errors.push("El usuario que está en sesión no es ni el propietario ni el interesado de la conversación");
                res.json({
                    errores: errors
                })
            }
        })
    });

    /**
     * Función que emplea el controlador de la petición DELETE de api/conversacion/:id. Se encarga de comprobar que
     * el usuario en sesión sea el propietario o el interesado de la conversación.
     * @param conversacionID que se utilizará como criterio para obtener la conversación de la base de datos
     * @param usuarioSesion que es el usuario que se encuentra en sesión
     * @param funcionCallback devolverá falso o verdadero dependiendo de si el usuario cumple esa condición o no
     */
    function usuarioInteresadoPropietario(conversacionID, usuarioSesion, funcionCallback) {
        let criterio = { "_id" : conversacionID}
        gestorBD.obtenerConversaciones(criterio,function(conversaciones){
            if ( conversaciones == null ){
            } else {
                if ((conversaciones[0]).propietario === (usuarioSesion) || (conversaciones[0]).interesado === (usuarioSesion)) {
                    funcionCallback(true)
                } else {
                    funcionCallback(false)
                }
            }
        });
    }
}