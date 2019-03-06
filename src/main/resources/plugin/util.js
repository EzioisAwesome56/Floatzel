// Floatzel Plugin API: Utils import
// written by EzioisAwesome56 for floatzel
// makes plugins less reliant on understanding how JDA-utils works

// function to make sending messages less annoying as shit
function sendMessage(message){
	channel.sendMessage(message).queue();
};

// convert a uint8array from a string
function getString(b){
	with (imports){
		return com.eziosoft.floatzel.Util.Plugin.attachTostring(b);
	}
}

// convert uint8array to string to send to java
function convertArray(what){
	return StringView.bytesToBase64(what);
};

// interface with java to get a byte array back
function sendFile(input, filename, event){
	with (imports){
		return com.eziosoft.floatzel.Util.Plugin.JSFileSend(input, event, filename);
	}
};