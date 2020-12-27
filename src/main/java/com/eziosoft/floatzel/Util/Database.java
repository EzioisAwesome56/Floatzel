package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.DatabaseModule;
import com.eziosoft.floatzel.Objects.Stock;
import com.eziosoft.floatzel.Objects.Tweet;
import com.eziosoft.floatzel.Objects.User;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;


public class Database {
    // most of this file is Deprecated, old code. Do not use it
    // instead, interface with the provided dbdriver object to get what you need from the database
    public static DatabaseModule dbdriver;

    public static void sendConninfo(String json){
        dbdriver.sendConninfo(json);
    }

    /* all of whats left of this file is actually just one big compatibility layer
    instead of like, actually reworking the commands to use a new database layout
    i just wrote around all the old functions. You can see this a lot with stuff like dbinccount and dbdefaultsave
    these date all the way back to FloatzelDB days, and god, where those days absolutely horrible

    however, since i am extremely lazy, this will remain a compatibility layer. DatabaseModule may have more of what you're looking for

    I really shouldnt be put in charge of bot dev, huh
     */
    private static String bloanperm = "bloan";
    private static String stocktable = "stocks";
    private static String tweets = "tweets";
    // rethink db!
    private static final RethinkDB r = RethinkDB.r;
    private static Connection thonk;
    private static Cursor cur = null;




    // init whatever driver was loaded
    public static void dbinit() {
        dbdriver.init();
    }



    @Deprecated
    // check if db entry exists
    public static Boolean dbcheckifexist(String id) throws DatabaseException{
        return dbdriver.checkforuser(id);
    }


    @Deprecated
    // function to write to a new db file (OLD: DO NOT USE)
    public static void dbsave(String id, String data) throws DatabaseException{
       // i dont know why this is still here, but just make it call dbsaveint for laziness
        Database.dbsaveint(id, Integer.parseInt(data));
    }

    @Deprecated
    // function to save to bank accounts as ints
    public static void dbsaveint(String id, int data) throws DatabaseException {
        // first, load user profile
        User h = dbdriver.getProfile(id);
        // run thru old checks

        // fix for overflow bug
        if (data < -100){
            data = Integer.MAX_VALUE;
        }

        // update profile
        h.setBal(data);
        // save it to db
        dbdriver.saveProfile(h);
    }

    @Deprecated
    // load an integer from a db entry
    public static int dbloadint(String id) throws DatabaseException {
        return dbdriver.getProfile(id).getBal();

    }

    @Deprecated
    // check if the user has a loan out
    public static boolean dbcheckifloan(String id) throws DatabaseException{
        return dbdriver.checkforuser(id);
    }

    @Deprecated
    // default saver
    public static void dbdefaultsave(String id, int location) throws GenericException, DatabaseException {
        //  simplify this function as it serves only 1 purpose
        if (location == 2){
            // run the loan saver
            try {
                Database.sqlblankloan(id);
            } catch (DatabaseException e){
                throw e;
            }
            return;
        } else {
            throw new GenericException("No arguments provided!");
        }
    }

    @Deprecated
    // sql fucntion to write a 0 to a new loan entry
    private static void sqlblankloan(String id) throws DatabaseException{
        return;
    }

    @Deprecated
    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time) throws DatabaseException{
        User h = dbdriver.getProfile(id);
        h.setLastloan(time);
        dbdriver.saveProfile(h);
    }

    @Deprecated
    // return a long with the stored nano time
    public static long dbloadtime(String id) throws DatabaseException{
        User h = dbdriver.getProfile(id);
        return h.getLastloan();
    }

    @Deprecated // new permission checker coming soon
    public static boolean dbcheckbloan(String id) throws DatabaseException{
        return dbdriver.getProfile(id).getPerms()[0];
    }

    @Deprecated
    public static void dbbuycmd(int cmd, String uid) throws DatabaseException{
        dbdriver.setPerm(uid, cmd);
    }

    @Deprecated
    // check to see if anything is in the table at all
    public static Boolean dbcheckstock() throws DatabaseException{
        return dbdriver.totalStocks() > 0;
    }


    @Deprecated
    // sql fucntion to write a 0 to a new loan entry
    public static void dbnewstock(int id, String name, int units, int price) throws DatabaseException{
        Stock s = new Stock(id, name, units, price, 0);
        dbdriver.createNewStock(s);
    }

    @Deprecated
    // HOTFIX: deleting stocks
    public static void deletestock(int id) throws DatabaseException{
        dbdriver.deleteStock(id);
    }

    @Deprecated
    public static void dbinccount(){
       // since rethink db isnt shit like sqlite, we dont need this anymore
        // as such, this function does nothing
        return;

    }

    @Deprecated
    public static int dbgetcount() throws DatabaseException{
        return dbdriver.totalStocks();
    }

    @Deprecated
    public static void dbupdatestock(int id, boolean isbuy, int price, int diff, int unit) throws DatabaseException {
        Stock s = dbdriver.loadStock(id);
        int newunit;
        int newprice;
        int newdiff;
        if (isbuy){
            newunit = unit;
            newprice = s.getPrice();
            newdiff = s.getDiff();
        } else {
            newunit = s.getUnits();
            newprice = price;
            newdiff = diff;
        }
        s = new Stock(s.getId(), s.getName(), newunit, newprice, newdiff);
        dbdriver.updateStock(s);
    }

    @Deprecated
    // methood to get current price
    public static int dbgetprice(int id) throws DatabaseException{
        return dbdriver.loadStock(id).getPrice();
    }

    @Deprecated
    // get the name of a stock
    public static String dbgetname(int id) throws DatabaseException{
        return dbdriver.loadStock(id).getName();
    }


    @Deprecated
    // get dif
    public static int dbgetdiff(int id) throws DatabaseException{
        return dbdriver.loadStock(id).getDiff();
    }

    @Deprecated
    // get units
    public static int dbgetunits(int id) throws DatabaseException{
        return dbdriver.loadStock(id).getUnits();
    }

    @Deprecated
    public static int dbcounttweets() throws DatabaseException{
        return dbdriver.totalTweets();
    }

    @Deprecated
    public static boolean dbsavetweet(String text, int id) throws DatabaseException{
        dbdriver.saveTweet(new Tweet(text, id));
        return true;
    }

    @Deprecated
    // check if the user has bought a stock yet
    public static boolean dbcheckifstock(String uid) throws DatabaseException{
        return dbdriver.getProfile(uid).getStockid() != -1;
    }

    @Deprecated
    // validate a stock id
    public static boolean dbvalidatestockid(int id) throws DatabaseException{
        return dbdriver.checkForStock(id);
    }

    @Deprecated
    // buy a stock
    public static void dbbuystock(String uid, int id) throws DatabaseException{
        User h = dbdriver.getProfile(uid);
        h.setStockid(id);
        dbdriver.saveProfile(h);
    }

    @Deprecated
    // get the id of the stock a user bought
    public static int dbloadstockid(String uid) throws DatabaseException{
        return dbdriver.getProfile(uid).getStockid();
    }

    @Deprecated
    // deleting a user's entry on the table
    public static void dbdeletestock(String uid) throws DatabaseException{
        User h = dbdriver.getProfile(uid);
        h.setStockid(-1);
        dbdriver.saveProfile(h);
    }

    @Deprecated // this method totally fucking sucks
    // so we'll fix the one method that actually calls it directly
    public static Cursor dbgetalltweets() throws DatabaseException, NoSuchMethodException {
        throw new NoSuchMethodException("Method is deprecated!");
    }

    @Deprecated
    // this is a command ment to be used by EVAL
    public static boolean dbmaketable(String name, String key) throws DatabaseException{
       dbdriver.makeTable(name, key);
       return false;
    }

    @Deprecated // assume true for the very little implemented code
    // check if a server has set floatzel to be an asshole or not
    public static boolean dbcheckifass(String gid) throws DatabaseException{
        return true;
    }

    @Deprecated
    // setting the option
    public static void dbsetass(String gid, String option) throws DatabaseException{
        // since im censoring floatzel entirely, this does nothing
        return;
    }


}
