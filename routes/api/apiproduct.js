var express = require('express');
var router = express.Router();
const path = require('path');
const fs = require('fs');

const Product = require('../../database/esquema/product');
const Image = require('../../database/esquema/image');


const fileFilter = (req, file, cb) => {
    if (file.mimetype === 'image/png' || file.mimetype === 'image/jpeg' ) {
        return cb(null, true);
    }
    return cb(new Error('Admite imagenes de formato JPEG - JPG - PNG'));
}


/* Agregar nuevo producto */
router.post("/product", (req, res) => {

    upload(req, res, (error) => {
      if(error){
        return res.status(500).json({
          "error" : error.message
        });
      }else{
        if (req.file == undefined) {
          return res.status(400).json({
            "error" : 'No se recibio la imagen'
          });
        }
        let fields = req.body
        var img = {
          name : req.file.originalname,
          idUsers: fields.vendedor,
          path : req.file.path,
        };
        var modelImage = new Image(img);
        modelImage.save()
          .then( (result) => {

            let datos = {
                vendedor:fields.vendedor,
                descripcion:fields.descripcion,
                precio:fields.precio,
                stock:fields.stock,
                foto:'/api/apiimage/' + result._id,
            }

            if (fields.stock == 0 && fields.estado == 'disponible') {
                datos.estado = 'agotado';
            }else{
                datos.estado = fields.estado;
            }
            const modelProduct = new Product(datos);
            return modelProduct.save()
          })
          .then(result => {
            res.status(201).json({message: 'Producto agragado con exito',result});
          })
          .catch(err => {
            res.status(500).json({error:err.message})
          });
      }
    });
  });


/* Mostrar todos los productos en una lista */
router.get('/product', function (req, res, next) {

    let criterios = {};

    if(req.query.descripcion != undefined){
        criterios['$text'] = {$search: req.query.descripcion}
    }

    Product.find(criterios).ne('estado','no disponible').select('-__v').exec().then(docs => {
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

/* Detalle de un determinando producto*/
router.get('/product:id', function (req, res, next) {
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
/* LIstar Productos de un vendedor */
router.get('/product/vendedor/:id', function (req, res, next) {
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

/*actualizar un determinado producto*/
router.patch('/product:id', function (req, res) {
    let idProduct = req.params.id;
    const datos = {};

    Object.keys(req.body).forEach((key) => {
      if (key != 'vendedor' ||key != 'foto'  ) {
        datos[key] = req.body[key];
      }
    });
    console.log(datos);
    Producto.updateOne({_id: idProduct}, datos).exec()
        .then(result => {
            let message = 'Datos actualizados';
            if (result.ok == 0) {
                message = 'Verifique los datos, no se realizaron cambios';
            }
            if (result.ok == 1 && result.n == 0) {
                message = 'No se encontro el recurso';
            }
            if (result.ok == 1 && result.n == 1 && result.nModified == 0) {
                message = 'Se recibieron los mismos datos antiguos,no se realizaron cambios';
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

router.delete('/product:id', function (req, res) {
    let idProduct = req.params.id;
    Producto.deleteOne({_id: idProduct}).exec()
        .then(result => {
            let message = 'Se elimino el recurso';
            if (result.ok == 0) {
                message = 'Verifique los datos, no se realizaron cambios';
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
