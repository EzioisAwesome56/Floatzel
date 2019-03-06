// Floatzel Plugin API Support file
// Written by EzioisAwesome56
// contains various things used by the internal engine to check before runtime

// function to check permissions
function checkPermission(){
	if (ownerCmd == true){
		return false;
	} else if (adminCmd == true){
		return false;
	} else {
		return true;
	}
};

// used for checking if the permission needed is bot owner
function isOwner(){
	return ownerCmd;
};

// used for checking if command requires bot admin permissions
function isAdmin(){
	return adminCmd;
};

// the next 2 are used for getting plugin names and help text
function getName(){
	return name;
};

function getHelp(){
	return help;
};