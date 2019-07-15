var express = require('express');
const IMAGE = require('../../database/esquema/image');
var router = express.Router();
const path = require('path');
const fs = require('fs');



/*Obtener todas las imagenes */
router.get("/image", (req, res) => {
    IMAGE.find().exec()
    .then(docs => {
      res.json({
        data: docs
      });
    })
    .catch(err => {
      res.status(500).json({
          error: err.message
      })
    });
  });


module.exports = router;
