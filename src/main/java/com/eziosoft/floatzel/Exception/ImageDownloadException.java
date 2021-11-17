package com.eziosoft.floatzel.Exception;

public class ImageDownloadException extends Exception{
    private int statuscode;

    public ImageDownloadException(int code, String message){
        super(message);
        this.statuscode = code;
    }

    public ImageDownloadException(int code, String msg, Throwable c){
        super(msg, c);
        this.statuscode = code;
    }

    public int getStatuscode() {
        return statuscode;
    }
}
