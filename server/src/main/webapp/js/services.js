'use strict';

var plyticsServices = angular.module('plyticsServices', [ 'ngResource' ]);

plyticsServices.service('User', function($resource, $http, $cookieStore, $window) {
    var registerRes = $resource('rest/auth/register');
    var loginRes = $resource('rest/auth/login');
    var logoutRes = $resource('rest/auth/logout');
    var userInfoRes = $resource('rest/auth/userinfo');

    var user = {};

    var initUser = function() {
        user.username = null;
        user.password = null;
        user.name = null;
        user.lastName = null;
        user.loggedIn = false;
        user.admin = false;
    };

    initUser();


    var loadUserInfo = function(success) {
        userInfoRes.get(function(userInfo) {
            if (userInfo.id) {
                user.id = userInfo.id
                user.username = userInfo.username;
                user.name = userInfo.name;
                user.lastName = userInfo.lastName;
                user.loggedIn = true;
                user.admin = userInfo.admin;
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
        initUser();
        localStorage.removeItem("logged-in");

        logoutRes.save({}, function() {
            $window.location.href = "#/";
        }, function(e) {
            console.log("Failed to log out!");
        });
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


plyticsServices.service('App', function($resource, $window) {
    var appInfoRes = $resource('rest/appinfo');

    var appInfo = {
        isSetUp : true
    };

    appInfo.loadInfo = function() {
        appInfoRes.get(function(result) {
            if (result.setUp != null) {
                appInfo.isSetUp = result.setUp;
            }

            if (!appInfo.isSetUp) {
                $window.location.href = "#/register";
            }
        }, function(e) {
            console.log("Failed to load appinfo");
        });
    };

    appInfo.loadInfo();
    return appInfo;
});
