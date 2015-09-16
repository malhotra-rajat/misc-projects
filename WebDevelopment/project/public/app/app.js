// create the module and name it movieApp
var movieApp = angular.module('movieApp', ['ngRoute', 'ngAnimate']);

movieApp.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
});

// configure our routes
movieApp.config(function ($routeProvider) {
    $routeProvider

        // route for the home page
        .when('/', {
            templateUrl: '../views/home.html',
            controller: 'homeController'
        })

        .when('/home/:reset', {
            templateUrl: '../views/home.html',
            controller: 'homeController'
        })

        // route for the movie page
        .when('/movie/:id', {
            templateUrl: '../views/movie.html',
            controller: 'movieController'
        })
        // route for the search page
        .when('/search/:query', {
            templateUrl: '../views/search.html',
            controller: 'searchController'
        })
        .when('/profile', {
            templateUrl: '../views/profile.html',
            controller: 'profileController'
        })
        .when('/about', {
            templateUrl: '../views/about.html',
            controller: 'aboutController'
        })
        .when('/profile/:email', {
            templateUrl: '../views/profile.html',
            controller: 'profileController'
        });
});

var apikey = "pmgdsyuxwn732frc64qptnxr";
var baseUrl = "http://api.rottentomatoes.com/api/public/v1.0";

//HOME CONTROLLER
var pageNumber = 1;
var listSize = 5;

movieApp.controller("homeController", ["$scope", "$routeParams", function ($scope, $routeParams) {

    var total = null;
    var moviesInTheatersUrl = baseUrl + '/lists/movies/in_theaters.json?page_limit=5&country=us&apikey=' + apikey;

    var upcomingMoviesUrl = baseUrl + '/lists/movies/upcoming.json?page_limit=8&page=1&country=us&apikey=' + apikey;


    $scope.display = function () {
        $("#message").empty();

        console.log($routeParams.reset);
        if ($routeParams.reset == 1) {
            pageNumber = 1;
            $("#message").empty();
            window.location.href = '#';
            return;
        }

        console.log(moviesInTheatersUrl + '&page=' + pageNumber);
        $.ajax({
            url: moviesInTheatersUrl + '&page=' + pageNumber,
            dataType: "jsonp",
            success: displayCallback
        });

        $.ajax({
            url: upcomingMoviesUrl,
            dataType: "jsonp",
            success: displayCallbackUpcoming
        });
    }

    function displayCallbackUpcoming(data) {
        $scope.upcomingmovies = data.movies;
        $scope.$apply();
    }

    function displayCallback(data) {

        $("#prvPageBtnHome").prop('disabled', false);
        $("#nxtPageBtnHome").prop('disabled', false);

        total = data.total;
        var onLastPage = (pageNumber == Math.floor((total / listSize) + 1));

        console.log(pageNumber);
        console.log(total);
        console.log(listSize);



        if (total == undefined || total == "" || total == null) {
            $("#message").append('No results found');
            $("#prvPageBtnHome").prop('disabled', true);
            $("#nxtPageBtnHome").prop('disabled', true);
        }
        else {
            if (total < listSize) { //only 1 page is there
                $("#message").append('Showing ' + ((pageNumber * listSize) - (listSize - 1)) + '-' + total + ' of ' + total + ' results');
                $("#prvPageBtnHome").prop('disabled', true);
                $("#nxtPageBtnHome").prop('disabled', true);
            }
            if (pageNumber == 1) {
                $("#prvPageBtnHome").prop('disabled', true);
            }
            if (onLastPage) {
                alert('onlastpage');
                $("#nxtPageBtnHome").prop('disabled', true);
                $("#message").append('Showing ' + ((pageNumber * listSize) - (listSize - 1)) + '-' + total + ' of ' + total + ' results');
            }
            else { //not on first page, last lage and there are at least 2 pages
                $("#message").append('Showing ' + ((pageNumber * listSize) - (listSize - 1)) + '-' + (pageNumber * listSize) + ' of ' + total + ' movies');
            }
            $scope.movies = data.movies;
            $scope.$apply();
            $("#results").show();
        }
    }
    $scope.previousPage = function () {
        if (pageNumber != 1) {
            pageNumber = pageNumber - 1;
            $scope.display();
        }
    }
    $scope.nextPage = function () {
        if (pageNumber != (total / listSize)) {
            pageNumber = pageNumber + 1;
            $scope.display();
        }
    }

    $("#results").hide();
    $scope.display();

}]);


//MOVIE CONTROLLER
movieApp.controller("movieController", ["$scope", "$routeParams", "$http", function ($scope, $routeParams, $http) {
    var id = $routeParams.id;
    var moviesDetailsUrl = 'http://api.rottentomatoes.com/api/public/v1.0/movies/' + id + '.json?apikey=' + apikey;

    var userEmail = $.cookie("userEmail");
    

    //console.log(moviesDetailsUrl);
    $scope.fetchMovieDetails = function () {
        $.ajax({
            url: moviesDetailsUrl,
            dataType: "jsonp",
            success: successCallback
        });
    }
    function successCallback(data) {
        $scope.movie_id = data.id;
        $scope.synopsis = data.synopsis;
        $scope.title = data.title;
        $scope.poster = data.posters.detailed;
        $scope.critics_score = data.ratings.critics_score;
        $scope.audience_score = data.ratings.audience_score;
        $scope.studio = data.studio;
        $scope.releaseDate = data.release_dates.theater;

        if (userEmail == undefined)
        {
            $("#likeUnlikeBtn").prop('disabled', true);
            $scope.likeUnlikeText = "Please login to Like!"
            $("#commentBtn").prop('disabled', true);
            $scope.commentBtnText = "Please login to Comment!"
            
        }
        else
        {
            $http.get("/checklikeUnlike/" + id + "/" + userEmail)
           .success(function (response) {
               $scope.likeUnlikeText = response;
           });
            $scope.commentBtnText = "Comment";
        }

        var castTemp = "";
        $.each(data.abridged_cast, function (idx, obj) {
          if (idx == (Object.keys(data.abridged_cast).length - 1))
          {
              castTemp = castTemp + obj.name;
          }
          else
          {
              castTemp = castTemp + obj.name + ", ";
          }
        });
        $scope.cast = castTemp;

        var directorsTemp = "";
        $.each(data.abridged_directors, function (idx, obj) {
            if (idx == (Object.keys(data.abridged_directors).length - 1)) {
                directorsTemp = directorsTemp + obj.name;
            }
            else
            {
                directorsTemp = directorsTemp + obj.name + ", ";
            }
        });
        $scope.directors = directorsTemp;

        $.ajax({
            url: data.links.similar + '?limit=5&apikey=' + apikey,
            dataType: "jsonp",
            success: function (response)
            {
                $scope.similarMovies = response.movies;
                $scope.$apply();
            }
        });
        $scope.getComments(data.id);
        $scope.$apply();
    }

    $scope.likeUnlike = function () {
       //var userEmail = $.cookie("userEmail");
       var id = $scope.movie_id;
       
       if ($scope.likeUnlikeText == "Like") {
           $http.get("/like/" + id + "/" + userEmail)
            .success(function (response) {
                console.log("Successfully liked");
                $scope.likeUnlikeText = response;
            });
        
       }
       else {
           $http.get("/unlike/" + id + "/" + userEmail)
            .success(function (response) {
                console.log("Successfully unliked");
                $scope.likeUnlikeText = response;
            });
       }
    }

    $scope.changeClass = function(likeUnlikeText)
    {
        if (userEmail == undefined)
        {
            return "btn btn-disabled";
        }
        else
        {
            if (likeUnlikeText == "Like")
                return "btn btn-success";
            if (likeUnlikeText == "Unlike")
                return "btn btn-danger";
        }
    }
    $scope.changeCommentBtnClass = function()
    {
        if (userEmail == undefined)
        {
            return "btn btn-disabled";
        }
        else
        { 
            return "btn btn-primary";
        }
    }

    $scope.comment = function () {
        //var userEmail = $.cookie("userEmail");
        var id = $scope.movie_id;
        var comment = $scope.commentText;

        var userFirstName = $.cookie("userName").split(' ')[0];
     
        $http.get("/comment/" + id + "/" + userEmail + "/" + userFirstName + "/" + comment)
           .success(function (response) {
               console.log("Successfully commented");
           });
        $scope.commentText = "";
        $scope.getComments(id);
    }

    $scope.getComments = function (movie_id) {
        //var id = $scope.movie_id;
        $http.get("/getComments/" + movie_id)
       .success($scope.renderComments);
    }
    
    $scope.renderComments = function (response) {
        $.each(response, function (idx, obj) {
            var date = moment(obj.creationDate).format("ddd, MMM Do YYYY");
            obj.creationDate = date;
            userEmail = $.cookie("userEmail");

            if (obj.useremail == userEmail)
            {
                obj.showHideEditDeleteBtns = true;
            }
            else
            {
                obj.showHideEditDeleteBtns = false;
            }
        });

        $scope.comments = response;
    
    };

    $scope.deleteComment = function (comment_id) {
        console.log(comment_id);
        var r = confirm("Are you sure you want to delete the comment?");
        if (r == true) {
            $http.delete("/deleteComment/" + comment_id)
                .success(function (response) {
                    console.log("comment deleted");
                });
            $scope.getComments($scope.movie_id);
        } 
    }

    var comment_idModal;

    $scope.editComment = function (comment_idOpen, commentTextOld) {

        console.log("updating comment");
        $("#newComment").val(commentTextOld);
        comment_idModal = comment_idOpen;
        $scope.getComments($scope.movie_id);
    }

    $scope.submitUpdatedComment = function ()
    {
        var commentTextNew = $("#newComment").val();
        $http.get("/editComment/" + comment_idModal + "/" + commentTextNew)
             .success(function (response) {
                 console.log("comment updated");
             });
        $scope.getComments($scope.movie_id);
    }
    $scope.fetchMovieDetails();
}]);


// SEARCH CONTROLLER
var pageNumberSearch = 1;
var listSizeSearch = 25;

movieApp.controller("searchController", ["$scope", "$routeParams", function ($scope, $routeParams) {

    var totalSearch = null;
    var moviesSearchUrl = baseUrl + '/movies.json?apikey=' + apikey + '&page_limit=' + listSizeSearch;

    var query = $routeParams.query;
    console.log(query);

    $scope.search = function () {
        $("#message").empty();
        console.log(moviesSearchUrl + '&q=' + encodeURI(query) + '&page=' + pageNumberSearch);
        if (query == "" || query == null || query == undefined) {
            alert("Please enter something in the search box.");
        }
        else {
            // send off the query
            $.ajax({
                url: moviesSearchUrl + '&q=' + encodeURI(query) + '&page=' + pageNumberSearch,
                dataType: "jsonp",
                success: searchCallback
            });
        }
    }

    // callback for when we get back the results
    function searchCallback(data) {
        totalSearch = data.total;
        if (totalSearch == undefined || totalSearch == "" || totalSearch == null) {
            $("#message").append('No results found for: ' + query);
        }
        else {

            if (totalSearch < listSizeSearch) //only 1 page is there
            {
                $("#message").append('Showing ' + ((pageNumberSearch * listSizeSearch) - (listSizeSearch - 1)) + '-' + totalSearch + ' of ' + totalSearch + ' results for ' + "'" + query + "'");
            }
   
            else //not on first page, last lage and there are at least 2 pages
            {
                $("#message").append('Showing the top ' + listSizeSearch + ' results for ' + "'" + query + "'");
            }

            $scope.movies = data.movies;
            $scope.$apply();
            $("#results").show();
        }
    }

    $scope.search();
}]);

movieApp.controller("profileController", ["$scope", "$routeParams", "$http", function ($scope, $routeParams, $http) {

    $scope.getProfile = function () {
        var userEmail = $.cookie("userEmail");
        var emailToFetch = $routeParams.email;
        
        $http.get("/getProfile/" + emailToFetch)
         .success(function (response) {
             //$scope.profilePic = "../img/profile_placeholder.png";
             $scope.aboutme = response.local.aboutme;
             $scope.name = response.local.name;
             $scope.email = response.local.email;

             //$scope.profilePic = "/uploads/" + response.local.name + response.local.email;
             $scope.profilePic = response.local.profilePicUrl;
             console.log($scope.profilePic);
             console.log(response);
             if (response.local.email == userEmail) {
                 $scope.showDeleteAccountButton = true;
                 $scope.showFollowButton = false;
             }
             else {
                 $scope.showDeleteAccountButton = false;
                 $scope.showFollowButton = true;
                 if (userEmail == undefined) {
                     $("#followBtn").prop('disabled', true);
                     $scope.followBtnText = "Please login to follow!"
                 }
                 else
                 {
                    $http.get("/checkfollowUnfollow/" + userEmail + "/" + emailToFetch)
                        .success(function (response) {
                            $scope.followBtnText = response;
                        });
                 }
             }
         });
    }


    $scope.followUnfollow = function () {
        var userEmail = $.cookie("userEmail");
        var emailToFetch = $routeParams.email;

        if ($scope.followBtnText == "Follow") {
            $http.get("/followUser/" + userEmail + "/" + emailToFetch)
             .success(function (response) {
                 console.log("Successfully followed");
                 $scope.followBtnText = response;
             });
            
        }
        else {
            $http.get("/unfollowUser/" + userEmail + "/" + emailToFetch)
             .success(function (response) {
                 console.log("Successfully unfollowed");
                 $scope.followBtnText = response;
             });
        }
    }

    $scope.changeFollowBtnClass = function (followBtnText) {
        var userEmail = $.cookie("userEmail");
        if (userEmail == undefined) {
            return "btn btn-disabled";
        }
        else
        {
            if (followBtnText == "Follow")
                return "btn btn-success";
            if (followBtnText == "Unfollow")
                return "btn btn-danger";
        }
    }

    $scope.deleteAccount = function () {
        var userEmail = $.cookie("userEmail");

        var r = confirm("Are you sure you want to delete your account?");
        if (r == true) {
            $http.delete("/deleteAccount/" + userEmail)
                .success(function (response) {
                    if (response == "deleted")
                    {
                        alert("Account deleted! We are sorry to see you go :(. ");
                    }
                    window.location.href = '/logout';
                });
            $scope.getComments($scope.movie_id);
        }
    }

    $scope.getLikedMovies = function()
    {
        $scope.likedMovies = [];
        var emailToFetch = $routeParams.email;
        
        $http.get("/getLikes/" + emailToFetch)
             .success(function (response) {
                 if (response != null || response != undefined)
                 {
                     $.each(response, function (idx, obj) {
                        
                         var movie_id = obj.movieid;

                         var moviesDetailsUrlLikes = 'http://api.rottentomatoes.com/api/public/v1.0/movies/' + movie_id + '.json?apikey=' + apikey;

                         $.ajax({
                                 url: moviesDetailsUrlLikes,
                                 dataType: "jsonp",
                                 success: getLikesCallback
                             });
                     });
                 }
             });
    }

    function getLikesCallback(movie_data) {
        $scope.likedMovies.push(movie_data);
        $scope.$apply();
    }


    $scope.getFollowedUsers = function () {
        $scope.followedUsers = [];
        var emailToFetch = $routeParams.email;

        $http.get("/getFollowedUsers/" + emailToFetch)
             .success(getFollowedCallback);
    }

    function getFollowedCallback(response) {
        if (response != null || response != undefined) {
            console.log(response);
            angular.forEach(response, function (obj) {
                $scope.followedUsers.push(obj);
            });
        }
    }

    $scope.getProfile();
    $scope.getLikedMovies();
    $scope.getFollowedUsers();
}]);

movieApp.controller("aboutController", ["$scope", function ($scope) {}]);