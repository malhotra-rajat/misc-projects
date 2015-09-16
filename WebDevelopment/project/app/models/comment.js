var mongoose = require('mongoose');

var commentSchema = mongoose.Schema({
    useremail: String,
    userFirstname: String,
    movieid: String,
    commentText: String,
    creationDate: Date,
});

module.exports = mongoose.model('Comment', commentSchema);
