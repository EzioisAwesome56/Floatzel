package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Floatzel;
import com.eziosoft.floatzel.SlashCommands.FSlashCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableCommand;
import com.eziosoft.floatzel.SlashCommands.FSlashableImageCommand;
import com.eziosoft.floatzel.Util.Database;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class ModLoader {

    private final File[] mfolder;

    private final Mod[] mods;

    private final URLClassLoader child;

    private static final Gson gson = new Gson();

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

    public void loadAll() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean dbloaded = false;
        for (Mod mod : mods){
            switch (mod.getType()) {
                case "command": {
                    System.out.println("Loading mod mainclass " + mod.getMainclass());
                    Class<?> toLoad = Class.forName(mod.getMainclass(), true, child);
                    Object loadedMod = toLoad.getConstructor().newInstance();
                    Floatzel.commandClient.addCommand((FCommand) loadedMod);
                    break;
                }
                case "database": {
                    if (dbloaded) {
                        System.out.println("Database already loaded! skipping loading class " + mod.getMainclass());
                    } else {
                        System.out.println("Now Loading Database plugin from main class " + mod.getMainclass() + "...");
                        Class<?> toLoad = Class.forName(mod.getMainclass(), true, child);
                        Object dbc = toLoad.getConstructor().newInstance();
                        // load it as a db module
                        Database.dbdriver = new DatabaseModule((GenaricDatabase) dbc);
                        Database.sendConninfo(gson.toJson(new ConnInfo(Floatzel.conf.getHostname(), Floatzel.conf.dbUser(), Floatzel.conf.dbPass(), Floatzel.conf.getPort())));
                        dbloaded = true;
                    }
                    break;
                }
                case "commands": {
                    System.out.println("--- START BATCH LOAD ---");
                    String[] clases;
                    Class<?> toLoad;
                    Object loadedclass;
                    if (mod.getMainclasses() == null){
                        System.err.println("WARNING: Providing main classes via a function call will be removed in a future release!");
                        System.err.println("Please update your mod to include the main classes in an array in the modinfo.json file!");
                        System.out.println("[LEGACY COMPAT] Batch loading commands as defined by " + mod.getMainclass());
                        // first we need to get the list of commands loaded
                        toLoad = Class.forName(mod.getMainclass(), true, child);
                        loadedclass = toLoad.getConstructor().newInstance();
                        clases = (String[]) toLoad.getDeclaredMethod("getClasses").invoke(loadedclass);
                    } else {
                        System.out.println("Loading commands as defined in modinfo.json");
                        clases = mod.getMainclasses().toArray(new String[]{});
                    }
                    for (String mainclass : clases) {
                        System.out.println("Loading " + mainclass);
                        toLoad = Class.forName(mainclass, true, child);
                        loadedclass = toLoad.getConstructor().newInstance();
                        Floatzel.commandClient.addCommand((FCommand) loadedclass);
                    }
                    System.out.println("--- END BATCH LOAD ---");
                    break;
                }
                case "slashactions": {
                    System.out.println("--- START BATCH LOAD ---");
                    // start loading them
                    if (mod.getMainclasses() == null){
                        System.err.println("No mainclass array defined for mod! skipping!");
                        break;
                    }
                    Class<?> toLoad;
                    Object loadedClass;
                    for (String classname : mod.getMainclasses()){
                        System.out.println("Loading " + classname);
                        toLoad = Class.forName(classname, true, child);
                        loadedClass = toLoad.getConstructor().newInstance();
                        // check what kind of action it is
                        if (loadedClass instanceof FSlashableCommand){
                            Floatzel.dualRegister((FSlashableCommand) loadedClass);
                        } else if (loadedClass instanceof FSlashableImageCommand){
                            Floatzel.dualRegisterImage((FSlashableImageCommand) loadedClass);
                        } else {
                            System.err.println("ERROR: class " + classname + " is not of a supported action type, skipping...");
                        }
                    }
                    System.out.println("--- END BATCH LOAD ---");
                    break;
                }
                case "slashcommand": {
                    System.out.println("Loading registerable slashcommand from " + mod.getMainclass());
                    Class<?> toLoad = Class.forName(mod.getMainclass(), true, child);
                    Object loadedClass = toLoad.getConstructor().newInstance();
                    if (!(loadedClass instanceof FSlashCommand)){
                        System.err.println("ERROR: loaded class is not a valid slash command! skipping...");
                    } else {
                        FSlashCommand cmd = (FSlashCommand) loadedClass;
                        Floatzel.scm.addRegisterable(cmd.name, cmd);
                    }
                    break;
                }
                case "customInit": {
                    System.out.println("Mod has requested custom init routine, loading and running...");
                    Class<?> toload = Class.forName(mod.getMainclass(), true, child);
                    Object loadedclass = toload.getConstructor().newInstance();
                    toload.getDeclaredMethod("customInit").invoke(loadedclass);
                    break;
                }
            }
        }
        if (!dbloaded){
            if (!Floatzel.fallback) {
                throw new ClassNotFoundException("No database plugin found!");
            } else {
                System.out.println("Mo database mod was found, however you have chosen to run with the fallback driver.\n" +
                        "this will not allow saving of any information, such as user profiles, and should not be used for production.\n" +
                        "you have been warned!");
                System.out.println("loading fallback database driver...");
                Database.dbdriver = new DatabaseModule(new FallbackDB());
            }
        }
    }
}
