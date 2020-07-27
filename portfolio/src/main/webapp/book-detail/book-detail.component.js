angular.module('bookDetail').component('bookDetail', {
  templateUrl: 'book-detail/book-detail.template.html',
  controller: function BookDetailController($http, $routeParams) {
    var vm = this;
    vm.bookId = $routeParams.bookId;
    $http.get('book', {params: {'id': vm.bookId}})
        .then(function(response) {
          vm.book = response.data;
        })
  },
  controllerAs: 'bookDetailCtrl'
});
