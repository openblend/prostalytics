'use strict';

var plyticsServices = angular.module('plyticsServices', [ 'ngResource' ]);

plyticsServices.service('User', function($resource, $http, $cookieStore, $window) {
    var registerRes = $resource('rest/auth/register');
    var loginRes = $resource('rest/auth/login');
    var logoutRes = $resource('rest/auth/logout');
    var userInfoRes = $resource('rest/auth/userinfo');

    var user = {};

    var initUser = function() {
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

    user.login = function(email, password, success, error) {
        user.email = email;
        user.password = password;
        loginRes.save({
            email : user.email,
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
        isSetUp : true,
        setupChecked: false
    };

    appInfo.checkSetup = function(success) {
        if (appInfo.setUpChecked && appInfo.isSetUp) {
            // do nothing
            if (success)
                success();
            return;
        }

        appInfoRes.get(function(result) {
            if (result.setUp != null) {
                appInfo.setUpChecked = true;
                appInfo.isSetUp = result.setUp;
            } else {
                console.log("/appinfo result contains no field 'setUp'");
            }

            if (success)
                success();
        }, function(e) {
            console.log("Failed to load appinfo");
        });
    };

    appInfo.checkSetup(function() {
        if (!appInfo.isSetUp) {
            $window.location.href = "#/register";
        }
    });
    return appInfo;
});
