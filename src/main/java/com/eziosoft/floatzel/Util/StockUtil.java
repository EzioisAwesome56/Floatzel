package com.eziosoft.floatzel.Util;
import com.eziosoft.floatzel.Util.Database.*;

import java.util.Random;

public class StockUtil {

    private Random random = new Random();


    public static boolean initStock(){
        // initilize the stock market values
        System.out.println("Floatzel is initilizing the stock market, please wait...");
        // test to see if theres anytnhing in the tables at all
        boolean issetup = Database.dbcheckstock();
        if (!issetup){
            // for the time being, create 1 stock
            Database.dbnewstock(1, "FLoatzel Industries", 100, 250);
            // thats all for now...the admin can add and remove stocks later
            System.out.println("1 new stock created!\nInit complete");
            return true;
        } else {
            System.out.println("Initilization complete");
            return true;
        }
    }
}
