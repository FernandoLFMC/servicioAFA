var express = require('express');
const USER = require('../../database/esquema/users');
var router = express.Router();
const path = require('path');
const fs = require('fs');





router.post('/user', function (req, res, next) {
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

router.post('/login', (req, res, next) => {
    Usuario.find({
            email: req.body.email
        }).exec().then(user => {
          console.log(req.body);
            if (user.length < 1) {
                return res.status(401).json({
                    error: "Usuario inexistente"
                });
            }
            if (sha1(req.body.password)!= user[0].password) {
                return res.status(400).json({
                    error: "Fallo al autenticar, contraseña incorrecta"
                });
            }else{
                const token = jwt.sign({
                    email: user[0].email,
                    userId: user[0]._id
                    },
                    process.env.JWT_KEY || 'secret321', {
                        expiresIn: "2h"
                    });

                return res.status(200).json({
                    message: "Acceso correcto",
                    tipo: user[0].tipo,
                    token,
                    id:user[0]._id
                });
            }
        })
        .catch(err => {
            //console.log(err);
            res.status(500).json({
                error: err
            });
        });
});
////loginGoogle
router.post('/logingoogle',(req,res)=>{
    const token=jwt.sign({
              email:req.body.email
            },process.env.JWT_KEY||'miClave',{
              expiresIn:"2h"
            });
    res.status(200).json({
      message:token
    });
});
/////
router.patch('/:id', function (req, res, next) {
    let idUsuario = req.params.id;
    const datos = {};

    Object.keys(req.body).forEach((key) => {
      if (key != 'email' || key != 'tipo'|| key != 'sexo') {
        datos[key] = req.body[key];
      }
    });
    //console.log(datos);
    Usuario.updateOne({_id: idUsuario}, datos).exec()
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

module.exports = router;
