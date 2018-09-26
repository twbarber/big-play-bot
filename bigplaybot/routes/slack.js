var express = require('express');
var router = express.Router();

router.get('/oauth', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/add', function(req, res, next) {
  res.sendFile('add_to_slack.html', { root : './public' });
});

module.exports = router;