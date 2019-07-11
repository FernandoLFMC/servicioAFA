var express = require('express');
var USER = require('../database/users');
var router = express.Router();

router.post('/user', async(req, res) => {
  var params = req.body;
  params["registerdate"] = new Date();
  var users = new USER(params);
  var result = await users.save();
  res.status(200).json(result);
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
