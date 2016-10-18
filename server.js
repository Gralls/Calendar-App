// server.js

var express    = require('express');        
var app        = express();   

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var mongoose   = require('mongoose');
mongoose.connect('mongodb://tas_admin:tas123@ds021356.mlab.com:21356/tas-project');

var Task = require('./models/task');
var User = require('./models/user');

var port = process.env.PORT || 8080;        // set our port
var router = express.Router();              // get an instance of the express Router

app.use('/', express.static(__dirname));
app.get('/', function(req, res) {
  res.sendFile(__dirname + '/public/index.html');
});

router.use(function(req,res,next){
	console.log('Request');
	next();
});

//----------------------------------------------------------------------------------------

router.route('/tasks')
	.get(function(req,res){
		Task.find(function(err,tasks){
			if(err)
				res.send(err);
			res.json(tasks);
		});
	})
	.post(function(req,res){
		var task = new Task();
		task.title = req.body.title;

		task.save(function(err){
			if(err)
				res.send(err);
			res.json({message:'Task created!'});
		});
	});

router.route('/tasks/:task_id')
	.get(function(req,res){
		Task.findById(req.params.task_id,function(err,task){
			if(err)
				res.send(err);
			res.json(task);
		});
	})
	.delete(function(req,res){
		Task.remove({
			_id: req.params.task_id
		}, function(err,task){
			if(err)
				res.send(err);
			res.json({message: 'Succesfully deleted'});
		});
	})
	.put(function(req,res){
		Task.findById(req.params.task_id,function(err,task){
			if(err)
				res.send(err);
			task.title=req.body.title;
			task.save(function(err){
				if(err)
					res.send(err);
				res.json({message: 'Task updated!'});
			});
		});
	});

//--------------------------------------------------------------------------------------------
	
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
	
//---------------------------------------------------------------------------------------------

router.route('/users')
	.get(function(req,res){
		User.find(function(err,tasks){
			if(err)
				res.send(err);
			res.json(tasks);
		});
	})
	.post(function(req,res){
		var user = new User();
		user.name = req.body.name;
		user.login = req.body.login;
		user.email = req.body.email;
		user.password = req.body.password;
		user.save(function(err){
			if(err)
				res.send(err);
			res.json({message:'User created!'});
		});
	});

router.route('/users/:user_id')
	.get(function(req,res){
		User.findById(req.params.user_id,function(err,task){
			if(err)
				res.send(err);
			res.json(task);
		});
	})
	.delete(function(req,res){
		User.remove({
			_id: req.params.user_id
		}, function(err,task){
			if(err)
				res.send(err);
			res.json({message: 'User succesfully deleted'});
		});
	})
	.put(function(req,res){
		User.findById(req.params.user_id,function(err,user){
			if(err)
				res.send(err);
			
			user.save(function(err){
				if(err)
					res.send(err);
				res.json({message: 'User updated!'});
			});
		});
	});
// all of our routes will be prefixed with /api
app.use('/api', router);
app.listen(port);
console.log('Server started on port ' + port);