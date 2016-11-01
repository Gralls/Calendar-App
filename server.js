// server.js

var express    = require('express');        
var app        = express();   
var bodyParser = require('body-parser');
var mongoose   = require('mongoose');
var passport	= require('passport');
var jwt         = require('jwt-simple');

var config      = require('./config/database'); // get db config file


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

mongoose.connect(config.database);

require('./config/passport')(passport);


var Task = require('./models/task');
var User = require('./models/user');

var port = process.env.PORT || 8080;        // set our port
var router = express.Router();              // get an instance of the express Router

app.use(passport.initialize());

app.use('/', express.static(__dirname));
app.get('/', function(req, res) {
  res.sendFile(__dirname + '/public/index.html');
});

router.use(function(req,res,next){
	console.log('Request');
	next();
});


//----------------------------------------------------------------------------------------


router.route('/auth')
	.post(function(req, res){
	  User.findOne({
	    login: req.body.login
	  }, function(err, user) {
	    if (err) throw err;
	 
	    if (!user) {
	      res.send({success: false, msg: 'Authentication failed. User not found.'});
	    } else {
	      // check if password matches
	      user.comparePassword(req.body.password, function (err, isMatch) {
	        if (isMatch && !err) {
	          // if user is found and password is right create a token
	          var token = jwt.encode(user, config.secret);
	          // return the information including token as JSON
	          res.json({success: true, token: 'JWT ' + token});
	        } else {
	          res.send({success: false, msg: 'Authentication failed. Wrong password.'});
	        }
	      });
	    }
	  });
});

router.route('/checkauth')
	.get(passport.authenticate('jwt', { session: false}), function(req, res) {
  var token = getToken(req.headers);
  if (token) {
    var decoded = jwt.decode(token, config.secret);
    User.findOne({
      login: decoded.login
    }, function(err, user) {
        if (err) throw err;
 
        if (!user) {
          return res.status(403).send({success: false, msg: 'Authentication failed. User not found.'});
        } else {
          res.json({success: true, msg: 'Welcome in the member area ' + user.login + '!'});
        }
    });
  } else {
    return res.status(403).send({success: false, msg: 'No token provided.'});
  }
});
 
getToken = function (headers) {
  if (headers && headers.authorization) {
    var parted = headers.authorization.split(' ');
    if (parted.length === 2) {
      return parted[1];
    } else {
      return null;
    }
  } else {
    return null;
  }
};

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
		if (!req.body.login || !req.body.password) {
		    res.json({success: false, msg: 'Please pass name and password.'});
		  } else {
		    var newUser = new User({
  		      name: req.body.name,
		      login: req.body.login,
		      password: req.body.password,
		      email: req.body.email
		    });
		    // save the user
		    newUser.save(function(err) {
		      if (err) {
		        return res.json({success: false, msg: 'Username already exists.'});
		      }
		      res.json({success: true, msg: 'Successful created new user.'});
		    });
		  }
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