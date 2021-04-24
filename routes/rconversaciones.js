module.exports = function(app, swig, gestorBD) {
    app.get('/conversaciones/list', function (req, res) {
        let respuesta = swig.renderFile('views/conversaciones/list.html', {});
        res.send(respuesta);
    });

    app.get('/conversaciones/details/:id', function (req, res) {
        let respuesta = swig.renderFile('views/conversaciones/details.html', {});
        res.send(respuesta);
    });

}