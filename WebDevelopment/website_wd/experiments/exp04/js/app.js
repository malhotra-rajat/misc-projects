// app.js
var validationApp = angular.module('validationApp', []);
validationApp.controller('mainController', function ($scope) {
    $scope.submitForm = function (isValid) {
        if (isValid) {
            alert('All fields validated in the form');
        }
    };
});
