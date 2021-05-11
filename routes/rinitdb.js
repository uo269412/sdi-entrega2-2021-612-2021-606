module.exports = function(app, gestorBD) {
    /**
     * Este controlador recibe la petición GET /resetDatabase y se encarga de borrar el contenido de la base de datos.
     *
     */
    app.get("/resetDatabase", function(req, res) {
        console.log("[Reseteando la base de datos]");
        gestorBD.eliminarTodo(function (eliminado) {
            if (eliminado == null) {
                res.status(500);
            } else {
                insertarAdmin(res);
            }
        });
    });

    /**
     * Este controlador recibe la petición GET /restoreDatabasey se encarga de borrar el contenido de la base de datos.
     * Luego creará usuarios que son obligatorios para los tests.
     */
    app.get("/restoreDatabase", function(req, res) {
        console.log("[Restaurando la base de datos]");
        gestorBD.eliminarTodo(function (eliminado) {
            if (eliminado == null) {
                res.status(500);
            } else {
                insertarAdmin(res);
                insertarUsuario("laura_mar@gmail.com", "Laura", "Mar");
                insertarUsuario("futbol345@gmail.com", "Pedro", "González");
                insertarUsuario("paula_22@gmail.com", "Paula", "López");
                insertarUsuario("jueguetesParaTodos@gmail.com", "Mario", "Riestra");
                insertarUsuario("123jeremy@gmail.com", "Jeremy", "Fernández");
                insertarUsuario( "cuatro23@gmail.com", "Andrea", "del Río");
                insertarUsuario( "carcargol@gmail.com", "Carlos", "Méndez");

                let ofertalau1 = insertarOferta("Barco", "Un pequeño barco con apenas uso", 300.0, "laura_mar@gmail.com", null, false, res);
                let ofertalau2 = insertarOferta("Timón", "Con madera de roble", 50.0, "laura_mar@gmail.com", null, false, res);
                let ofertalau3 = insertarOferta("Catalejo", "En perfecto estado", 80.0, "laura_mar@gmail.com", "futbol345@gmail.com", false, res);
                let ofertalau4 = insertarOferta("Caña de pescar", "Medio rota, pero sirve para piezas", 20.0, "laura_mar@gmail.com", null, true, res);
                let ofertalau5 = insertarOferta("Set de anzuelos", "De excelente calidad", 40.0, "laura_mar@gmail.com", null, false, res);

                /*
                let converconLau = insertarConversacion("trenMieres@gmail.com", "laura_mar@gmail.com", ofertalau5, res);
                insertarMensaje("futbol345@gmail.com", "Buenas, me interesarían los anzuelos", true, converconLau,  res);
                insertarMensaje("laura_mar@gmail.com", "Perfecto, cuando quieres quedar?", true, converconLau,  res);
                insertarMensaje("futbol345@gmail.com", "Ahora mismo estoy de viaje, pero vuelvo mañana", true, converconLau,  res);
                insertarMensaje("futbol345@gmail.com", "Le parece bien el domingo?", true, converconLau,  res);
                insertarMensaje("laura_mar@gmail.com", "Sin problema, ya lo vamos hablando", true, converconLau,  res);
                 */

                let ofertape1 = insertarOferta("Bicicleta", "La mejor bicicleta del país", 100.0, "futbol345@gmail.com", null, false, res);
                let ofertape2 = insertarOferta("Coche", "El mejor coche del país", 80.0, "futbol345@gmail.com", "paula_22@gmail.com", false, res);
                let ofertape3 = insertarOferta("Moto", "La mejor moto del país", 20.0, "futbol345@gmail.com", "laura_mar@gmail.com", true, res);
                let ofertape4 = insertarOferta("Remolque", "Lleva lo que quieras a donde quieras", 10.0, "futbol345@gmail.com", null, false, res);

                let ofertapau1 = insertarOferta("Chaqueta", "Una chaqueta que abriga mucho por el invierno", 5.0, "paula_22@gmail.com", null, false, res);
                let ofertapau2 = insertarOferta("Falda", "De la colección de zara", 8.0, "paula_22@gmail.com", "jueguetesParaTodos@gmail.com", false, res);
                let ofertapau3 = insertarOferta("Pantalones", "Desgastados pero funcionales", 6.0, "paula_22@gmail.com", null, false, res);
                let ofertapau4 = insertarOferta("Camiseta", "Versión exclusiva de la película titanic", 9.0, "paula_22@gmail.com", null, true, res);
                let ofertapau5 = insertarOferta("Anorak", "De la marca que marca más tendencia actualmente", 10.0, "paula_22@gmail.com", null, false, res);

                let ofertamar1 = insertarOferta("Peluche", "Relleno de amor", 1.0, "jueguetesParaTodos@gmail.com", null, false, res);
                let ofertamar2 = insertarOferta("Figura de acción", "De la última película", 20.0, "jueguetesParaTodos@gmail.com", null, false, res);
                let ofertamar3 = insertarOferta("Muñeca", "Hecha a mano", 3.0, "jueguetesParaTodos@gmail.com", null, true, res);
                let ofertamar4 = insertarOferta("Canicas", "Set de 12", 2.0, "jueguetesParaTodos@gmail.com", null, false, res);

                let ofertamjer1 = insertarOferta("Raqueta de tenis", "Apenas dadas uso", 20.0, "123jeremy@gmail.com", "jueguetesParaTodos@gmail.com", false, res);
                let ofertamjer2 = insertarOferta("Raqueta de padel", "Sin abrir", 20.0, "123jeremy@gmail.com", "jueguetesParaTodos@gmail.com", true, res);
                let ofertamjer3 = insertarOferta("Raqueta de pingpong", "Se incluye red", 20.0, "123jeremy@gmail.com", null, true, res);
                let ofertamjer4 = insertarOferta("Raqueta de frontón", "Se incluyen dos pelotas", 90.0, "123jeremy@gmail.com", null, false, res);




            }
        });
    });


    /**
     * Función que mete al admin en la base de datos
     * @param res
     */
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

    /**
     * Función que inserta a un usuario en la base de datos
     * @param email
     * @param name
     * @param lastName
     * @param res
     */
    function insertarUsuario(email, name, lastName, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update("123456").digest('hex');
        let usuario = {
            email: email,
            name: name,
            lastName: lastName,
            saldo: parseFloat("100"),
            password: seguro,
            admin: false
        }
        gestorBD.insertarUsuario(usuario, function (id) {
            if (id == null) {
                res.status(500);
            } else {
                return id;
            }
        });
    }

    /**
     * Función que inserta una oferta en la base de datos
     * @param titulo
     * @param descripcion
     * @param precio
     * @param vendedor
     * @param comprador
     * @param destacada
     * @param res
     */
    function insertarOferta(titulo, descripcion, precio, vendedor, comprador, destacada, res) {
        let oferta = {
            titulo: titulo,
            descripcion: descripcion,
            fechaSubida: new Date(),
            precio: precio,
            vendedor: vendedor,
            comprador: comprador,
            destacada: destacada
        }
        gestorBD.insertarOferta(oferta, function (id) {
            if (id == null) {

                res.status(500);
            } else {
                return id;
            }
        });
    }

    /**
     * Función que inserta una conversación en la base de datos
     * @param interesado
     * @param propietario
     * @param oferta
     * @param res
     */
    function insertarConversacion(interesado, propietario, oferta, res) {
        let conversacion = {
            interesado: interesado,
            propietario: propietario,
            oferta: gestorBD.mongo.ObjectID(oferta)
        }
        gestorBD.insertarConversacion(conversacion, function (id) {
            if (id == null) {
                res.status(500);
            } else {
                return id;
            }
        });
    }

    /**
     * Función que inserta un mensaje en la base de datos
     * @param autor
     * @param texto
     * @param leido
     * @param conversacion
     * @param res
     */
    function insertarMensaje(autor, texto, leido, conversacion, res) {
        let mensaje = {
            autor: texto,
            fecha: new Date(),
            leido: leido,
            conversacion: gestorBD.mongo.ObjectID(conversacion)
        }
        gestorBD.insertarMensaje(mensaje, function (id) {
            if (id == null) {
                res.status(500);
            } else {
                return id;
            }
        });
    }
};