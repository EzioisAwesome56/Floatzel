package com.eziosoft.floatzel.Exception;

public class DatabaseException extends Exception {
    StackTraceElement[] dbStackTrace = null;
    public DatabaseException(String message, StackTraceElement[] stacktrace){
        super(message);
        this.dbStackTrace = stacktrace;
    }
}
