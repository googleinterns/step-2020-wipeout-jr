angular.module('userAuth').component('userAuth', {

    templateUrl: 'user-auth/user-auth.template.html',
    controller: function UserAuthController($http) {
        var vm = this;
        vm.loading = true;
        vm.authInfo = "";

        $http.get('auth').then(function(response) {
            // console.log("controller setting");
            console.log(response.data);
            vm.authInfo = response.data;
            console.log("authInfo: " + vm.authInfo);
            console.log("authInfo type: " + typeof vm.authInfo);
            console.log("authInfo size: " + vm.authInfo.length);
            vm.loading = false;
        });

        vm.isLoggedIn = function() {
            if (vm.authInfo.length > 1) { //authInfo includes both email and logout URL, so logged in
            console.log("size >1");
                return true;
            } else { //authInfo includes only login URL, so logged out
            console.log("size !>1");
                return false;
            }
        }
    },
    controllerAs:'userAuthCtrl'
});

// response.setContentType("text/html");
//     String urlToRedirectTo = "/";

//     UserService userService = UserServiceFactory.getUserService();

//     if (userService.isUserLoggedIn()) {
//       // If user has not set a nickname, redirect to nickname page
//       String nickname = getUserNickname(userService.getCurrentUser().getEmail());

//       if (nickname.equals("")) {
//         response.sendRedirect("/");
//         return;
//       }

//       String userEmail = userService.getCurrentUser().getEmail();
//       String logoutUrl = userService.createLogoutURL(urlToRedirectTo);

//       response.getWriter().println("<p>Hello " + userEmail + "!</p>");
    //   response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");

//     } else {
//       String loginUrl = userService.createLoginURL(urlToRedirectTo);

//       response.getWriter().println("<p>Hello stranger.</p>");
//       response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");