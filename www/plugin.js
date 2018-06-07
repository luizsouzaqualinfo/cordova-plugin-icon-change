
var exec = require('cordova/exec');

var PLUGIN_NAME = 'IconChange';

var IconChange = {
  change: function (name) {
    exec(null, null, PLUGIN_NAME, "change", [name]);
  }, 
  getCurrent: function () {
    exec(null, null, PLUGIN_NAME, "getCurrent", []);
  }
};

module.exports = IconChange;