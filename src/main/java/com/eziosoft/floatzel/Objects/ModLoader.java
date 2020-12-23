package com.eziosoft.floatzel.Objects;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class ModLoader {

    private File[] mfolder;

    private String[] mainclasses;

    private URLClassLoader child;

    public int getTotalMods(){
        return mfolder.length;
    }

    public ModLoader(String path) throws IOException {
        System.out.println("Eziosoft Modloader now starting up...");

        File temp = new File(path);
        if (!temp.exists()){
            throw new FileNotFoundException("Invalid mod folder path!");
        }
        // if we're past the error trap, finish initializing
        this.mfolder = temp.listFiles();
        // form into list
        ArrayList<URL> ulist = new ArrayList<URL>();
        for (int h = 0; h < getTotalMods(); h++){
            if (mfolder[h].isDirectory()){
                System.out.println("Mod \"" + mfolder[h].toString() + "\" is a Directory! Skipping...");
                continue;
            }
            if (!FilenameUtils.getExtension(mfolder[h].getAbsolutePath()).equals("jar")){
                System.out.println("Mod \"" + mfolder[h].toString() + "\" is not a JAR file! Skipping...");
                continue;
            }
            // past the error traps? get main class from jar
            JarFile jar = new JarFile(mfolder[h]);
            ulist.add(mfolder[h].toURI().toURL());
        }

        this.child = new URLClassLoader(
                ulist.toArray(new URL[]{}),
                ModLoader.class.getClassLoader());
    }

    public Object loadDatabase(){
        // do cool things here
    }




}
