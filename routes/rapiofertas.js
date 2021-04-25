
module.exports = function(app, gestorBD) {
    app.get("/api/ofertas", function(req, res) {
        console.log(res.usuario);
        let criterio = { "vendedor": {"$ne": res.usuario }};
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
}