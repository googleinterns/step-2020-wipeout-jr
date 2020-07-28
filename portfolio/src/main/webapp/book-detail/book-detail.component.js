angular.module('bookDetail').component('bookDetail', {
  templateUrl: 'book-detail/book-detail.template.html',
  controller: function BookDetailController($http, $routeParams) {
    var vm = this;
    vm.bookIsbn = $routeParams.bookIsbn;
    $http.get('book', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.book = response.data;
        })
    
    $http.get('/bookRecommendationById', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.recommendedBooks = response.data;
        })
    
    $http.get('/reviews', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.reviews = response.data;
        })
  },
  controllerAs: 'bookDetailCtrl'
});
