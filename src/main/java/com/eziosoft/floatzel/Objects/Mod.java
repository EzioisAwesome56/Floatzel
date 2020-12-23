package com.eziosoft.floatzel.Objects;

public class Mod {

    private String type;
    private String mainclass;

    public Mod(String type, String mainclass){
        this.type = type;
        this.mainclass = mainclass;
    }

    public String getMainclass() {
        return mainclass;
    }

    public String getType() {
        return type;
    }
}
