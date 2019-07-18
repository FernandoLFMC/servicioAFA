var express = require('express');
var router = express.Router();


const Cita = require('../../database/schema/cita');


<<<<<<< HEAD
// Agregar una cita
=======
//nueva cita
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.post("/", (req, res) => {

    let fields = req.body
    var datos = {
        vendedor : fields.file.vendedor,
        comprador : fields.file.comprador,
        producto: fields.producto,
        cantidad : fields.cantidad,
        estado : 'por confirmar',
        fechaCita : fields.fechaCita,
        horaCita : fields.horaCita,
        log : fields.log,
        lat : fields.lat,

    };

    var modelCita = new Cita(datos);
    modelCita.save()
        .then(result => {
<<<<<<< HEAD
        res.status(201).json({message: 'Cita Agregada',result});
=======
        res.status(201).json({message: 'Se creo una nueva cita',result});
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
        })
        .catch(err => {
        res.status(500).json({error:err.message})
        });
});

<<<<<<< HEAD
//ver mis citas
=======
//mostrar una Cita
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.get('/:id', function (req, res, next) {
    let idCita = req.params.id;
    Cita.findOne({_id: idCita}).select('-__v').exec().then(docs => {
        if(doc == null){
            return res.status(404).json({message: 'Recusro buscado inexistente'});
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
// lista de todas las Citas
=======
//listar Citas de un usuario
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.get('/user/:id', function (req, res, next) {
    let idUser = req.params.id;
    Cita.find().or([{comprador:idUser},{vendedor:idUser}]).select('-__v').exec().then(docs => {
        if(docs.length == 0){
            return res.status(404).json({message: 'No tiene Citas'});
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
//Modificar una cita
=======
//Modificar cita
>>>>>>> 102d80d0e17e3f93da9421d15eafba175768557c
router.patch('/:id', function (req, res) {
    let idCita = req.params.id;
    if (req.body.texto == undefined) {
        return res.status(400).json({
            error: "campo vacio, llenelo"
        })
    }
    const datos = {texto: req.body.texto};

    Cita.updateOne({_id: idCita}, datos).exec()
        .then(result => {
            let message = 'Datos actualizados';
            if (result.ok == 0) {
                message = 'Verifique sus datos porque existen cambios';
            }
            if (result.ok == 1 && result.n == 0) {
                message = 'No se encontro el recurso';
            }
            if (result.ok == 1 && result.n == 1 && result.nModified == 0) {
                message = 'mismos datos,no existen cambios';
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
//Eliminar cita
router.delete('/:id', function (req, res) {
    let idCita = req.params.id;
    Cita.deleteOne({_id: idCita}).exec()
        .then(result => {
            let message = 'Se elimino el recurso';
            if (result.ok == 0) {
                message = 'Verifique sus datos, no existen cambios';
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
