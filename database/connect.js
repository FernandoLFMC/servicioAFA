const mongoose = require('mongoose');
mongoose.connect("mongodb://172.27.0.2:27017/servicioAFA");
module.exports = mongoose;
