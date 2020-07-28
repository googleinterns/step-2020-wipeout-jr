angular.module('bookDetail').component('bookDetail', {
  templateUrl: 'book-detail/book-detail.template.html',
  controller: function BookDetailController($http, $routeParams) {
    var vm = this;
    vm.bookId = $routeParams.bookIsbn;
    $http.get('book', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.book = response.data;
        })
  },
  controllerAs: 'bookDetailCtrl'
});
