module.exports = function(app, swig, gestorBD) {

    //items/add
    app.get('/ofertas/agregar', function (req, res) {
        let respuesta = swig.renderFile('views/bagregar.html', {});
        res.send(respuesta);
    });

    //items/add
    app.post("/oferta", function(req, res) {

    });

    //items/listAll
    app.get("/ofertas", function(req, res) {

    });


    //items/delete/{id}
    app.get("/oferta/eliminar/:id", function(req, res) {

    });

    //items/comprar/{id}
    app.get("/oferta/comprar/:id", function(req, res) {

    });

}