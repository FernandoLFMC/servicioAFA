const mongoose = require('../connect');
const Schema = mongoose.Schema;

const productoSchema = Schema({

    //tipo de usuario en este caso vendedor

    vendedor: {
        type: Schema.Types.ObjectId,
        ref: "Users",
        require:'Falta info del vendedor'
    },
    //descripciop del producto a efertar
    descripcion: String,
    precio: {
        type: Number,
        require:'Producto debe tener un precio',
        min:0.50
    },
    //cantidad del producto
    stock:{
        type:Number,
        default:0,
        min:0
    },
    //verificacion si esta disponible o no disponible
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

productoSchema.index({descripcion: 'text'});

const producto = mongoose.model('Product', productSchema);

module.exports = product;