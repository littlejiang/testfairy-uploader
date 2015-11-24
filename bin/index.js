#! /usr/bin/env node

var exec = require('child_process').execFile;
var path = require('path')

var arguments = process.argv.slice(2);
var script = (process.platform == 'win32') ? './cli/testfairy-uploader.bat' : './cli/testfairy-uploader'
var scriptPath = path.join(__dirname, '../', script);

exec(scriptPath, arguments, function(err, stdout, stderr) {
  console.log(stdout);
  console.log(err);
  console.log(stderr);
});