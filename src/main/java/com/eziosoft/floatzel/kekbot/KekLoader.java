package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.kekbot.KekGlueUtil.KekCommand;
import javassist.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KekLoader {

    public static KekCommand LoadKek() throws NotFoundException, CannotCompileException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassPool p = ClassPool.getDefault();
        // add some classes to the classpath
        p.insertClassPath(new ClassClassPath(KekCommand.class));
        // add kekbot.jar to the classpath
        p.insertClassPath(new File("mods/kekbot/kekbot.jar").getAbsolutePath());
        CtClass c = p.get("com.godson.kekbot.command.commands.general.Invite");
        // delet imported packages...
        p.clearImportedPackages();
        // ...and impport our gluecode
        p.importPackage("com.eziosoft.floatzel.kekbot");
        // get the kekbot command we want, then convert it to a KekCommand
        System.out.println("Converting class to KekCommand...");
        c.setSuperclass(p.get("com.eziosoft.floatzel.kekbot.KekGlueUtil.KekCommand"));
        // setup a class remapper
        ClassMap map = new ClassMap();
        map.put("com.godson.kekbot.command.Command$Category", "com.jagrosh.jdautilities.command.Command$Category");
        c.getConstructors()[0].setBody(c.getConstructors()[0], map);
        System.err.println(c.getSuperclass().getName());
        return (KekCommand) c.toClass().getConstructor().newInstance();
    }
}
