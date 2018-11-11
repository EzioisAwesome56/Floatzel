package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;
import com.google.gson.*;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlError;
import com.rethinkdb.gen.exc.ReqlQueryLogicError;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;

import javax.xml.crypto.Data;
import java.io.*;


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


    // check if folder exist
    public static void dbinit() {
        System.out.println("Floatzel is starting RethinkDB...");
        // first, check if the database file exists
        if (r.dbList().contains("floatzel").equals("true")){
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
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id){
            boolean exist = false;
            // connection shit
            try {
                exist = r.table(banktable).get(id).count().equals(1);
            } catch (ReqlError e){
                Error.Catch(e.getStackTrace().toString(), e.getMessage());
                return false;
            }
            if (!exist){
                // the user does not have a bank account
                // make one instead!
                try {
                    r.table(banktable).insert(r.array(
                            r.hashMap("id", id)
                                    .with("bal", 0)
                    )).run(thonk);
                } catch (ReqlError e){
                    Error.Catch(e.getStackTrace().toString(), e.getMessage());
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
            r.table(banktable).filter(row -> row.g("id").eq(id)).update(r.hashMap("bal", data)).run(thonk);
        } catch (ReqlError e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());

        }
    }

    // load an integer from a db entry
    public static int dbloadint(String id) {
        String raw;
        int bal;
            try {
                raw = r.table(banktable).filter(row -> row.g("id").eq(id)).toJsonString().toString();
            } catch (ReqlError e){
                Error.Catch(e.getStackTrace().toString(), e.getMessage());
                return -999;
            }
            // do json things
        try {
            JsonElement jsone = new JsonParser().parse(raw);
            JsonObject json = jsone.getAsJsonObject();
            bal = Integer.valueOf(json.get("bal").getAsString());
        } catch (JsonSyntaxException e){
                Error.Catch(e.getStackTrace().toString(), e.getMessage());
                return -999;
        }
        return bal;

        }

    // check if the user has a loan out
    public static boolean dbcheckifloan(String id){
        boolean exist = false;
        // connection shit
        try {
            exist = r.table(loantable).get(id).count().equals(1);
        } catch (ReqlError e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
            return false;
        }
        if (!exist){
            // the user does not have a bank account
            // make one instead!
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
            Error.Catch("No stack trace provided!", "Unknown argument passed!");
            return;
        }
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void sqlblankloan(String id){
        // insert a thong into the loan table
        try {
            r.table(loantable).insert(r.array(
                    r.hashMap("id", id)
                            .with("time", 0L)
            )).run(thonk);
            return;
        } catch (ReqlError e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
            return;
        }
    }

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time){
        try {
            r.table(loantable).filter(row -> row.g("id").eq(id)).update(r.hashMap("time", time)).run(thonk);
            return;
        } catch (ReqlError e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
            return;
        }
            }

    // return a long with the stored nano time
    public static long dbloadtime(String id){
        String raw;
        long bal;
        try {
            raw = r.table(loantable).filter(row -> row.g("id").eq(id)).toJsonString().toString();
        } catch (ReqlError e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
            return -999;
        }
        // do json things
        try {
            JsonElement jsone = new JsonParser().parse(raw);
            JsonObject json = jsone.getAsJsonObject();
            bal = Long.valueOf(json.get("time").getAsString());
        } catch (JsonSyntaxException e){
            Error.Catch(e.getStackTrace().toString(), e.getMessage());
            return -999;
        }
        return bal;
    }

    public static boolean dbcheckbloan(String id){
        return false;
       /* // sql statement to check for this shit
            String sql = "SELECT id, perm FROM "+bloanperm+" WHERE id = '"+id+"'";
            // do those sql things
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt  = conn.prepareStatement(sql)){

                ResultSet rs  = pstmt.executeQuery();

                // error trap
                if (!rs.next()){
                    System.out.println("ERROR LOADING ROWS");
                    return false;
                }
                // so if its there, get the value and return it
                if (rs.getInt("perm") == 1){
                    // the user has the command, let them use it
                    return true;
                } else {
                    // they dont rofl lmao
                    return false;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }*/
        }

    public static void dbbuycmd(int cmd, String uid){
        // finish this later
        return;
        /*String table = "";
            if (cmd == 1){
                table = bloanperm;
            }
            // start the process of sqling
            String sql = "INSERT INTO "+table+"(id,perm) VALUES(?,?)";
            // do the actual inserting
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, uid);
                pstmt.setInt(2, 1);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }*/
        }

    // check to see if anything is in the table at all
    public static Boolean dbcheckstock(){
        return false;
        /*int id = 1;
        // the sql used to check if a person is in za database
        String sql = "SELECT * FROM "+stocktable+" WHERE id = "+1;
        // connection shit
        try (Connection conn = Database.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            ResultSet rs  = pstmt.executeQuery();

            if (!rs.next()){
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }*/
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void dbnewstock(int id, String name, int units, int price){
        /*// prepare to insert
        String sql = "INSERT INTO "+stocktable+"(id,name,price,diff,units) VALUES(?,?,?,?,?)";
        // insert
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, price);
            pstmt.setInt(5, units);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }*/
    }

    public static void dbinccount(){
       /* boolean exist = true;
        int newcount = -2;
        String sql = "SELECT * FROM "+stockc;
        try (Connection conn = Database.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // is there already a row here?
            exist = rs.next();
            if (!exist) {
                // if there isnt, make it
                sql = "INSERT INTO "+stockc+"(numb) VALUES(?)";
            } else {
                int oldcount = rs.getInt("numb");
                sql = "UPDATE "+ stockc + " SET numb = ? WHERE numb = "+oldcount;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return;
        }
        try (Connection conn = Database.connect(); PreparedStatement psmt = conn.prepareStatement(sql)){
            if (!exist){
                psmt.setInt(1, 1);
            } else {
                psmt.setInt(1, newcount);
            }
            psmt.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }*/

    }
    public static int dbgetcount(){
        return -999;
       /* String sql = "SELECT numb FROM "+stockc+" WHERE numb > ?";
        try (Connection conn = Database.connect(); PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, 0);
            ResultSet rs = pst.executeQuery();
            boolean exist = rs.next();
            int epic = rs.getInt("numb");
            rs.close();
            return epic;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -999;
        }*/
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
        return -999;
        /*String sql = "SELECT price FROM "+stocktable+" WHERE id = '"+id+"'";
        // connect to the db and get the row
        try (Connection conn = Database.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            // execute the query
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            if (!rs.next()){
                System.out.println("ERROR WHILE LOADING ROWS");
                rs.close();
                return -999;
            }
            // alright, get the value we need now
            int result = rs.getInt("price");
            rs.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -999;
        }*/
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
