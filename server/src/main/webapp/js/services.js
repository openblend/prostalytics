'use strict';

var plyticsServices = angular.module('plyticsServices', [ 'ngResource' ]);

plyticsServices.service('User', function($resource, $http, $cookieStore) {
    var registerRes = $resource('rest/auth/register');
    var loginRes = $resource('rest/auth/login');
    var logoutRes = $resource('rest/auth/logout');
    var userInfoRes = $resource('rest/auth/userinfo');

    var user = {
        username : null,
        password : null,
        name : null,
        lastName : null,
        loggedIn : false
    };

    var loadUserInfo = function(success) {
        userInfoRes.get(function(userInfo) {
            if (userInfo.id) {
                user.id = userInfo.id
                user.username = userInfo.username;
                user.name = userInfo.name;
                user.lastName = userInfo.lastName;
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
            username : user.username,
            password : user.password
        }, function(response) {
            if (response.id) {
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

        logoutRes.save({}, success, error);
    };

    user.register = function(user, success, error) {
        registerRes.save(user, function(response) {
            if (response.id) {
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
