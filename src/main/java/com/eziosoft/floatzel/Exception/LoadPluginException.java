package com.eziosoft.floatzel.Exception;

public class LoadPluginException extends Exception {
    public LoadPluginException(String message){
        super("A fault has been detected while loading the plugin!\n" +
                "Error Message: "+ message);
    }
}
