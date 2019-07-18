const mongoose = require('../connect');
const Schema = mongoose.Schema;

const citaSchema = Schema({
    vendedor: {
        type: Schema.Types.ObjectId,
        ref: "User",
        require:'Requiere informacion del vendedor'
    },
<<<<<<< HEAD:database/schema/cita.js
    comprador: {// /api/user/id 
=======
    comprador: {
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c:database/schema/cita.js
        type: Schema.Types.ObjectId,
        ref: "User",
        require:'Requiere informacion del comprador'
    },
    producto: {
        type: Schema.Types.ObjectId,
        ref: "Product",
        require:'Requiere informacion del producto'
    },
    cantidad:{
        type:Number,
        require:'Requiere una cantidad de producto'
    },
    estado: {   // por confirmar, cancelada, terminada
        type: String,
<<<<<<< HEAD:database/schema/cita.js
        required: 'requiere un estado estado'
=======
        required: 'Requiere un estado'
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c:database/schema/cita.js
    },
    fechaCita: Date,
    horaCita: String,
    log: Number,
    lat: Number,
    fechaRegistro: {
        type: Date,
        default: Date.now()
    }
});

const cita = mongoose.model('Cita', citaSchema);

module.exports = cita;
