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

// get a uint8array from string
function stringToBytes(string){
	return new TextEncoder("ISO-8859-1").encode(string);
}

// get a byte[] from input streams
function getAttachByte(){
	with (imports){
		return IOUtils.toByteArray(message.getAttachments().get(0).getInputStream());
	}
};

// convert uint8array to string to send to java
function convertArray(what){
	return new TextDecoder("ISO-8859-1").decode(what);
};

// interface with java to get a byte array back
function getFileBytes(input){
	with (imports){
		return com.eziosoft.floatzel.Util.Plugin.stringToByte(input);
	}
};