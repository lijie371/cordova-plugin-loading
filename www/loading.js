var Loading = {
    show: function (text) {
    	text = text || "loading";
        cordova.exec(null, null, "Loading", "show", [text]);
    },
    hide: function () {
        cordova.exec(null, null, "Loading", "hide", []);
    }
};

module.exports = Loading;