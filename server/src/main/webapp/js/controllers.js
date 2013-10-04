'use strict';

var selectOptions = new Object();
initFormConstants(selectOptions);

function AppInfoCtrl($scope, App, User, $location, $window) {
    $scope.appInfo = App;

    $scope.navList = [
        { url: '/', title: 'Home'},
        { url: '/patients', title: 'Browse'},
        { url: '/patient/0', title: 'Add'}
    ];

    function detectRoute() {
        angular.forEach($scope.navList, function(item) {
            console.log("$location.path(): " + $location.path() + ", item.url: " + item.url
                + ", match? " + ($location.path().match(new RegExp(item.url))));
            item.active = $location.path() === item.url;
            console.log("   item.active: " + item.active);
        });
    }

    $scope.$on('$routeChangeSuccess', detectRoute);

    $scope.$on('$routeChangeStart', function(event, next, current) {
        if (next.redirectTo === "/login" || next.redirectTo === "/register")
            return;

        // check appinfo if app has been set up
        // if not redirect to register
        if (!$scope.appInfo.isSetUp) {
            setTimeout(function() {$window.location.href='#/register'}, 1);
            return;
        }

        // check if user has been logged in
        if (!User.loggedIn) {
            // if not redirect to login form
            setTimeout(function() {$window.location.href='#/login'}, 1);
        }
    });
}

function UserCtrl($scope, User, $location) {
    $scope.user = User;
}

function LoginCtrl($scope, User, $location) {
	//TODO client side login check http://blog.brunoscopelliti.com/deal-with-users-authentication-in-an-angularjs-web-app
    $scope.email = null;
    $scope.password = null;
    $scope.failed = false;

    $scope.login = function() {

        User.login($scope.email, $scope.password, function() {
            //$('#loginModal').modal('hide');
            document.location.href='#/patients';
        }, function() {
            $scope.failed = true;
        });
    };
}

function RegisterCtrl($scope, User, App, $window) {
    $scope.u = {};

    $scope.register = function() {
        $scope.registered = false;
        $scope.failed = false;

        // set admin flag if this is first user registration
        if (!App.isSetUp) {
           $scope.u.admin = true;
        }

        User.register($scope.u, function(response) {
            $scope.registered = true;
            $scope.u = {};
            if (!App.isSetUp) {
                App.checkSetup(function() {
                    $window.location.href = "#/login";
                });
            }
        }, function(status) {
            $scope.failed = true;
            //$scope.failedMessage = status;
        });
    };
    
    registerFormData($scope);
}

function PatientListCtrl($scope, $routeParams, $resource) {
    //var Patients = $resource('rest-mock/patients.json');
    var Patients = $resource('rest/patient/find', {code:'', name:'', surname:'', externalId:''}); //TODO search
    
    $scope.patients = Patients.query({},function(data){}, onRestFail);
    
    //$scope.orderProp = 'age';
}

function PatientDetailCtrl($scope, $routeParams, $resource) {
    //var Patient = $resource('rest-mock/patient-:patientId.json');
    var Patient = $resource('rest/patient/:patientId');

    $scope.patient = new Object();
    
    var patientId = $routeParams.patientId;
    if (patientId > 0) {
        $scope.patient = Patient.get({patientId:patientId}, function(data){ patientFormData($scope);}, onRestFail);
    } else {
        $scope.patient = new Patient();
        patientFormData($scope);
    }
    
    $scope.update = function() {
        //alert(JSON.stringify($scope.patient));
        $scope.patient.$save(function(u, putResponseHeaders){document.location.href='#/patients';}, onRestFail);
    };

    $scope.cancel = function(patient) {
        document.location.href='#/patients';
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

//    $scope.formDefs.rrpBase = [
//        {type: "date", object: $scope.patient, prop: "trusDate", required: true, label: "Trus date"},
//        {type: "number", object: $scope.patient, prop: "trusPsaBx", required: true, label: "Trus PSA by BX"},
//        {type: "radio", object: $scope.patient, prop: "trusDre", required: true, label: "Trus DRE", options: selectOptions.positiveNegativeWithZero},
//    ];
}

function registerFormData($scope) {
    $scope.formDefs = new Object();

    $scope.formDefs.register = [
        {type: "email", object: $scope.u, prop: "email", required: true, label: "Email"},
        {type: "text", object: $scope.u, prop: "name", required: true, label: "Name"},
        {type: "text", object: $scope.u, prop: "lastName", required: true, label: "Last name"},
        {type: "password", object: $scope.u, prop: "password", required: true, label: "Password"}
    ];
}

function initFormConstants(selectOptions) {
    selectOptions.positiveNegativeWithZero = [
        {value: "no", label: "no"},
        {value: "positive", label: "positive"},
        {value: "negative", label: "negative"}
    ];
}

function onRestFail(err) {
    alert("Endpoint responded with error: " + JSON.stringify(err)); //TODO
}
