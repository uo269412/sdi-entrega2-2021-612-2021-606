module.exports = function(app, swig, gestorBD) {
    /**
     * Este controlador recibe la petición GET /registrarse que renderiza la vista signup.html para que el usuario
     * pueda utilizar el formulario para registrarse y entrar dentro de la aplicación
     */
    app.get("/registrarse", function(req, res) {
        console.log("[Accediendo a la vista para registrarse, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /registrarse");
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
        let errors = [];
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
            if (!(usuarios == null || usuarios.length === 0)) {
                errors.push("El usuario ya se encuentra registrado");
                let respuesta = swig.renderFile("views/signup.html", {
                    errores: errors
                })
                res.send(respuesta);
            } else {
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
                                console.log("[Registrando usuario, usuario: " + req.session.usuario + "] -> METHOD: POST, PATH: /usuario");
                                res.redirect("/home");
                            }
                        });
                    }
                })
            }
        })
    });

    /**
     * Este controlador recibe la petición POST /identificarse que recoge los campos que el usuario ha rellenado del
     * formulario de loggearse e intenta que se pueda entrar en la aplicación como este usuario. En caso correcto, se almacenará
     * en sesión el email, el saldo y si es un administrador o no.
     */
    app.post("/identificarse", function(req, res) {
        var errors = [];
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email : req.body.email,
            password : seguro
        }
        validaDatosIdentificarse(req.body.email, req.body.password, errors, function (errors) {
            if (errors != null && errors.length > 0) {
                let respuesta = swig.renderFile("views/login.html", {
                    errores: errors
                })
                res.send(respuesta);
            } else {
                gestorBD.obtenerUsuarios(criterio, function(usuarios) {
                    if (usuarios == null || usuarios.length === 0) {
                        let errors = [];
                        errors.push("El usuario no existe en la base de datos o la contraseña es incorrecta");
                        let respuesta = swig.renderFile("views/login.html", {
                            errores: errors
                        })
                        res.send(respuesta);
                    } else {
                        req.session.usuario = usuarios[0].email;
                        req.session.saldo = usuarios[0].saldo;
                        req.session.admin = usuarios[0].admin;
                        console.log("[Identificandose, usuario: " + req.session.usuario + "] -> METHOD: POST, PATH: /identificarse");
                        if (usuarios[0].admin === true){
                            res.redirect("/usuarios");
                        }
                        else
                            res.redirect("/home");
                    }
                });
            }
        })
    });

    /**
     * Esta función es la que se encarga de validar que los campos del formulario de identificación contengan datos correctos.
     * En caso contrario, se irán almacenando en un array una lista con los errores para que el usuario pueda ver
     * aquellos campos que se encuentran incorrectos.
     */
    function validaDatosIdentificarse(email, password, errors, funcionCallback) {
        if (email === null || typeof email === 'undefined' ||
            email === "") {
            errors.push("Se tiene que añadir un email")
        }
        if (password === null || typeof password === 'undefined' ||
            password === "") {
            errors.push("Se tiene que añadir una contraseña")
        }
        if (errors.length <= 0) {
            funcionCallback(null)
        } else {
            funcionCallback(errors)
        }
    }

    /**
     * Este controlador recibe la petición GET /identificarse que renderiza la vista login.html para que el usuario
     * pueda utilizar el formulario para identificarse y entrar dentro de la aplicación
     */
    app.get("/identificarse", function(req, res) {
        console.log("[Accediendo a la vista para identificarse, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /identificarse");
        let respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });


    /**
     * Este controlador recibe la petición GET /propias que obtiene aquellas ofertas las cuales tienen como campo
     * vendedor al usuario, es decir, las cuales el usuario oferta, para luego enviarlas a listMine.html
     * que las lista.
     */
    app.get("/propias", function(req, res) {
        console.log("[Listado de ofertas propias, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /propias");
        let criterio = { vendedor : req.session.usuario };
        gestorBD.obtenerOfertas(criterio, function(ofertas) {
            if (ofertas == null || ofertas.length === 0) {
                res.redirect("/error" + "?mensaje=Error al listar las ofertas propias"
                    + "&tipoMensaje=alert-danger");
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
        console.log("[Listado de compras, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /compras");
        let criterio = { comprador : req.session.usuario };
        gestorBD.obtenerOfertas(criterio, function(ofertas) {
            if (ofertas == null || ofertas.length === 0) {
                res.redirect("/error/" + "?mensaje=Error al listar las ofertas compradas"
                    + "&tipoMensaje=alert-danger");
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
        console.log("[Listado de usuarios, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /usuarios");
        let criterio = {admin: false};
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                res.redirect("/error" + "?mensaje=Error al obtener la lista de usuarios"
                    + "&tipoMensaje=alert-danger");
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
        console.log("[Desconectando, usuario: " + req.session.usuario + "] -> METHOD: GET, PATH: /desconectarse");
        req.session.usuario = null;
        req.session.saldo = null;
        req.session.admin = null;
        res.redirect("/identificarse");
    });

    /**
     * Este controlador recibe la petición POST de /usuarios/eliminar, a la cual solo podrá acceder el administrador y
     * desde la cual podrá borrar aquellos seleccionados, además de borrar sus ofertas que no se hayan vendido
     */
    app.post('/usuarios/eliminar', function (req, res) {
        console.log("[Eliminando usuarios, usuario: " + req.session.usuario + "] -> METHOD: POST, PATH: /usuarios/eliminar");
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
      
        gestorBD.obtenerUsuarios(criterioFinal, function(usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                res.redirect("/error" + "?mensaje=Error al eliminar los usuarios"
                    + "&tipoMensaje=alert-danger");
            } else {
                let criterioEmails = [];
                for (let user of usuarios){
                    criterioEmails.push({"vendedor" : user.email, "comprador": null });
                }
                gestorBD.eliminarOfertas({"$or": criterioEmails},function (ofertas) {
                    if (ofertas === null || ofertas.length === 0 ){
                        res.send("Error al eliminar ofertas");
                    } else {
                        gestorBD.eliminarUsuarios(criterioFinal, function(usuarios) {
                            if (usuarios == null) {
                                res.send("No se ha encontrado el/los usuarios a borrar");
                            } else {
                                res.redirect('/usuarios');
                            }
                        });
                    }
                });
            }
        });
    });
};