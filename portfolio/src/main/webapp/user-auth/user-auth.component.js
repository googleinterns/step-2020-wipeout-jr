angular.module('userAuth').component('userAuth', {

  templateUrl: 'user-auth/user-auth.template.html',
  controller: function UserAuthController($http) {
    var vm = this;
    vm.loading = true;

    $http.get('user-info').then(function(response) {
      vm.userStatus = response.data;
      vm.loading = false;
    });

    vm.isLoggedIn = function() {
      if (vm.userStatus === 'Logged Out') {
        return false;
      } else {
        return true;
      }
    }
  },
  controllerAs: 'userAuthCtrl'
});
