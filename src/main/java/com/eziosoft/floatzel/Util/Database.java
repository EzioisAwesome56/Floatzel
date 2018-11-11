package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
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
    public static String stockc = "count";
    public static String tweets = "tweets";
    // rethink db!
    private static final RethinkDB r = RethinkDB.r;
    private static Connection thonk = r.connection().hostname("localhost").port(28015).connect();
    public static Gson g = new Gson();
    private static Cursor cur = null;




    // check if folder exist
    public static void dbinit() {
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
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id){
            boolean exist = false;
            // connection shit
            try {
                exist = (boolean) r.table(banktable).filter(
                        r.hashMap("uid", id)
                ).count().eq(1).run(thonk);
            } catch (ReqlError e){
                Error.Catch(e);
                return false;
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
                    Error.Catch(e);
                    return false;
                }
                return exist;
            } else {
                return exist;
            }
        }


    // function to write to a new db file (OLD: DO NOT USE)
    public static void dbsave(String id, String data){
       // i dont know why this is still here, but just make it call dbsaveint for laziness
        int number = Integer.valueOf(data);
        Database.dbsaveint(id, number);
        return;
    }

    // function to save to bank accounts as ints
    public static void dbsaveint(String id, int data) {
        // fix for overflow bug
        if (data < -100){
            data = Integer.MAX_VALUE;
        }
        // do the things
        try {
            r.table(banktable).filter(row -> row.g("uid").eq(id)).update(r.hashMap("bal", data)).run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);

        }
    }

    // load an integer from a db entry
    public static int dbloadint(String id) {
        String result;
            try {
                cur = r.table(banktable).filter(row -> row.g("uid").eq(id)).getField("bal").run(thonk);
            } catch (ReqlError e){
                Error.Catch(e);
                return -999;
            }
            // do json things
        // return value of bal
        result = Utils.getValue(cur);
        return Integer.valueOf(result);

        }

    // check if the user has a loan out
    public static boolean dbcheckifloan(String id){
        boolean exist = false;
        // connection shit
        try {
            exist = (boolean) r.table(loantable).filter(
                    r.hashMap("uid", id)
            ).count().eq(1).run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return false;
        }
        if (!exist){
            return exist;
        } else {
            return exist;
        }
        }

    // default saver
    public static void dbdefaultsave(String id, int location){
        //  simplify this function as it serves only 1 purpose
        if (location == 2){
            // run the loan saver
            Database.sqlblankloan(id);
            return;
        } else {
            Error.SpecialError("No stack trace provided!", "Unknown argument passed!");
            return;
        }
    }

    // sql fucntion to write a 0 to a new loan entry
    private static void sqlblankloan(String id){
        // insert a thong into the loan table
        try {
            r.table(loantable).insert(r.array(
                    r.hashMap("uid", id)
                            .with("time", Long.toString(0L))
            )).run(thonk);
            return;
        } catch (ReqlError e){
            Error.Catch(e);
            return;
        }
    }

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time){
        try {
            r.table(loantable).filter(row -> row.g("uid").eq(id)).update(r.hashMap("time", Long.toString(time))).run(thonk);
            return;
        } catch (ReqlError e){
            Error.Catch(e);
            return;
        }
            }

    // return a long with the stored nano time
    public static long dbloadtime(String id){
        String result;
        try {
            cur = r.table(loantable).filter(row -> row.g("uid").eq(id)).getField("time").run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return -999;
        }
        result = Utils.getValue(cur);
        return Long.valueOf(result);
    }

    public static boolean dbcheckbloan(String id){
        // sql statement to check for this shit
        boolean exist = false;
        try {
            exist = (boolean) r.table(bloanperm).filter(row -> row.g("uid").eq(id)).count().eq(1).run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return exist;
        }
        // then just return the result
            return exist;
        }

    public static void dbbuycmd(int cmd, String uid){
        String table = "";
            if (cmd == 1){
                table = bloanperm;
            }
            try {
                r.table(table).insert(r.hashMap("uid", uid)).run(thonk);
            } catch (ReqlError e){
                Error.Catch(e);
            }
        }

    // check to see if anything is in the table at all
    public static Boolean dbcheckstock(){
        boolean exist = false;
        long total;
        try{
            total = r.table(stocktable).count().run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return exist;
        }
        int realtotal = Math.toIntExact(total);
        if (realtotal >= 1){
            exist = true;
        }
        return exist;
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void dbnewstock(int id, String name, int units, int price){
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
            Error.Catch(e);
        }
    }

    public static void dbinccount(){
       // since rethink db isnt shit like sqlite, we dont need this anymore
        // as such, this function does nothing
        return;

    }
    public static int dbgetcount(){
        // one of the advatages of rethink: figuring out how many rows are in a table
        // thank lord!
        long total = 0;
        try {
            total = r.table(stocktable).count().run(thonk);
      } catch (ReqlError e){
            Error.Catch(e);
            return -999;
        }
        return Math.toIntExact(total);
    }
    public static void dbupdatestock(int id, boolean isbuy, int price, int diff, int unit){
        return;

        /*// update it instead
        String sql;
        if (!isbuy) {
             sql = "UPDATE " + stocktable + " SET price = ?, diff = ? WHERE id = '" + id + "'";
        } else {
            sql = "UPDATE " + stocktable + " SET units = ? WHERE id = '" + id + "'";
        }
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                if(!isbuy) {
                    pstmt.setInt(1, price);
                    pstmt.setInt(2, diff);
                } else {
                    pstmt.setInt(1, unit);
                }
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
    }

    // methood to get current price
    public static int dbgetprice(int id){
        try {
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("price").run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return -999;
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    // get the name of a stock
    public static String dbgetname(int id) {
        try {
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("name").run(thonk);
        } catch (ReqlError e) {
            Error.Catch(e);
            return "ERROR";
        }
        return Utils.getValue(cur);
    }


    // get dif
    public static int dbgetdiff(int id){
        try{
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("diff").run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return -999;
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    // get units
    public static int dbgetunits(int id){
        try{
            cur = r.table(stocktable).filter(row -> row.g("sid").eq(id)).getField("units").run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
        }
        return Integer.valueOf(Utils.getValue(cur));
    }

    public static int dbcounttweets(){
        return -999;
        /*String sql = "SELECT * FROM "+tweets;
        int rows = 0;
        // do a thing
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            // sqlite is dumb
            while (rs.next()){
                rs.next();
                rows = rs.getRow();
            }
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        return rows;*/
    }


}
