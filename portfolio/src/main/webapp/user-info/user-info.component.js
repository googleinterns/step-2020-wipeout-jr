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
      if (!vm.userInfo.includes('_ah/login')) {
        if (!vm.userInfo == '') {  // TODO: input validation on nicknames so
                                   // can't have empty string
          vm.nicknameSet = true;
        } else {
          vm.nicknameSet = false;
        }
        return true;
      } else {
        return false;
      }
    };
  },
  controllerAs: 'userInfoCtrl'
});
