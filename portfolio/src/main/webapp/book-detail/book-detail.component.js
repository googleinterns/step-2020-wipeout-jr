angular.
  module('bookDetail').
  component('bookDetail', {
    templateUrl: 'book-detail/book-detail.template.html',
    controller: ['$routeParams',
      function BookDetailController($routeParams) {
          // This gets the ID from the URL. For example ...#!/book-detail/2  ->  2
          this.bookId = $routeParams.bookId;
      }
    ]
  });
