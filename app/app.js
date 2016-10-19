(function(){
'use strict';
var app = angular.module('app',[]);

app.controller('RegisterController', function RegisterController($scope, $http){

	$scope.user = {};
	
	
	$scope.$watch('password', function(val){if(val){ $scope.user.password = val;}});		
	$scope.$watch('email', function(val){if(val){ $scope.user.email = val;}});	
	$scope.$watch('login', function(val){if(val){$scope.user.login = val;}});	
	$scope.$watch('name', function(val){if(val){$scope.user.name = val;}});	

		
	$scope.addUser = function(){
		return $http.post("/api/users", $scope.user )
			.then(function successCallback(response) {
				return response.data;
				console.log("To nie jeblo");
			  }, function errorCallback(response) {
				console.log("TO jeblo");
			  });	
	}	
	
});




})();