var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TaskSchema = new Schema({
	title : String,
	startDate : String,
	endDate : String,
	description : String,
	userID : String
});

module.exports = mongoose.model('Task',TaskSchema);