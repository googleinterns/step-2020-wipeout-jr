angular.module('userInfo').component('userInfo', {
    // template: '<div ng-include="getTemplateUrl()"></div>',
    // templateUrl: "<ng-include src='getTemplateUrl()'></ng-include>",
    templateUrl: 'user-info/user-info-login.template.html',
    // templateUrl: function userInfoTemplate($http) {
    //     var vm = this;
    //     $http.get('user-info').then(function(response) {
    //         console.log("template url setting");
    //         console.log(response.data);
    //         vm.userInfo = response.data;
    //         console.log(typeof vm.userInfo);
    //         if (!vm.userInfo.includes('_ah/login')) {
    //             console.log("Nickname time");
    //             return 'user-info/user-info-nickname.template.html';
    //         } else {
    //             console.log("Logging in");
    //             return 'user-info/user-info-login.template.html';
    //         }
    //     })
    //     console.log("reached this part of function")},
    controller: function UserInfoController($http, $scope) {
        var vm = this;
        vm.loading = true;
        vm.userInfo = "";

        $http.get('user-info').then(function(response) {
            console.log("controller setting");
            console.log(response.data);
            vm.userInfo = response.data;
            vm.loading = false;
        });

        vm.isLoggedIn = function() {
            console.log("getting template url");
            if (!vm.userInfo.includes('_ah/login')) {
                console.log("Nickname time");
                return true;
            } else {
                console.log("Logging in");
                return false;
            }
        }
    },
    controllerAs:'userInfoCtrl'
});
