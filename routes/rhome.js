module.exports = function(app, swig) {

    /**
     * Este controlador recibe la petición GET /home, que manda al home.html datos del email y saldo del usuario en
     * sesión
     */
    app.get('/home', function (req, res) {
        console.log("[Accediendo a home] -> METHOD: GET, PATH: /home");
        let respuesta = swig.renderFile('views/home.html', {email: req.session.usuario, saldo: req.session.saldo});
        res.send(respuesta);
    });

    /**
     * Este controlador recibe la petición GET /, que renderiza la vista index.html
     */
    app.get('/*', function (req, res) {
        console.log("[Accediendo al index] -> METHOD: GET, PATH: /*");
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

    /**
     * Controlador que responde a la petición GET /error/. Se encarga de renderizar la vista error.html, a la que se
     * la pasarán aquellos errores relacionados con la base de datos o aquellos errores en los que el usuario, aun
     * siendo incapaz de hacer algo desde las vistas, intenta realizar alguna operación
     */
    app.get('/error/', function (req, res) {
        console.log("[Accediendo a la vista de errores] -> METHOD: GET, PATH: /error");
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
        console.log("[Petición no apta, redirigiendo al index] -> METHOD: GET, PATH: *");
        res.redirect("/");
    });
}