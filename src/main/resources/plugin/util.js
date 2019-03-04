// Floatzel Plugin API: Utils import
// written by EzioisAwesome56 for floatzel
// makes plugins less reliant on understanding how JDA-utils works

// function to make sending messages less annoying as shit
function sendMessage(message){
	channel.sendMessage(message).queue();
};