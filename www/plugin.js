
var exec = require('cordova/exec');

var PLUGIN_NAME = 'IconChange';

var IconChange = {
  change: function (name, success, error) {
    exec(success, error, PLUGIN_NAME, "change", [name]);
  }, 
  getCurrent: function (success, error) {
    exec(success, error, PLUGIN_NAME, "getCurrent", []);
  }
};

module.exports = IconChange;