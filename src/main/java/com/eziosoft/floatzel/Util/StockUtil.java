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
        boolean isSuperCrash;
        while (counter <= stocks){
            // randomly pick if the stock will boom or crash
            int crash = random.nextInt(50);
            if (crash == 27){
                isCrash = true;
                isSuperCrash = false;
            } else if (crash == 49){
                // uh oh. A super crash/boom is happening
                isSuperCrash = true;
                isCrash = false;
            } else {
                isCrash = false;
                isSuperCrash = false;
            }
            // generate the amount to add or subtract from the stock price
            int change = (random.nextInt(24)) + 1;
            // double it if its a crash/boom
            if (isCrash){
                change = change * 2;
            } else if (isSuperCrash){
                change = (change * 2) * 3;
            }
            // load the price of the current stock
            int price = Database.dbgetprice(counter);
            // first, determine if a stock goes up or down in price
            int magic = random.nextInt(5);
            if (magic == 0){
                System.out.println("Stock id "+Integer.toString(counter)+" has no change!");
            } else if (magic == 1 || magic == 4){
                // increase in stock price
                price = price + change;
            } else if (magic == 2 || magic == 5){
                price = price - change;
                if (price < 0){
                    price = 1;
                }
            }
            // finally, save it back to the database
            Database.dbupdatestock(counter, false, price, change, -2);
            // and then add 1 to the counter
            counter++;
        }
        // if we are here, we should be done
        System.out.println("Floatzel stock updater has completed!");
        // let people use stocks again
        canstocks = true;
        return;
    }
}
