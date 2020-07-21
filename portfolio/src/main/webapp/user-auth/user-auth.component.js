angular.module('userAuth').component('userAuth', {

  templateUrl: 'user-auth/user-auth.template.html',
  controller: function UserAuthController($http) {
    var vm = this;
    vm.loading = true;

    $http.get('user-status').then(function(response) {
      vm.userStatus = response.data;
      vm.loading = false;
    });

    vm.isLoggedIn = function() {
      if (vm.userStatus == 'Logged In') {
        console.log('logged in');
        return true;
      } else {
        console.log('not logged in');
        return false;
      }
    }
  },
  controllerAs: 'userAuthCtrl'
});
