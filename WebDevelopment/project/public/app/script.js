function appController($scope, $http) {
    $scope.search = function () {
        var query = $scope.queryText;
        if (query == "" || query == null || query == undefined) {
            alert("Please enter something in the search box.");
        }
        else {
            // send off the query
            window.location.href = '#/search/' + query;
        }
    }
    $scope.resetPage = function () {
        var reset = 1;
        window.location.href = '#/home/' + reset;
    }

    $scope.showHideProfileBtn = function () {
        var userEmail = $.cookie("userEmail");
        if (userEmail != undefined) {
            $scope.profileBtnVisible = true;
            $('#profileBtn').attr("href", "#/profile/" + userEmail);
        }
        else {
            $scope.profileBtnVisible = false;
        }
    }
    $scope.showHideProfileBtn();
}

$(document).ready(function () {
    var userEmail = $.cookie("userEmail");
    var userName = $.cookie("userName");

    if (userEmail != undefined) {
        $('#logInOutBtn').text("Logout");
        $('#nameEmail').text("Hi, " + userName.split(' ')[0]);
        $('#logInOutBtn').attr("href", "/logout");
    }
    else {
        $('#logInOutBtn').text("Login");
        $('#logInOutBtn').attr("href", "/login");

    }
});