package com.eziosoft.floatzel.Util;
import com.eziosoft.floatzel.Util.Database.*;

import java.util.Random;

public class StockUtil {

    private static Random random = new Random();
    public static boolean canstocks = true;


    public static boolean initStock(){
        // initilize the stock market values
        System.out.println("Floatzel is initilizing the stock market, please wait...");
        // test to see if theres anytnhing in the tables at all
        boolean issetup = Database.dbcheckstock();
        if (!issetup){
            // for the time being, create 1 stock
            Database.dbnewstock(1, "FLoatzel Industries", 100, 250);
            // thats all for now...the admin can add and remove stocks later
            Database.dbinccount();
            System.out.println("1 new stock created!\nInit complete");
            return true;
        } else {
            System.out.println("Initilization complete");
            return true;
        }
    }

    public static void updateStock(){
        // dont let people buy stocks during a market update...duh
        canstocks = false;
        System.out.println("Floatzel is now updating the stocks...");
        // setup the counters
        int stocks = Database.dbgetcount();
        int counter = 1;
        boolean isCrash;
        while (counter < stocks){
            // randomly pick if the stock will boom or crash
            int crash = random.nextInt(50);
            if (crash == 27){
                isCrash = true;
            }
            // load the price of the current stock
            int price = Database.dbgetprice(counter);
            // first, determine if a stock goes up or down in price
            int magic = random.nextInt(2);
            if (magic == 0){
                System.out.println("Stock id "+Integer.toString(counter)+" has no change!");
            } else if (magic == 1){
                // increase in stock price
            } else if (magic == 2){
                // decrease in stock price
            }
            counter++;
        }
        return;
    }
}
