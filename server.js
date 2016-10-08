// server.js

var express    = require('express');        // call express
var app        = express();                 // define our app using express
var mongoose   = require('mongoose');

var port = process.env.PORT || 8080;        // set our port
var router = express.Router();              // get an instance of the express Router

router.use(function(req,res,next){
	console.log('Request');
	next();
});

router.route('/tasks')
	.get(function(req,res){
		res.json({message: 'List of current tasks'});
	})
	.post(function(req,res){
		res.json({message: 'Task created!'});
	});

router.route('/tasks/:task_id')
	.get(function(req,res){
		res.json({message: 'Task with id: '+req.params.task_id});
	})
	.delete(function(req,res){
		res.json({message: 'Task with id: '+req.params.task_id+' deleted'});
	})
	.put(function(req,res){
		res.json({message: 'Task with id: ' +req.params.task_id + ' updated'});
	});
	
router.route('/meetings')
	.get(function(req,res){
		res.json({message: 'List of current meetings'});
	})
	.post(function(req,res){
		res.json({message: 'Meeting created!'});
	});

router.route('/meetings/:meeting_id')
	.get(function(req,res){
		res.json({message: 'Meeting with id: '+ req.params.meeting_id});
	})
	.delete(function(req,res){
		res.json({message: 'Meeting with id: ' + req.params.meeting_id + ' deleted'});
	})
	.put(function(req,res){
		res.json({message: 'Meeting with id: ' + req.params.meeting_id + ' updated'});
	});
// all of our routes will be prefixed with /api
app.use('/api', router);
app.listen(port);
console.log('Server started on port ' + port);