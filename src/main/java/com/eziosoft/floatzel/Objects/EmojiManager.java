package com.eziosoft.floatzel.Objects;

import com.eziosoft.floatzel.Floatzel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.dv8tion.jda.api.entities.Emoji;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmojiManager {

    @Expose
    private List<SimpleEmoji> loading;
    @Expose
    private List<SimpleEmoji> other;

    // do not expose these
    private final Gson g = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public List<SimpleEmoji> getLoading() {
        return loading;
    }
    public List<SimpleEmoji> getOther() {
        return other;
    }

    public EmojiManager(List<SimpleEmoji> a, List<SimpleEmoji> b){
        this.loading = a;
        this.other = b;
    }

    private final Random rand = new Random();

    public EmojiManager(){}

    public String getRandomLoadEmote(){
        return this.loading.get(this.rand.nextInt(this.loading.size())).getEmoji();
    }

    public void initilize(){
        System.out.println("Now initializing emoji manager...");
        File f = new File("emoji.json");
        if (f.exists()){
            // load all emojis from json i guess
            try {
                BufferedReader br = new BufferedReader(new FileReader("emoji.json"));
                EmojiManager em = g.fromJson(br, EmojiManager.class);
                this.loading = em.getLoading();
                this.other = em.getOther();
                br.close();
            } catch (IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            System.err.println("No emoji.json found! creating default one...");
            List<SimpleEmoji> temp = new ArrayList<SimpleEmoji>();
            temp.add(new SimpleEmoji("put some cool loading emotes here!"));
            List<SimpleEmoji> temp2 = new ArrayList<SimpleEmoji>();
            temp2.add(new SimpleEmoji("put some other emotes here"));
            EmojiManager em = new EmojiManager(temp, temp2);
            String json = g.toJson(em);
            // save it
            try {
                FileWriter writer = new FileWriter("emoji.json");
                writer.write(json);
                writer.close();
            } catch (IOException e){
                System.err.println("ERROR WHILE SAVING EMOJI");
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.exit(2);
            }
            // also load them ig
            this.other = em.getOther();
            this.loading = em.getLoading();
            System.err.println("saved new emoji.json!");
        }
        System.out.println("Initialization complete!");
    }
}
