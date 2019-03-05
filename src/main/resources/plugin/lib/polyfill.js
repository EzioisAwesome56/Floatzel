// nashorn polyfill.js
// adds some shit from node that isnt present
// from: https://www.n-k.de/riding-the-nashorn/

var global = this;
var window = this;
var process = {env:{}};

var console = {};
console.debug = print;
console.log = print;
console.warn = print;
console.error = print;