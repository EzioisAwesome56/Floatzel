package com.eziosoft.floatzel.Util;

public class StockUtil {

    public static boolean initStock(){
        // initilize the stock market values
        System.out.println("Floatzel is initilizing the stock market, please wait...");
        // test to see if theres anytnhing in the tables at all
        boolean issetup = Database.dbcheckstock();
        if (!issetup){

        } else {
            System.out.println("Initilization complete");
            return true;
        }
        return false;
    }
}
