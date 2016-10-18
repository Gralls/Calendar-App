var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
	name : String,
	login : String,
	password : String,
	email : String
});

module.exports = mongoose.model('User',UserSchema);