angular.module('userStatus').component('userStatus', {
  templateUrl: 'user-status/user-status.template.html',
  controller: function UserController($http) {
    var vm = this;
    $http.get('user-status').then(function(response) {
      vm.userStatus = response.data;
      if (vm.userStatus == 'Logged In') {
        vm.userStatusAction = 'Log Out';
      } else {
        vm.userStatusAction = 'Log In';
      };
    })
  },
  controllerAs: 'userStatusCtrl'
});
