'use strict';

function UserCtrl($scope, User, $location) {
    $scope.user = User;
}

function LoginCtrl($scope, User, $location) {
    $scope.username = null;
    $scope.password = null;
    
    $scope.login = function() {
        $scope.failed = false;

        User.login($scope.username, $scope.password, function() {
            $('#loginModal').modal('hide');
            $location.path('/');
        }, function() {
            $scope.failed = true;
        });
    };
}

function RegisterCtrl($scope, User) {
    $scope.u = {};

    $scope.register = function() {
        $scope.registered = false;
        $scope.failed = false;

        User.register($scope.u, function(response) {
            $scope.registered = true;
            $scope.u = {};
        }, function(status) {
            $scope.failed = true;
            $scope.failedMessage = status;
        });
    };
}

function PatientFormCtrl($scope, $http) {
    var id = getURLParameter('id');

    if (id != null) {
        /*$http.get('rest-mock/patient-' + id + '.json').success(function(data) {
         $scope.patient = data;
         });*/
        $.ajax({
            //async: false,
            dataType: "json",
            url: 'rest/patient/' + id,
            success: (function(data) {
                $scope.patient = data;
            })
        });
    }

    $scope.postFormData = function(form) {
        form.submitted = true;
        if (form.$valid) {
            alert("posting: " + angular.toJson($scope.patient));
            $http({
                method: 'POST',
                url: 'rest/patient/',
                data: angular.toJson($scope.patient),
                headers: {'Content-Type': 'application/json'}
            });
            /*$.ajax({
             type: "POST",
             url: '/rest/patient/',
             data: data,
             success: success,
             dataType: dataType
             });*/
        } else {
            alert("incomplete");
        }
    };

    $scope.validateField = function(form, field) {
        //return (!field.$valid && (field.$dirty || form.submitted))
        return (!field.$valid )
    };

    /* $scope.dateOptions = {
     dateFormat: "yy-mm-dd"
     };*/

    $scope.activeTab = 'personal';
    $scope.switchTab = function(tabName, valid) {
        if(valid) {
            $scope.activeTab = tabName;
        } else {
            alert("invalid form");
        }
    }

    // TODO: what's that good for?
    window.scope = $scope;
}
