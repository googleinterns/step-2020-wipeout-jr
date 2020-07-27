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

    $http.get('admin-book-upload').then(function(response) {
        vm.result = response.data;
    })

    $http.get('user-review').then(function(response) {
      vm.userReviews = response.data;
      vm.loadingReviews = false;
    })

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
