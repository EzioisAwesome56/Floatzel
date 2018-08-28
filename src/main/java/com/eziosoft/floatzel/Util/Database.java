package com.eziosoft.floatzel.Util;

import java.io.*;
import java.nio.file.Files;

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

    // check if folder exist
    public static void dbinit(){
        System.out.println("Checking if database folder has been created...");
        if (!dbdir.exists()){
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

    }

    // SQLITE init function
    public static void sqlinit(){
        // file path i think
        String url = System.getProperty("user.dir");
        System.out.println(url);
    }

    // check if db entry exists
    public static Boolean dbcheckifexist(String id){
        File userfile = new File(bank+id+ext);
        if (!userfile.exists()){
            Database.dbsave(id, "0");
            return false;
        } else {
            return true;
        }
    }


    // function to write to a new db file
    public static void dbsave(String id, String data){
        File dbentry = new File(bank+id+ext);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
            writer.write(data);
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    // function to save to bank accounts as ints
    public static void dbsaveint(String id, int data){
        File dbentry = new File(bank+id+ext);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbentry));
            writer.write(Integer.toString(data));
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    // load an integer from a db entry
    public static int dbloadint(String id){
        File dbentry = new File(bank+id+ext);
        try {
            FileReader read = new FileReader(dbentry);
            BufferedReader bread = new BufferedReader(read);
            String content = bread.readLine();
            return Integer.valueOf(content);
        } catch (NumberFormatException | IOException e){
            e.printStackTrace();
            return -1;
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
