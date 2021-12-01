package com.eziosoft.floatzel.Objects;

public class gameTexts {

    private String[] games;
    private String[] stupid;

    public gameTexts(){}

    public String[] getGames() {
        return games;
    }

    public void loadDefaults(){
        this.games = new String[]{"put cool games to play here!", "no games set in games.json"};
        this.stupid = new String[]{"funni joke mode text goes here"};
    }

    public String[] getStupid() {
        return stupid;
    }
}
