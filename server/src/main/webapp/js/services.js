'use strict';

var plyticsServices = angular.module('plyticsServices', [ 'ngResource' ]);

plyticsServices.service('User', function($resource, $http, $cookieStore) {
    var registerRes = $resource('/rest/auth/register');
    var loginRes = $resource('/rest/auth/login');
    var logoutRes = $resource('/rest/auth/logout');
    var userInfoRes = $resource('/rest/auth/userinfo');

    var user = {
        username : null,
        password : null,
        roles : null,
        loggedIn : false
    };

    var loadUserInfo = function(success) {
        userInfoRes.get(function(userInfo) {
            if (userInfo.userId) {
                user.username = userInfo.userId;
                user.name = userInfo.userId;

                if (userInfo.fullName && userInfo.fullName != "null null") {
                    user.name = userInfo.fullName.replace(/\ null$/g, '').replace(/^null\ /g, '');
                }

                user.roles = userInfo.roles;
                user.loggedIn = true;

                if (success) {
                    success();
                }
            }
        }, function() { 
            localStorage.removeItem("logged-in");
        });
    };

    user.login = function(username, password, success, error) {
        user.username = username;
        user.password = password;
        loginRes.save({
            userId : user.username,
            password : user.password
        }, function(response) {
            if (response.loggedIn) {
                localStorage.setItem("logged-in", "true");

                loadUserInfo(success);
            } else if (error) {
                error();
            }
        }, error);
    };

    user.logout = function() {
        user.username = null;
        user.password = null;
        user.loggedIn = false;

        localStorage.removeItem("logged-in");

        logoutRes.get();
    };

    user.register = function(user, success, error) {
        registerRes.save(user, function(response) {
            if (response.registered) {
                success();
            } else {
                error(response.status);
            }
        }, error);
    };

    if (!user.loggedIn && localStorage.getItem("logged-in")) {
        loadUserInfo();
    }

    return user;
});
