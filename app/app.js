angular.module('app', ['ui.router'])
  .config(function ($stateProvider, $urlRouterProvider) {
  $stateProvider
    .state('login', {
      url: "/login",
      templateUrl: 'public/partials/login.html',
      controller: 'LoginCtrl',
    })
    .state('home', {
      url: "/home",
      templateUrl: 'public/partials/home.html',
    })
    .state('register', {
      url: "/register",
      templateUrl: 'public/partials/register.html',
      controller: 'RegisterCtrl'
    })
    $urlRouterProvider.otherwise('/login');
});