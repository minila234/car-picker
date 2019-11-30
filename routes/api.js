var express =require('express');
var router = express.Router();

var Status =require('../models/status');

Status.methods(['get','post','put','delete']);
Status.register(router,'/status');

module.exports =router;