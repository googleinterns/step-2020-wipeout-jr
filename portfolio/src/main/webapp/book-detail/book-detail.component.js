angular.
  module('bookDetail').
  component('bookDetail', {
    templateUrl: 'book-detail/book-detail.template.html',
    controller: function BookDetailController($scope,$http,$routeParams) {
        this.id = $routeParams.bookId;
      $http.get('book-data')
      .then(function(response) {
          $scope.bookList = response.data;
      })
    }
  });
