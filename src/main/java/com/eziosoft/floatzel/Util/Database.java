package com.eziosoft.floatzel.Util;

import com.eziosoft.floatzel.Config;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class Database {
    // set the root db path
    public static String path = "database/";
    public static String bank = "database/bank/";
    public static String loan = "database/loan/";
    public static String bagle = "database/bagle/";
    public static String bloan = "database/betterloan/";
    public static String trackbloan = "database/lotto";
    public static File dbdir = new File(path);
    public static File bankdir = new File(bank);
    public static File loandir = new File(loan);
    public static File lottodir = new File(trackbloan);
    public static File bagledir = new File(bagle);
    public static File bldir = new File(bloan);
    public static String ext = ".db";
    // new sqlite database location, stored as a file
    public static String sqlthing = System.getProperty("user.dir") + "/floatzel.db";
    public static File sqldb = new File(sqlthing);
    // actual url to the db
    public static String url = "jdbc:sqlite:" + sqlthing;
    // tables
    public static String banktable = "bank";

    // check if folder exist
    public static void dbinit() {
        // read from settings to find out what database the user has selected to use
        if (Config.olddb) {
            System.out.println("Checking if database folder has been created...");
            if (!dbdir.exists()) {
                System.out.println("Database not created, setting up database...");
                dbdir.mkdir();
                bankdir.mkdir();
                loandir.mkdir();
                lottodir.mkdir();
                bagledir.mkdir();
                bldir.mkdir();
                System.out.println("Database setup completed!");
                return;
            } else {
                System.out.println("Database already configured!");
                return;
            }
        } else if (!Config.olddb) {
            // first, check if the database file exists
            if (!sqldb.exists()){
                // okay, it hasnt been initalized yet, so do that
                Database.sqlinit();
            } else {
                // quickly run through the tables just to make sure
                Database.sqltable();
                System.out.println("database already installed!");
            }
        }
    }

    // SQLITE init function
    public static void sqlinit() {
        // copy paste from a website
        // sqlite database creation tool
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //sqlite table initilization
    public static void sqltable() {
        // string that holds the code needed to create a tab;e
        String banktable = "CREATE TABLE IF NOT EXISTS bank (\n"
                + " id text PRIMARY KEY,\n"
                + "	bal integer NOT NULL\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create all the tables
            stmt.execute(banktable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // sqlite connection thing, for easily connecting later
    private static Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id){
        if (Config.olddb) {
        File userfile = new File(bank+id+ext);
        if (!userfile.exists()){
            Database.dbsave(id, "0");
            return false;
        } else {
            return true;
        }
        } else {
            // the sql used to check if a person is in za database
            String sql = "SELECT 1 FROM "+banktable+" WHERE id = '"+id+"' LIMIT 1";
            // connection shit
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt  = conn.prepareStatement(sql)){

                ResultSet rs  = pstmt.executeQuery();

                if (!rs.next()){
                    // oh dear god here we go
                    // insert a blank entry into the table
                    sql = "INSERT INTO "+banktable+"(id, bal) VALUES('"+id+"',0)";
                    Statement stmt = conn.createStatement();
                    stmt.execute(sql);
                    return false;
                } else {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }


    // function to write to a new db file
    public static void dbsave(String id, String data){
       // i dont know why this is still here, but just make it call dbsaveint for laziness
        int number = Integer.valueOf(data);
        Database.dbsaveint(id, number);
        return;
    }

    // function to save to bank accounts as ints
    public static void dbsaveint(String id, int data) {
        if (Config.olddb) {

            File dbentry = new File(bank + id + ext);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
                writer.write(Integer.toString(data));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            // define the sql string
            String sql = "UPDATE " + banktable + " SET bal = ? WHERE id = '" + id + "'";
            try (Connection conn = Database.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, data);
                pstmt.executeUpdate();
                return;
            } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    }

    // load an integer from a db entry
    public static int dbloadint(String id) {
        if (Config.olddb) {
            File dbentry = new File(bank + id + ext);
            try {
                FileReader read = new FileReader(dbentry);
                BufferedReader bread = new BufferedReader(read);
                String content = bread.readLine();
                return Integer.valueOf(content);
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
                return -1;
            }
        } else {
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
    }

    // check if the user has a loan out
    public static boolean dbcheckifloan(String id){
        File dbentry = new File(loan+id+ext);
        if (dbentry.exists()){
            return true;
        } else {
            return false;
        }
    }

    // default saver
    public static void dbdefaultsave(String id, int location){
        String defpath = "";
        if (location == 1){
            defpath = bank;
        } else if (location == 2){
            defpath = loan;
        } else if (location == 3){
            defpath = trackbloan;
        } else if (location == 4){
            defpath = bloan;
        } else {
            System.out.println("Error: invalid location id");
            return;
        }
        // write the default value to the db entry
        File dbentry = new File(defpath+id+ext);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
            writer.write("0");
            writer.close();
        } catch (IOException e){
            System.out.println("ERROR SAVING DB");
            return;
            }
    }

    // fucntion for saving time to the loan
    public static void dbsavetime(String id, long time){
        File dbentry = new File(loan+id+ext);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
            writer.write(Long.toString(time));
            writer.close();
        } catch (IOException e){
            System.out.println("ERROR WRITING FILE");
            return;
        }
    }

    // return a long with the stored nano time
    public static long dbloadtime(String id){
        File dbentry = new File(loan+id+ext);
        try{
            FileReader read = new FileReader(dbentry);
            BufferedReader bread = new BufferedReader(read);
            String content = bread.readLine();
            return Long.valueOf(content);
        } catch (IOException e){
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean dbcheckbloan(String id){
        File dbentry = new File(bloan+id+ext);
        try{
            if (!dbentry.exists()){
                Database.dbdefaultsave(id, 4);
                return false;
            }
            FileReader read = new FileReader(dbentry);
            BufferedReader bread = new BufferedReader(read);
            String content = bread.readLine();
            if (content.equals("1")){
                return true;
            } else {
                return false;
            }
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void dbbuycmd(int cmd, String uid){
        String path = "";
        if (cmd == 1){
            path = bloan;
        }
        // make new file object for the db entry
        File dbentry = new File(path+uid+ext);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
            writer.write(Integer.toString(1));
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
    }



}
