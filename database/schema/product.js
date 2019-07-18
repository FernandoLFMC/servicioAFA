const mongoose = require('../connect');
const Schema = mongoose.Schema;

const productSchema = Schema({

    vendedor: {
        type: Schema.Types.ObjectId,
        ref: "Usuario",
<<<<<<< HEAD:database/schema/product.js
        require:'Falta info del vendedor'
=======
        require:'Requiere informacion del vendedor'
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c:database/schema/product.js
    },
    descripcion: String,
    precio: {
        type: Number,
<<<<<<< HEAD:database/schema/product.js
        require:'Producto debe tener un precio',
        min:0.50
=======
        require:'Requiere el precio',
        min:10
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c:database/schema/product.js
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
