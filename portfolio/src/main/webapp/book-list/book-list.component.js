angular.module('bookList').component('bookList', {
  // This name is what AngularJS uses to match to the `<book-list>` element.
  templateUrl: 'book-list/book-list.template.html',
  controller: function BookListController($http) {
    var vm = this;
    $http.get('book-data').then(function(response) {
      vm.bookList = response.data;
    })
  },
  controllerAs: 'bookListCtrl'
});
