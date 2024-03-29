const mongoose = require('../connect');
const Schema = mongoose.Schema;

const productSchema = Schema({

    vendedor: {
        type: Schema.Types.ObjectId,
        ref: "Usuario",

        require:'Requiere informacion del vendedor'

    },
    descripcion: String,
    precio: {
        type: Number,

        require:'Requiere el precio',
        min:10

    },
    stock:{
        type:Number,
        default:0,
        min:0
    },
    estado:{
        type: String,
        default: 'agotado',
        enum:['disponible','no disponible','agotado'],
    },
    fechaRegistro: {
        type: Date,
        default: Date.now()
    },
    foto: String
});
mongoose.set('useCreateIndex', true);

productSchema.index({descripcion: 'text'});

const product = mongoose.model('Product', productSchema);

module.exports = product;
