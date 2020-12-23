package com.eziosoft.floatzel.Objects;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class ModLoader {

    private File[] mfolder;

    private Mod[] mods;

    private URLClassLoader child;

    private static Gson gson = new Gson();

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
        ArrayList<Mod> modlist = new ArrayList<Mod>();
        for (int h = 0; h < getTotalMods(); h++){
            if (mfolder[h].isDirectory()){
                System.out.println("Mod \"" + mfolder[h].toString() + "\" is a Directory! Skipping...");
                continue;
            }
            if (!FilenameUtils.getExtension(mfolder[h].getAbsolutePath()).equals("jar")){
                System.out.println("Mod \"" + mfolder[h].toString() + "\" is not a JAR file! Skipping...");
                continue;
            }
            // past the error traps? get "mod" object from jar's json
            JarFile jar = new JarFile(mfolder[h]);
            Reader read = new InputStreamReader(jar.getInputStream(jar.getJarEntry("modinfo.json")));
            Mod tempmod = gson.fromJson(read, Mod.class);
            // store it away
            modlist.add(tempmod);
            // also add mod url to the url list
            ulist.add(mfolder[h].toURI().toURL());
            // broadcast that mods have been found
            System.out.println("Mod \"" + mfolder[h].toString() + "\" has been queued for load");
        }

        this.child = new URLClassLoader(
                ulist.toArray(new URL[]{}),
                ModLoader.class.getClassLoader());

        this.mods = modlist.toArray(new Mod[]{});
        System.out.println("Eziosoft Modloader Initialization complete!");
    }




}
