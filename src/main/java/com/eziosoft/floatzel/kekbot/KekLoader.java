package com.eziosoft.floatzel.kekbot;

import com.eziosoft.floatzel.kekbot.KekGlueUtil.KekCommand;
import javassist.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

//import static java.nio.ByteBuffer.compare;

public class KekLoader {

    public static KekCommand LoadKek() throws NotFoundException, CannotCompileException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        ClassPool p = ClassPool.getDefault();
        // add some classes to the classpath
        p.insertClassPath(new ClassClassPath(KekLoader.class));
        // add kekbot.jar to the classpath
        p.insertClassPath(new File("mods/kekbot/kekbot.jar").getAbsolutePath());
        // this is the dirtiest hack ive ever written oh my god
        byte[] byteclass = p.get("com.godson.kekbot.command.commands.general.Invite").toBytecode();
        byte[] inject = "+(Lcom/eziosoft/floatzel/kekbot/KekGlue/CommandEvent;)V".getBytes(StandardCharsets.US_ASCII);
        byte[] fixed = null;
        byte[] query = new byte[]{0x2B,0x28,0x4C,0x63,0x6F,0x6D,0x2F,0x67,0x6F,0x64,0x73,0x6F,0x6E,0x2F,0x6B,0x65,0x6B,0x62,0x6F,0x74,0x2F,0x63,0x6F
                ,0x6D,0x6D,0x61,0x6E,0x64,0x2F,0x43,0x6F,0x6D,0x6D,0x61,0x6E,0x64,0x45,0x76,0x65,0x6E,0x74,0x3B,0x29,0x56};
        int location = bytesFindIndexOfQuery(byteclass, query);
        // copy the first part of the array to the new array
        fixed = new byte[(byteclass.length - query.length) + inject.length];
        System.arraycopy(byteclass, 0, fixed, 0, location);
        System.arraycopy(inject, 0, fixed, location, inject.length);
        System.arraycopy(byteclass, location + inject.length, fixed, (byteclass.length - query.length) + inject.length, byteclass.length - query.length);
        // delet imported packages...
        p.clearImportedPackages();
        // ...and impport our gluecode
        p.importPackage("com.eziosoft.floatzel.kekbot");
        // get the kekbot command we want, then convert it to a KekCommand
        System.out.println("Converting class to KekCommand...");
        // setup a class remapper
        ClassMap map = new ClassMap();
        map.put("com.godson.kekbot.command.Command$Category", "com.jagrosh.jdautilities.command.Command$Category");
        map.put("com.godson.kekbot.command.CommandEvent", "com.eziosoft.floatzel.kekbot.KekGlue.CommandEvent");
        assert fixed != null;
        CtClass c = p.makeClass(new ByteArrayInputStream(fixed));
        CtClass kek = p.makeClass("E");
        kek.addMethod(CtNewMethod.copy(c.getMethod(c.getDeclaredMethod("onExecuted").getName(), c.getDeclaredMethod("onExecuted").getSignature()), kek, map));
        kek.addConstructor(CtNewConstructor.copy(c.getConstructors()[0], kek, map));
        kek.setSuperclass(p.get("com.eziosoft.floatzel.kekbot.KekGlueUtil.KekCommand"));
        //System.out.println(c.getDeclaredMethod("onExecuted").getSignature());
        return (KekCommand) kek.toClass().getConstructor().newInstance();
    }

    static int bytesFindIndexOfQuery(byte[] bytes, byte[] query) {
        int bytesLen = bytes.length;
        int queryLen = query.length;
        int limitLen = (bytesLen - queryLen) + 1;
        outer: for (int i = 0; i < limitLen; i++) {
            for (int ii = 0; ii < queryLen; ii++) {
                if (bytes[i + ii] != query[ii]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

}
