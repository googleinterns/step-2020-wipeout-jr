angular.module('userInfo').component('userInfo', {
    // template: '<div ng-include="getTemplateUrl()"></div>',
    template: "<div ng-include='getTemplateUrl()'>Hello</div>",
    // templateUrl: 'user-info/user-info-login.template.html',
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
    controller: function UserInfoController($http,$scope) {
        var vm = this;
        $http.get('user-info').then(function(response) {
            console.log("controller setting");
            console.log(response.data);
            vm.userInfo = response.data;
    // })
        // function getTemplateUrl() {
        $scope.getTemplateUrl = function() {
            console.log("getting template url");
            if (!vm.userInfo.includes('_ah/login')) {
                console.log("Nickname time");
                return 'user-info/user-info-nickname.template.html';
            } else {
                console.log("Logging in");
                return 'user-info/user-info-login.template.html';
            }
}; })
    },
    controllerAs:'userInfoCtrl'
});
