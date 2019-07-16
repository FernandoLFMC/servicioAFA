const mongoose = require('../connect');
const Schema = mongoose.Schema;

const userSchema = Schema({
    nombre: {
        type: String,
        required: 'Falta el nombre'
    },
    email: {
        type: String,
        required: 'Falta el email',
        //restriccion para el correo "ejemplo@gmail.com"
        match: /^(([^<>()\[\]\.,;:\s @\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i,
    },
    password: String,
    telefono: Number,
    sexo:String,

    avatar: String,
        //seleccion de tipo vendedor o comprador
    tipo: {
        type: String,
        required: 'Seleccione tipo de usuario',
        enum: ['vendedor', 'comprador']
    },
    //calificacion de me gusta
    mgusta: {
        type: Number,
        default: 0
    },
    //calificacion de no me gusta
    nmgusta: {
        type: Number,
        default: 0
    },
    lat: Number,
    lon: Number,

    fechaRegistro: {
        type: Date,
        default: Date.now()
    },

});

const user = mongoose.model('User', userSchema);

module.exports = user;
