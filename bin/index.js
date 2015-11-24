#! /usr/bin/env node

var exec = require('child_process').execFile;
var os = require('os');
var arguments = process.argv.slice(2);
var script = (os.platform() == 'win32') ? 'cli/testfairy-uploader.bat' : 'cli/testfairy-uploader'

exec(script, arguments, function(err, stdout, stderr) {
  console.log(stdout);
  console.log(err);
  console.log(stderr);
});