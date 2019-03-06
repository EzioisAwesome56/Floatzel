package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Exception.DatabaseException;
import com.eziosoft.floatzel.Exception.GenericException;
import com.eziosoft.floatzel.Floatzel;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;


public class Database {
    // tables
    private static String banktable = "bank";
    private static String loantable = "loan";
    private static String bloanperm = "bloan";
    private static String stocktable = "stocks";
    private static String stockbuy = "boughtstock";
    private static String tweets = "tweets";
    private static String tagperm = "gtagperm";
    private static String tags = "tags";
    // rethink db!
    private static final RethinkDB r = RethinkDB.r;
    private static Connection thonk;
    private static Cursor cur = null;




    // check if folder exist
    public static void dbinit() {
        Connection.Builder builder = r.connection().hostname("localhost").port(28015);
        if (Floatzel.conf.dbUser() != null) {
            builder.user(Floatzel.conf.dbUser(), Floatzel.conf.dbPass() != null ? Floatzel.conf.dbPass() : "");
        } else {
            builder.user("admin", Floatzel.conf.dbPass() != null ? Floatzel.conf.dbPass() : "");
        }

        thonk = builder.connect();
        System.out.println("Floatzel is starting RethinkDB...");
        // first, check if the database file exists
        if (!(boolean) r.dbList().contains("floatzel").run(thonk)){
            System.out.println("No database found! Creating a new db!");
            // okay, it hasnt been initalized yet, so do that
            r.dbCreate("floatzel").run(thonk);
            thonk.use("floatzel");
            System.out.println("Creating tables...");
            Database.makeTables();
        } else {
            // set the default db for rethonk
            thonk.use("floatzel");
            System.out.println("ReThinkDB started!");
        }
        return;
    }

    // create all the tables!
    private static void makeTables(){
        // run a bunch of rethink commands
        r.tableCreate(banktable).run(thonk);
        r.tableCreate(loantable).run(thonk);
        r.tableCreate(bloanperm).run(thonk);
        r.tableCreate(stocktable).run(thonk);
        r.tableCreate(tweets).run(thonk);
        r.tableCreate(tagperm).run(thonk);
        r.tableCreate(tags).run(thonk);
        r.tableCreate(stockbuy).run(thonk);
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id) throws DatabaseException{
            boolean exist = false;
            // connection shit
            try {
                exist = (boolean) r.table(banktable).filter(
                        r.hashMap("uid", id)
                ).count().eq(1).run(thonk);
            } catch (ReqlError e){
                throw new DatabaseException(e.getMessage(), e.getStackTrace());
            }
            if (!exist){
                // the user does not have a bank account
                // make one instead!
                try {
                    r.table(banktable).insert(r.array(
                            r.hashMap("uid", id)
                                    .with("bal", 0)
                    )).run(thonk);
                } catch (ReqlError e){
                    throw new DatabaseException(e.getMessage(), e.getStackTrace());
                }
                return exist;
            } else {
                return exist;
            }
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


    // function to write to a new db file (OLD: DO NOT USE)
    public static void dbsave(String id, String data) throws DatabaseException{
       // i dont know why this is still here, but just make it call dbsaveint for laziness
        int number = Integer.valueOf(data);
        try {
            Database.dbsaveint(id, number);
        } catch (DatabaseException e){
            throw e;
        }
        return;
    }

    // function to save to bank accounts as ints
    public static void dbsaveint(String id, int data) throws DatabaseException {
        // fix for overflow bug
        if (data < -100){
            data = Integer.MAX_VALUE;
        }
        // do the things
        try {
            r.table(banktable).filter(row -> row.g("uid").eq(id)).update(r.hashMap("bal", data)).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // load an integer from a db entry
    public static int dbloadint(String id) throws DatabaseException {
        String result;
            try {
                cur = r.table(banktable).filter(row -> row.g("uid").eq(id)).getField("bal").run(thonk);
            } catch (ReqlError e){
                throw new DatabaseException(e.getMessage(), e.getStackTrace());
            }
            // do json things
        // return value of bal
        result = Utils.getValue(cur);
        return Integer.valueOf(result);

        }

    // check if the user has a loan out
    public static boolean dbcheckifloan(String id) throws DatabaseException{
        boolean exist = false;
        // connection shit
        try {
            exist = (boolean) r.table(loantable).filter(
                    r.hashMap("uid", id)
            ).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        if (!exist){
            return exist;
        } else {
            return exist;
        }
        }

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

    // sql fucntion to write a 0 to a new loan entry
    private static void sqlblankloan(String id) throws DatabaseException{
        // insert a thong into the loan table
        try {
            r.table(loantable).insert(r.array(
                    r.hashMap("uid", id)
                            .with("time", Long.toString(0L))
            )).run(thonk);
            return;
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time) throws DatabaseException{
        try {
            r.table(loantable).filter(row -> row.g("uid").eq(id)).update(r.hashMap("time", Long.toString(time))).run(thonk);
            return;
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
            }

    // return a long with the stored nano time
    public static long dbloadtime(String id) throws DatabaseException{
        String result;
        try {
            cur = r.table(loantable).filter(row -> row.g("uid").eq(id)).getField("time").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        result = Utils.getValue(cur);
        return Long.valueOf(result);
    }

    public static boolean dbcheckbloan(String id) throws DatabaseException{
        // sql statement to check for this shit
        boolean exist = false;
        try {
            exist = (boolean) r.table(bloanperm).filter(row -> row.g("uid").eq(id)).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        // then just return the result
            return exist;
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
        boolean exist = false;
        long total;
        try{
            total = r.table(stocktable).count().run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        int realtotal = Math.toIntExact(total);
        if (realtotal >= 1){
            exist = true;
        }
        return exist;
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
        boolean exist = false;
        try{
            exist = (boolean) r.table(stockbuy).filter(row -> row.g("uid").eq(uid)).count().eq(1).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        return exist;
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
        try{
            r.table(stockbuy).insert(
                    r.hashMap("uid", uid)
                    .with("sid", id)
            ).run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
    }

    // get the id of the stock a user bought
    public static int dbloadstockid(String uid) throws DatabaseException{
        try{
            cur = r.table(stockbuy).filter(row -> row.g("uid").eq(uid)).getField("sid").run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
        int id = Integer.valueOf(Utils.getValue(cur));
        return id;
    }

    // deleting a user's entry on the table
    public static void dbdeletestock(String uid) throws DatabaseException{
        try {
            r.table(stockbuy).filter(row -> row.g("uid").eq(uid)).delete().run(thonk);
        } catch (ReqlError e){
            throw new DatabaseException(e.getMessage(), e.getStackTrace());
        }
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


}
