package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.List;


public class Database {
    // set the root db path
    public static String bank = "database/bank/";
    public static String bloan = "database/betterloan/";
    public static String trackbloan = "database/lotto";
    // new sqlite database location, stored as a file
    public static String sqlthing = System.getProperty("user.dir") + "/floatzel.db";
    public static File sqldb = new File(sqlthing);
    // actual url to the db
    public static String url = "jdbc:sqlite:" + sqlthing;
    // tables
    public static String banktable = "bank";
    public static String loantable = "loan";
    public static String bloanperm = "bloan";
    public static String stocktable = "stocks";
    public static String stockc = "count";
    public static String tweets = "tweets";
    // rethink db!
    public static final RethinkDB r = RethinkDB.r;
    public static Connection thonk = r.connection().hostname("localhost").port(28015).connect();
    public static Gson g = new Gson();
    public static Cursor cur = null;
    // error shitto
    public static String weed;




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
    public static void sqlblankloan(String id){
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
        try{
            exist = r.table(stocktable).getAll().count().eq(1).run(thonk);
        } catch (ReqlError e){
            Error.Catch(e);
            return exist;
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
        int total = 0;
        try {
            total = r.table(stocktable).count().run(thonk);
      } catch (ReqlError e){
            Error.Catch(e);
            return -999;
        }
        return total;
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
    public static String dbgetname(int id){
        return "ERROR";
        /*String sql = "SELECT name FROM "+stocktable+" WHERE id = "+id;
        // connect to the db
        try (Connection conn = Database.connect(); PreparedStatement pst = conn.prepareStatement(sql)){
            // run the query
            ResultSet rs = pst.executeQuery();

            if (!rs.next()){
                System.out.println("SQL FAULT!");
                return "FUCK!";
            }
            String name = rs.getString("name");
            return name;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return "A";
        }*/
    }

    // get dif
    public static int dbgetdiff(int id){
        return -999;
        /*String sql = "SELECT diff FROM "+stocktable+" WHERE id = "+id;
        // connect to the db
        try (Connection conn = Database.connect(); PreparedStatement pst = conn.prepareStatement(sql)){
            // run the query
            ResultSet rs = pst.executeQuery();

            if (!rs.next()){
                System.out.println("SQL FAULT!");
                return -999;
            }
            int diff = rs.getInt("diff");
            return diff;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -999;
        }*/
    }

    // get units
    public static int dbgetunits(int id){
        return -999;
        /*String sql = "SELECT units FROM "+stocktable+" WHERE id = "+id;
        // connect to the db
        try (Connection conn = Database.connect(); PreparedStatement pst = conn.prepareStatement(sql)){
            // run the query
            ResultSet rs = pst.executeQuery();

            if (!rs.next()){
                System.out.println("SQL FAULT!");
                return -999;
            }
            int diff = rs.getInt("units");
            return diff;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -999;
        }*/
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
