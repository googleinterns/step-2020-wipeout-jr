// Configure URL paths and the AngularJS views / components they correspond to.
angular.module('betterReadsApp').config([
  '$routeProvider',
  function config($routeProvider) {
    $routeProvider.when('/book-list', {template: '<book-list></book-list>'})
        .when('/book-detail/:bookIsbn', {template: '<book-detail></book-detail>'})
        .when('/profile-page', {template: '<profile-page><profile-page>'})
        .otherwise('/book-list');
  }
]);
