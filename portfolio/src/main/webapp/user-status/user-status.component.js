angular.module('userStatus').component('userStatus', {
  templateUrl: 'user-status/user-status.template.html',
  controller: function UserController($http) {
    var vm = this;
    $http.get('user-info').then(function(response) {
      vm.userStatus = response.data;
      vm.isLoggedIn;
      
      if (vm.userStatus == 'Logged Out') {
        vm.isLoggedIn = false;
      } else {
        vm.isLoggedIn = true;
      };
    });

  },
  controllerAs: 'userStatusCtrl'
});
