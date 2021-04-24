module.exports = function(app, swig, gestorBD) {
    /**
     * Este controlador recibe la petición GET /registrarse que renderiza la vista signup.html para que el usuario
     * pueda utilizar el formulario para registrarse y entrar dentro de la aplicación
     */
    app.get("/registrarse", function(req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    /**
     * Esta función es la que se encarga de validar que los campos del formulario de registro contengan datos correctos.
     * En caso contrario, se irán almacenando en un array una lista con los errores para que el usuario pueda ver
     * aquellos campos que se encuentran incorrectos.
     */
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

    /**
     * Este controlador recibe la petición POST /usuario que recoge los campos que el usuario ha rellenado del
     * formulario de registrp e intenta que se pueda entrar en la aplicación como este usuario. Al ser un nuevo usuario,
     * se comprobará que este usuario ya no existe. Además se comprueba que el formulario está completo. En caso
     * correcto, se almacenará en sesión el email, el saldo y si es un administrador o no y se redireccionará
     * a /home.html.
     */
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

    /**
     * Este controlador recibe la petición POST /identificarse que recoge los campos que el usuario ha rellenado del
     * formulario de loggearse e intenta que se pueda entrar en la aplicación como este usuario. En caso correcto, se almacenará
     * en sesión el email, el saldo y si es un administrador o no.
     */
    app.post("/identificarse", function(req, res) {
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

    /**
     * Este controlador recibe la petición GET /identificarse que renderiza la vista login.html para que el usuario
     * pueda utilizar el formulario para identificarse y entrar dentro de la aplicación
     */
    app.get("/identificarse", function(req, res) {
        let respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });


    /**
     * Este controlador recibe la petición GET /propias que obtiene aquellas ofertas las cuales tienen como campo
     * vendedor al usuario, es decir, las cuales el usuario oferta, para luego enviarlas a listMine.html
     * que las lista.
     */
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

    /**
     * Este controlador recibe la petición GET /compras que obtiene aquellas ofertas las cuales tienen como campo
     * comprador al usuario, es decir, las cuales ha comprado el usuario, para luego enviarlas a listPurcharsed.html
     * que las lista.
     */
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

    /**
     * Este controlador recibe la petición GET /usuarios, mediante la cual obtendrá los usuarios de la base de datos
     * y se los pasará a la vista list.html
     */
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

    /**
     * Este controlador recibe la petición GET /desconectarse, que desloggeará al usuario y redireccionará al index.html
     */
    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        req.session.saldo = null;
        req.session.admin = null;
        res.redirect("/");
    });

    app.get('/error/', function (req, res) {
        let respuesta = swig.renderFile('views/error.html', {
            mensaje: req.mensaje,
            tipoMensaje: req.tipoMensaje
        });
        res.send(respuesta)
    });

    app.post('/usuarios/eliminar', function (req, res) {
        let criterio = [];
        if (typeof req.body.deleteUser === "string"){
            criterio.push({"_id": gestorBD.mongo.ObjectID(req.body.deleteUser)});
        }else{
            let usersMarked = req.body.deleteUser;

            for (let user of usersMarked) {
                criterio.push({"_id": gestorBD.mongo.ObjectID(user)});
            }
        }
        let criterioFinal = {"$or": criterio};
        gestorBD.eliminarUsuarios(criterioFinal, function(usuarios) {
            if (usuarios == null) {
                res.send("No hay ningún usuario que borrar");
            } else {
                res.redirect('/usuarios');
            }
        });

    });
};