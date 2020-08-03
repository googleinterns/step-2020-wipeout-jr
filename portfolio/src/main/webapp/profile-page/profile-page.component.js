angular.module('profilePage').component('profilePage', {

  templateUrl: 'profile-page/profile-page.template.html',
  controller: function profileController($http) {
    var vm = this;
    vm.loadingInfo = true;
    vm.loadingReviews = true;


    $http.get('user-info').then(function(response) {
      vm.userInfo = response.data;
      vm.loadingInfo = false;
    });

    $http.get('user-review').then(function(response) {
      vm.userReviews = response.data;
      vm.loadingReviews = false;
      vm.correspondingBooks = getBooks();
    });

    function getBooks() {
      correspondingBooks = [];

      vm.userReviews.forEach(
          review =>
              $http
                  .get('book', {
                    params: {'isbn': parseInt(review.isbn.slice(0, 13), 10)}
                  })
                  .then(function(response) {
                    var newBook = new Object();
                    newBook.title = response.data.title;
                    newBook.isbn = response.data.isbn;
                    newBook.userReview = review.fullText;
                    correspondingBooks.push(newBook);
                  }))
      return correspondingBooks;
    };

    vm.isLoggedIn = function() {
      if (!(vm.userInfo === 'Logged Out')) {
        return true;
      } else {
        return false;
      }
    };
  },
  controllerAs: 'profileCtrl'
});
