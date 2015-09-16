var express = require('express');
var app = express();

var mongoose = require('mongoose');
var passport = require('passport');
var flash = require('connect-flash');

var configDB = require('./config/database.js');
var fs = require('fs');

if (process.env.OPENSHIFT_MONGODB_DB_PASSWORD) {
    mongoose.connect('mongodb://' +
        process.env.OPENSHIFT_MONGODB_DB_USERNAME + ':' +
        process.env.OPENSHIFT_MONGODB_DB_PASSWORD + '@' +
        process.env.OPENSHIFT_MONGODB_DB_HOST + ':' +
        process.env.OPENSHIFT_MONGODB_DB_PORT +
         process.env.OPENSHIFT_APP_NAME + '/project');
}
else {
    mongoose.connect(configDB.url);
}

app.use(express.static(__dirname + '/public'));
app.use('/uploads', express.static(__dirname + '/uploads'));

var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var session = require('express-session');

app.use(bodyParser.urlencoded({ extended: false }));

app.use(bodyParser.json());

require('./config/passport')(passport); // pass passport for configuration

app.use(cookieParser()); // read cookies (needed for auth)
app.set('view engine', 'ejs'); // set up ejs for templating

// required for passport
app.use(session({
    secret: 'mymovieapp',
    resave: false,
    saveUninitialized: true
}))

app.use(passport.initialize());
app.use(passport.session()); 
app.use(flash()); 

require('./app/routes.js')(app, passport); // load routes

//---------------------------------------------------------------------------------------------------------

app.get('/env', function (req, res) {
    res.send(process.env);
});

app.get('/movieapp', function (req, res) {
    res.sendFile('public/movieapp.html' , {"root": __dirname});
});

app.get('/like/:id/:email', function (req, res) {
    //console.log("Likefunction");
    var email = req.params.email;
    var id = req.params.id;

    var Like = require('./app/models/like');

    var like = new Like();
    like.useremail = email;
    like.movieid = id;

    Like.findOne({ useremail: email, movieid: id }, function (err, result) {
       // console.log(result);
        if (result == null) {
            like.save(function (err, like) {
                if (err) {
                    return console.error(err);
                }
                else {
                    //console.log("saved like");
                    res.send("Unlike");
                }
            });
        }
    });
});

app.get('/unlike/:id/:email', function (req, res) {

    //console.log("Unlikefunction");
    var email = req.params.email;
    var id = req.params.id;

    var Like = require('./app/models/like');

    var like = new Like();
    like.useremail = email;
    like.movieid = id;

    Like.findOne({ useremail: email, movieid: id }, function (err, result) {
        //console.log(result);
        if (result != null) {
            Like.remove({ useremail: email, movieid: id }, function (err) {
                if (err)
                {
                    return console.error(err);
                }
                else
                {
                    //console.log("removed like");
                    res.send("Like");
                }
            });
    }});
});

app.get('/checklikeUnlike/:id/:email', function (req, res) {

    //console.log("checklikeUnlike Function");
    var email = req.params.email;
    var id = req.params.id;

    var Like = require('./app/models/like');

    Like.findOne({ useremail: email, movieid: id }, function (err, result) {
        //console.log(result);
        if (result != null) {
            res.send("Unlike");
        }
        else
        {
            res.send("Like");
        }
    });
});

app.get('/comment/:id/:email/:userfirstname/:comment', function (req, res) {
    //console.log("commentfunction");
    var email = req.params.email;
    var userfirstname = req.params.userfirstname;
    var id = req.params.id;
    var comment = req.params.comment;
    var date = new Date();

    var Comment = require('./app/models/comment');

    var commentObj = new Comment();
    commentObj.useremail = email;
    commentObj.userFirstname = userfirstname;
    commentObj.movieid = id;
    commentObj.commentText = comment;
    commentObj.creationDate = date;

    commentObj.save(function (err, commentObj) {
        if (err) {
            return console.error(err);
        }
        else {
            console.log("saved comment");
        }
    });
});
 
app.get('/getComments/:id', function (req, res) {
    var id = req.params.id;
    var Comment = require('./app/models/comment');

    Comment.find({ movieid: id }).sort({ creationDate: -1 }).exec(
        function (err, result) {
           // console.log(result);
            res.send(result);
    });
});

app.get('/getLikes/:useremail', function (req, res) {
    var email = req.params.useremail;
    var Like = require('./app/models/like');

    Like.find({ useremail: email }).exec(
        function (err, result) {
            // console.log(result);
            res.send(result);
        });
});


app.delete("/deleteComment/:id", function (req, res) {

    var comment_id = req.params.id;
    var Comment = require('./app/models/comment');
    Comment.remove({ _id: comment_id }, function (err) {
        if (err)
        {
            console.log("error in deleting comment");
        }
        else
        {
            console.log("deleted comment");
        }
    });
});

app.get("/editComment/:id/:commentTextNew", function (req, res) {
    console.log("in edit comment");
    var comment_id = req.params.id;
    var commentTextNew = req.params.commentTextNew;
    var Comment = require('./app/models/comment');

    Comment.update({ _id: comment_id }, { $set: { commentText: commentTextNew } }, function (err) {
        console.log("comment updated"); if (err) {
            console.log("error in updating comment");
        }
        else {
            console.log("updated comment");
        }
    });
});

app.get("/getProfile/:useremail", function (req, res) {
   
    var useremail = req.params.useremail;
   
    var User = require('./app/models/user');
    console.log(useremail);
    User.findOne({ 'local.email': useremail }, function (err, result) {
        //console.log(result);
        if (result != null) {
            res.send(result);
        }
    });
});


app.delete("/deleteAccount/:useremail", function (req, res) {
    
    var useremail = req.params.useremail;
    console.log(useremail);
    var User = require('./app/models/user');
    var name;

    User.findOne({ 'local.email': useremail }, function (err, result) {
        //console.log(result);
        if (result != null) {
            name = result.local.name;
            console.log(name);
        }
    });
    
    User.find({ 'local.email': useremail }).remove(function (err)
    {
        if (err) {
            console.log(err);
            console.log("error in deleting account in user collection");
        }
        else {
            fs.unlink('uploads/' + name + useremail, function (err) {
                if (err) throw err;
                console.log('successfully deleted profile pic');
            });

            console.log("deleted account in user collection");
            var Follow = require('./app/models/follow');
            
                Follow.find({ useremailfollowed: useremail }).remove(function (err) {
                if (err) {
                    console.log("error in deleting account in follow collection");
                }
                else {
                    console.log("deleted account in follow collection");
                    var Like = require('./app/models/like');
                   
                    Like.find({ useremail: useremail }).remove(function (err) {
                        if (err) {
                            console.log("error in deleting account in like collection");
                        }
                        else {
                            console.log("deleted account in like collection");
                            res.send("deleted");
                        }
                    });
                }
            });
        }
    });
});

app.get("/followUser/:followerEmail/:followedEmail", function (req, res) {

    var followerEmail = req.params.followerEmail;
    var followedEmail = req.params.followedEmail;

    var Follow = require('./app/models/follow');

    var follow = new Follow();
    follow.useremailfollower = followerEmail;
    follow.useremailfollowed = followedEmail;
    
    Follow.findOne({ useremailfollower: followerEmail, useremailfollowed: followedEmail }, function (err, result) {
        console.log(result);
        if (result == null) {
            follow.save(function (err, like) {
                if (err) {
                    return console.error(err);
                }
                else {
                    //console.log("saved follow");
                    res.send("Unfollow");
                }
            });
        }
    });
});

app.get("/unfollowUser/:followerEmail/:followedEmail", function (req, res) {

    var followerEmail = req.params.followerEmail;
    var followedEmail = req.params.followedEmail;

    var Follow = require('./app/models/follow');

    Follow.findOne({ useremailfollower: followerEmail, useremailfollowed: followedEmail }, function (err, result) {
        console.log(result);
        if (result != null) {
            Follow.remove({useremailfollower: followerEmail, useremailfollowed: followedEmail},function (err, like) {
                if (err) {
                    return console.error(err);
                }
                else {
                    //console.log("saved follow");
                    res.send("Follow");
                }
            });
        }
    });
});

app.get('/checkfollowUnfollow/:followerEmail/:followedEmail', function (req, res) {

    console.log("checklikeUnlike Function");

    var followerEmail = req.params.followerEmail;
    var followedEmail = req.params.followedEmail;

    var Follow = require('./app/models/follow');

    Follow.findOne({ useremailfollower: followerEmail, useremailfollowed: followedEmail }, function (err, result) {
        console.log(result);
        if (result != null) {
            res.send("Unfollow");
        }
        else {
            res.send("Follow");
        }
    });
});

app.get('/getFollowedUsers/:followerEmail', function (req, res) {
    var usersFollowing = [];
    
    var followerEmail = req.params.followerEmail;
    var Follow = require('./app/models/follow');
    var User = require('./app/models/user');
    
    Follow.find({ useremailfollower: followerEmail }).exec(
        function (err, followedFound) {
            // console.log(result);
            if (followedFound != null) {
                followedFound.forEach (function (followedFounduser, index, followedFound)
                {
                    var query = User.where({ 'local.email': followedFounduser.useremailfollowed });
                    query.findOne(function (err, userFound) {
                        if (userFound != null) {

                            usersFollowing.push(userFound);
                            if (index == (followedFound.length - 1))
                            {
                                res.send(usersFollowing);
                            }
                        }

                    });
                })
            }
     });
});

//---------------------------------------------------------------------------------------------------------


var ipaddress = process.env.OPENSHIFT_NODEJS_IP || "127.0.0.1";
var port = process.env.OPENSHIFT_NODEJS_PORT || 8085;

app.listen(port, ipaddress);