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

plyticsModule.directive('moDateInput', function ($window) {
    return {
        require:'^ngModel',
        restrict:'A',
        link:function (scope, elm, attrs, ctrl) {
            var moment = $window.moment;
            var dateFormat = attrs.moMediumDate;
            attrs.$observe('moDateInput', function (newValue) {
                if (dateFormat == newValue || !ctrl.$modelValue) return;
                dateFormat = newValue;
                ctrl.$modelValue = new Date(ctrl.$setViewValue);
            });

            ctrl.$formatters.unshift(function (modelValue) {
                scope = scope;
                if (!dateFormat || !modelValue) return "";
                var retVal = moment(modelValue).format(dateFormat);
                return retVal;
            });

            ctrl.$parsers.unshift(function (viewValue) {
                scope = scope;
                var date = moment(viewValue, dateFormat);
                return (date && date.isValid() && date.year() > 1950 ) ? date.toDate() : "";
            });
        }
    };
});
