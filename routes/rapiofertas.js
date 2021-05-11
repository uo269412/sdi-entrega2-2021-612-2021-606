
module.exports = function(app, gestorBD) {
    /**
     * Este controlador responde a la petición /apu/ofertas, y devuelve todas aquellas ofertas que no son del usuario
     * que está en sesión
     */
    app.get("/api/ofertas", function(req, res) {
        console.log("[Listado de ofertas, usuario: " + res.usuario + "] -> METHOD: POST, PATH: /api/ofertas");
        let criterio = {$and:[{ "vendedor": {"$ne": res.usuario } }, {"comprador" : null}]}
        gestorBD.obtenerOfertas( criterio , function(ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(ofertas) );
            }
        });
    });

    /**
     * Este controlador responde a la petición /api/oferta/:id, y devuelve aquella oferta que se corresponda con el id
     * especificado
     */
    app.get("/api/oferta/:id", function(req, res) {
        console.log("[Seleccionando oferta en específico, usuario: " + res.usuario + "] -> METHOD: POST, PATH: /api/oferta/:id");
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)}
        gestorBD.obtenerOfertas( criterio , function(ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(ofertas[0]) );
            }
        });
    });
}