angular.module('app')
 
.controller('LoginCtrl',
  function ($scope, $location, AuthService) {

    $scope.user = {
        password: '',
        login: '',
    };

    $scope.login = function () {
      // call login from service
      AuthService.login($scope.user)
        // handle success
        .then(function () {
          $location.path('/home');
        })
        // handle error
        .catch(function () {
          console.log('Nie wyszło');
        });
    };
})
 
.controller('RegisterCtrl',
  function ($scope, $location, AuthService) {

    $scope.user = {
        name: '',
        password: '',
        login: '',
        email: ''
    };

    $scope.register = function() {
      // call register from service
      AuthService.register($scope.user)
        // handle success
        .then(function () {
          $location.path('/login');
        })
        // handle error
        .catch(function () {
          console.log('Nie pykło');
        });
    };
});