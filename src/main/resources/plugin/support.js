// Floatzel Plugin API Support file
// Written by EzioisAwesome56
// contains various things used by the internal engine to check before runtime

// function to check permissions
function checkPermission(){
	if (ownerCmd){
		return false;
	} else if (adminCmd){
		return false;
	} else {
		return true;
	}
};