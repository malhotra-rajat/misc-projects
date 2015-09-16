module.exports = function(app, passport) {

    var multer = require('multer');
    app.use(multer({
        dest: "./uploads/"
    }));

	app.get('/login', function(req, res) {
		res.render('login.ejs', { message: req.flash('loginMessage') });
	});

	// process the login form
	app.post('/login', passport.authenticate('local-login', {
		successRedirect : '/success', // redirect to the secure profile section
		failureRedirect : '/login', // redirect back to the signup page if there is an error
		failureFlash : true // allow flash messages
	}));

	// show the signup form
	app.get('/signup', function(req, res) {

		// render the page and pass in any flash data if it exists
		res.render('signup.ejs', { message: req.flash('signupMessage') });
	});

	// process the signup form
	app.post('/signup', passport.authenticate('local-signup', {
	    successRedirect: '/login', 
		failureRedirect : '/signup', // redirect back to the signup page if there is an error
		failureFlash: true // allow flash messages
	}));

	
	// we will want this protected so you have to be logged in to visit
	// we will use route middleware to verify this (the isLoggedIn function)
	app.get('/success', isLoggedIn, function(req, res) {
	    res.cookie('userEmail', req.user.local.email, { maxAge: 2592000000 });
	    res.cookie('userName', req.user.local.name, { maxAge: 2592000000 });
        res.redirect('/movieapp')
	});

	app.get('/logout', function (req, res) {
	    req.logout();
	    res.clearCookie('userName');
	    res.clearCookie('userEmail');
		res.redirect('/movieapp');
	});
};

// route middleware to make sure
function isLoggedIn(req, res, next) {

	// if user is authenticated in the session, carry on
	if (req.isAuthenticated())
		return next();

	// if they aren't redirect them to the home page
	res.redirect('/');
}
