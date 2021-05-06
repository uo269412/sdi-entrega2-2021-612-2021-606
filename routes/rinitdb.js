module.exports = function(app, gestorBD) {
    /**
     * Este controlador recibe la petición GET /resetDatabase y se encarga de borrar el contenido de la base de datos.
     * Luego creará usuarios que son obligatorios para los tests.
     */
    app.get("/resetDatabase", function(req, res) {
        gestorBD.eliminarTodo(function (eliminado) {
            if (eliminado == null) {
                res.status(500);
            } else {
                insertarAdmin(res);
            }
        });
    });

    function insertarAdmin(res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update("admin").digest('hex');
        let usuario = {
            email: "admin@email.com",
            name: "Admin",
            lastName: "Admin",
            saldo: parseFloat("100"),
            password: seguro,
            admin: true
        }
        gestorBD.insertarUsuario(usuario, function (id) {
            if (id == null) {
                res.status(500);
            } else {
                res.redirect("/");
            }
        });
    }
};