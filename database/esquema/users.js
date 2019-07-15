const mongoose = require("../connect");
const Schema = mongoose.Schema;

const usuarioSchema = Schema({
    nombre: {
        type: String,
        required: 'Falta el nombre'
    },
    telf: Number,
    email: {
        type: String,
        required: 'Falta el email'
    },
    password: String,

    sexo:String,
    direccion:String,
    log: Number,
    lat: Number,
    vendOcomp: {
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

const usuario = mongoose.model('USER', usuarioSchema);

module.exports = usuario;
