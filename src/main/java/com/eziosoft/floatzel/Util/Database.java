package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;
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


    // check if folder exist
    public static void dbinit() {
        System.out.println("Floatzel is starting RethinkDB...");
        // first, check if the database file exists
        if (r.dbList().contains("floatzel").run(thonk)){
            System.out.println("No database found! Creating a new db!");
            // okay, it hasnt been initalized yet, so do that
            r.dbCreate("floatzel").run(thonk);
            thonk.use("floatzel");
            System.out.println("Creating tables...");
            // nothing
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
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id){
            // the sql used to check if a person is in za database
            String sql = "SELECT 1 FROM "+banktable+" WHERE id = '"+id+"' LIMIT 1";
            // connection shit
            boolean exist = r.table(banktable).get(id).count().equals(1);
            if (!exist){
                // the user does not have a bank account
                // make one instead!
                r.table(banktable).insert(r.array(
                        r.hashMap("id", id)
                        .with("bal", 0)
                )).run(thonk);
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
            // prepare the sql to load the user's table
            String sql = "SELECT id, bal FROM "+banktable+" WHERE id = '"+id+"'";
            // connect to the db and get the row
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt  = conn.prepareStatement(sql)){
                // execute the query
                ResultSet rs  = pstmt.executeQuery();

                // loop through the result set
                if (!rs.next()){
                    System.out.println("ERROR WHILE LOADING ROWS");
                    return -999;
                }
                // alright, get the value we need now
                return rs.getInt("bal");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return -999;
            }
        }

    // check if the user has a loan out
    public static boolean dbcheckifloan(String id){
            // find a row with the user's id
            String sql = "SELECT id, time FROM "+loantable+" WHERE id = '"+id+"'";
            // execute that string and get a results set
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt  = conn.prepareStatement(sql)){

                ResultSet rs  = pstmt.executeQuery();

                // loop through the result set
                if (!rs.next()){
                    // right, so they dont have it here at all, oof that sucks
                    System.out.println("USER HAD NO LOAN");
                    return false;
                } else {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

    // default saver
    public static void dbdefaultsave(String id, int location){
        String defpath = "";
        if (location == 1){
            defpath = bank;
        } else if (location == 2){
            // yeah, bascially we dont need this anymore
                Database.sqlblankloan(id);
                return;
        } else if (location == 3){
            defpath = trackbloan;
        } else if (location == 4){
            defpath = bloan;
        } else {
            System.out.println("Error: invalid location id");
            return;
        }
        // write the default value to the db entry
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void sqlblankloan(String id){
        // prepare to insert
        String sql = "INSERT INTO "+loantable+"(id,time) VALUES(?,?)";
        // insert
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setLong(2, 0L);
            pstmt.executeUpdate();
            return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time){
                // update it instead
                String sql = "UPDATE " + loantable + " SET time = ? WHERE id = '" + id + "'";
                try (Connection conn = Database.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setLong(1, time);
                    // update
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

    // return a long with the stored nano time
    public static long dbloadtime(String id){
            // prepare the sql to load the user's table
            String sql = "SELECT id, time FROM "+loantable+" WHERE id = ?";
            // connect to the db and get the row
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt  = conn.prepareStatement(sql)){
                // execute the query
                pstmt.setString(1, id);
                ResultSet rs  = pstmt.executeQuery();

                // loop through the result set
                if (!rs.next()){
                    System.out.println("ERROR WHILE LOADING ROWS");
                    return -999L;
                }
                // alright, get the value we need now
                return rs.getLong("time");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return -999L;
            }
        }

    public static boolean dbcheckbloan(String id){
            // sql statement to check for this shit
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
            }
        }

    public static void dbbuycmd(int cmd, String uid){
            String table = "";
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
            }
        }

    // check to see if anything is in the table at all
    public static Boolean dbcheckstock(){
        int id = 1;
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
        }
    }

    // sql fucntion to write a 0 to a new loan entry
    public static void dbnewstock(int id, String name, int units, int price){
        // prepare to insert
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
        }
    }

    public static void dbinccount(){
        boolean exist = true;
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
        }

    }
    public static int dbgetcount(){
        String sql = "SELECT numb FROM "+stockc+" WHERE numb > ?";
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
        }
    }
    public static void dbupdatestock(int id, boolean isbuy, int price, int diff, int unit){
        // update it instead
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
        }
    }

    // methood to get current price
    public static int dbgetprice(int id){
        String sql = "SELECT price FROM "+stocktable+" WHERE id = '"+id+"'";
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
        }
    }

    // get the name of a stock
    public static String dbgetname(int id){
        String sql = "SELECT name FROM "+stocktable+" WHERE id = "+id;
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
        }
    }

    // get dif
    public static int dbgetdiff(int id){
        String sql = "SELECT diff FROM "+stocktable+" WHERE id = "+id;
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
        }
    }

    // get units
    public static int dbgetunits(int id){
        String sql = "SELECT units FROM "+stocktable+" WHERE id = "+id;
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
        }
    }

    public static int dbcounttweets(){
        String sql = "SELECT * FROM "+tweets;
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
        return rows;
    }


}
