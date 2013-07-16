'use strict';

var formsDef = {};

//blocking request to read form metadata
$.ajax({
    async: false,
    dataType: "json",
    url: 'rest-mock/patient-form.json',
    success: (function(data) {
        window.formsDef["patient"] = data;
    })
});