'use strict';

function UserCtrl($scope, User, $location) {
    $scope.user = User;
}

function LoginCtrl($scope, User, $location) {

    $scope.username = null;
    $scope.password = null;
    $scope.failed = false;

    $scope.login = function() {

        User.login($scope.username, $scope.password, function() {
            //$('#loginModal').modal('hide');
            document.location.href='home.jsf';
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
            document.location.href = 'login.html';
        }, function(status) {
            $scope.failed = true;
            //$scope.failedMessage = status;
        });
    };
}

function PatientListCtrl($scope, $http) {
    $http.get('rest-mock/patients.json').success(function(data) {
        $scope.patients = data;
    });
    //$scope.orderProp = 'age';
}

function PatientDetailCtrl($scope, $routeParams, $http) {
    $scope.patient = new Object();
    $scope.patientId = $routeParams.patientId;
    $http.get('rest-mock/patient-' + $routeParams.patientId + '.json').success(function(data) {
        $scope.patient = data;
        patientFormData($scope);
    });

    $scope.selectOptions = new Object();
    $scope.selectOptions.positiveNegativeWithZero = [
        {value: "no", label: "no"},
        {value: "positive", label: "positive"},
        {value: "negative", label: "negative"}
    ];
    
    $scope.update = function(patient) {
        alert(JSON.stringify(patient));
    };
}

function patientFormData($scope) {
    $scope.formDefs = new Object();

    $scope.formDefs.personalData = [
        {type: "text", object: $scope.patient, prop: "surname", required: true, label: "Surname"},
        {type: "text", object: $scope.patient, prop: "name", required: true, label: "Name"},
        {type: "date", object: $scope.patient, prop: "birthDate", required: true, label: "Birth date"},
        {type: "text", object: $scope.patient, prop: "externalId", required: true, label: "External ID"}
    ];

    $scope.formDefs.rrpBase = [
        {type: "date", object: $scope.patient, prop: "trusDate", required: true, label: "Trus date"},
        {type: "number", object: $scope.patient, prop: "trusPsaBx", required: true, label: "Trus PSA by BX"},
        {type: "radio", object: $scope.patient, prop: "trusDre", required: true, label: "Trus DRE", options: $scope.selectOptions.positiveNegativeWithZero},
    ];
}
