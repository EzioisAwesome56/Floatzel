package com.eziosoft.floatzel.Objects;

import java.util.List;

public class Mod {

    private String type;
    private String mainclass;
    private List<String> mainclasses;

    public Mod(String type, String mainclass){
        this.type = type;
        this.mainclass = mainclass;
    }

    public Mod(){}

    public String getMainclass() {
        return mainclass;
    }

    public String getType() {
        return type;
    }

    public List<String> getMainclasses() {
        return mainclasses;
    }
}
