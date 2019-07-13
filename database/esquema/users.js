const mongoose = require("../connect");
const Schema = mongoose.Schema;

const usuarioSchema = Schema({
    nombre: {
        type: String,
        required: 'Falta el nombre'
    },
    email: {
        type: String,
        required: 'Falta el email'
    },
    password: String,
    telefono: Number,
    sexo:String,
    direccion:String,
    log: Number,
    lat: Number,
    avatar: String,
    tipo: {
        type: String,
        required: 'Debe seleccionar tipo de usuario',
        enum: ['vendedor', 'comprador']// vendedor, comprador
    },
    mgusta: {
        type: Number,
        default: 0
    },
    nmgusta: {
        type: Number,
        default: 0
    },
    fechaRegistro: {
        type: Date,
        default: Date.now()
    },

});

const usuario = mongoose.model('Usuario', usuarioSchema);

module.exports = usuario;
