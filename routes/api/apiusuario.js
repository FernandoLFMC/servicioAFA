var express = require('express');
const USER = require('../../database/esquema/users');
var router = express.Router();


const path = require('path');
const fs = require('fs');
//const ObjectId = require('mongoose').Types.ObjectId;



router.post('/', function (req, res, next) {
    //verificar que no exista mismo correo
    USER.findOne({email:req.body.email})
    .exec()
    .then(doc => {
      //console.log(doc);

      if (doc != null) {
        return res.status(401).json({error:'el correo ya esta en uso'});
      }
      //console.log('true');

      const datos = {
        nombre: req.body.nombre,
        email: req.body.email,
        telefono: req.body.telefono,
        sexo:req.body.sexo,
        direccion:req.body.direccion,
        log: req.body.log,
        lat: req.body.lat,
        tipo: req.body.tipo,//el tipo de usuario
      };
      if (req.body.password == undefined || req.body.password == '') {
        return res.status(401).json({
          error: 'Falta la contraseña'
        })
      }
      datos.password = (req.body.password);
      //console.log(datos);
      var modelUsuario = new USER(datos);
      return modelUsuario.save()

    }).then((result) => {
      res.json({
          message: "Registro exitoso",
          result
      });
    })
    .catch(err => {
      res.status(500).json({
          error: err.message
      })
    });
});



router.get("/user", (req, res) => {
var params = req.query;
console.log(params);
var limit = 100;
if (params.limit != null) {
limit = parseInt(params.limit);
}
var order = -1;
if (params.order != null) {
if (params.order == "desc") {
order = -1;
} else if (params.order == "asc") {
order = 1;
}
}
var skip = 0;
if (params.skip != null) {
skip = parseInt(params.skip);
}
USER.find({}).limit(limit).sort({_id: order}).skip(skip).exec((err, docs) => {
res.status(200).json(docs);
});
});

//Creación del servicio de GET PATCH.
router.patch("/user", (req, res) => {
if (req.query.id == null) {
res.status(300).json({
msn: "Error no existe id"
});
return;
}
var id = req.query.id;
var params = req.body;
USER.findOneAndUpdate({_id: id}, params, (err, docs) => {
res.status(200).json(docs);
});
});

//Creación del servicio DELETE
router.delete("/user", async(req, res) => {
if (req.query.id == null) {
res.status(300).json({
msn: "Error no existe id"
});
return;
}
var r = await USER.remove({_id: req.query.id});
res.status(300).json(r);
});

module.exports = router;
