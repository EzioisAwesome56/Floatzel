package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.Objects.DatabaseModule;
import com.eziosoft.floatzel.Objects.User;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;


public class Database {
    /* a lot of this file is actually just one big compatibility layer
    instead of like, actually reworking the commands to use a new database layout
    i just wrote around all the old functions. You can see this a lot with stuff like dbinccount and dbdefaultsave
    these date all the way back to FloatzelDB days, and god, where those days absolutely horrible

    however, since i am extremely lazy, this will remain a compatibility layer. DatabaseModule may have more of what you're looking for

    I really shouldnt be put in charge of bot dev, huh
     */

    // put the new Database module here
    private static DatabaseModule dbdriver;

    public static void setDbdriver(DatabaseModule db){
        dbdriver = db;
    }
    public static void sendConninfo(String json){
        dbdriver.sendConninfo(json);
    }
    private static String bloanperm = "bloan";
    private static String stocktable = "stocks";
    private static String tweets = "tweets";
    private static String tagperm = "gtagperm";
    private static String tags = "tags";
    // new in 2.6: the ability to make floatzel not be an ass
    private static String ass = "ass";
    // rethink db!
    private static final RethinkDB r = RethinkDB.r;
    private static Connection thonk;
    private static Cursor cur = null;




    // init whatever driver was loaded
    public static void dbinit() {
        dbdriver.init();
    }



    // check if db entry exists
    public static Boolean dbcheckifexist(String id) throws DatabaseException{
        if (1 == 2){
            throw new DatabaseException("what", new StackTraceElement[]{new StackTraceElement("gtfo lol", "fuck", "dank", 2)});
        }
        return dbdriver.checkforuser(id);
    }

        // funtion to check if a guild has bought the tag command already
    public static boolean dbcheckiftag(String gid) throws DatabaseException{
        boolean exist = false;
        try{
            exist = (boolean) r.table(tagperm).filter(r.hashMap("gid", gid)).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return exist;
    }


    @Deprecated
    // function to write to a new db file (OLD: DO NOT USE)
    public static void dbsave(String id, String data) throws DatabaseException{
       // i dont know why this is still here, but just make it call dbsaveint for laziness
        try {
            Database.dbsaveint(id, Integer.valueOf(data));
        } catch (DatabaseException e){
            throw e;
        }
        return;
    }

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

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time) throws DatabaseException{
        User h = dbdriver.getProfile(id);
        h.setLastloan(time);
        dbdriver.saveProfile(h);
    }

    // return a long with the stored nano time
    public static long dbloadtime(String id) throws DatabaseException{
        User h = dbdriver.getProfile(id);
        return h.getLastloan();
    }

    @Deprecated // new permission checker coming soon
    public static boolean dbcheckbloan(String id) throws DatabaseException{
        return dbdriver.getProfile(id).getPerms()[0];
    }

    public static void dbbuycmd(int cmd, String uid) throws DatabaseException{
        String table = "";
            if (cmd == 1){
                table = bloanperm;
            }
            try {
                r.table(table).insert(r.hashMap("uid", uid)).run(thonk);
            } catch (ReqlError e){
                throw new DatabaseException(e.getMessage(), e.getStackTrace());
            }
        }

    // check to see if anything is in the table at all
    public static Boolean dbcheckstock() throws DatabaseException{
        if (dbdriver.totalStocks() > 0){
            return true;
        } else {
            return false;
        }
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void dbnewstock(int id, String name, int units, int price) throws DatabaseException{
        try {
            r.table(stocktable).insert(
                    r.array(
                            r.hashMap("sid", id)
                            .with("name", name)
                            .with("units", units)
                            .with("price", price)
                            .with("diff", 0)
                    )
            ).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // HOTFIX: deleting stocks
    public static void deletestock(int id) throws DatabaseException{
        try{
            r.table(stocktable).filter(row -> row.g("sid").eq(id)).delete().run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    @Deprecated
    public static void dbinccount(){
       // since rethink db isnt shit like sqlite, we dont need this anymore
        // as such, this function does nothing
        return;

    }
    public static int dbgetcount() throws DatabaseException{
        // one of the advatages of rethink: figuring out how many rows are in a table
        // thank lord!
        long total = 0;
        try {
            total = r.table(stocktable).count().run(thonk);
      } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Math.toIntExact(total);
    }
    public static void dbupdatestock(int id, boolean isbuy, int price, int diff, int unit) throws DatabaseException {
        try {
            if (!isbuy) {
                r.table(stocktable).filter(row -> row.g("sid").eq(id)).update(
                                r.hashMap("price", price)
                ).run(thonk);
                r.table(stocktable).filter(row -> row.g("sid").eq(id)).update(
                        r.hashMap("diff", diff)
                ).run(thonk);
            } else {
                r.table(stocktable).filter(row -> row.g("sid").eq(id)).update(
                        r.hashMap("units", unit)
                ).run(thonk);
            }
        } catch (ReqlError e) {
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // methood to get current price
    public static int dbgetprice(int id) throws DatabaseException{
        try {
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("price").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    // get the name of a stock
    public static String dbgetname(int id) throws DatabaseException{
        try {
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("name").run(thonk);
        } catch (ReqlError e) {
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Utils.getValue(cur);
    }


    // get dif
    public static int dbgetdiff(int id) throws DatabaseException{
        try{
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("diff").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    // get units
    public static int dbgetunits(int id) throws DatabaseException{
        try{
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("units").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    public static int dbcounttweets() throws DatabaseException{
        long total;
        try{
            total = r.table(tweets).count().run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return Math.toIntExact(total);
    }

    public static boolean dbsavetweet(String text, int id) throws DatabaseException{
        try {
            r.table(tweets).insert(r.array(
                    r.hashMap("tid", id)
                    .with("txt", text)
            )).run(thonk);
            return true;
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }

    }

    // insert the guild into the permission table
    public static void dbsettagperm(String gid) throws DatabaseException{
        try{
            r.table(tagperm).insert(
                    r.hashMap("gid", gid)
            ).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // checking if a tag exists
    public static boolean dbchecktag(String gid, String tname) throws DatabaseException{
        boolean exist = false;
        try {
            exist = (boolean) r.table(tags).filter(
                    row -> row.g("gid").eq(gid).and(
                            row.g("tname").eq(tname)
                    )
            ).distinct().count().gt(0).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return exist;
    }
    // used for saving tags to the database
    public static void dbsavetag(String gid, String tname, String tcont) throws DatabaseException{
        try {
            r.table(tags).insert(r.array(
                    r.hashMap("gid", gid)
                    .with("tname", tname)
                    .with("cont", tcont)
            )).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // used for loading tags
    public static String dbloadtag(String gid, String tname) throws DatabaseException{
        String tcont = "";
        try{
            cur = r.table(tags).filter(
                    row -> row.g("gid").eq(gid).and(
                            row.g("tname").eq(tname)
                    )
            ).getField("cont").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        tcont = Utils.getValue(cur);
        return tcont;
    }

    // check if the user has bought a stock yet
    public static boolean dbcheckifstock(String uid) throws DatabaseException{
        if (dbdriver.getProfile(uid).getStockid() == -1){
            return false;
        } else {
            return true;
        }
    }

    // validate a stock id
    public static boolean dbvalidatestockid(int id) throws DatabaseException{
        boolean exist = false;
        try{
            exist = (boolean) r.table(stocktable).filter(row -> row.g("sid").eq(id)).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return exist;
    }

    // buy a stock
    public static void dbbuystock(String uid, int id) throws DatabaseException{
        User h = dbdriver.getProfile(uid);
        h.setStockid(id);
        dbdriver.saveProfile(h);
    }

    // get the id of the stock a user bought
    public static int dbloadstockid(String uid) throws DatabaseException{
        return dbdriver.getProfile(uid).getStockid();
    }

    // deleting a user's entry on the table
    public static void dbdeletestock(String uid) throws DatabaseException{
        User h = dbdriver.getProfile(uid);
        h.setStockid(-1);
        dbdriver.saveProfile(h);
    }

    // get all tweets
    public static Cursor dbgetalltweets() throws DatabaseException{
        try{
            cur = r.table(tweets).getField("txt").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
            //Floatzel.fail = true;
            //return cur;
        }
        return cur;
    }

    // this is a command ment to be used by EVAL
    public static boolean dbmaketable(String name) throws DatabaseException{
        try {
            r.tableCreate(name).run(thonk);
            return true;
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // check if a server has set floatzel to be an asshole or not
    public static boolean dbcheckifass(String gid) throws DatabaseException{
        // check to see if an entry even exists in the table
        boolean exist = false;
        try{
           exist = (boolean) r.table(ass).filter(
                    r.hashMap("gid", gid)
            ).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        // does it even exist?
        if (!exist) {
            System.out.println("no option set for this GUILD!\nSetting option to NO!");
            try{
                r.table(ass).insert(r.array(
                        r.hashMap("gid", gid)
                                .with("option" , "false")
                )).run(thonk);
            } catch (ReqlError e){
                throw new DatabaseException(e.getMessage(), e.getStackTrace());
            }
            // since we just set the option to no, return false
            return false;
        }
        // if we made it here, the guild clearly has the option set, so load that option
        try {
            cur = r.table(ass).filter(row -> row.g("gid").eq(gid)).getField("option").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        // get the value from the cursor + return that
        return Boolean.valueOf(Utils.getValue(cur));
    }

    // setting the option
    public static void dbsetass(String gid, String option) throws DatabaseException{
        // do the things
        try {
            r.table(ass).filter(row -> row.g("gid").eq(gid)).update(r.hashMap("option", option)).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }


}
