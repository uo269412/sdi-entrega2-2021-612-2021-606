module.exports = function(app, swig) {
    /**
     * Controlador que responde a la petición GET /error/. Se encarga de renderizar la vista error.html, a la que se
     * la pasarán aquellos errores relacionados con la base de datos o aquellos errores en los que el usuario, aun
     * siendo incapaz de hacer algo desde las vistas, intenta realizar alguna operación
     */
    app.get('/error/', function (req, res) {
        let respuesta = swig.renderFile('views/error.html', {
            mensaje: req.mensaje,
            tipoMensaje: req.tipoMensaje
        });
        res.send(respuesta)
    });
}