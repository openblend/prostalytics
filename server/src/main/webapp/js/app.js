'use strict';

var plyticsModule = angular.module('plytics', [ 'plyticsServices', 'ngCookies', 'ngSanitize' ]);
var loadCount = 0;


plyticsModule.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {
        templateUrl : 'partials/login.html',
        controller : LoginCtrl
    }).when('/register', {
        templateUrl : 'partials/register.html',
        controller : RegisterCtrl
    }).otherwise({
        redirectTo : '/'
    });
} ]);


plyticsModule.config(function($httpProvider) {
    $httpProvider.responseInterceptors.push('loadingInterceptor');
    $httpProvider.responseInterceptors.push('errorInterceptor');

    var spinnerFunction = function(data, headersGetter) {
        loadCount++;
        $('#loading').show();
        return data;
    };

    $httpProvider.defaults.transformRequest.push(spinnerFunction);
});

plyticsModule.factory('loadingInterceptor', function($q, $window) {
    return function(promise) {
        return promise.then(function(response) {
            loadCount--;
            if (loadCount == 0) {
                $('#loading').hide();
            }
            return response;
        }, function(response) {
            loadCount--;
            if (loadCount == 0) {
                $('#loading').hide();
            }
            return $q.reject(response);
        });
    };
});

plyticsModule.factory('errorInterceptor', function($q, $window) {
    $('#error').hide();
    return function(promise) {
        return promise.then(function(response) {
            $('#errorStats').text();
            $('#error').hide();
            return response;
        }, function(response) {
            $('#errorStats').text(response.status);
            $('#error').show();
            return $q.reject(response);
        });
    };
});

/* http://www.benlesh.com/2012/12/angular-js-custom-validation-via.html */
plyticsModule.directive('bvRules', function() {

    var bvRulesValidate = function(elem, attr, value) {

        var valid = true;
        var ngSplit = attr.ngModel.split(".");
        var object = ngSplit[0];
        var field = ngSplit[1];

        if (object != undefined && field != undefined ) {
            var fieldDef = window.formsDef[object][field];
            if (fieldDef.required != undefined) {
                if (fieldDef.required == 'nonEmpty') {
                    valid = (value != undefined && value != "");
                } else if (fieldDef.required == 'date') {
                    valid = datePatt.test(value);
                }
            }
        }
        return valid;
    };

    return {
        // restrict to an attribute type.
        restrict: 'A',

        // element must have ng-model attribute.
        require: 'ngModel',

        // scope = the parent scope
        // elem = the element the directive is on
        // attr = a dictionary of attributes on the element
        // ctrl = the controller for ngModel.
        link: function(scope, elem, attr, ctrl) {

            // add a parser that will process each time the value is
            // parsed into the model when the user updates it.
            ctrl.$parsers.unshift(function(value) {
                // test and set the validity after update.
                var valid = bvRulesValidate(elem, attr, value);
                ctrl.$setValidity('bvRules', valid);

                // if it's valid, return the value to the model,
                // otherwise return undefined.
                return valid ? value : undefined;
            });

            // add a formatter that will process each time the value
            // is updated on the DOM element.
            ctrl.$formatters.unshift(function(value) {
                // validate.
                var valid = bvRulesValidate(elem, attr, value);
                ctrl.$setValidity('bvRules', valid);
                // return the value or nothing will be written to the DOM.
                return value;
            });
        }
    };
});

