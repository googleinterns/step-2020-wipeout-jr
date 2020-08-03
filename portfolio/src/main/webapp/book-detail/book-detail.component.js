angular.module('bookDetail').component('bookDetail', {
  templateUrl: 'book-detail/book-detail.template.html',
  controller: function BookDetailController($http, $routeParams) {
    var vm = this;
    vm.bookIsbn = $routeParams.bookIsbn;
    $http.get('book', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.book = response.data;
          vm.book.categories = formatList(vm.book.categories);
          vm.book.authors = formatList(vm.book.authors);
          vm.book.genre = formatList(vm.book.genre);
          vm.book.titleURL = searchurl(vm.book.title);
          vm.book.publisherURL = searchurl(vm.book.publisher);
          vm.book.authorURL = searchurl(vm.book.authors);
        })

    $http.get('/bookRecommendationById', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.recommendedBooks = response.data;
        })

    $http.get('/sentimentRecommendationById', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.sentimentRecs = response.data;
        })
    
    $http.get('/reviews', {params: {'isbn': vm.bookIsbn}})
        .then(function(response) {
          vm.reviews = response.data;
        })

    function formatList(list) {
      var formattedList = '';
      var i;
      for (i = 0; i < list.length; i++) {
        if (i == list.length - 1) {
          var element = list[i];
        } else {
          var element = list[i] + ', ';
        }
        formattedList += element;
      }
      return formattedList;
    }

    function searchurl(str){
        var words = str.split(" ");
        var query = "https://www.google.com/search?q=";
        for (i = 0; i < words.length - 1; i++) {
            query += words[i] + "+"
        }
        query += words[words.length - 1];
        return query;
    }
  },
  controllerAs: 'bookDetailCtrl'
});
