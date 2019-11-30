var restful = require('node-restful');
var mongoose = restful.mongoose;
const Schema = mongoose.Schema;

var custSchema = new mongoose.Schema({

    First_Name: String,
    Last_Name: String,
    NIC_Passport_No: String,
    DateOfBirth: Date,
    Deposit_Amount: Number,
    Password: String,
    Email_Address: String,
    Mobile: String
});

module.exports =restful.model('customer', custSchema);