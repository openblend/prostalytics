'use strict';

var plyticsModule = angular.module('plytics', [ 'plyticsServices', 'ngCookies', 'ngSanitize' ]);
var loadCount = 0;


plyticsModule.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/login', {
        templateUrl : 'partials/login.html',
        controller : LoginCtrl,
    	access: { isFree: true }
    }).when('/register', {
        templateUrl : 'partials/register.html',
        controller : RegisterCtrl,
    	access: { isFree: true }
    }).when('/patients', {
    	templateUrl: 'partials/patient-list.html',   
    	controller: PatientListCtrl
	}).when('/patient/:patientId', {
		templateUrl: 'partials/patient-detail.html', 
		controller: PatientDetailCtrl
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

plyticsModule.directive('datepicker', function() {
    return {
        restrict: 'A',
        require : 'ngModel',
        link : function (scope, element, attrs, ngModelCtrl) {
            $(function(){
                element.datepicker({
                    dateFormat:'yy-mm-dd',
                    onSelect:function (date) {
                        ngModelCtrl.$setViewValue(date);
                        scope.$apply();
                    }
                });
            });
        }
    };
});


