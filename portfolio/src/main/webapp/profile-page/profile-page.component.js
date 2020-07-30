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

    });

    function getBooks() {
        correspondingBooks = [];

        vm.userReviews.forEach(review => 
            $http.get('book', {params: {'isbn': review.isbn}})
            .then(function(response) {
                console.log(response.data);
                correspondingBooks.push(response.data);
            })
        )
        return correspondingBooks;
    };

    vm.correspondingBooks = getBooks();

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
