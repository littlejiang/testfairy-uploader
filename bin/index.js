#! /usr/bin/env node

var exec = require('child_process').execFile;
var arguments = process.argv.slice(2);
exec('cli/testfairy-uploader', arguments, function(err, stdout, stderr) {
  console.log(stdout);
  console.log(err);
  console.log(stderr);
});