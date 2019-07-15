const mongoose = require('../connect');
const Schema = mongoose.Schema;

const imageSchema = Schema({
  nombre : String,
  idUsers : String,
  path : String,
  fechaRegistro : {
    type : Date,
    default : Date.now()
  }
});
const image = mongoose.model('IMAGE', imageSchema);
module.exports = image;
