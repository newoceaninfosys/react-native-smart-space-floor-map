var fs = require('fs');
var fse = require('fs-extra');
var path = require('path');

var dist = path.join(__dirname, 'android/release');
var release_template = path.join(__dirname, 'android/release_template');
var src = path.join(__dirname, 'android/app/src');

try {
  fse.removeSync(dist);
  fs.mkdirSync(dist);
  fse.copySync(release_template, dist);
  fse.copySync(src, path.join(dist, 'src'));
  console.log('success!')
} catch (err) {
  console.error(err)
}