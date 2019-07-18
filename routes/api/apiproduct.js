var express = require('express');
var router = express.Router();
const multer = require('multer');
const path = require('path');
const fs = require('fs');

const Producto = require('../../database/schema/product');
const Imagen = require('../../database/schema/imagen');

const storage = multer.diskStorage({
    destination: function (res, file, cb) {
        try {
            fs.statSync('./uploads/');
        } catch (e) {
            fs.mkdirSync('./uploads/');
        }
        cb(null, './uploads/');
    },
    filename: (res, file, cb) => {
        cb(null, 'IMG-' + Date.now() + path.extname(file.originalname))
    }
})
const fileFilter = (req, file, cb) => {
    if (file.mimetype === 'image/png' || file.mimetype === 'image/jpeg' ) {
        return cb(null, true);
    }
    return cb(new Error('Solo se admiten imagenes png, jpg y jpeg'));
}

const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: {
        fileSize: 1024 * 1024 * 10
    }
}).single('foto');

<<<<<<< HEAD
/* Agregar nuevo producto */
=======
//Agregar nuevo producto
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.post("/", (req, res) => {

    upload(req, res, (error) => {
      if(error){
        return res.status(500).json({
          "error" : error.message
        });
      }else{
        if (req.file == undefined) {
          return res.status(400).json({
            "error" : 'imagen no guardada'
          });
        }
        let fields = req.body
        var img = {
          name : req.file.originalname,
          idUsuario: fields.vendedor,
          path : req.file.path,
        };
        var modelImagen = new Imagen(img);
        modelImagen.save()
          .then( (result) => {

            let datos = {
                vendedor:fields.vendedor,
                descripcion:fields.descripcion,
                precio:fields.precio,
                stock:fields.stock,
<<<<<<< HEAD
                foto:'/api/imagenes/' + result._id,
=======
                foto:'/api/apiimagenes/' + result._id,
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
            }

            if (fields.stock == 0 && fields.estado == 'disponible') {
                datos.estado = 'agotado';
            }else{
                datos.estado = fields.estado;
            }
            const modelProducto = new Producto(datos);
            return modelProducto.save()
          })
          .then(result => {
<<<<<<< HEAD
            res.status(201).json({message: 'Se Agrego el producto',result});
=======
            res.status(201).json({message: 'Producto agregado',result});
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
          })
          .catch(err => {
            res.status(500).json({error:err.message})
          });
      }
    });
  });
<<<<<<< HEAD
/* listar Productos para el comprador */
=======
//listar Productos para el comprador
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.get('/', function (req, res, next) {

    let criterios = {};

    if(req.query.descripcion != undefined){
        criterios['$text'] = {$search: req.query.descripcion}
    }

    Producto.find(criterios).ne('estado','no disponible').select('-__v').exec().then(docs => {
        if(docs.length == 0){
        return res.status(404).json({message: 'No existen Productos disponibles'});
        }
        res.json({data:docs});
    })
    .catch(err => {
        res.status(500).json({
            error: err.message
        })
    });
});

<<<<<<< HEAD
/* Ver un producto*/
=======
// Ver un determinado producto
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.get('/:id', function (req, res, next) {
    Producto.findOne({_id:req.params.id}).select('-__v').exec().then(doc => {
        if(doc == null){
          return res.status(200).json({message: 'No existen Productos registrados'});
        }
        res.json(doc);
    })
    .catch(err => {
        res.status(500).json({
            error: err.message
        })
    });
});
<<<<<<< HEAD
/* LIstar Productos de un vendedor */
=======
//LIstar Productos de un vendedor
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.get('/vendedor/:id', function (req, res, next) {
    Producto.find({vendedor:req.params.id}).select('-__v').exec().then(docs => {
        if(docs.length == 0){
        return res.status(404).json({message: 'No existen Productos registrados'});
        }
        res.json({data:docs});
    })
    .catch(err => {
        res.status(500).json({
            error: err.message
        })
    });
});
router.patch('/:id', function (req, res) {
    let idProducto = req.params.id;
    const datos = {};

    Object.keys(req.body).forEach((key) => {
      if (key != 'vendedor' ||key != 'foto'  ) {
        datos[key] = req.body[key];
      }
    });
    console.log(datos);
    Producto.updateOne({_id: idProducto}, datos).exec()
        .then(result => {
            let message = 'Datos actualizados';
            if (result.ok == 0) {
<<<<<<< HEAD
                message = 'Verifique sus datos porque no existen cambios';
=======
                message = 'Verifique los datos no existe cambios';
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
            }
            if (result.ok == 1 && result.n == 0) {
                message = 'No se encontro el recurso';
            }
            if (result.ok == 1 && result.n == 1 && result.nModified == 0) {
<<<<<<< HEAD
                message = 'mismos datos ,no existen cambios';
=======
                message = 'no se relizaron cambios en los datos';
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
            }
            res.json({
                message,
                result
            });

        }).catch(err => {
            res.status(500).json({
                error: err
            })
        });
});

router.delete('/:id', function (req, res) {
    let idProducto = req.params.id;
    Producto.deleteOne({_id: idProducto}).exec()
        .then(result => {
            let message = 'Se elimino el recurso';
            if (result.ok == 0) {
                message = 'Verifique sus datos porque no existen cambios';
            }
            if (result.ok == 1 && result.n == 0) {
                message = 'No se encontro el recurso';
            }
            res.json({
                message,
                result
            });
        })
        .catch(err => {
            res.status(500).json({
                error: err
            })
        });
});

module.exports = router;
