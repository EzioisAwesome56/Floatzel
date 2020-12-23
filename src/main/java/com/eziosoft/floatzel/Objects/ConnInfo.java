package com.eziosoft.floatzel.Objects;

public class ConnInfo {
    private String host;
    private int port;
    private String user;
    private String pass;

    public ConnInfo(){}
    public ConnInfo(String host, String user, String pass, int port){
        this.port = port;
        this.user = user;
        this.host = host;
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }
}
