module.exports = function(app, swig, gestorBD) {
    app.get('/conversaciones/list', function (req, res) {
        let respuesta = swig.renderFile('views/conversaciones/list.html', {email: req.session.usuario,
            saldo: req.session.saldo});
        res.send(respuesta);
    });

    app.get('/conversaciones/details/:id', function (req, res) {
        let respuesta = swig.renderFile('views/conversaciones/details.html', {email: req.session.usuario,
            saldo: req.session.saldo});
        res.send(respuesta);
    });
}