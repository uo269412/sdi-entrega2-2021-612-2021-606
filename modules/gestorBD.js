module.exports = { mongo : null, app : null,
    init : function(app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },

    obtenerOfertasPg : function(criterio,pg,funcionCallback){
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('ofertas');
                collection.count(function(err, count){
                    collection.find(criterio).skip( (pg-1)*4 ).limit( 4 )
                        .toArray(function(err, ofertas) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(ofertas, count);
                            }
                            db.close();
                        });
                });
            }
        });
    },

    insertarUsuario : function(usuario, funcionCallback) { this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('usuarios');
            collection.insert(usuario, function(err, result) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(result.ops[0]._id);
                }
                db.close();
            });
        }
    });
    },

    eliminarComentario : function(criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('comentarios');
                collection.remove(criterio, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },
    añadirCompra : function(criterio, oferta, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('ofertas');
                collection.update(criterio, {$set: oferta}, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },
    insertarComentario : function(comentario, funcionCallback) { this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('comentarios');
            collection.insert(comentario, function(err, result) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(result.ops[0]._id);
                }
                db.close();
            });
        }
    });
    },

    obtenerComentarios : function(criterio,funcionCallback){ this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('comentarios');
            collection.find(criterio).toArray(function(err, comentarios) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(comentarios);
                } db.close();
            });
        }
    });
    },


    obtenerUsuarios : function(criterio,funcionCallback){ this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('usuarios');
            collection.find(criterio).toArray(function(err, usuarios) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(usuarios);
                } db.close();
            });
        }
    });
    },
    obtenerOfertas : function(criterio, funcionCallback){ this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('ofertas');
            collection.find(criterio).toArray(function(err, ofertas) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(ofertas);
                } db.close();
            });
        }
    });
    },
    insertarOferta : function(cancion, funcionCallback) { this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('ofertas');
            collection.insertOne(cancion, function(err, result) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(result.ops[0]._id);
                }
                db.close();
            });
        }
    });
    }
};