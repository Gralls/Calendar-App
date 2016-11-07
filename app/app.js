angular.module('app', ['ngRoute'])
  .config(function ($routeProvider) {
  $routeProvider
    .when('/login', {
      templateUrl: 'public/partials/login.html',
      controller: 'LoginCtrl',
    })
    .when('/home', {
      templateUrl: 'public/partials/home.html',
    })
    .when('/register', {
      templateUrl: 'public/partials/register.html',
      controller: 'RegisterCtrl'
    })
    .otherwise({
      redirectTo: '/login'
    });
});