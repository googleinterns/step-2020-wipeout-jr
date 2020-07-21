angular.module('userInfo').component('userInfo', {

  templateUrl: 'user-info/user-info.template.html',
  controller: function UserInfoController($http) {
    var vm = this;
    vm.loading = true;
    vm.userInfo = '';

    $http.get('user-info').then(function(response) {
      vm.userInfo = response.data;
      vm.loading = false;
    });

    vm.isLoggedIn = function() {
        if (!(typeof vm.userInfo === "string" || vm.userInfo instanceof String)){ //if currently logged in
            return true;
        } else {
            return false;
        }
    };
  },
  controllerAs: 'userInfoCtrl'
});
