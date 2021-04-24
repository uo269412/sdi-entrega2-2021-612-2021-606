module.exports = function(app, swig, gestorBD) {
    app.get("/registrarse", function(req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    function validaDatosRegistroUsuario(usuario, errors, password, passwordConfirm, funcionCallback) {
        if (usuario.email === null || typeof usuario.email === 'undefined' ||
            usuario.email === "") {
            errors.push("Se tiene que añadir un email")
        }
        if (usuario.name === null || typeof usuario.name === 'undefined' ||
            usuario.name === "") {
            errors.push("Se tiene que añadir un nombre")
        }
        if (usuario.lastName === null || typeof usuario.lastName === 'undefined' ||
            usuario.lastName === "") {
            errors.push("Se tiene que insertar un apellido")
        }
        if ((password !== passwordConfirm) || (usuario.password === null || typeof usuario.password === 'undefined' ||
            usuario.password === "") || (passwordConfirm === null || typeof passwordConfirm === 'undefined' ||
            passwordConfirm === "")) {
            errors.push("Las contraseñas no coinciden")
        }
        if (errors.length <= 0) {
            funcionCallback(null)
        } else {
            funcionCallback(errors)
        }
    }

    app.post('/usuario', function(req, res) {
        let errors = new Array();
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let usuario = {
            email : req.body.email,
            name: req.body.name,
            lastName: req.body.lastName,
            saldo: parseFloat("100"),
            password : seguro,
            admin: false
        }

        let criterioUsuarios = {"email": req.body.email};
        gestorBD.obtenerUsuarios(criterioUsuarios, function(usuarios) {
            if (!(usuarios == null || usuarios.length == 0)) {
                errors.push("El usuario ya se encuentra registrado");
            }
        })
        validaDatosRegistroUsuario(usuario, errors, req.body.password, req.body.passwordConfirm, function (errors) {
            if (errors != null && errors.length > 0) {
                let respuesta = swig.renderFile("views/signup.html", {
                    errores: errors
                })
                res.send(respuesta);
            } else {
                gestorBD.insertarUsuario(usuario, function(id) {
                    if (id == null){
                        res.redirect("/registrarse?mensaje=Error al registrar usuario");
                    } else {
                        req.session.usuario = usuario.email;
                        req.session.saldo = usuario.saldo;
                        req.session.admin = usuario.admin;
                        res.redirect("/home");
                    }
                });
            }
        })
    });

    app.post("/identificarse", function(req, res) {
        let errores = new Array();
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email : req.body.email,
            password : seguro
        }
        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                req.session.usuario = null;
                res.redirect("/identificarse" +
                    "?mensaje=Email o password incorrecto"+
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.saldo = usuarios[0].saldo;
                req.session.admin = usuarios[0].admin;
                if (usuarios[0].admin === true){
                    res.redirect("/usuarios");
                }
                else
                    res.redirect("/home");
            }
        });
    });

    app.get("/identificarse", function(req, res) {
        let respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });


    //items/listMine
    app.get("/propias", function(req, res) {
        let criterio = { vendedor : req.session.usuario };
        gestorBD.obtenerOfertas(criterio, function(ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let respuesta = swig.renderFile('views/offers/listMine.html',
                    {
                        ofertas : ofertas,
                        email: req.session.usuario,
                        saldo: req.session.saldo,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

    //items/listPurchased
    app.get("/compras", function(req, res) {
        let criterio = { comprador : req.session.usuario };
        gestorBD.obtenerOfertas(criterio, function(ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let respuesta = swig.renderFile('views/offers/listPurchased.html',
                    {
                        ofertas : ofertas,
                        email: req.session.usuario,
                        saldo: req.session.saldo,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

    app.get("/usuarios", function(req, res) {
        let criterio = {admin: false};
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.send("Error al listar");
            } else {
                let respuesta = swig.renderFile('views/user/list.html',
                    {
                        usuarios : usuarios,
                        admin: req.session.admin
                    });
                res.send(respuesta);
            }
        });
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.redirect("/");
    });

    app.get('/error/', function (req, res) {
        let respuesta = swig.renderFile('views/error.html', {
            mensaje: req.mensaje,
            tipoMensaje: req.tipoMensaje
        });
        res.send(respuesta)
    });

    app.get('/home', function (req, res) {
        let respuesta = swig.renderFile('views/home.html', {admin: req.session.admin,email: req.session.usuario, saldo: req.session.saldo});
        res.send(respuesta);
    });

    app.get('/', function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {admin: req.session.admin});
        res.send(respuesta);
    });

};