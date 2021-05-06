module.exports = function(app, swig) {

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
    app.get('/*', function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

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

    /**
     * Responde al resto de peticiones y redirige al index
     */
    app.get('*', function (req, res) {
        res.redirect("/");
    });
}