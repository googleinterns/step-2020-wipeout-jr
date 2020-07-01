angular.module('bookList').component('bookList', {
  // This name is what AngularJS uses to match to the `<book-list>` element.
  templateUrl: 'book-list/book-list.template.html',
  controller: function BookListController($scope, $http) {
    // TODO: style guide Y030-032
    $http.get('book-data').then(function(response) {
      $scope.bookList = response.data;
    })
  }
});
