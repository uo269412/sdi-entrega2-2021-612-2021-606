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
                    collection.find(criterio).skip( (pg-1)*5 ).limit( 5 )
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
    eliminarConversacion : function(criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('conversaciones');
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
    modificarOferta : function(criterio, oferta, funcionCallback) {
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
    modificarMensaje : function(criterio, mensaje, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('mensajes');
                collection.update(criterio, {$set: mensaje}, function(err, result) {
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
    modificarUsuario : function(criterio, usuario, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('usuarios');
                collection.update(criterio, {$set: usuario}, function(err, result) {
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
    insertarConversacion : function(conversacion, funcionCallback) { this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('conversaciones');
            collection.insert(conversacion, function(err, result) {
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
    insertarMensaje : function(mensaje, funcionCallback) { this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('mensajes');
            collection.insert(mensaje, function(err, result) {
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
    obtenerMensajes : function(criterio,funcionCallback){ this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('mensajes');
            collection.find(criterio).toArray(function(err, mensajes) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(mensajes);
                } db.close();
            });
        }
    });
    },
    obtenerConversaciones : function(criterio,funcionCallback){ this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
        if (err) {
            funcionCallback(null);
        } else {
            let collection = db.collection('conversaciones');
            collection.find(criterio).toArray(function(err, conversaciones) {
                if (err) {
                    funcionCallback(null);
                } else {
                    funcionCallback(conversaciones);
                } db.close();
            });
        }
    });
    },
    eliminarOferta : function(criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('ofertas');
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
    },
    eliminarUsuarios : function(criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('usuarios');
                collection.deleteMany(criterio, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    },eliminarOfertas: function(criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function(err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('ofertas');
                collection.deleteMany(criterio, function(err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result);
                    }
                    db.close();
                });
            }
        });
    }
};